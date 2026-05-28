package com.rsui.rs_network;

import android.app.Application;

import androidx.datastore.preferences.core.Preferences;
import androidx.datastore.preferences.rxjava3.RxPreferenceDataStoreBuilder;
import androidx.datastore.rxjava3.RxDataStore;

import com.rsui.rs_network.network.ApiClient;

public class MyApplication extends Application {
    RxDataStore<Preferences> dataStore;

    @Override
    public void onCreate() {
        super.onCreate();

        // Khởi tạo ApiClient
        ApiClient.initialize(this);

        // Khởi tạo DataStore
        dataStore = new RxPreferenceDataStoreBuilder(this, "my_app_preferences").build();
    }

    public RxDataStore<Preferences> getDataStore() {
        return dataStore;
    }
}
