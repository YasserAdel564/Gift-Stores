package com.gift.app.ui.Home.stores;

import android.annotation.SuppressLint;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.blankj.utilcode.util.NetworkUtils;
import com.gift.app.App;
import com.gift.app.data.models.Department;
import com.gift.app.data.models.DepartmentsResponse;
import com.gift.app.data.models.Store;
import com.gift.app.data.models.StoresResponse;
import com.gift.app.data.storages.remote.RetrofitBuilder;
import com.gift.app.utils.UiStates;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class StoresViewModel extends ViewModel {
    UiStates state = new UiStates();
    MutableLiveData<UiStates> liveState = new MutableLiveData<>();
    List<Store> listStores;

    Boolean isOpen = false;
    public int departmentId;
    private String uid = App.getPreferencesHelper().getUid();


    @SuppressLint("CheckResult")
    public void getStores() {

        if (!NetworkUtils.isConnected()) {
            state.onNoConnection = true;
            liveState.postValue(state);
        } else {
            Observable<StoresResponse> observable = RetrofitBuilder.getRetrofit().getStores(departmentId, uid)
                    .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());

            Observer<StoresResponse> observer = new Observer<StoresResponse>() {
                @Override
                public void onSubscribe(Disposable d) {
                    state.onLoading = true;
                    liveState.postValue(state);
                }

                @Override
                public void onNext(StoresResponse value) {

                    if (value.getData().size() > 0) {
                        state.onSuccess = true;
                        listStores = value.getData();
                        liveState.postValue(state);
                    } else {
                        state.onEmpty = true;
                        liveState.postValue(state);
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