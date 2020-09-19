package com.gift.app.ui.auth.register;

import android.annotation.SuppressLint;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.blankj.utilcode.util.NetworkUtils;
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

public class RegisterViewModel extends ViewModel {

    Boolean isOpen = false;
    UiStates state = new UiStates();
    public MutableLiveData<UiStates> liveState = new MutableLiveData<>();
    public AuthResponse response;
    public String country_code = "+2";
    public String name = null;
    public String mobile = null;


    @SuppressLint("CheckResult")
    public void register() {

        if (!NetworkUtils.isConnected()) {
            state.onNoConnection = true;
            liveState.postValue(state);
        } else {
            Observable<AuthResponse> observable = RetrofitBuilder.getRetrofit().postRegister(
                    RequestBody.create(MediaType.parse("text/plain"), country_code),
                    RequestBody.create(MediaType.parse("text/plain"), name),
                    RequestBody.create(MediaType.parse("text/plain"), mobile))
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
                        response = value;
                    } else
                        state.onEmpty = true;

                    liveState.postValue(state);

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