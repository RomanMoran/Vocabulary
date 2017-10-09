package com.example.roman.vocabulary.api_utility;

import com.example.roman.vocabulary.data.lingualeo.Example;

import io.reactivex.Flowable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by roman on 28.08.2017.
 */

public interface ApiService {


    @GET(ApiConstants.GET_TRANSLATED)
    Flowable<Example>getTranslated(@Query("word")CharSequence word);



}
