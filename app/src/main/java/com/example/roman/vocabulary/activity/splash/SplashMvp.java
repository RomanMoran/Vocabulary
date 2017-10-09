package com.example.roman.vocabulary.activity.splash;


import com.example.roman.vocabulary.base_mvp.BaseMvp;

public interface SplashMvp {

    interface View extends BaseMvp.View {

        void showMainView();

    }

    interface Presenter extends BaseMvp.Presenter {

        void checkLogin();

    }


}
