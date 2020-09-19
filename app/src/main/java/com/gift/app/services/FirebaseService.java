package com.gift.app.services;

import android.util.Log;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.DeviceUtils;
import com.gift.app.App;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class FirebaseService extends FirebaseMessagingService {

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        App.getPreferencesHelper().setFirebaseToken(s);
        App.getPreferencesHelper().setUid(DeviceUtils.getAndroidID());
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
    }

}
