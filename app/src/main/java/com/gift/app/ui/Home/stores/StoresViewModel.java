package com.gift.app.ui.Home.stores;

import android.annotation.SuppressLint;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.blankj.utilcode.util.NetworkUtils;
import com.gift.app.App;
import com.gift.app.data.models.Store;
import com.gift.app.data.models.StoresResponse;
import com.gift.app.data.storages.remote.RetrofitBuilder;
import com.gift.app.utils.UiStates;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class StoresViewModel extends ViewModel {

    MutableLiveData<String> liveState = new MutableLiveData<>();
    ArrayList<Store> listStores =new ArrayList<>();

    Boolean isOpen = false;
    public int departmentId;
    private String uid = App.getPreferencesHelper().getUid();


    @SuppressLint("CheckResult")
    public void getStores() {

        if (!NetworkUtils.isConnected()) {
            liveState.postValue(UiStates.onNoConnection);
        } else {
            Observable<StoresResponse> observable = RetrofitBuilder.getRetrofit().getStores(departmentId, uid)
                    .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());

            Observer<StoresResponse> observer = new Observer<StoresResponse>() {
                @Override
                public void onSubscribe(Disposable d) {
                    liveState.postValue(UiStates.onLoading);
                }

                @Override
                public void onNext(StoresResponse value) {
                    listStores.clear();
                    listStores.addAll(value.getData());
                    if (listStores.size() > 0) {
                        liveState.postValue(UiStates.onSuccess);
                        Log.e("xxxxxxxxx","xxxxxxxxxx");
                    } else {
                        liveState.postValue(UiStates.onEmpty);
                    }
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
}