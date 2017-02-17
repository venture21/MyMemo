package com.venture.android.bbsbasic.interfaces;

import com.venture.android.bbsbasic.domain.Memo;

import java.sql.SQLException;

/**
 * Created by pc on 2/14/2017.
 */

public interface ListInterface {
    public void goDetail();
    public void goDetail(int position);
    public void delFromList(Memo position) throws SQLException;
}