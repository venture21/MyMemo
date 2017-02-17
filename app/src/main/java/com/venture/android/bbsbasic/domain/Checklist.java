package com.venture.android.bbsbasic.domain;

/**
 * Created by parkheejin on 2017. 2. 17..
 */

public class Checklist {
    boolean checkbox;

    //default
    public Checklist(){

    }
    // create시에 사용할 생성자
    public Checklist(boolean checkbox) {
        this.checkbox = checkbox;
    }

    public boolean isCheckbox() {
        return checkbox;
    }

    public void setCheckbox(boolean checkbox) {
        this.checkbox = checkbox;
    }
}
