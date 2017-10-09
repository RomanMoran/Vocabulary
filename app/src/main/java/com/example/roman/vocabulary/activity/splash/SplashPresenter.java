package com.example.roman.vocabulary.activity.splash;


import android.os.Handler;

import com.example.roman.vocabulary.base_mvp.BasePresenterImpl;


/**
 * Created by Kulykov Anton on 9/8/17.
 */

public class SplashPresenter extends BasePresenterImpl<SplashMvp.View> implements SplashMvp.Presenter {


    public SplashPresenter(SplashMvp.View view) {
        super(view);

    }


    @Override
    public void checkLogin() {
        if (!isExistsView())
            return;
        mView.setProgressIndicator(true);

        new Handler().postDelayed(() -> isLogin(true), 1000);

    }

    public void isLogin(boolean isLogin) {
        if (isExistsView()) {
            mView.setProgressIndicator(false);
            if (isLogin)
                mView.showMainView();

        }
    }
}
