package com.example.roman.vocabulary.activity;

import android.os.Bundle;

import com.example.roman.vocabulary.R;
import com.example.roman.vocabulary.activity.base.BaseActivity;

/**
 * Created by roman on 09.10.2017.
 */

public class AddWordsActivity extends BaseActivity{
    @Override
    public int getLayoutResId() {
        return R.layout.activity_container;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showAddWordFragment();
    }
}
