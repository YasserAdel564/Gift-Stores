package com.gift.app.ui.cart;

import android.annotation.SuppressLint;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.blankj.utilcode.util.NetworkUtils;
import com.gift.app.App;
import com.gift.app.data.models.CartResponse;
import com.gift.app.data.models.OrderResponse;
import com.gift.app.data.storages.remote.RetrofitBuilder;
import com.gift.app.utils.UiStates;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class CartViewModel extends ViewModel {
    public MutableLiveData<String> liveState = new MutableLiveData<>();
    public CartResponse response;
    private String mobile = App.getPreferencesHelper().getUserMobile();


    @SuppressLint("CheckResult")
    public void getCart() {

        if (!NetworkUtils.isConnected()) {
            liveState.postValue(UiStates.onLoading);
        } else {
            Observable<CartResponse> observable = RetrofitBuilder.getRetrofit().getCart(mobile)
                    .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());

            Observer<CartResponse> observer = new Observer<CartResponse>() {
                @Override
                public void onSubscribe(Disposable d) {
                    liveState.postValue(UiStates.onLoading);
                }

                @Override
                public void onNext(CartResponse value) {

                    if (value.getData().size() > 0) {
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


    //=======Confirm Order

    public MutableLiveData<String> liveStateOrder = new MutableLiveData<>();
    public OrderResponse responseOrder;

    @SuppressLint("CheckResult")
    public void addOrder() {

        if (!NetworkUtils.isConnected()) {
            liveStateOrder.postValue(UiStates.onNoConnection);
        } else {
            Observable<OrderResponse> observable = RetrofitBuilder.getRetrofit().postConfirmOrder(
                    RequestBody.create(MediaType.parse("text/plain"), mobile))
                    .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());

            Observer<OrderResponse> observer = new Observer<OrderResponse>() {
                @Override
                public void onSubscribe(Disposable d) {
                    liveStateOrder.postValue(UiStates.onLoading);
                }

                @Override
                public void onNext(OrderResponse value) {

                    if (value.getStatus()) {
                        responseOrder = value;
                        liveStateOrder.postValue(UiStates.onSuccess);
                    } else
                        liveStateOrder.postValue(UiStates.onEmpty);


                }

                @Override
                public void onError(Throwable e) {
                    liveStateOrder.postValue(UiStates.onError);

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