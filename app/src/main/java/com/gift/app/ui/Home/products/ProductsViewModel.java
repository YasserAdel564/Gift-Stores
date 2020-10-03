package com.gift.app.ui.Home.products;

import android.annotation.SuppressLint;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.blankj.utilcode.util.NetworkUtils;
import com.gift.app.App;
import com.gift.app.data.models.PostCartResponse;
import com.gift.app.data.models.Product;
import com.gift.app.data.models.ProductsResponse;
import com.gift.app.data.storages.remote.RetrofitBuilder;
import com.gift.app.utils.UiStates;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class ProductsViewModel extends ViewModel {
    MutableLiveData<String> liveState = new MutableLiveData<>();
    ArrayList<Product> listStores = new ArrayList<>();
    public int storeId;

    @SuppressLint("CheckResult")
    public void getProducts() {

        if (!NetworkUtils.isConnected()) {
            liveState.postValue(UiStates.onNoConnection);
        } else {
            Observable<ProductsResponse> observable = RetrofitBuilder.getRetrofit().getProducts(storeId)
                    .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());

            Observer<ProductsResponse> observer = new Observer<ProductsResponse>() {
                @Override
                public void onSubscribe(Disposable d) {
                    liveState.postValue(UiStates.onLoading);
                }

                @Override
                public void onNext(ProductsResponse value) {
                    listStores.clear();
                    listStores.addAll(value.getData());

                    if (listStores.size() > 0)
                        liveState.postValue(UiStates.onSuccess);
                    else
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

    //=====Del Card


    public MutableLiveData<String> liveStateCart = new MutableLiveData<>();
    public PostCartResponse response;
    private String mobile = App.getPreferencesHelper().getUserMobile();
    public String address = App.getPreferencesHelper().getUserAddress();
    public String productId;
    public String product_count = "1";
    public String department_id;


    @SuppressLint("CheckResult")
    public void delCart() {

        if (!NetworkUtils.isConnected()) {
            liveStateCart.postValue(UiStates.onNoConnection);
        } else {
            Observable<PostCartResponse> observable = RetrofitBuilder.getRetrofit().postDelCart(
                    RequestBody.create(MediaType.parse("text/plain"), productId)
                    , RequestBody.create(MediaType.parse("text/plain"), mobile))
                    .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());

            Observer<PostCartResponse> observer = new Observer<PostCartResponse>() {
                @Override
                public void onSubscribe(Disposable d) {
                    liveStateCart.postValue(UiStates.onLoading);
                }

                @Override
                public void onNext(PostCartResponse value) {

                    if (value.getStatus()) {
                        response = value;
                        liveStateCart.postValue(UiStates.onSuccess);
                    } else
                        liveStateCart.postValue(UiStates.onEmpty);
                }

                @Override
                public void onError(Throwable e) {
                    liveStateCart.postValue(UiStates.onError);
                }

                @Override
                public void onComplete() {

                }
            };
            observable.subscribeWith(observer);

        }

    }


    //==Add Cart


    @SuppressLint("CheckResult")
    public void addCart() {

        if (!NetworkUtils.isConnected()) {
            liveStateCart.postValue(UiStates.onNoConnection);
        } else {
            Observable<PostCartResponse> observable = RetrofitBuilder.getRetrofit().postAddCart(
                    RequestBody.create(MediaType.parse("text/plain"), mobile),
                    RequestBody.create(MediaType.parse("text/plain"), productId),
                    RequestBody.create(MediaType.parse("text/plain"), product_count),
                    RequestBody.create(MediaType.parse("text/plain"), String.valueOf(storeId)),
                    RequestBody.create(MediaType.parse("text/plain"), department_id),
                    RequestBody.create(MediaType.parse("text/plain"), address)
            ).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());

            Observer<PostCartResponse> observer = new Observer<PostCartResponse>() {
                @Override
                public void onSubscribe(Disposable d) {
                    liveStateCart.postValue(UiStates.onLoading);
                }

                @Override
                public void onNext(PostCartResponse value) {

                    if (value.getStatus()) {
                        response = value;
                        liveStateCart.postValue(UiStates.onSuccess);
                    } else
                        liveStateCart.postValue(UiStates.onEmpty);
                }
                @Override
                public void onError(Throwable e) {
                    liveStateCart.postValue(UiStates.onError);
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

