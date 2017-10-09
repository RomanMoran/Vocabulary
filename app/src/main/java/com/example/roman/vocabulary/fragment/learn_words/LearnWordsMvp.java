package com.example.roman.vocabulary.fragment.learn_words;

import com.example.roman.vocabulary.activity.base.BaseActivity;
import com.example.roman.vocabulary.base_mvp.BaseMvp;
import com.example.roman.vocabulary.data.Words;

import java.util.List;

/**
 * Created by roman on 24.08.2017.
 */

public interface LearnWordsMvp {


    interface View extends BaseMvp.View {

        @Override
        void showToast(String text);

        @Override
        void showToast(int text);

        @Override
        void setProgressIndicator(boolean active);

        void showItems(List<Words> items);
    }

    interface Presenter{

        void setItemsByLang(String lang);


        void onResume(BaseActivity baseActivity, LearnWordsMvp.View view);


    }
}
