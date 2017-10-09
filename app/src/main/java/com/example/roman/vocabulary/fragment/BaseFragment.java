package com.example.roman.vocabulary.fragment;

/**
 * Created by roman on 23.08.2017.
 */

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.roman.vocabulary.R;
import com.example.roman.vocabulary.activity.base.BaseActivity;
import com.example.roman.vocabulary.base_mvp.BaseMvp;
import com.example.roman.vocabulary.utilities.DialogUtility;
import com.hannesdorfmann.fragmentargs.FragmentArgs;

import butterknife.ButterKnife;

/**
 * Created by roman on 13.03.2017.
 */

public abstract class BaseFragment extends Fragment implements BaseMvp.View {

    protected BaseActivity activity;
    private Dialog mWaitingDialog;


    protected abstract int getLayoutResId();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentArgs.inject(this);
        activity = (BaseActivity)getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(getLayoutResId(),null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this,view);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        if (activity ==null || activity.getNavigationDrawerFragment()==null)
            return;
        if (!activity.haveFragmentInBackStack())
            activity.getNavigationDrawerFragment().setHamburgerControl();
        else
            activity.getNavigationDrawerFragment().setBackControl();
       /* if (activity.haveFragmentInBackStack())
            activity.getNavigationDrawerFragment().setB*/

    }

    @Override
    public void showToast(String text) {
        Toast.makeText(activity, text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showToast(int text) {
        Toast.makeText(activity, text, Toast.LENGTH_SHORT).show();
    }


    public void initCustomSearchView(SearchView searchView) {
        initCustomSearchView(searchView, true);
    }

    public void initCustomSearchView(SearchView searchView, boolean enabled) {
        if (searchView == null)
            return;
        View searchPlateView = searchView.findViewById(android.support.v7.appcompat.R.id.search_plate);
        if (searchPlateView != null)
            searchPlateView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorFullTransparent));

        ImageView searchButton = (ImageView) searchView.findViewById(android.support.v7.appcompat.R.id.search_mag_icon);
        if (searchButton != null)
            searchButton.setImageResource(android.R.drawable.ic_menu_search);

        ImageView searchCloseBtn = (ImageView) searchView.findViewById(android.support.v7.appcompat.R.id.search_close_btn);
        if (searchCloseBtn != null) {
            //searchCloseBtn.setImageResource(R.drawable.ic_clean_search);
            //searchCloseBtn.setEnabled(enabled);
        }

        SearchView.SearchAutoComplete searchEditText =
                (SearchView.SearchAutoComplete) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        if (searchEditText != null) {
            searchEditText.setFocusable(enabled);
            searchEditText.setEnabled(enabled);
        }
        // searchView.setSubmitButtonEnabled(enabled);


    }



    protected ActionBar getActionBar(){
        return ((AppCompatActivity) getActivity()).getSupportActionBar();
    }

    protected void setSupportActionBarWithButton(Toolbar toolbar){
        activity.setSupportActionBar(toolbar);
        ActionBar actionBar = getActionBar();
        if (actionBar!=null){
            //actionBar.setHomeAsUpIndicator(R.drawable.arrow);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public void setProgressIndicator(boolean active) {
        activity.runOnUiThread(() -> {
            if (active) {
                /*if (mWaitingDialog == null)*/ mWaitingDialog = DialogUtility.getWaitDialog(
                        getContext(), BaseFragment.this.getString(R.string.wait), false);
                mWaitingDialog.show();
            } else {
                DialogUtility.closeDialog(mWaitingDialog);
            }
        });

    }
}
