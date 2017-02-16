package com.venture.android.bbsbasic.domain;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by pc on 2/14/2017.
 */

@DatabaseTable(tableName = "memo")
public class Memo {

    @DatabaseField(generatedId = true)
    int id;

    @DatabaseField
    String content;

    @DatabaseField
    String date;

    @DatabaseField
    String title;

    @DatabaseField
    String uri;

    boolean checkbox;


    public Memo(){
        // default
    }

    // create 시에 사용할 생성자
    public Memo(String memo, String title, String uri, String date){
        this.content = memo;
        this.title = title;
        this.uri = uri;
        this.date = date;
    }

    /**
     * 메모의 ID 값을 저장할 int
     * @return
     */
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    /**
     * 메모 내용을 저장할 String
     * @return
     */
    public String getContents() {
        return content;
    }

    public void setContents(String memo) {
        this.content = memo;
    }


    /**
     * 메모가 작성된 시간을 저장할 Date
     * @return
     */
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
        //this.date = new Date(System.currentTimeMillis());
    }

    /**
     * 제목을 저장할 String
     * @return
     */
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * 사진의 경로를 저장할 Uri
     * @return
     */
    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public boolean isCheckbox() {
        return checkbox;
    }

    public void setCheckbox(boolean checkbox) {
        this.checkbox = checkbox;
    }
}