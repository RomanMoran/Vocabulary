package com.example.roman.vocabulary.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.roman.vocabulary.R;
import com.example.roman.vocabulary.activity.base.BaseActivity;
import com.example.roman.vocabulary.adapter.WordsAdapter;
import com.example.roman.vocabulary.db_utility.DBHelper;
import com.example.roman.vocabulary.utilities.Utility;

import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;

public class MainActivity extends BaseActivity {

    @BindView(R.id.rvWords)
    RecyclerView rvWords;
    private WordsAdapter wordsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Date currentTime = Calendar.getInstance().getTime();
        Log.d("TAG",currentTime.toString());
        wordsAdapter = new WordsAdapter(Utility.showItemsByLangFromEnd(10,"EN"));

        if (DBHelper.hasItems()) {
            rvWords.setLayoutManager(new LinearLayoutManager(this));
            rvWords.setAdapter(wordsAdapter);
            //wordsAdapter.showItems(DBHelper.getWords(),10);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        wordsAdapter.notifyDataSetChanged();
    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_main;
    }
}
