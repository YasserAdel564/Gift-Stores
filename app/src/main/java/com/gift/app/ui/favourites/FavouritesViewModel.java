package com.gift.app.ui.favourites;

import android.annotation.SuppressLint;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.blankj.utilcode.util.NetworkUtils;
import com.gift.app.App;
import com.gift.app.data.models.AddFavouriteResponse;
import com.gift.app.data.models.Department;
import com.gift.app.data.models.DepartmentsResponse;
import com.gift.app.data.models.FavStoresResponse;
import com.gift.app.data.models.Store;
import com.gift.app.data.storages.remote.RetrofitBuilder;
import com.gift.app.utils.UiStates;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class FavouritesViewModel extends ViewModel {

    Boolean isOpen = false;
    UiStates state = new UiStates();
    public MutableLiveData<UiStates> liveState = new MutableLiveData<>();
    public AddFavouriteResponse response;
    private String uid = App.getPreferencesHelper().getUid();
    public Integer storeId = null;


    @SuppressLint("CheckResult")
    public void addFavourites() {

        if (!NetworkUtils.isConnected()) {
            state.onNoConnection = true;
            liveState.postValue(state);
        } else {
            Observable<AddFavouriteResponse> observable = RetrofitBuilder.getRetrofit().postAddFav(
                    storeId
                    , RequestBody.create(MediaType.parse("text/plain"), uid))
                    .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());

            Observer<AddFavouriteResponse> observer = new Observer<AddFavouriteResponse>() {
                @Override
                public void onSubscribe(Disposable d) {
                    state.onLoading = true;
                    liveState.postValue(state);
                }

                @Override
                public void onNext(AddFavouriteResponse value) {

                    if (value.getStatus()) {
                        state.onSuccess = true;
                        response = value;
                    } else
                        state.onEmpty = true;

                    liveState.postValue(state);

                    Log.e("addFavourites", value.getMsg());

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

    @SuppressLint("CheckResult")
    public void delFavourites() {

        if (!NetworkUtils.isConnected()) {
            state.onNoConnection = true;
            liveState.postValue(state);
        } else {
            Observable<AddFavouriteResponse> observable = RetrofitBuilder.getRetrofit().
                    postDeleteFav(storeId, RequestBody.create(MediaType.parse("text/plain"), uid))
                    .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());

            Observer<AddFavouriteResponse> observer = new Observer<AddFavouriteResponse>() {
                @Override
                public void onSubscribe(Disposable d) {
                    state.onLoading = true;
                    liveState.postValue(state);
                }

                @Override
                public void onNext(AddFavouriteResponse value) {

                    if (value.getStatus()) {
                        state.onSuccess = true;
                        response = value;
                    } else
                        state.onEmpty = true;

                    liveState.postValue(state);
                    Log.e("delFavourites", value.getMsg());

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


    //===============Get Favourites


    UiStates stateFav = new UiStates();
    public MutableLiveData<UiStates> liveStateFav = new MutableLiveData<>();
    public List<Store> favList;


    @SuppressLint("CheckResult")
    public void getFavourites() {

        if (!NetworkUtils.isConnected()) {
            stateFav.onNoConnection = true;
            liveStateFav.postValue(stateFav);
        } else {
            Observable<FavStoresResponse> observable = RetrofitBuilder.getRetrofit().getFavStores(
                    uid)
                    .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());

            Observer<FavStoresResponse> observer = new Observer<FavStoresResponse>() {
                @Override
                public void onSubscribe(Disposable d) {
                    stateFav.onLoading = true;
                    liveStateFav.postValue(stateFav);
                }

                @Override
                public void onNext(FavStoresResponse value) {

                    if (value.getData().size() > 0) {
                        stateFav.onSuccess = true;
                        favList = value.getData();
                    } else
                        stateFav.onEmpty = true;

                    liveStateFav.postValue(stateFav);

                    Log.e("addFavourites", value.getMsg());

                }

                @Override
                public void onError(Throwable e) {
                    stateFav.onError = true;
                    liveStateFav.postValue(stateFav);

                }

                @Override
                public void onComplete() {

                }
            };

            observable.subscribeWith(observer);

        }

    }
}