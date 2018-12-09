package com.example.pk.wifinotes;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.preference.PreferenceManager;
import android.support.v4.os.ConfigurationCompat;

import java.util.Locale;

public class LocaleManager {

    private static final String LANGUAGE_ENGLISH = "en";
    private static final String LANGUAGE_POLISH = "pl";
    private static final String LANGUAGE_KEY = "language";

    public static Context setLocale(Context c) {
        return updateResources(c, getLanguage(c));
    }

    public static void changeLocale(Context c) {
        if(getLanguage(c).equalsIgnoreCase(LANGUAGE_ENGLISH)) {
            persistLanguage(c, LANGUAGE_POLISH);
            updateResources(c, LANGUAGE_POLISH);
        } else {
            persistLanguage(c, LANGUAGE_ENGLISH);
            updateResources(c, LANGUAGE_ENGLISH);
        }
    }

    private static String getLanguage(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(LANGUAGE_KEY, ConfigurationCompat.getLocales(Resources.getSystem().getConfiguration()).get(0).getLanguage());
    }

    private static void persistLanguage(Context context, String language) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putString(LANGUAGE_KEY, language).apply();
    }

    private static Context updateResources(Context context, String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);

        Resources res = context.getResources();
        Configuration config = new Configuration(res.getConfiguration());
        config.setLocale(locale);

        context = context.createConfigurationContext(config);

        return context;
    }
}
