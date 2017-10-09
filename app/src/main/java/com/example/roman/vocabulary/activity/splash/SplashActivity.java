package com.example.roman.vocabulary.activity.splash;

import android.os.Bundle;

import com.example.roman.vocabulary.R;
import com.example.roman.vocabulary.activity.MainActivity;
import com.example.roman.vocabulary.activity.base.BaseMvpActivity;


public class SplashActivity extends BaseMvpActivity<SplashMvp.Presenter> implements SplashMvp.View {


    private static final String TAG = SplashActivity.class.getName();

    @Override
    public int getLayoutResId() {
        return R.layout.activity_splash;
    }


    @Override
    protected SplashMvp.Presenter createPresenter() {
        return new SplashPresenter(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter.checkLogin();

    }


    @Override
    public void showMainView() {
        newInstance(this, MainActivity.class, false);
        finish();
    }

}
