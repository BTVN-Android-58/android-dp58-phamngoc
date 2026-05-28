package com.rsui.rs_network.storage;


import androidx.datastore.preferences.core.Preferences;
import androidx.datastore.preferences.core.PreferencesKeys;

public class PreferenceKeys {
    public static final Preferences.Key<String> USERNAME = PreferencesKeys.stringKey("username");
    public static final Preferences.Key<Integer> USER_AGE = PreferencesKeys.intKey("user_age");
    public static final Preferences.Key<Boolean> IS_LOGGED_IN = PreferencesKeys.booleanKey("is_logged_in");
    public static final Preferences.Key<String> THEME = PreferencesKeys.stringKey("theme");
    public static final Preferences.Key<Float> RATING = PreferencesKeys.floatKey("rating");
    public static final Preferences.Key<Long> LAST_LOGIN = PreferencesKeys.longKey("last_login");

    private PreferenceKeys() {}
}
