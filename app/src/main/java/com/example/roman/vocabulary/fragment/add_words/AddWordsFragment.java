package com.example.roman.vocabulary.fragment.add_words;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.bumptech.glide.Glide;
import com.example.roman.vocabulary.R;
import com.example.roman.vocabulary.activity.base.BaseActivity;
import com.example.roman.vocabulary.adapter.FindWordsAdapter;
import com.example.roman.vocabulary.api_utility.ApiClient;
import com.example.roman.vocabulary.data.Words;
import com.example.roman.vocabulary.data.lingualeo.Example;
import com.example.roman.vocabulary.db_utility.DBHelper;
import com.example.roman.vocabulary.fragment.BaseFragment;
import com.example.roman.vocabulary.utilities.Constants;
import com.example.roman.vocabulary.utilities.Utility;
import com.jakewharton.rxbinding.widget.RxTextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.subscribers.DisposableSubscriber;
import rx.Subscription;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by roman on 24.08.2017.
 */

public class AddWordsFragment extends BaseFragment implements AddWordsMvp.View {

    public static final String TAG = AddWordsFragment.class.getName();
    @BindView(R.id.flipWrite)
    ViewFlipper viewFlipper;
    @BindView(R.id.flipSearch)
    ViewFlipper viewFlipper2;
    @BindView(R.id.content)
    ViewFlipper content;
    @BindView(R.id.etTranslatable)
    EditText etTranslatable;
    @BindView(R.id.etTranslated)
    EditText etTranslated;
    @BindView(R.id.etAssociation)
    EditText etAssociation;
    @BindView(R.id.toolBar)
    Toolbar toolbar;

    @BindView(R.id.imgAssociation)
    ImageView imgAssociation;
    @BindView(R.id.tvTranslated)
    TextView tvTranslated;
    @BindView(R.id.tvTranslatable)
    TextView tvTranslatable;

    private Subscription subscription = null;
    @BindView(R.id.rvOtherTranslation)
    RecyclerView recyclerView;
    private FindWordsAdapter findWordsAdapter;

    private AddWordsPresenter presenter;


    private ApiClient apiClient = ApiClient.getInstance();

    private String wordEn,wordRu,wordAssociation;

    public ApiClient getApiClient() {
        return apiClient;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_add_word;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (BaseActivity)getActivity();
        presenter = new AddWordsPresenter(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (presenter != null) presenter.onResume(activity,this);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewFlipper2.setDisplayedChild(2);

        setSupportActionBarWithButton(toolbar);

    }

    public void onError(Throwable t) {
        Toast.makeText(activity, t.toString(), Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.btnSave)
    void save() {

        wordEn = etTranslatable.getText().toString();
        wordAssociation = etAssociation.getText().toString();

        if (wordEn.isEmpty()) {
            showToast(R.string.empty_en);
            return;
        }

        String newWords = "";
        if (findWordsAdapter != null) {
            int sizeWords = findWordsAdapter.selectedItems.size();
            if (sizeWords != 0) {
                for (int i = 0; i < sizeWords; i++) {
                    if (findWordsAdapter.selectedItems.get(i))
                        newWords += findWordsAdapter.getItems().get(i) + (sizeWords > 0 ? "," : "");
                }
            }
        }
        wordRu = viewFlipper2.getDisplayedChild() == 0 ?
                etTranslated.getText().toString()
                : newWords;
        if (wordRu.isEmpty()) {
            showToast(R.string.empty_ru);
            return;
        }

        presenter.checkWordExist(wordEn);


    }

    @Override
    public void showResult(boolean exist) {
        if (!exist) {
            Words word = new Words();
            word.setWordEn(wordEn);
            word.setWordRu(wordRu);
            word.setAssociation(wordAssociation);
            Date currentTime = Calendar.getInstance().getTime();
            word.setDate(new SimpleDateFormat(Constants.OUTPUT_DATE).format(currentTime));
            DBHelper
                    .addWord(word)
                    .subscribe(aBoolean -> showToast(R.string.word_add),this::onError);
        }else{
            showToast(R.string.already_exist);
        }
    }

    @OnClick({R.id.flipSearch, R.id.flipWrite})
    void flip() {
        viewFlipper2.showNext();
        viewFlipper.showNext();
        content.showNext();
        if (content.getDisplayedChild() == 1) {
            subscription = RxTextView.textChanges(etTranslatable)
                    .debounce(2, TimeUnit.SECONDS)
                    .subscribe(charSequence -> getApiClient().getTranslated(charSequence)
                            .subscribe(example -> {
                                if (example.getErrorMsg().equalsIgnoreCase("")) {
                                    if (example.getWordForms().size() != 0) {
                                        Glide.with(getContext())
                                                .load(example.getPicUrl())
                                                .into(imgAssociation);
                                        Toast.makeText(activity, "There're some words", Toast.LENGTH_SHORT).show();

                                        List<String> words = new ArrayList<>();
                                        for (int i = 0; i < example.getTranslate().size(); i++) {
                                            words.add(example.getTranslate().get(i).getValue());
                                        }
                                        findWordsAdapter = new FindWordsAdapter(words);
                                        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                                        recyclerView.setAdapter(findWordsAdapter);

                                        tvTranslatable.setText(example.getWordForms().get(0).getWord());
                                    }
                                    Toast.makeText(activity, R.string.words_not_found, Toast.LENGTH_SHORT).show();
                                }
                            }, this::onError), this::onError);
        } else {
            subscription.unsubscribe();

        }
    }


    public static AddWordsFragment newInstance() {
        AddWordsFragment fragment = new AddWordsFragment();
        return fragment;
    }

}
