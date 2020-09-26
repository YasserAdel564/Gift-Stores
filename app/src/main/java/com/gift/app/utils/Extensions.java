package com.gift.app.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;

import com.gift.app.R;
import com.google.android.material.snackbar.Snackbar;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Extensions {

    public static void noInternetSnakeBar(View view) {
        Snackbar.make(view, R.string.no_connection, Snackbar.LENGTH_LONG)
                .show();
    }

    public static void generalErrorSnakeBar(View view) {
        Snackbar.make(view, R.string.general_error, Snackbar.LENGTH_LONG)
                .show();
    }


    public static void generalSuccess(View view) {
        Snackbar.make(view, R.string.success, Snackbar.LENGTH_LONG)
                .show();
    }

    public static void Success(View view, String message) {
        Snackbar.make(view, message, Snackbar.LENGTH_LONG)
                .show();
    }


    public static void makeCall(Context context, String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + phoneNumber));
        context.startActivity(intent);
    }

    public static Boolean checkInServices() {
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");

        Date StartTime = null;
        Date EndTime = null;
        Date CurrentTime = null;
        try {
            StartTime = dateFormat.parse("08:00");
            EndTime = dateFormat.parse("21:58");
            CurrentTime = dateFormat.parse(dateFormat.format(new Date()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        assert CurrentTime != null;
        return CurrentTime.before(EndTime) && CurrentTime.after(StartTime);

    }


}
