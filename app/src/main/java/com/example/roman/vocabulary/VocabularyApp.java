package com.example.roman.vocabulary;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.roman.vocabulary.adapter.MenuItemAdapter;
import com.example.roman.vocabulary.data.MenuItem;
import com.example.roman.vocabulary.utilities.DebugUtility;
import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by roman on 23.08.2017.
 */

public class VocabularyApp extends Application {
    private static final String TAG = VocabularyApp.class.getName();
    private static VocabularyApp instance;
    private MenuItemAdapter menuItemAdapter;

    public static VocabularyApp getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        instance = this;
        super.onCreate();
        FlowManager.init(new FlowConfig.Builder(this).build());
    }

    public static boolean hasNetwork() {
        boolean hasNetwork = instance.checkIfHasNetwork();
        DebugUtility.logTest(TAG, "hasNetwork " + hasNetwork);
        return hasNetwork;
    }

    public boolean checkIfHasNetwork() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    private void initMenuAdapter(){
        List<MenuItem> menu = new ArrayList<>();

        menu.add(new MenuItem(R.string.lear_words,R.drawable.icon_learn,MenuItem.MENU_TYPE.LEARN_WORDS));
        menu.add(new MenuItem(R.string.add_words,R.drawable.icon_add,MenuItem.MENU_TYPE.ADD_WORDS));

        menuItemAdapter = new MenuItemAdapter();
        menuItemAdapter.setItems(menu);
    }

    public MenuItemAdapter getMenuItemAdapter(){
        if (menuItemAdapter == null)
            initMenuAdapter();
        return menuItemAdapter;
    }

}