package com.gift.app.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.gift.app.App;
import com.gift.app.R;
import com.gift.app.data.models.DepartmentsResponse;
import com.gift.app.databinding.ActivityMainBinding;
import com.gift.app.utils.Constants;
import com.gift.app.utils.MyContextWrapper;


public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);


    }

    @Override
    protected void attachBaseContext(Context newBase) {
        if (App.getPreferencesHelper().getLanguage() == null)
            super.attachBaseContext(MyContextWrapper.wrap(newBase, Constants.ARABIC));
        else
            super.attachBaseContext(MyContextWrapper.wrap(newBase, App.getPreferencesHelper().getLanguage()));

    }
}