package com.example.roman.vocabulary.activity;

import android.os.Bundle;

import com.example.roman.vocabulary.R;
import com.example.roman.vocabulary.activity.base.BaseActivity;

/**
 * Created by roman on 30.08.2017.
 */

public class LearnWordsActivity extends BaseActivity {
    @Override
    public int getLayoutResId() {
        return R.layout.activity_container;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showLearnWordsFragment();
    }
}
