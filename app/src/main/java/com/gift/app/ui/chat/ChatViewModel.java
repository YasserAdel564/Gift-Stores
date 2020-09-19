package com.gift.app.ui.chat;

import android.annotation.SuppressLint;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.blankj.utilcode.util.NetworkUtils;
import com.gift.app.App;
import com.gift.app.data.models.ChatModel;
import com.gift.app.data.models.ChatResponse;
import com.gift.app.data.models.FavStoresResponse;
import com.gift.app.data.models.PostChatResponse;
import com.gift.app.data.models.Store;
import com.gift.app.data.storages.remote.RetrofitBuilder;
import com.gift.app.utils.UiStates;

import java.io.File;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class ChatViewModel extends ViewModel {
    UiStates state = new UiStates();
    public MutableLiveData<UiStates> liveState = new MutableLiveData<>();
    public List<ChatModel> List;
    private String phoneNumber = App.getPreferencesHelper().getUserMobile();


    @SuppressLint("CheckResult")
    public void getChat() {

        if (!NetworkUtils.isConnected()) {
            state.onNoConnection = true;
            liveState.postValue(state);
        } else {
            Observable<ChatResponse> observable = RetrofitBuilder.getRetrofit().getChat("45944",
                    phoneNumber
            )
                    .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());

            Observer<ChatResponse> observer = new Observer<ChatResponse>() {
                @Override
                public void onSubscribe(Disposable d) {
                    state.onLoading = true;
                    liveState.postValue(state);
                }

                @Override
                public void onNext(ChatResponse value) {

                    if (value.getData().size() > 0) {
                        state.onSuccess = true;
                        List = value.getData();
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


    UiStates statePost = new UiStates();
    public MutableLiveData<UiStates> liveStatePost = new MutableLiveData<>();
    public String chatMessage;
    public String chatPhoto;
    MultipartBody.Part body;

    @SuppressLint("CheckResult")
    public void postChat() {

        if (chatPhoto != null) {
            File file = new File("chatPhoto");
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            body = MultipartBody.Part.createFormData("image", file.getName(), requestFile);
        }

        if (!NetworkUtils.isConnected()) {
            statePost.onNoConnection = true;
            liveStatePost.postValue(statePost);
        } else {
            Observable<PostChatResponse> observable = RetrofitBuilder.getRetrofit().postChat(
                    RequestBody.create(MediaType.parse("text/plain"), phoneNumber),
                    RequestBody.create(MediaType.parse("text/plain"), chatMessage),
                    null
            )
                    .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
            Observer<PostChatResponse> observer = new Observer<PostChatResponse>() {
                @Override
                public void onSubscribe(Disposable d) {
                    statePost.onLoading = true;
                    liveStatePost.postValue(statePost);
                }

                @Override
                public void onNext(PostChatResponse value) {

                    if (value.getStatus()) {
                        statePost.onSuccess = true;
                    } else
                        statePost.onEmpty = true;

                    liveStatePost.postValue(state);

                    Log.e("addFavourites", value.getMsg());

                }

                @Override
                public void onError(Throwable e) {
                    statePost.onError = true;
                    liveStatePost.postValue(state);

                }

                @Override
                public void onComplete() {

                }
            };

            observable.subscribeWith(observer);

        }

    }
}