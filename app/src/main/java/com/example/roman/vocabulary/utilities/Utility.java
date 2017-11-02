package com.example.roman.vocabulary.utilities;

import android.content.Context;
import android.widget.Toast;

import com.example.roman.vocabulary.data.Words;
import com.example.roman.vocabulary.data.lingualeo.Example;
import com.example.roman.vocabulary.db_utility.DBHelper;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import io.reactivex.Flowable;
import io.reactivex.FlowableTransformer;
import io.reactivex.SingleTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by roman on 26.08.2017.
 */

public class Utility {

    public static List<Words> showItemsByLangFromEnd(int size, String lang) {
        List<Words> wordsList = new ArrayList<>();
        DBHelper.getWords()
                .flatMapIterable(wordsList1 -> wordsList1)
                .takeLast(size)
                .subscribe(word -> {
                    word.setWordTranslatable(lang.equals("EN") ? word.getWordEn() : word.getWordRu());
                    word.setWordTranslated(lang.equals("RU") ? word.getWordEn() : word.getWordRu());
                    wordsList.add(word);
                });
        return wordsList;
    }

    public static void showToast(Context context, int textId) {
        String text = null;
        try {
            text = context.getString(textId);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        showToast(context, text);
    }

    public static void showToast(Context context, String text) {
        try {
            Toast.makeText(context, text, Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getNormalData(Date data){
        SimpleDateFormat output = new SimpleDateFormat(Constants.OUTPUT_DATE, Locale.getDefault());

        return output.format(data);
    }

    public static Date getFormattedDate(int year,int month,int day){
        GregorianCalendar gc = new GregorianCalendar(year, month - 1, day);
        return new Date(gc.getTime().getTime());
    }


    public static int countDaysInMonth(int month,int year){
        Calendar calendar = new GregorianCalendar(year, month, 1);
        int daysInMonth= calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        return daysInMonth;
    }

    public static int getMonth(int month){
        return month>11?0:(month<0?11:month);
    }

    /*public static List<Words>showItemsByDate(Date date){
        DBHelper.getWordsByDate(date)
                .subscribe()
    }*/

    public static List<Words>sorting(List<Words>list){
        Collections.sort(list, (words, t1) -> words.getReveal()-t1.getReveal());
        return list;
    }

    public static List<Words>sortingData(List<Words>list){
        Collections.sort(list, (words, t1) -> (int)words.getId()+(int)t1.getId());
        return list;
    }


    public static List<Words> showItemsByLangFromStar1t(String lang) {
        List<Words> wordsList = new ArrayList<>();
        DBHelper.getWords()
                .flatMapIterable(wordsList1 -> wordsList1)
                .compose(Utility.applySchedulers())
                .subscribe(word -> {
                    word.setWordTranslatable(lang.equals("EN") ? word.getWordEn() : word.getWordRu());
                    word.setWordTranslated(lang.equals("RU") ? word.getWordEn() : word.getWordRu());
                    wordsList.add(word);
                });
        return wordsList;
    }


    public static List<Words> wordsBetween(int index,int index2,String lang) {
        List<Words> wordsList = new ArrayList<>();
        List<Words> words = DBHelper.getWordsBetween(index,index2);
        for (int i = 0; i < words.size(); i++) {
            Words word = words.get(i);
            word.setWordTranslatable(lang.equals("EN") ? word.getWordEn() : word.getWordRu());
            word.setWordTranslated(lang.equals("RU") ? word.getWordEn() : word.getWordRu());
            wordsList.add(words.get(i));
        }
        return wordsList;
    }

    public static <T> FlowableTransformer<T, T> applySchedulers() {
        return observable -> observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static <T> SingleTransformer<T, T> applySingleSchedulers() {
        return observable -> observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

}
