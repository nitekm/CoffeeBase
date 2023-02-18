package com.ncodedev.coffeebase.utils;

import android.app.Activity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import com.ncodedev.coffeebase.BuildConfig;

public final class Utils {
    private Utils() {}

    public static void showProgressBar(ProgressBar progressBar, Activity activity) {
        progressBar.setVisibility(View.VISIBLE);
        activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
    }

    public static void hideProgressBar(ProgressBar progressBar, Activity activity) {
        progressBar.setVisibility(View.INVISIBLE);
        activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
    }

    public static String imageDownloadUrl(String imageName) {
        return BuildConfig.BaseURL + "downloadFile/" + imageName;
    }

}
