package com.gift.app.ui.Home.products;

import android.annotation.SuppressLint;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.blankj.utilcode.util.NetworkUtils;
import com.gift.app.App;
import com.gift.app.data.models.AddFavouriteResponse;
import com.gift.app.data.models.PostCartResponse;
import com.gift.app.data.models.Product;
import com.gift.app.data.models.ProductsResponse;
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
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class ProductsViewModel extends ViewModel {
    UiStates state = new UiStates();
    MutableLiveData<UiStates> liveState = new MutableLiveData<>();
    List<Product> listStores;

    public int storeId;


    @SuppressLint("CheckResult")
    public void getProducts() {

        if (!NetworkUtils.isConnected()) {
            state.onNoConnection = true;
            liveState.postValue(state);
        } else {
            Observable<ProductsResponse> observable = RetrofitBuilder.getRetrofit().getProducts(64)
                    .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());

            Observer<ProductsResponse> observer = new Observer<ProductsResponse>() {
                @Override
                public void onSubscribe(Disposable d) {
                    state.onLoading = true;
                    liveState.postValue(state);
                }

                @Override
                public void onNext(ProductsResponse value) {

                    if (value.getData().size() > 0) {
                        state.onSuccess = true;
                        listStores = value.getData();
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

    //=====Del Card


    UiStates stateCart = new UiStates();
    public MutableLiveData<UiStates> liveStateCart = new MutableLiveData<>();
    public PostCartResponse response;
    private String mobile = App.getPreferencesHelper().getUserMobile();
    public String productId = null;
    public String delivery_price = "10";
    public String product_count = "1";
    public String address = "45st";
    public String store_id = "64";
    public String department_id = "37";


    @SuppressLint("CheckResult")
    public void delCart() {

        if (!NetworkUtils.isConnected()) {
            stateCart.onNoConnection = true;
            liveStateCart.postValue(state);
        } else {
            Observable<PostCartResponse> observable = RetrofitBuilder.getRetrofit().postDelCart(
                    RequestBody.create(MediaType.parse("text/plain"), productId)
                    , RequestBody.create(MediaType.parse("text/plain"), mobile))
                    .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());

            Observer<PostCartResponse> observer = new Observer<PostCartResponse>() {
                @Override
                public void onSubscribe(Disposable d) {
                    stateCart.onLoading = true;
                    liveStateCart.postValue(stateCart);
                }

                @Override
                public void onNext(PostCartResponse value) {

                    if (value.getStatus()) {
                        stateCart.onSuccess = true;
                        response = value;
                    } else
                        stateCart.onEmpty = true;

                    liveStateCart.postValue(stateCart);

                    Log.e("addFavourites", value.getMsg());

                }

                @Override
                public void onError(Throwable e) {
                    stateCart.onError = true;
                    liveStateCart.postValue(stateCart);

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
            stateCart.onNoConnection = true;
            liveStateCart.postValue(state);
        } else {
            Observable<PostCartResponse> observable = RetrofitBuilder.getRetrofit().postAddCart(
                    RequestBody.create(MediaType.parse("text/plain"), mobile),
                    RequestBody.create(MediaType.parse("text/plain"), delivery_price),
                    RequestBody.create(MediaType.parse("text/plain"), productId),
                    RequestBody.create(MediaType.parse("text/plain"), product_count),
                    RequestBody.create(MediaType.parse("text/plain"), store_id),
                    RequestBody.create(MediaType.parse("text/plain"), department_id),
                    RequestBody.create(MediaType.parse("text/plain"), address)
            )
                    .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());

            Observer<PostCartResponse> observer = new Observer<PostCartResponse>() {
                @Override
                public void onSubscribe(Disposable d) {
                    stateCart.onLoading = true;
                    liveStateCart.postValue(stateCart);
                }

                @Override
                public void onNext(PostCartResponse value) {

                    if (value.getStatus()) {
                        stateCart.onSuccess = true;
                        response = value;
                    } else
                        stateCart.onEmpty = true;

                    liveStateCart.postValue(stateCart);

                    Log.e("addFavourites", value.getMsg());

                }

                @Override
                public void onError(Throwable e) {
                    stateCart.onError = true;
                    liveStateCart.postValue(stateCart);

                }

                @Override
                public void onComplete() {

                }
            };

            observable.subscribeWith(observer);

        }

    }


}

