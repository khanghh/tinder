package com.example.tinder.search_friend;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.model.User;
import com.example.tinder.R;

import java.util.ArrayList;
import java.util.Random;

import androidx.navigation.Navigation;

public class SearchFriendPagerAdapter extends PagerAdapter {

    static final int PAGE_NUM = 2000;
    static final int SCREEN_NUM = 1;

    private Context context;
    private SearchFriendData searchFriendData;
    private boolean isFirst;
    private FriendView currentPage;

    private OnCurrentPageInit onCurrentPageInit;

    public void setOnCurrentPageInit(OnCurrentPageInit onCurrentPageInit) {
        this.onCurrentPageInit = onCurrentPageInit;
    }

    public SearchFriendPagerAdapter(Context context) {
        this.context = context;
        searchFriendData = SearchFriendData.getInstance();
        isFirst = true;
    }

    public SearchFriendPagerAdapter(Context context, FriendView currentPage) {
        this.context = context;
        searchFriendData = SearchFriendData.getInstance();
        isFirst = true;
        this.currentPage = currentPage;
    }

    public FriendView getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(FriendView currentPage) {
        this.currentPage = currentPage;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        FriendView view;
        if (isFirst && currentPage != null) {
            view = new FriendView(this.context, currentPage.getFriend());
        } else {
            view = new FriendView(this.context, searchFriendData.getUserData());
        }
        if (isFirst && onCurrentPageInit != null) {
            onCurrentPageInit.inited(view);
        }
        isFirst = false;
        searchFriendData.addOnDataLoadDoneListener(view);

        // add view to container
        view.setTag(position);
        container.addView(view);
        return view;
    }

    @Override
    public int getCount() {
        return PAGE_NUM;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup collection, int position, @NonNull Object view) {
        if (view instanceof SearchFriendData.OnDataLoadDoneListener) {
            searchFriendData.removeDataLoadDondListener((SearchFriendData.OnDataLoadDoneListener) view);
        }
        collection.removeView((View) view);
    }

    interface OnCurrentPageInit {
        void inited(FriendView page);
    }
}