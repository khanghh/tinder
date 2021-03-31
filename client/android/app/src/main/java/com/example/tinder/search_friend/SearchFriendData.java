package com.example.tinder.search_friend;

import android.util.Log;

import com.example.model.User;
import com.example.rest.RetrofitClient;
import com.example.rest.service.SearchFriendService;
import com.example.tinder.authentication.UserAuth;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchFriendData {

    private static SearchFriendData searchFriendData;

    private List<SearchFriendService.User> dataBuff;
    private boolean isLoading;
    private boolean isOutOfData;
    private int index;

    public int getIndex() {
        return index;
    }

    private List<OnDataLoadDoneListener> listeners;

    public void addOnDataLoadDoneListener (OnDataLoadDoneListener listener) {
        this.listeners.add(listener);
    }

    public boolean removeDataLoadDondListener(OnDataLoadDoneListener listener) {
        return listeners.remove(listener);
    }

    private SearchFriendData() {
        dataBuff = new ArrayList<>();
        isLoading = false;
        isOutOfData = false;
        listeners = new ArrayList<>();
        index = 0;
    }

    public static SearchFriendData getInstance() {
        if (searchFriendData == null) {
            searchFriendData = new SearchFriendData();
        }
        return searchFriendData;
    }

    public void notifyDataSetChange() {
        for (OnDataLoadDoneListener listener : listeners) {
            listener.onLoadDone();
        }
    }

    public boolean removeDataItem(int id) {
        Iterator<SearchFriendService.User> iterator = dataBuff.listIterator();
        while (iterator.hasNext()) {
            if (iterator.next().getId() == id) {
                iterator.remove();
                return true;
            }
        }
        return false;
    }

    private void getUsersFromServer() {
        RetrofitClient.getSearchFriendService().getUsers(UserAuth.getInstance().getUser().getHeaderAuthenToken())
                .enqueue(new Callback<List<SearchFriendService.User>>() {
            @Override
            public void onResponse(Call<List<SearchFriendService.User>> call, Response<List<SearchFriendService.User>> response) {
                if (response.body() != null) {
                    SearchFriendData.this.dataBuff = response.body();
                    if (response.body().size() < 6) {
                        SearchFriendData.this.isOutOfData = true;
                    }
                    notifyDataSetChange();
                }
                SearchFriendData.this.isLoading = false;
            }

            @Override
            public void onFailure(Call<List<SearchFriendService.User>> call, Throwable t) {
                t.printStackTrace();
                SearchFriendData.this.isLoading = false;
            }
        });
    }

    /**
     *
     * @return: user item
     */
    public User getUserData () {
        User newUser = null;

        // set first item to view and remove it from buffer
        if (!this.isBufferEmpty()) {
            increaseIndex();
            if (index < dataBuff.size()) {
                newUser = new User(dataBuff.get(index));
            }
        }
        if (this.isExhaustedBuff()) {
            this.loadData();
        }
        return newUser;
    }

    private void increaseIndex() {
        index++;
        if (index >= dataBuff.size()) {
            index = 0;
        }
    }

    public boolean isBufferEmpty() {
        return this.dataBuff.size() < 1;
    }

    public void loadData() {
        if (this.isLoading || this.isOutOfData) {
            return;
        }
        this.isLoading = true;
        getUsersFromServer();
    }

    public boolean isExhaustedBuff() {
        return this.dataBuff.size() < 5;
    }

    interface OnDataLoadDoneListener {
        void onLoadDone();
    }

}
