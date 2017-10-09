package com.example.roman.vocabulary.activity.base;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.example.roman.vocabulary.R;
import com.example.roman.vocabulary.VocabularyApp;
import com.example.roman.vocabulary.base_mvp.BaseMvp;
import com.example.roman.vocabulary.fragment.add_words.AddWordsFragment;
import com.example.roman.vocabulary.fragment.learn_words.LearnWordsFragment;
import com.example.roman.vocabulary.fragment.line_chart.LineChartFragment;
import com.example.roman.vocabulary.fragment.navigation_drawer.NavigationDrawerFragment;
import com.example.roman.vocabulary.utilities.DialogUtility;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by roman on 23.08.2017.
 */

public abstract class BaseActivity extends AppCompatActivity implements BaseMvp.View {

    protected NavigationDrawerFragment mNavigationDrawerFragment;
    @Nullable
    @BindView(R.id.drawerLayout)
    DrawerLayout mDrawerLayout;
    @Nullable
    @BindView(R.id.activityContainer)
    View mActivityContainer;
    @Nullable
    @BindView(R.id.toolBar)
    Toolbar mToolBar;
    @Nullable
    @BindView(R.id.toolBarTitle)
    TextView tvToolBarTitle;

    private Dialog waitingDialog;


    private FragmentManager mCurrentFragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(getLayoutResId());
        ButterKnife.bind(this);
        if (mToolBar != null) setSupportActionBar(mToolBar);
        getCurrentFragmentManager();
        initNavigationDrawer();

    }

    public abstract int getLayoutResId();

    public static void newInstance(Context context, final Class<? extends AppCompatActivity> activityClass) {
        newInstance(context, activityClass, false);
    }

    public static void newInstance(Context context, final Class<? extends AppCompatActivity> activityClass, boolean clearBackStack) {
        if (activityClass == context.getClass()) {
            return;
        }
        Intent intent = new Intent(context, activityClass);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        if (clearBackStack)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        ActivityCompat.startActivity(context, intent, null);
    }


    public void showFragment(int container, Fragment fragment, String tag, boolean addToBackStack){
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(container,fragment,tag);
        if (addToBackStack) ft.addToBackStack(tag);
        ft.commitAllowingStateLoss();
    }

    public void showAddWordFragment(){
        String tag = AddWordsFragment.TAG;
        AddWordsFragment fragment = AddWordsFragment.newInstance();
        showFragment(R.id.fragment_container,fragment,tag,false);
    }

    public void showLearnWordsFragment(){
        String tag = LearnWordsFragment.TAG;
        LearnWordsFragment fragment = LearnWordsFragment.newInstance();
        showFragment(R.id.fragment_container,fragment,tag,false);
    }

    public void showLineChartFragment(boolean learned){
        String tag = LineChartFragment.TAG;
        LineChartFragment fragment = LineChartFragment.newInstance(learned);
        showFragment(R.id.fragment_container,fragment,tag,true);
    }


    /*@Override
    public void onBackPressed() {
        int count = getFragmentManager().getBackStackEntryCount();
        if (count==0){
            getFragmentManager().popBackStack();
        }else{
            super.onBackPressed();
        }
        super.onBackPressed();
    }*/

    @Override
    public void onBackPressed() {
        FragmentManager fm = getSupportFragmentManager();
        OnBackPressedListener backPressedListener = null;
        for (Fragment fragment: fm.getFragments()) {
            if (fragment instanceof  OnBackPressedListener) {
                backPressedListener = (OnBackPressedListener) fragment;
                break;
            }
        }

        if (backPressedListener != null) {
            backPressedListener.onBackPressed();
        } else {
            super.onBackPressed();
        }
    }

    public void onBackPressed(boolean flag){
        if (flag)
            super.onBackPressed();
        else onBackPressed();
    }

    public interface OnBackPressedListener {
        void onBackPressed();
    }


    public VocabularyApp getVocabularyApp() {
        return (VocabularyApp) getApplication();
    }

    protected void initNavigationDrawer(){
        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigationDrawer);
        if (mNavigationDrawerFragment == null)
            return;
        mNavigationDrawerFragment.setUp(R.id.navigationDrawer,
                mDrawerLayout,mToolBar);
        VocabularyApp vocabularyApp = getVocabularyApp();
        if (vocabularyApp.getMenuItemAdapter() != null)
            mNavigationDrawerFragment.setAdapter(vocabularyApp.getMenuItemAdapter());
    }

    public FragmentManager getCurrentFragmentManager() {
        if (mCurrentFragmentManager == null)
            mCurrentFragmentManager = getSupportFragmentManager();
        return mCurrentFragmentManager;
    }

    @Override
    public void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showToast(int text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setProgressIndicator(boolean active) {
        runOnUiThread(() -> {
            if (active){
                //if (waitingDialog == null)
                waitingDialog = DialogUtility.getWaitDialog(this,getString(R.string.wait),active);
                waitingDialog.show();
            }else {
                //if (waitingDialog!=null)
                DialogUtility.closeDialog(waitingDialog);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                hideKeyboard();
                onBackPressed();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    public void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


    public boolean haveFragmentInBackStack() {
        return mCurrentFragmentManager != null ? mCurrentFragmentManager.getBackStackEntryCount() > 0 : false;
    }


    public NavigationDrawerFragment getNavigationDrawerFragment() {
        return mNavigationDrawerFragment;
    }





}
