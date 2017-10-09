package com.example.roman.vocabulary.fragment.navigation_drawer;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.roman.vocabulary.R;
import com.example.roman.vocabulary.activity.base.BaseActivity;
import com.example.roman.vocabulary.adapter.MenuItemAdapter;
import com.example.roman.vocabulary.fragment.BaseFragment;

import butterknife.BindView;

/**
 * Created by roman on 23.08.2017.
 */

public class NavigationDrawerFragment extends BaseFragment implements NavigationDrawerMvp.View{

    public static final String TAG = NavigationDrawerFragment.class.getName();

    @BindView(R.id.navigationDrawerList)
    RecyclerView rvNavigationDrawerList;

    private View fragmentContainerView;
    private DrawerLayout drawerLayout;
    private OnDrawerSlideListener onDrawerSlideListener;
    private ActionBarDrawerToggle actionBarDrawerToggle;


    public void setBackControl() {
        setDrawerIndicatorEnabled(false);
        //final Drawable upArrow = ContextCompat.getDrawable(getActivity(), R.drawable.back_icon_white);
        //actionBarDrawerToggle.setHomeAsUpIndicator(upArrow);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(false);
        actionBarDrawerToggle.setToolbarNavigationClickListener(v -> {
            //activity.closeKeyboard();
            activity.onBackPressed();
        });
    }

    public interface OnDrawerSlideListener {
        void onTranslateContainer(float moveFactor);
    }

    private NavigationDrawerPresenter presenter;


    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_navigation_drawer;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (BaseActivity)getActivity();
        presenter = new NavigationDrawerPresenter();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (presenter != null) presenter.onResume(activity,this);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //initMenuView();
    }


    @Override
    public void setAdapter(MenuItemAdapter menuItemAdapter) {
        if (rvNavigationDrawerList !=null)
            rvNavigationDrawerList.setLayoutManager(new LinearLayoutManager(activity));
            rvNavigationDrawerList.setAdapter(menuItemAdapter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void setUp(int fragmentId, DrawerLayout drawerLayout, Toolbar toolBar){
        this.fragmentContainerView = activity.findViewById(fragmentId);
        this.drawerLayout = drawerLayout;
        this.drawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        if (toolBar != null){
            ActionBar actionBar = activity.getSupportActionBar();  //getActivity().getActionBar();
            if (actionBar != null){
                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setHomeButtonEnabled(true);
                actionBarDrawerToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout, toolBar,
                        0, 0);
                this.drawerLayout.post(() -> actionBarDrawerToggle.syncState());
            }
        }

    }

    public void setHamburgerControl() {
        setDrawerIndicatorEnabled(true);
    }

    private void setDrawerIndicatorEnabled(boolean enable) {
        actionBarDrawerToggle.setDrawerIndicatorEnabled(enable);
    }

    /*public void setBackControl() {
        setDrawerIndicatorEnabled(false);
        final Drawable upArrow = ContextCompat.getDrawable(getActivity(), R.drawable.back_icon_white);
        upArrow.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
        actionBarDrawerToggle.setHomeAsUpIndicator(upArrow);
        actionBarDrawerToggle.setToolbarNavigationClickListener(v -> {
            mBaseActivity.closeKeyboard();
            mBaseActivity.onBackPressed();
        });
    }


    public void setHamburgerControl() {
        setDrawerIndicatorEnabled(true);
    }

    private void setDrawerLockMode(int lockMode, int edgeGravity) {
        drawerLayout.setDrawerLockMode(lockMode, edgeGravity);
    }

    private void setDrawerIndicatorEnabled(boolean enable) {
        actionBarDrawerToggle.setDrawerIndicatorEnabled(enable);
        setDrawerLockMode(enable ? DrawerLayout.LOCK_MODE_UNLOCKED : DrawerLayout.LOCK_MODE_LOCKED_CLOSED, Gravity.LEFT);
    }*/



    /*private void initMenuView() {
        List<MenuItem> menu = new ArrayList<>();

        menu.add(new MenuItem(R.string.show_all_words, R.drawable.icon_list));

        MenuItemAdapter menuItemAdapter = new MenuItemAdapter();
        menuItemAdapter.setItems(menu);


    }*/
}
