package com.venture.android.bbsbasic.interfaces;

import com.venture.android.bbsbasic.domain.Memo;

import java.sql.SQLException;

/**
 * Created by pc on 2/14/2017.
 */

public interface DetailInterface {
    public void backToList();
    public void saveToList(Memo memo) throws SQLException;
    public void editToList(Memo memo) throws SQLException;
    public void delToList(Memo memo) throws SQLException;

}