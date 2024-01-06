package ncodedev.coffeebase.ui.utility;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import androidx.preference.PreferenceManager;

import java.util.Locale;

import static ncodedev.coffeebase.ui.utility.SharedPreferencesNames.LANGUAGE;

public class ChangeLanguageHandler {

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
//        Intent intent = activity.getIntent();
//        activity.finish();
//        activity.startActivity(intent);
    }

    public static LanguageCode getLanguageCodeByValue(String value) {
        return switch (value) {
            case "\uD83C\uDDEC\uD83C\uDDE7 English" -> LanguageCode.EN;
            case "\uD83C\uDDEA\uD83C\uDDF8 Español" -> LanguageCode.ES;
            case "\uD83C\uDDE9\uD83C\uDDEA Deutsch" -> LanguageCode.DE;
            case "\uD83C\uDDEB\uD83C\uDDF7 Français" -> LanguageCode.FR;
            case "\uD83C\uDDF5\uD83C\uDDF1 Polski" -> LanguageCode.PL;
            case "\uD83C\uDDF5\uD83C\uDDF9 Português" -> LanguageCode.PT;
            default -> LanguageCode.EN;
        };
    }
}
