package com.rsui.rs_network.storage;

import androidx.datastore.preferences.core.MutablePreferences;
import androidx.datastore.preferences.core.Preferences;
import androidx.datastore.rxjava3.RxDataStore;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;

public class PreferencesRepository {
    private final RxDataStore<Preferences> dataStore;

    public PreferencesRepository(RxDataStore<Preferences> dataStore) {
        this.dataStore = dataStore;
    }

    // Read operations
    public Flowable<String> getUsername() {
        return dataStore.data().map(preferences ->
                preferences.get(PreferenceKeys.USERNAME)
        );
    }

    public Flowable<Integer> getUserAge() {
        return dataStore.data().map(preferences ->
                preferences.get(PreferenceKeys.USER_AGE)
        ).map(age -> age != null ? age : 0);
    }

    public Flowable<Boolean> isLoggedIn() {
        return dataStore.data().map(preferences -> {
            Boolean value = preferences.get(PreferenceKeys.IS_LOGGED_IN);
            return value != null ? value : false;
        });
    }

    // Write operations
    public Completable setUsername(String username) {
        return dataStore.updateDataAsync(preferences -> {
            MutablePreferences mutablePreferences = preferences.toMutablePreferences();
            mutablePreferences.set(PreferenceKeys.USERNAME, username);
            return Single.just(mutablePreferences);
        }).ignoreElement();
    }

    public Completable setUserAge(int age) {
        return dataStore.updateDataAsync(preferences -> {
            MutablePreferences mutablePreferences = preferences.toMutablePreferences();
            mutablePreferences.set(PreferenceKeys.USER_AGE, age);
            return Single.just(mutablePreferences);
        }).ignoreElement();
    }

    public Completable setLoggedIn(boolean loggedIn) {
        return dataStore.updateDataAsync(preferences -> {
            MutablePreferences mutablePreferences = preferences.toMutablePreferences();
            mutablePreferences.set(PreferenceKeys.IS_LOGGED_IN, loggedIn);
            return Single.just(mutablePreferences);
        }).ignoreElement();
    }

    // Remove key
    public Completable removeKey(Preferences.Key<?> key) {
        return dataStore.updateDataAsync(preferences -> {
            MutablePreferences mutablePreferences = preferences.toMutablePreferences();
            mutablePreferences.remove(key);
            return Single.just(mutablePreferences);
        }).ignoreElement();
    }

    // Clear all data
    public Completable clearAll() {
        return dataStore.updateDataAsync(preferences -> {
            MutablePreferences mutablePreferences = preferences.toMutablePreferences();
            mutablePreferences.clear();
            return Single.just(mutablePreferences);
        }).ignoreElement();
    }
}
