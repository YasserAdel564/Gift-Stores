package com.gift.app.ui.auth.register;

import android.annotation.SuppressLint;

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
    public MutableLiveData<String> liveState = new MutableLiveData<>();
    public AuthResponse response;
    public String country_code = "+2";
    public String name = null;
    public String mobile = null;


    @SuppressLint("CheckResult")
    public void register() {

        if (!NetworkUtils.isConnected()) {
            liveState.postValue(UiStates.onNoConnection);
        } else {
            Observable<AuthResponse> observable = RetrofitBuilder.getRetrofit().postRegister(
                    RequestBody.create(MediaType.parse("text/plain"), country_code),
                    RequestBody.create(MediaType.parse("text/plain"), name),
                    RequestBody.create(MediaType.parse("text/plain"), mobile))
                    .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());

            Observer<AuthResponse> observer = new Observer<AuthResponse>() {
                @Override
                public void onSubscribe(Disposable d) {
                    liveState.postValue(UiStates.onLoading);
                }

                @Override
                public void onNext(AuthResponse value) {

                    if (value.getStatus()) {
                        response = value;
                        liveState.postValue(UiStates.onSuccess);
                    } else
                        liveState.postValue(UiStates.onLoading);

                }

                @Override
                public void onError(Throwable e) {
                    liveState.postValue(UiStates.onError);

                }

                @Override
                public void onComplete() {

                }
            };

            observable.subscribeWith(observer);

        }

    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }
}