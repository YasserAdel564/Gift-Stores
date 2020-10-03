package com.gift.app.data.storages.local;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreference {

    private static SharedPreferences mSharedPreferences;
    private static final String PREF_FILE_NAME = "app_pref_file";
    private static final String PREF_KEY_UID = "uid";
    private static final String PREF_KEY_Mobile = "mobile";
    private static final String PREF_KEY_FirebaseToken = "token";
    private static final String PREF_KEY_Language = "language";
    private static final String PREF_KEY_SHOW_INTRO = "intro";
    private static final String PREF_KEY_USER_NAME = "name";
    private static final String PREF_KEY_USER_Address = "address";
    private static final String PREF_KEY_LOGIN = "isLogin";
    private static final String PREF_OUT_OF_SERVICES = "outServices";


    public SharedPreference(Context context) {
        mSharedPreferences = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
    }

    public void setFirebaseToken(String firebaseToken) {
        mSharedPreferences.edit().putString(PREF_KEY_FirebaseToken, firebaseToken).apply();
    }

    public String getFirebaseToken() {
        return mSharedPreferences.getString(PREF_KEY_FirebaseToken, null);
    }

    public void setUid(String uid) {
        mSharedPreferences.edit().putString(PREF_KEY_UID, uid).apply();
    }

    public String getUid() {
        return mSharedPreferences.getString(PREF_KEY_UID, null);
    }

    public void setLanguage(String language) {
        mSharedPreferences.edit().putString(PREF_KEY_Language, language).apply();
    }

    public String getLanguage() {
        return mSharedPreferences.getString(PREF_KEY_Language, null);
    }

    public void setShowIntro(Boolean showIntro) {
        mSharedPreferences.edit().putBoolean(PREF_KEY_SHOW_INTRO, showIntro).apply();
    }

    public Boolean getShowIntro() {
        return mSharedPreferences.getBoolean(PREF_KEY_SHOW_INTRO, false);
    }


    public void setUserName(String userName) {
        mSharedPreferences.edit().putString(PREF_KEY_USER_NAME, userName).apply();
    }

    public String getUserName() {
        return mSharedPreferences.getString(PREF_KEY_USER_NAME, null);
    }
    public void setUserAddress(String address) {
        mSharedPreferences.edit().putString(PREF_KEY_USER_Address, address).apply();
    }

    public String getUserAddress() {
        return mSharedPreferences.getString(PREF_KEY_USER_Address, "");
    }

    public void setIsLogin(Boolean showIntro) {
        mSharedPreferences.edit().putBoolean(PREF_KEY_LOGIN, showIntro).apply();
    }

    public Boolean getIsLogin() {
        return mSharedPreferences.getBoolean(PREF_KEY_LOGIN, false);
    }


    public void setUserMobile(String mobile) {
        mSharedPreferences.edit().putString(PREF_KEY_Mobile, mobile).apply();
    }

    public String getUserMobile() {
        return mSharedPreferences.getString(PREF_KEY_Mobile, "");
    }

    public void setInServices(Boolean inServices) {
        mSharedPreferences.edit().putBoolean(PREF_OUT_OF_SERVICES, inServices).apply();
    }

    public Boolean getInServices() {
        return mSharedPreferences.getBoolean(PREF_OUT_OF_SERVICES, true);
    }


}
