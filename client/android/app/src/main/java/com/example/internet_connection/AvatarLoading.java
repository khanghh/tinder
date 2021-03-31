package com.example.internet_connection;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.view.View;

import com.example.model.User;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.MalformedURLException;
import java.net.URL;

public class AvatarLoading extends AsyncTask<Void, Void, Object[]> {

    private int userId;
    private WeakReference<ShimmerFrameLayout> loadingManagement;
    private OnImageLoadDoneListener onImageLoadDoneListener;

    public void setLoadingManagement(ShimmerFrameLayout loadingManagement) {
        this.loadingManagement = new WeakReference<>(loadingManagement);
    }

    public AvatarLoading(int userId, ShimmerFrameLayout loadingManagement, OnImageLoadDoneListener onImageLoadDoneListener) {
        this.userId = userId;
        this.onImageLoadDoneListener = onImageLoadDoneListener;
        this.loadingManagement = new WeakReference<>(loadingManagement);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        ShimmerFrameLayout loading = loadingManagement.get();
        if (loading != null) {
            loading.startShimmerAnimation();
            loading.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected Object[] doInBackground(Void... voids) {
        for (int i = 1; i < 6; i++) {
            try {
                InputStream is = (InputStream) new URL(User.getImageUrl(userId, i)).getContent();
                Drawable d = Drawable.createFromStream(is, "avatar");
                return new Object[]{d, i};
            } catch (MalformedURLException e) {

            } catch (IOException e) {
            }
        }

        // get default avatar
        try {
            InputStream is = (InputStream) new URL(User.getDefaultAvatarUrl("male")).getContent();
            Drawable d = Drawable.createFromStream(is, "avatar");
            return new Object[]{d, 1};
        } catch (MalformedURLException e) {

        } catch (IOException e) {
        }
        return null;
    }

    @Override
    protected void onPostExecute(Object[] drawable) {
        super.onPostExecute(drawable);
        try {
            onImageLoadDoneListener.onImageLoadDone((Drawable) drawable[0], (int)drawable[1]);
        } catch (Exception e) {

        }
        ShimmerFrameLayout loading = loadingManagement.get();
        if (loading != null) {
            loading.stopShimmerAnimation();
            loading.setVisibility(View.GONE);
        }
    }

}
