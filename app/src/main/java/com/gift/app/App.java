package com.gift.app;

import android.app.Application;
import com.gift.app.data.storages.local.SharedPreference;
import com.gift.app.data.storages.remote.RetrofitBuilder;


public class App extends Application {

    private static App mInstance;
    private static SharedPreference preferencesHelper;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        createPreferencesHelper();
        RetrofitBuilder.createRetrofit();
    }


    private void createPreferencesHelper() {
        preferencesHelper = new SharedPreference(this);
    }

    public static SharedPreference getPreferencesHelper() {
        return preferencesHelper;
    }


}
