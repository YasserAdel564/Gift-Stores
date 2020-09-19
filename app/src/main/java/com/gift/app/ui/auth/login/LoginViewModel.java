package com.gift.app.ui.auth.login;

import android.annotation.SuppressLint;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.blankj.utilcode.util.NetworkUtils;
import com.bumptech.glide.load.engine.Resource;
import com.gift.app.App;
import com.gift.app.data.models.AddFavouriteResponse;
import com.gift.app.data.models.AuthResponse;
import com.gift.app.data.storages.remote.RetrofitBuilder;
import com.gift.app.utils.UiStates;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class LoginViewModel extends ViewModel {


    Boolean isOpen = false;
    UiStates state = new UiStates();
    public MutableLiveData<UiStates> liveState = new MutableLiveData<>();
    public AuthResponse response;
    public String phoneNumber = null;

    @SuppressLint("CheckResult")
    public void login() {

        if (!NetworkUtils.isConnected()) {
            state.onNoConnection = true;
            liveState.postValue(state);
        } else {
            Observable<AuthResponse> observable = RetrofitBuilder.getRetrofit().postLogin(
                    RequestBody.create(MediaType.parse("text/plain"), phoneNumber))
                    .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());

            Observer<AuthResponse> observer = new Observer<AuthResponse>() {
                @Override
                public void onSubscribe(Disposable d) {
                    state.onLoading = true;
                    liveState.postValue(state);
                }

                @Override
                public void onNext(AuthResponse value) {

                    if (value.getStatus()) {
                        state.onSuccess = true;
                        liveState.postValue(state);
                        response = value;

                    } else {
                        state.onEmpty = true;
                        liveState.postValue(state);
                        response = value;
                    }


                }

                @Override
                public void onError(Throwable e) {
                    state.onError = true;
                    liveState.postValue(state);

                }

                @Override
                public void onComplete() {

                }
            };

            observable.subscribeWith(observer);

        }

    }


}