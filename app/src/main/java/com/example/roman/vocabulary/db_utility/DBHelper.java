package com.example.roman.vocabulary.db_utility;

import com.example.roman.vocabulary.data.Words;
import com.example.roman.vocabulary.data.Words_Table;
import com.raizlabs.android.dbflow.rx2.language.RXSQLite;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.GregorianCalendar;
import java.util.List;
import java.util.StringTokenizer;

import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Created by roman on 23.08.2017.
 */

public class DBHelper {

    public static boolean hasItems() {
        return SQLite.select().from(Words.class).count() > 0;
    }

    public static Flowable<Boolean> addWord(Words word) {
        return word.save()
                .observeOn(AndroidSchedulers.mainThread())
                .toFlowable();
               // .subscribe();
    }

    public static List<Words> getWordsBetween(int index, int index2) {
        return SQLite.select().from(Words.class).queryList().subList(index, index2);
    }

    public static void update(long id, String en, String ru) {
        getWord(id)
                .subscribe(words -> {
                    words.setWordRu(ru);
                    words.setWordEn(en);
                    addWord(words).subscribe();
                });
    }

    public static void setReveal(long id, int reveal) {
        getWord(id)
                .subscribe(words -> {
                    words.setReveal(reveal);
                    addWord(words).subscribe();
                });
    }

    public static void delete(long id) {
        getWord(id)
                .subscribe(words -> words
                        .delete()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe()
                );
    }



    public static Flowable<List<Words>> getWords() {
        return RXSQLite.rx(SQLite.select().from(Words.class)).queryList().toFlowable();
    }

    public static Maybe<List<Words>> isWordExist(String word){
        return RXSQLite.rx(SQLite.select().from(Words.class).where(Words_Table.wordEn.is(word))).queryList().toMaybe();
    }

    public static Flowable<List<Words>> getWordsByDate(String date) {
        return RXSQLite.rx(SQLite.select().from(Words.class).where(Words_Table.date.like("%"+date+"%"))).queryList().toFlowable();
    }

    public static Flowable<List<Words>> getWordsByDateLearned(String date) {
        return RXSQLite.rx(SQLite.select().from(Words.class).where(Words_Table.date_of_learned.like("%"+date+"%"))).queryList().toFlowable();
    }

    public static Flowable<List<Words>> getWordsByDateOrLearned(String date,boolean learned) {
        return RXSQLite.rx(SQLite.select()
                .from(Words.class)
                .where(learned?
                        Words_Table.date_of_learned.like("%"+date+"%")
                        :
                        Words_Table.date.like("%"+date+"%")
                )
        ).queryList().toFlowable();
    }

    public static Flowable<List<Words>> getWordsByDate2(String date) {
        StringTokenizer stringTokenizer = new StringTokenizer(date,"/");
        stringTokenizer.nextElement();
        date = stringTokenizer.nextElement().toString();
        return RXSQLite.rx(SQLite.select().from(Words.class).where(Words_Table.date_of_learned.like("%/"+date+"/%"))).queryList().toFlowable();
    }


    public static Flowable<List<Words>> getWordsByMonth(String date) {
        StringTokenizer stringTokenizer = new StringTokenizer(date, "/");
        Integer day = Integer.parseInt(stringTokenizer.nextElement().toString());
        Integer month = Integer.parseInt(stringTokenizer.nextElement().toString());
        Integer year = Integer.parseInt(stringTokenizer.nextElement().toString());
        GregorianCalendar gc = new GregorianCalendar(year, month - 1, day);
        GregorianCalendar gc1 = new GregorianCalendar(year, month - 1, 0);
        java.util.Date monthEndDate = new java.util.Date(gc.getTime().getTime());
        System.out.println(monthEndDate);
        return RXSQLite.rx(SQLite.select().from(Words.class).where(Words_Table.date.like("%/"+month+"/%"))).queryList().toFlowable();
    }

    public static Flowable<List<Words>> getWordsByStartChar(CharSequence charSequence) {
        return RXSQLite.rx(SQLite.select().from(Words.class).where(Words_Table.wordEn.like("%" + charSequence + "%"))).queryList().toFlowable();
    }

    public static Flowable<Words> getWord(long index) {
        return RXSQLite.rx(SQLite.select().from(Words.class).where(Words_Table.id.eq(index))).querySingle().toFlowable();
    }

}
