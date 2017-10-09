package com.example.roman.vocabulary.fragment.learn_words;

import com.example.roman.vocabulary.activity.base.BaseActivity;
import com.example.roman.vocabulary.base_mvp.BaseMvp;
import com.example.roman.vocabulary.base_mvp.BasePresenterImpl;

/**
 * Created by roman on 07.10.2017.
 */

public class LearnWordsPresenter extends BasePresenterImpl implements LearnWordsMvp.Presenter {
    public LearnWordsPresenter(BaseMvp.View view) {
        super(view);
    }

    @Override
    public void setItemsByLang(String lang) {

    }

    @Override
    public void onResume(BaseActivity baseActivity, LearnWordsMvp.View view) {

    }
}
