
package com.example.roman.vocabulary.data.lingualeo;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Example {

    @SerializedName("error_msg")
    @Expose
    private String errorMsg;
    @SerializedName("error_code")
    @Expose
    private int errorCode;

    @SerializedName("translate_source")
    @Expose
    private String translateSource;
    @SerializedName("is_user")
    @Expose
    private int isUser;
    @SerializedName("word_forms")
    @Expose
    private List<WordForm> wordForms = null;
    @SerializedName("pic_url")
    @Expose
    private String picUrl;
    @SerializedName("translate")
    @Expose
    private List<Translate> translate = null;
    @SerializedName("transcription")
    @Expose
    private String transcription;
    @SerializedName("word_id")
    @Expose
    private int wordId;
    @SerializedName("word_top")
    @Expose
    private int wordTop;
    @SerializedName("sound_url")
    @Expose
    private String soundUrl;

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String getTranslateSource() {
        return translateSource;
    }

    public void setTranslateSource(String translateSource) {
        this.translateSource = translateSource;
    }

    public int getIsUser() {
        return isUser;
    }

    public void setIsUser(int isUser) {
        this.isUser = isUser;
    }

    public List<WordForm> getWordForms() {
        return wordForms;
    }

    public void setWordForms(List<WordForm> wordForms) {
        this.wordForms = wordForms;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public List<Translate> getTranslate() {
        return translate;
    }

    public void setTranslate(List<Translate> translate) {
        this.translate = translate;
    }

    public String getTranscription() {
        return transcription;
    }

    public void setTranscription(String transcription) {
        this.transcription = transcription;
    }

    public int getWordId() {
        return wordId;
    }

    public void setWordId(int wordId) {
        this.wordId = wordId;
    }

    public int getWordTop() {
        return wordTop;
    }

    public void setWordTop(int wordTop) {
        this.wordTop = wordTop;
    }

    public String getSoundUrl() {
        return soundUrl;
    }

    public void setSoundUrl(String soundUrl) {
        this.soundUrl = soundUrl;
    }


    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

}
