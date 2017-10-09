package com.example.roman.vocabulary.data;

/**
 * Created by roman on 23.08.2017.
 */

public class MenuItem {

    private int idTitleString;
    private int idImageDrawable;

    private MENU_TYPE menuType;

    public MenuItem(int idTitleString, int idImageDrawable, MENU_TYPE menuType) {
        this.idTitleString = idTitleString;
        this.idImageDrawable = idImageDrawable;
        this.menuType = menuType;
    }

    public int getIdTitleString() {
        return idTitleString;
    }

    public void setIdTitleString(int idTitleString) {
        this.idTitleString = idTitleString;
    }

    public int getIdImageDrawable() {
        return idImageDrawable;
    }

    public void setIdImageDrawable(int idImageDrawable) {
        this.idImageDrawable = idImageDrawable;
    }

    public enum MENU_TYPE{
        LEARN_WORDS,ADD_WORDS
    }

    public MENU_TYPE getMenuType() {
        return menuType;
    }
}
