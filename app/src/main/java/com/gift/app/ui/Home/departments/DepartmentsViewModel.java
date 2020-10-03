package com.gift.app.ui.Home.departments;

import android.annotation.SuppressLint;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.blankj.utilcode.util.NetworkUtils;
import com.gift.app.App;
import com.gift.app.data.models.Department;
import com.gift.app.data.models.DepartmentsResponse;
import com.gift.app.data.storages.remote.RetrofitBuilder;
import com.gift.app.utils.UiStates;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class DepartmentsViewModel extends ViewModel {

    MutableLiveData<String> liveState = new MutableLiveData<>();
    List<Department> listDepartments;
    Boolean isOpen = false;
    private String uid = App.getPreferencesHelper().getUid();
    private String firebasetoken = App.getPreferencesHelper().getFirebaseToken();
    private String mobile = App.getPreferencesHelper().getUserMobile();


    @SuppressLint("CheckResult")
    public void getDepartments() {

        if (!NetworkUtils.isConnected()) {
            liveState.postValue(UiStates.onNoConnection);
        } else {
            Observable<DepartmentsResponse> observable = RetrofitBuilder.getRetrofit().getDepartments(uid, firebasetoken, mobile)
                    .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());

            Observer<DepartmentsResponse> observer = new Observer<DepartmentsResponse>() {
                @Override
                public void onSubscribe(Disposable d) {
                    liveState.postValue(UiStates.onLoading);
                }

                @Override
                public void onNext(DepartmentsResponse value) {

                    if (value.getData().size() > 0) {
                        listDepartments = value.getData();
                        liveState.postValue(UiStates.onSuccess);
                    } else
                        liveState.postValue(UiStates.onEmpty);

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