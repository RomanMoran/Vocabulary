package com.example.roman.vocabulary.fragment.navigation_drawer;

import com.example.roman.vocabulary.activity.base.BaseActivity;
import com.example.roman.vocabulary.activity.LearnWordsActivity;
import com.example.roman.vocabulary.activity.AddWordsActivity;
import com.example.roman.vocabulary.data.MenuItem;

/**
 * Created by roman on 23.08.2017.
 */

public class NavigationDrawerPresenter implements NavigationDrawerMvp.Presenter {

    public static final String TAG = NavigationDrawerPresenter.class.getName();
    private BaseActivity baseActivity;
    private NavigationDrawerMvp.View mView;

    @Override
    public void onItemClicked(MenuItem item) {
        switch (item.getMenuType()){
            case ADD_WORDS:
                baseActivity.newInstance(baseActivity,AddWordsActivity.class);
                break;
            case LEARN_WORDS:
                baseActivity.newInstance(baseActivity, LearnWordsActivity.class);
                break;
        }
    }

    @Override
    public void onStop() {
        mView = null;
    }

    @Override
    public void onResume(BaseActivity baseActivity, NavigationDrawerMvp.View view) {
        this.baseActivity = baseActivity;
        this.mView = view;
        if (baseActivity.getVocabularyApp().getMenuItemAdapter()!=null){
            baseActivity.getVocabularyApp().getMenuItemAdapter()
                    .setOnItemClickListener(
                            (itemView, position, item) -> onItemClicked(item));
            mView.setAdapter(baseActivity.getVocabularyApp().getMenuItemAdapter());
        }
    }
}
