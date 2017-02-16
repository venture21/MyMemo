package com.venture.android.bbsbasic.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.venture.android.bbsbasic.domain.Memo;

import java.sql.SQLException;

/**
 * Created by pc on 2/14/2017.
 */

public class DBHelper extends OrmLiteSqliteOpenHelper {

    public static final String DB_NAME = "memo.db";
    public static final int DB_VERSION = 2;

    public DBHelper(Context context){
        super(context, DB_NAME, null, DB_VERSION);
    }


    /**
     * 생성자에서 호출되는 super(context... 에서 database.db 파일이 생성되어 있지 않으면 호출된다
     */
    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, Memo.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 생성자에서 호출되는 super(context... 에서 database.db 파일이 존재하지만 DB_VERSION 이 증가되면 호출된다
     * @param database
     * @param connectionSource
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            // 기존 데이터베이스 버전이 1일때 설치한 유저는 Memo 테이블만 생성해준다
            if(oldVersion == 1){
                TableUtils.createTable(connectionSource, Memo.class);
            }else{
                onCreate(database, connectionSource);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // DBHelper 를 싱글턴으로 사용하기 때문에 dao 객체도 열어놓고 사용가능하다
    private Dao<Memo, Integer> memoDao = null;
    public Dao<Memo, Integer> getMemoDao() throws SQLException {
        if(memoDao == null){
            memoDao = getDao(Memo.class);
        }
        return memoDao;
    }
    public void releaseMemoDao() {
        if(memoDao != null){
            memoDao = null;
        }
    }
}