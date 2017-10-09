package com.example.roman.vocabulary.fragment.navigation_drawer;

import com.example.roman.vocabulary.activity.base.BaseActivity;
import com.example.roman.vocabulary.adapter.MenuItemAdapter;
import com.example.roman.vocabulary.data.MenuItem;

/**
 * Created by roman on 23.08.2017.
 */

public interface NavigationDrawerMvp {
    interface View{
        void setAdapter(MenuItemAdapter menuItemAdapter);
    }

    interface Presenter {

        void onItemClicked(MenuItem item);

        void onStop();

        void onResume(BaseActivity baseActivity,NavigationDrawerMvp.View view);
    }
}
