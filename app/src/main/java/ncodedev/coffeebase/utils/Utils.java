package ncodedev.coffeebase.utils;

import ncodedev.coffeebase.BuildConfig;

public final class Utils {
    private Utils() {}

    public static String imageDownloadUrl(String imageName) {
        return BuildConfig.BaseURL + "downloadFile/" + imageName;
    }
}
