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
import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiConsumer;
import io.reactivex.schedulers.Schedulers;

public class DepartmentsViewModel extends ViewModel {

    UiStates state = new UiStates();
    MutableLiveData<UiStates> liveState = new MutableLiveData<>();
    List<Department> listDepartments;

    Boolean isOpen = false;

    private String uid = App.getPreferencesHelper().getUid();
    private String firebasetoken = App.getPreferencesHelper().getFirebaseToken();



    @SuppressLint("CheckResult")
    public void getDepartments() {

        if (!NetworkUtils.isConnected()) {
            state.onNoConnection = true;
            liveState.postValue(state);
        } else {
            Observable<DepartmentsResponse> observable = RetrofitBuilder.getRetrofit().getDepartments(uid,firebasetoken)
                    .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());

            Observer<DepartmentsResponse> observer = new Observer<DepartmentsResponse>() {
                @Override
                public void onSubscribe(Disposable d) {
                    state.onLoading = true;
                    liveState.postValue(state);
                }

                @Override
                public void onNext(DepartmentsResponse value) {

                    if (value.getData().size() > 0) {
                        state.onSuccess = true;
                        listDepartments = value.getData();
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
                    Log.e("onComplete", "onComplete");

                }
            };

            observable.subscribeWith(observer);

        }

    }


}