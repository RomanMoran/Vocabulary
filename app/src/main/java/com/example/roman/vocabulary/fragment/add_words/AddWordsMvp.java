package com.example.roman.vocabulary.fragment.add_words;

import com.example.roman.vocabulary.activity.base.BaseActivity;
import com.example.roman.vocabulary.base_mvp.BaseMvp;
import com.example.roman.vocabulary.fragment.navigation_drawer.NavigationDrawerMvp;

/**
 * Created by roman on 24.08.2017.
 */

public interface AddWordsMvp {


    interface View extends BaseMvp.View {

        @Override
        void showToast(String text);

        @Override
        void showToast(int text);

        @Override
        void setProgressIndicator(boolean active);

        void showResult(boolean exist);
    }

    interface Presenter{

        void checkWordExist(String word);

        void onResume(BaseActivity baseActivity, AddWordsMvp.View view);


    }
}
