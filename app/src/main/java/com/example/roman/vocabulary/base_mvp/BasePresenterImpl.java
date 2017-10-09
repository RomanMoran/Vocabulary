package com.example.roman.vocabulary.base_mvp;

import com.example.roman.vocabulary.VocabularyApp;
import com.example.roman.vocabulary.api_utility.ApiClient;
import com.example.roman.vocabulary.utilities.DebugUtility;
import com.jakewharton.retrofit2.adapter.rxjava2.HttpException;

/**
 * Created by roman on 14.08.2017.
 */

public class BasePresenterImpl<V extends BaseMvp.View> implements BaseMvp.Presenter, BaseMvp.InteractionFinishedListener {
    public static final String TAG = BasePresenterImpl.class.getName();
    protected V mView;

    private ApiClient apiClient = ApiClient.getInstance();

    public BasePresenterImpl(V view) {
        this.mView = view;
    }

    public ApiClient getApiClient() {
        return apiClient;
    }

    protected boolean isExistsView() {
        return mView != null;
    }

    @Override
    public void onDestroy() {
        mView = null;
    }

    @Override
    public void onError(String text) {
        DebugUtility.logTest(TAG, "onError " + text);
        if (mView != null) {
            mView.setProgressIndicator(false);
            mView.showToast(text);
        }
    }

    @Override
    public void onError(int textId) {
        DebugUtility.logTest(TAG, "onError " + textId);
        if (mView != null) {
            mView.setProgressIndicator(false);
            mView.showToast(textId);
        }
    }

    @Override
    public void onError(Throwable throwable) {
        if (throwable != null) {
           /* if (throwable instanceof ApiException) {
                ApiException apiException = (ApiException) throwable;
                if (apiException.isUnauthorized())
                {
                    onUnauthorized();
                    return;
                }
            }
            else */
            if (throwable instanceof HttpException) {
                HttpException httpException = (HttpException) throwable;
                if (httpException.code() == 401) {
                    onUnauthorized();
                    return;
                }
            }
            if (throwable.getMessage().contains("No address associated") && !VocabularyApp.hasNetwork()) {
                onNetworkDisable();
                return;
            }
            onError(throwable.getMessage());
        }

    }

    @Override
    public void onComplete() {
        setProgressIndicator(false);
    }


    @Override
    public void setProgressIndicator(boolean active) {
        if (mView != null)
            mView.setProgressIndicator(active);
    }


    @Override
    public void onUnauthorized() {
        if (mView != null) {
            mView.setProgressIndicator(false);
            //mView.onUnauthorized();
        }
    }

    @Override
    public void onNetworkDisable() {
        if (mView != null) {
            mView.setProgressIndicator(false);
            //mView.onNetworkDisable();
        }
    }

}
