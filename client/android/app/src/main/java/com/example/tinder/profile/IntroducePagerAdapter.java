package com.example.tinder.profile;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.tinder.R;

import java.util.ArrayList;
import java.util.Random;

import androidx.navigation.Navigation;

public class IntroducePagerAdapter extends PagerAdapter {

    public static final int NUM_PAGE = 4;

    private static final String HEADER1 = "Cập Nhật Hồ Sơ";
    private static final String HEADER2 = "Tìm Kiếm Bạn Bè";
    private static final String HEADER3 = "Trò Chuyện";
    private static final String HEADER4 = "Ứng Dụng Đa Dạng";

    private static final String DESCRIPTION1 = "Cập nhật hồ sơ dễ dàng và nhanh chống";
    private static final String DESCRIPTION2 = "Tìm kiếm nhiều bạn mới, cơ hội thoát ế";
    private static final String DESCRIPTION3 = "Trò chuyện với bạn bè mọi lúc mọi nơi";
    private static final String DESCRIPTION4 = "Ứng dụng đẹp, đa dạng, chia sẻ vị trí";

    private Context context;

    public IntroducePagerAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.introduce_slider_item, container, false);

        switch (position) {
            case 0:
                ((TextView)view.findViewById(R.id.txtHeader)).setText(HEADER1);
                ((TextView)view.findViewById(R.id.txtDescription)).setText(DESCRIPTION1);
                break;
            case 1:
                ((TextView)view.findViewById(R.id.txtHeader)).setText(HEADER2);
                ((TextView)view.findViewById(R.id.txtDescription)).setText(DESCRIPTION2);
                break;
            case 2:
                ((TextView)view.findViewById(R.id.txtHeader)).setText(HEADER3);
                ((TextView)view.findViewById(R.id.txtDescription)).setText(DESCRIPTION3);
                break;
            case 3:
                ((TextView)view.findViewById(R.id.txtHeader)).setText(HEADER4);
                ((TextView)view.findViewById(R.id.txtDescription)).setText(DESCRIPTION4);
                break;
            default:
                break;
        }

        // add view to container
        container.addView(view);
        return view;
    }

    @Override
    public int getCount() {
        return NUM_PAGE;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup collection, int position, @NonNull Object view) {
        Log.d("remove", "item removed");
        collection.removeView((View) view);
    }
}
