package com.example.roman.vocabulary.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import com.example.roman.vocabulary.data.Words;
import com.example.roman.vocabulary.fragment.ShowDetailFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by roman on 30.08.2017.
 */

public class ViewPagerAdapter extends FragmentStatePagerAdapter  implements ViewPager.OnPageChangeListener {

    private List<Words>wordsList;
    private ArrayList<ShowDetailFragment> mFragments = new ArrayList<>();

    private int positionFlipper ;

    public ViewPagerAdapter(FragmentManager fm, List<Words>wordsList,int positionFlipper) {
        super(fm);
        this.wordsList = wordsList;
        for (Words words:wordsList)
            mFragments.add(null);
        this.positionFlipper = positionFlipper;

    }

    @Override
    public Fragment getItem(int position) {
        ShowDetailFragment showDetailFragment = ShowDetailFragment.newInstance(wordsList,position/*wordsList.get(position)*/,positionFlipper);
        mFragments.set(position,showDetailFragment);
        return showDetailFragment;
    }



    @Override
    public int getCount() {
        return wordsList.size();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
