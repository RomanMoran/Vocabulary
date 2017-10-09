
package com.example.roman.vocabulary.data.lingualeo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Translate {

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("value")
    @Expose
    private String value;
    @SerializedName("votes")
    @Expose
    private int votes;
    @SerializedName("is_user")
    @Expose
    private int isUser;
    @SerializedName("pic_url")
    @Expose
    private String picUrl;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getVotes() {
        return votes;
    }

    public void setVotes(int votes) {
        this.votes = votes;
    }

    public int getIsUser() {
        return isUser;
    }

    public void setIsUser(int isUser) {
        this.isUser = isUser;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

}
