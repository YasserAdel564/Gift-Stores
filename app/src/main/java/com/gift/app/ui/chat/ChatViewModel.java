package com.gift.app.ui.chat;

import android.annotation.SuppressLint;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.blankj.utilcode.util.NetworkUtils;
import com.gift.app.App;
import com.gift.app.data.models.ChatModel;
import com.gift.app.data.models.ChatResponse;
import com.gift.app.data.models.PostChatResponse;
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

    Boolean isOpen = false;
    public MutableLiveData<String> liveState = new MutableLiveData<>();
    public List<ChatModel> List;
    private String phoneNumber = App.getPreferencesHelper().getUserMobile();

    @SuppressLint("CheckResult")
    public void getChat() {

        if (!NetworkUtils.isConnected()) {
            liveState.postValue(UiStates.onNoConnection);
        } else {
            Observable<ChatResponse> observable = RetrofitBuilder.getRetrofit().getChat("45944",
                    phoneNumber
            )
                    .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());

            Observer<ChatResponse> observer = new Observer<ChatResponse>() {
                @Override
                public void onSubscribe(Disposable d) {
                    liveState.postValue(UiStates.onLoading);
                }

                @Override
                public void onNext(ChatResponse value) {

                    if (value.getData().size() > 0) {
                        List = value.getData();
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


    public MutableLiveData<String> liveStatePost = new MutableLiveData<>();
    public String chatMessage;
    public String chatPhoto;
    MultipartBody.Part body;


    @SuppressLint("CheckResult")
    public void postChat() {

        if (chatPhoto != null) {
            File file = new File(chatPhoto);
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            body = MultipartBody.Part.createFormData("photo", file.getName(), requestFile);
        }

        if (!NetworkUtils.isConnected()) {
            liveStatePost.postValue(UiStates.onNoConnection);
        } else {
            Observable<PostChatResponse> observable = RetrofitBuilder.getRetrofit().postChat(
                    RequestBody.create(MediaType.parse("text/plain"), phoneNumber),
                    RequestBody.create(MediaType.parse("text/plain"), chatMessage),
                    body
            )
                    .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
            Observer<PostChatResponse> observer = new Observer<PostChatResponse>() {
                @Override
                public void onSubscribe(Disposable d) {
                    liveStatePost.postValue(UiStates.onLoading);
             }

                @Override
                public void onNext(PostChatResponse value) {
                    if (value.getStatus())
                        liveStatePost.postValue(UiStates.onSuccess);
                    else
                        liveStatePost.postValue(UiStates.onEmpty);

                }

                @Override
                public void onError(Throwable e) {
                    liveStatePost.postValue(UiStates.onError);

                }

                @Override
                public void onComplete() {

                }
            };

            observable.subscribeWith(observer);
            body = null;

        }

    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }
}