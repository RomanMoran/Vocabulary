package com.example.roman.vocabulary.fragment.add_words;

import com.example.roman.vocabulary.activity.base.BaseActivity;
import com.example.roman.vocabulary.base_mvp.BasePresenterImpl;
import com.example.roman.vocabulary.data.Words;
import com.example.roman.vocabulary.db_utility.DBHelper;
import com.example.roman.vocabulary.utilities.Utility;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by roman on 24.08.2017.
 */

public class AddWordsPresenter extends BasePresenterImpl implements AddWordsMvp.Presenter {

    private BaseActivity baseActivity;
    private AddWordsMvp.View mView;

    private boolean exist;

    public AddWordsPresenter(AddWordsMvp.View view) {
        super(view);
        this.mView = view;
    }


    @Override
    public void checkWordExist(String wordEn) {
        exist = false;
        DBHelper.getWords()
                .flatMapIterable(wordsList -> wordsList)
                .filter(words -> wordEn.equalsIgnoreCase(words.getWordEn()))
                .compose(Utility.applySchedulers())
                .subscribe(this::wordExist,this::onError,this::onComplete);

        /*DefaultHttpClient httpClient = new DefaultHttpClient(new ThreadSafeClientConnManager());
        SkPublishAPI api = new SkPublishAPI(baseUrl + "/api/v1", accessKey, httpClient);
        api.setRequestHandler(new SkPublishAPI.RequestHandler() {
            public void prepareGetRequest(HttpGet request) {
                System.out.println(request.getURI());
                request.setHeader("Accept", "application/json");
            }
        });*/


        DBHelper.isWordExist(wordEn)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();



    }

    public void wordExist(Words word){
        exist = true;
    }

    public void onComplete(){
        mView.showResult(exist);
    }

    @Override
    public void onResume(BaseActivity baseActivity, AddWordsMvp.View view) {
        this.baseActivity = baseActivity;
        this.mView = view;
    }
}
