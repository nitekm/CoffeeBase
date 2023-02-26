package com.ncodedev.coffeebase.utils;

import com.ncodedev.coffeebase.BuildConfig;

public final class Utils {
    private Utils() {}

    public static String imageDownloadUrl(String imageName) {
        return BuildConfig.BaseURL + "downloadFile/" + imageName;
    }

}
