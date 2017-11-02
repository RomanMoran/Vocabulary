package com.example.roman.vocabulary.fragment.learn_words;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.example.roman.vocabulary.R;
import com.example.roman.vocabulary.activity.base.BaseActivity;
import com.example.roman.vocabulary.adapter.LearnWordsAdapter;
import com.example.roman.vocabulary.adapter.ViewPagerAdapter;
import com.example.roman.vocabulary.data.Words;
import com.example.roman.vocabulary.db_utility.DBHelper;
import com.example.roman.vocabulary.fragment.BaseFragment;
import com.example.roman.vocabulary.utilities.Utility;
import com.jakewharton.rxbinding.support.v7.widget.RxSearchView;
import com.jakewharton.rxbinding.support.v7.widget.SearchViewQueryTextEvent;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by roman on 30.08.2017.
 */

public class LearnWordsFragment extends BaseFragment implements BaseActivity.OnBackPressedListener,SearchView.OnQueryTextListener {

    public static final String TAG = LearnWordsFragment.class.getName();

    @BindView(R.id.toolBar)
    Toolbar toolbar;
    @BindView(R.id.flipperStepsLearn)
    ViewFlipper flipperStepsLearn;


    @BindView(R.id.rgLangs1)
    RadioGroup rgLangs1;
    @BindView(R.id.rbEn1)
    RadioButton rbEn1;
    @BindView(R.id.rbRu1)
    RadioButton rbRu1;
    @BindView(R.id.rbRuEn1)
    RadioButton rbRuEn1;
    @BindView(R.id.rvSelectableWords)
    RecyclerView rvSelectableWords;

    @BindView(R.id.btnCheckYourself)
    Button btnCheckYourSelf;
    @BindView(R.id.viewPagerShowing)
    ViewPager viewPagerShowing;
    @BindView(R.id.searchView)
    SearchView searchView;

    @BindView(R.id.rgLangs)
    RadioGroup rgLangs;
    @BindView(R.id.rbEn)
    RadioButton rbEn;
    @BindView(R.id.rbRu)
    RadioButton rbRu;
    @BindView(R.id.rbRuEn)
    RadioButton rbRuEn;
    @BindView(R.id.rvWords)
    RecyclerView rvWords;
    @BindView(R.id.learned)
    Button learned;

    private ViewPagerAdapter mViewPagerAdapter;

    private ViewPagerAdapter mViewPagerAdapterLearning;

    @BindView(R.id.viewPagerLearning)
    ViewPager mViewPagerLearning;

    private List<Words> selectedWords = new ArrayList<>();
    private List<Words> currentWords = new ArrayList<>();

    private LearnWordsAdapter learnWordsAdapter = new LearnWordsAdapter();
    private LearnWordsAdapter wordsAdapter;


    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_learn_words;
    }

    public static LearnWordsFragment newInstance() {
        LearnWordsFragment fragment = new LearnWordsFragment();
        return fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
        setSupportActionBarWithButton(toolbar);
        flipperStepsLearn.setDisplayedChild(0);
        initCustomSearchView(searchView);


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, true);
        linearLayoutManager.setStackFromEnd(true);
        rvSelectableWords.setLayoutManager(linearLayoutManager);
        defaultItems();

        rvSelectableWords.setAdapter(learnWordsAdapter);

        searchView.setOnQueryTextListener(this);

        rgLangs1.check(R.id.rbRuEn1);
        rgLangs1.setOnCheckedChangeListener((radioGroup, checkedId) -> {
            switch (checkedId) {
                case R.id.rbEn1:
                    learnWordsAdapter.setItems(currentWords,"EN");
                    break;
                case R.id.rbRu1:
                    learnWordsAdapter.setItems(currentWords,"RU");
                    break;
                case R.id.rbRuEn1:
                    learnWordsAdapter.setItems(currentWords,"");
                    break;
            }
        });

    }

    private void defaultItems() {
        DBHelper
                .getWords()
                .flatMapIterable(words -> words)
                .map(word -> {
                    word.setWordTranslatable(word.getWordEn());
                    word.setWordTranslated(word.getWordEn());
                    return word;
                }).toList().toFlowable()
                .compose(Utility.applySchedulers())
                .subscribe(this::onNext,this::onError,this::onComplete);
    }


    public void onNext(List<Words> wordsList){
        setProgressIndicator(true);
        if (wordsList.size() != 0) {
            learnWordsAdapter.setItems(wordsList);
            currentWords = wordsList;
        } else {
            if (!searchView.getQuery().toString().isEmpty())
                Toast.makeText(activity, R.string.words_not_found, Toast.LENGTH_SHORT).show();
                defaultItems();
        }
    }

    public void onComplete(){
        setProgressIndicator(false);
    }


    public rx.Observable<SearchViewQueryTextEvent> getSearchInputObservable() {
        return RxSearchView.queryTextChangeEvents(searchView)
                .filter(text -> !TextUtils.isEmpty(text.toString()))
                .throttleLast(200L, TimeUnit.MILLISECONDS)
                //.debounce(500, TimeUnit.MILLISECONDS,AndroidSchedulers.mainThread())
                //.doOnNext(searchViewQueryTextEvent -> showToast(R.string.searching))
                .onBackpressureLatest();
    }


    public void onError(Throwable t) {
        Toast.makeText(activity, t.toString(), Toast.LENGTH_SHORT).show();
    }


    @OnClick(R.id.btnLearn)
    void learn() {
        SparseBooleanArray sb = learnWordsAdapter.selectedItemsBd;
        selectedWords = getWordsSelected(sb);

        if (selectedWords.size() < 2) {
            showToast(R.string.select_at_least_two_words);
            return;
        }


        mViewPagerAdapter = new ViewPagerAdapter(activity.getCurrentFragmentManager(), selectedWords, 0);
        viewPagerShowing.setAdapter(mViewPagerAdapter);
        viewPagerShowing.addOnPageChangeListener(mViewPagerAdapter);
        viewPagerShowing.setCurrentItem(0);
        viewPagerShowing.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == selectedWords.size() - 1)
                    btnCheckYourSelf.setVisibility(View.VISIBLE);
                else btnCheckYourSelf.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        viewPagerShowing.post(() -> mViewPagerAdapter.onPageSelected(0));

        flipperStepsLearn.showNext();
    }

    @OnClick(R.id.btnCheckYourself)
    void next() {
        if (flipperStepsLearn.getDisplayedChild() == 1) {
            btnCheckYourSelf.setVisibility(View.INVISIBLE);
            mViewPagerAdapterLearning = new ViewPagerAdapter(activity.getCurrentFragmentManager(), selectedWords, 1);
            mViewPagerLearning.setAdapter(mViewPagerAdapterLearning);
            mViewPagerLearning.addOnPageChangeListener(mViewPagerAdapterLearning);
            mViewPagerLearning.setCurrentItem(0);
            mViewPagerLearning.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    if (position == selectedWords.size() - 1)
                        btnCheckYourSelf.setVisibility(View.VISIBLE);
                    else btnCheckYourSelf.setVisibility(View.INVISIBLE);

                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
            mViewPagerLearning.post(() -> mViewPagerAdapterLearning.onPageSelected(0));
        }
        if (flipperStepsLearn.getDisplayedChild() == 2) {
            //mViewPagerAdapterCheck = new ViewPagerAdapter(activity.getCurrentFragmentManager(), selectedWords, 2);
            btnCheckYourSelf.setVisibility(View.GONE);
            initThirdFlip();
        }
        flipperStepsLearn.showNext();

    }

    private void initThirdFlip() {
        rgLangs.check(R.id.rbRuEn);
        wordsAdapter = new LearnWordsAdapter(selectedWords);
        rgLangs.setOnCheckedChangeListener((radioGroup, checkedId) -> {
            switch (checkedId) {
                case R.id.rbEn:
                    wordsAdapter.setItems(selectedWords,"EN");
                    break;
                case R.id.rbRu:
                    wordsAdapter.setItems(selectedWords,"RU");
                    break;
                case R.id.rbRuEn:
                    wordsAdapter.setItems(selectedWords,"");
                    break;
            }
            rvWords.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
            rvWords.setAdapter(wordsAdapter);
        });
        rvWords.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        wordsAdapter.setOnCheckedListener((position, checked) -> {
            if (wordsAdapter.selectedItems.size()==0) learned.setText("Не выучил");
            else learned.setText("Выучил");
        });
        //wordsAdapter.setOnCheckedListener((position, checked) -> {});
        rvWords.setAdapter(wordsAdapter);
    }

    @OnClick(R.id.learned)
    void learned() {
        for (int i = 0; i < selectedWords.size(); i++) {
            if (wordsAdapter.selectedItems.get(i)) {
                Words word = selectedWords.get(i);
                DBHelper.setReveal(word.getId(), word.getReveal() + 1);
            }
        }
        updateList();
        //btnCheckYourSelf.setVisibility(View.GONE);
        flipperStepsLearn.showNext();
    }


    private List<Words> getWordsSelected(SparseBooleanArray sb) {
        List<Words> words = new ArrayList<>();

        Observable.just(/*Utility.showItemsByLangFromStart("EN")*/learnWordsAdapter.getItems()).
                flatMapIterable(wordsList -> wordsList).
                filter(words1 -> sb.get((int) words1.getId() - 1))
                //.subscribeOn(Schedulers.io())
                //.observeOn(AndroidSchedulers.mainThread())
                .subscribe(words::add);
        return words;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_sort, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.menu_on_learned_date:
                dateProcessing(true);
                break;
            case R.id.menu_on_add_date:
                dateProcessing(false);
                break;
            case R.id.menu_statistic_learned:
                statisticsProcessing(true);
                break;
            case R.id.menu_statistic_add:
                statisticsProcessing(false);
                break;
            case R.id.menu_sort:
                item.setChecked(!item.isChecked());
                sorting(item.isChecked());
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void dateProcessing(boolean learned){
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                (view, year, monthOfYear, dayOfMonth) ->
                        giveWordsByDate
                                (String.format("%02d/%02d/%02d", dayOfMonth, monthOfYear + 1, year),learned), mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    private void statisticsProcessing(boolean learned){
        activity.showLineChartFragment(learned);
    }



    private void sorting(boolean reveal) {
        learnWordsAdapter.setItems(Utility.sorting(currentWords));
    }

    private void giveWordsByDate(String date,boolean learned) {
        (learned?
                DBHelper.getWordsByDateLearned(date):DBHelper.getWordsByDate(date))
                .subscribeOn(Schedulers.io())
                .observeOn(io.reactivex.android.schedulers.AndroidSchedulers.mainThread())
                .subscribe(words -> learnWordsAdapter.setItems(words),this::onError);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int pos = learnWordsAdapter.getPosition();
        Words word = learnWordsAdapter.getItem(pos);
        switch (item.getItemId()) {
            case R.id.menu_edit:
                View view = activity.getLayoutInflater().inflate(R.layout.view_edit, (ViewGroup) getView(), false);
                EditText etEng = ButterKnife.findById(view, R.id.etEng);
                EditText etRus = ButterKnife.findById(view, R.id.etRus);
                EditText etAssociation = ButterKnife.findById(view, R.id.etAssociation);

                etEng.setText(word.getWordEn());
                etRus.setText(word.getWordRu());
                etAssociation.setText(word.getAssociation());

                new SweetAlertDialog(activity, SweetAlertDialog.NORMAL_TYPE)
                        .setTitleText(getString(R.string.editable))
                        .setConfirmText(getString(R.string.dialog_ok))
                        .setCustomView(view)
                        .setConfirmClickListener(sweetAlertDialog -> {
                            update(word.getId(), etEng.getText().toString(), etRus.getText().toString(),etAssociation.getText().toString());
                            sweetAlertDialog.cancel();
                        })
                        .setCancelClickListener(SweetAlertDialog::dismissWithAnimation)
                        .show();
                break;
            case R.id.menu_delete:
                DBHelper.delete(word.getId());
                updateList();
                Toast.makeText(activity, "Delete", Toast.LENGTH_SHORT).show();
                break;
            case R.id.menu_forgot:
                DBHelper.setReveal(word.getId(),0);
                updateList();
                showToast("Updated");
                break;
            case R.id.menu_association:
                EditText editTextAssociation = new EditText(getContext());
                editTextAssociation.setHint(R.string.add_association);
                new SweetAlertDialog(activity, SweetAlertDialog.NORMAL_TYPE)
                        .setTitleText(getString(R.string.add_association))
                        .setConfirmText(getString(R.string.dialog_ok))
                        .setCustomView(editTextAssociation)
                        .setConfirmClickListener(sweetAlertDialog -> {
                            update(word.getId(), word.getWordEn(), word.getWordRu(),editTextAssociation.getText().toString());
                            sweetAlertDialog.cancel();
                        })
                        .setCancelClickListener(SweetAlertDialog::dismissWithAnimation)
                        .show();
                break;

        }

        return super.onContextItemSelected(item);
    }


    private void update(long id, String en, String ru,String association) {
        DBHelper.update(id, en, ru,association);
        updateList();
    }

    private void updateAssociation(long id, String en, String ru,String association) {
        DBHelper.update(id, en, ru,association);
        updateList();
    }

    private Disposable updateList() {
        return Observable.just(DBHelper.getWordsByStartChar(searchView.getQuery().toString()))
                .observeOn(io.reactivex.android.schedulers.AndroidSchedulers.mainThread())
                .subscribe(wordsList ->
                        wordsList.subscribe(wordsList1 -> learnWordsAdapter.setItems(wordsList1))
                );
    }


    @Override
    public void onBackPressed() {
        if (flipperStepsLearn.getDisplayedChild() == 1) {
            flipperStepsLearn.setDisplayedChild(0);
        } else if (flipperStepsLearn.getDisplayedChild() == 2) {
            flipperStepsLearn.setDisplayedChild(1);
        } else if (flipperStepsLearn.getDisplayedChild() == 3) {
            flipperStepsLearn.setDisplayedChild(2);
        } else if (flipperStepsLearn.getDisplayedChild() == 0) {
            activity.onBackPressed(true);
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        DBHelper.getWordsByStartChar(newText)
                .compose(Utility.applySchedulers())
                .subscribe(this::onNext,this::onError,this::onComplete);
       /* getSearchInputObservable()
                .map(searchViewQueryTextEvent -> DBHelper.getWordsByStartChar(searchViewQueryTextEvent.queryText()))
                //.subscribeOn(rx.schedulers.Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(listFlowable -> listFlowable.subscribe(this::onNext, this::onError,this::onComplete));*/
        return true;
    }
}
