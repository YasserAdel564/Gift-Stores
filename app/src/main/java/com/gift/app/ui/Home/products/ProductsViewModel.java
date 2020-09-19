package com.gift.app.ui.Home.products;

import android.annotation.SuppressLint;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.blankj.utilcode.util.NetworkUtils;
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

public class ProductsViewModel extends ViewModel {
    UiStates state = new UiStates();
    MutableLiveData<UiStates> liveState = new MutableLiveData<>();
    List<Product> listStores;

    public int storeId ;


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

    }}