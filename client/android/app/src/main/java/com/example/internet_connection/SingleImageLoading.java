package com.example.internet_connection;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;

import com.example.model.User;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class SingleImageLoading extends AsyncTask<Void, Void, Object[]> {
    private int userId;
    private int imageNumber;
    private OnImageLoadDoneListener onImageLoadDoneListener;

    public SingleImageLoading(int userId, int imageNumber, OnImageLoadDoneListener onImageLoadDoneListener) {
        this.userId = userId;
        this.imageNumber = imageNumber;
        this.onImageLoadDoneListener = onImageLoadDoneListener;
    }

    @Override
    protected Object[] doInBackground(Void... voids) {
        try {
            InputStream is = (InputStream) new URL(User.getImageUrl(userId, imageNumber)).getContent();
            Drawable d = Drawable.createFromStream(is, "avatar");
            return new Object[]{d, imageNumber};
        } catch (MalformedURLException e) {

        } catch (IOException e) {

        }
        return null;
    }

    @Override
    protected void onPostExecute(Object[] drawable) {
        super.onPostExecute(drawable);
        try {
            onImageLoadDoneListener.onImageLoadDone((Drawable) drawable[0], (int) drawable[1]);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
