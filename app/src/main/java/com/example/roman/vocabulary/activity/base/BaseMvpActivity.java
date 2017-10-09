package com.example.roman.vocabulary.activity.base;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.example.roman.vocabulary.base_mvp.BaseMvp;


public abstract class BaseMvpActivity<P extends BaseMvp.Presenter> extends BaseActivity {
    private static final String TAG = BaseMvpActivity.class.getName();


    protected P mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = createPresenter();
    }

    protected abstract P createPresenter();

    @Override
    protected void onDestroy() {
        if (mPresenter != null) mPresenter.onDestroy();
        super.onDestroy();
    }
}
