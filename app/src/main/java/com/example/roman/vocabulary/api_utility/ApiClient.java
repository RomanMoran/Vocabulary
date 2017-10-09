package com.example.roman.vocabulary.api_utility;


import com.example.roman.vocabulary.data.lingualeo.Example;
import com.example.roman.vocabulary.utilities.RetrofitUtils;
import com.example.roman.vocabulary.utilities.Utility;

import io.reactivex.Flowable;

/**
 * Created by roman on 28.08.2017.
 */

public class ApiClient {
    private static final String TAG = ApiClient.class.getName();
    private static ApiClient instance;

    private ApiService mApiService = RetrofitUtils.getInstance().getApiService();

    protected ApiClient(){}

    public static ApiClient getInstance(){
        if (instance == null) instance = new ApiClient();
        return instance;
    }

    public Flowable<Example> getTranslated(CharSequence word){
        return mApiService.getTranslated(word).compose(Utility.applySchedulers());
    }

    //public Observable

}
