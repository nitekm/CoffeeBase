package ncodedev.coffeebase.ui.utility;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import androidx.preference.PreferenceManager;

import java.util.Locale;

public class ChangeLanguageHandler {

    private static final String LANGUAGE = "language";

    public static void translateIU(Activity activity) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activity);
        String chosenLanguage = preferences.getString(LANGUAGE, "en");
        Locale locale = new Locale(chosenLanguage);
        Resources resources = activity.getResources();
        Configuration configuration = resources.getConfiguration();
        configuration.setLocale(locale);
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
    }

    public static void changeLanguage(LanguageCode languageCode, Activity activity) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activity);
        preferences.edit().putString(LANGUAGE, languageCode.name()).apply();

        translateIU(activity);
        Intent intent = activity.getIntent();
        activity.finish();
        activity.startActivity(intent);
    }
}
