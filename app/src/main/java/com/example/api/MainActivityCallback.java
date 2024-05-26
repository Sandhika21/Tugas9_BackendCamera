package com.example.api;

import android.graphics.Bitmap;

public interface MainActivityCallback {
    public void updateImageView(Bitmap imageBitmap);
    public void showUploadButton();
}