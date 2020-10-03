package com.gift.app.ui.favourites;

import android.annotation.SuppressLint;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.blankj.utilcode.util.NetworkUtils;
import com.gift.app.App;
import com.gift.app.data.models.AddFavouriteResponse;
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
    public MutableLiveData<String> liveState = new MutableLiveData<>();
    public AddFavouriteResponse response;
    private String uid = App.getPreferencesHelper().getUid();
    public Integer storeId = null;


    @SuppressLint("CheckResult")
    public void addFavourites() {

        if (!NetworkUtils.isConnected()) {
            liveState.postValue(UiStates.onNoConnection);
        } else {
            Observable<AddFavouriteResponse> observable = RetrofitBuilder.getRetrofit().postAddFav(
                    storeId
                    , RequestBody.create(MediaType.parse("text/plain"), uid))
                    .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());

            Observer<AddFavouriteResponse> observer = new Observer<AddFavouriteResponse>() {
                @Override
                public void onSubscribe(Disposable d) {
                    liveState.postValue(UiStates.onLoading);
                }

                @Override
                public void onNext(AddFavouriteResponse value) {

                    if (value.getStatus()) {
                        response = value;
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

    @SuppressLint("CheckResult")
    public void delFavourites() {

        if (!NetworkUtils.isConnected()) {
            liveState.postValue(UiStates.onSuccess);
        } else {
            Observable<AddFavouriteResponse> observable = RetrofitBuilder.getRetrofit().
                    postDeleteFav(storeId, RequestBody.create(MediaType.parse("text/plain"), uid))
                    .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());

            Observer<AddFavouriteResponse> observer = new Observer<AddFavouriteResponse>() {
                @Override
                public void onSubscribe(Disposable d) {
                    liveState.postValue(UiStates.onLoading);
                }

                @Override
                public void onNext(AddFavouriteResponse value) {

                    if (value.getStatus()) {
                        response = value;
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


    //===============Get Favourites


    public MutableLiveData<String> liveStateFav = new MutableLiveData<>();
    public List<Store> favList;


    @SuppressLint("CheckResult")
    public void getFavourites() {

        if (!NetworkUtils.isConnected()) {
            liveStateFav.postValue(UiStates.onNoConnection);
        } else {
            Observable<FavStoresResponse> observable = RetrofitBuilder.getRetrofit().getFavStores(
                    uid)
                    .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());

            Observer<FavStoresResponse> observer = new Observer<FavStoresResponse>() {
                @Override
                public void onSubscribe(Disposable d) {
                    liveStateFav.postValue(UiStates.onLoading);
                }

                @Override
                public void onNext(FavStoresResponse value) {

                    if (value.getData().size() > 0) {
                        favList = value.getData();
                        liveStateFav.postValue(UiStates.onSuccess);
                    } else
                        liveStateFav.postValue(UiStates.onEmpty);


                }

                @Override
                public void onError(Throwable e) {
                    liveStateFav.postValue(UiStates.onError);

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