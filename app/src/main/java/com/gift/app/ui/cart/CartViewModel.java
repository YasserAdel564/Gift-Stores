package com.gift.app.ui.cart;

import android.annotation.SuppressLint;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.blankj.utilcode.util.NetworkUtils;
import com.gift.app.App;
import com.gift.app.data.models.CartModel;
import com.gift.app.data.models.CartResponse;
import com.gift.app.data.models.OrderResponse;
import com.gift.app.data.models.StoresResponse;
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

public class CartViewModel extends ViewModel {
    UiStates state = new UiStates();
    public MutableLiveData<UiStates> liveState = new MutableLiveData<>();
    public CartResponse response;
    private String mobile = App.getPreferencesHelper().getUserMobile();


    @SuppressLint("CheckResult")
    public void getCart() {

        if (!NetworkUtils.isConnected()) {
            state.onNoConnection = true;
            liveState.postValue(state);
        } else {
            Observable<CartResponse> observable = RetrofitBuilder.getRetrofit().getCart(mobile)
                    .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());

            Observer<CartResponse> observer = new Observer<CartResponse>() {
                @Override
                public void onSubscribe(Disposable d) {
                    state.onLoading = true;
                    liveState.postValue(state);
                }

                @Override
                public void onNext(CartResponse value) {

                    if (value.getData().size() > 0) {
                        state.onSuccess = true;
                        response = value;
                    } else
                        state.onEmpty = true;

                    liveState.postValue(state);

                    Log.e("addourites", value.getMsg());

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


    //=======Confirm Order

    UiStates stateOrder = new UiStates();
    public MutableLiveData<UiStates> liveStateOrder = new MutableLiveData<>();
    public OrderResponse responseOrder;

    @SuppressLint("CheckResult")
    public void addOrder() {

        if (!NetworkUtils.isConnected()) {
            stateOrder.onNoConnection = true;
            liveStateOrder.postValue(stateOrder);
        } else {
            Observable<OrderResponse> observable = RetrofitBuilder.getRetrofit().postConfirmOrder(
                    RequestBody.create(MediaType.parse("text/plain"), mobile))
                    .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());

            Observer<OrderResponse> observer = new Observer<OrderResponse>() {
                @Override
                public void onSubscribe(Disposable d) {
                    stateOrder.onLoading = true;
                    liveStateOrder.postValue(stateOrder);
                }

                @Override
                public void onNext(OrderResponse value) {

                    if (value.getStatus()) {
                        stateOrder.onSuccess = true;
                        responseOrder = value;
                    } else
                        stateOrder.onEmpty = true;

                    liveStateOrder.postValue(stateOrder);


                }

                @Override
                public void onError(Throwable e) {
                    stateOrder.onError = true;
                    liveStateOrder.postValue(stateOrder);

                }

                @Override
                public void onComplete() {

                }
            };

            observable.subscribeWith(observer);

        }

    }


}