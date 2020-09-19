package com.gift.app.utils;

import android.app.Application;
import android.content.Context;

public class AppContext extends Application {


    private Context context;

    public Context getContext() {
        context = getApplicationContext();
        return context;
    }

}
