package com.example.roman.vocabulary.data;

import com.example.roman.vocabulary.db_utility.AppDatabase;
import com.example.roman.vocabulary.utilities.Constants;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.rx2.structure.BaseRXModel;
import com.raizlabs.android.dbflow.structure.BaseModel;

import org.parceler.Parcel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by roman on 23.08.2017.
 */

@Parcel( value = Parcel.Serialization.BEAN)
@Table(database = AppDatabase.class)
public class Words extends BaseRXModel {
    @PrimaryKey(autoincrement = true)
    private long id;
    private String wordTranslatable;
    private String wordTranslated;

    public Words() {}

    public Words(String wordRu, String wordEn) {
        this.wordRu = wordRu;
        this.wordEn = wordEn;
    }

    @Column
    private String wordRu;
    @Column
    private String wordEn;
    @Column
    private String date;
    @Column
    private int reveal;
    @Column(name = "date_of_learned")
    private String dateOfLearned;
    @Column
    private String association;

    public String getAssociation() {
        return association!=null?association:"";
    }

    public void setAssociation(String association) {
        this.association = association;
    }

    public int getReveal() {
        return reveal;
    }

    public void setReveal(int reveal) {
        this.reveal = reveal>4?4:reveal;
        if (reveal>=4){
            setDateOfLearned(new SimpleDateFormat(Constants.OUTPUT_DATE).format(Calendar.getInstance().getTime()));
        }
    }

    public void setDateOfLearned(String date){
        this.dateOfLearned = date;
    }

    public String getDateOfLearned() {
        return dateOfLearned;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getWordTranslatable() {
        return wordTranslatable;
    }

    public void setWordTranslatable(String wordTranslatable) {
        this.wordTranslatable = wordTranslatable;
    }

    public String getWordTranslated() {
        return wordTranslated;
    }

    public void setWordTranslated(String wordTranslated) {
        this.wordTranslated = wordTranslated;
    }

    public String getWordRu() {
        return wordRu;
    }

    public void setWordRu(String wordRu) {
        this.wordRu = wordRu;
    }

    public String getWordEn() {
        return wordEn;
    }

    public void setWordEn(String wordEn) {
        this.wordEn = wordEn;
    }

    public String getDate() {
        return date;
    }

    public String getFormattedDate(){
        SimpleDateFormat output = new SimpleDateFormat(Constants.OUTPUT_DATE, Locale.getDefault());
        return date;
    }


    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Words{" +
                "id=" + id +
                ", wordRu='" + wordRu + '\'' +
                ", wordEn='" + wordEn + '\'' +
                '}';
    }
}
