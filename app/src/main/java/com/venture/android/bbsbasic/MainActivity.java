package com.venture.android.bbsbasic;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.FrameLayout;

import com.j256.ormlite.dao.Dao;
import com.venture.android.bbsbasic.data.DBHelper;
import com.venture.android.bbsbasic.domain.Memo;
import com.venture.android.bbsbasic.interfaces.DetailInterface;
import com.venture.android.bbsbasic.interfaces.ListInterface;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements ListInterface, DetailInterface{
    private final int REQ_PERMISSION = 100;           // 권한 요청 코드


    private static final String TAG="MemoMain";
    ListFragment list;
    DetailFragment detail;

    FrameLayout main;
    FragmentManager manager;

    DBHelper dbHelper;

    List<Memo> datas = new ArrayList<>();
    Dao<Memo, Integer> memoDao;
    Memo memo;
    Bundle bundle;
    boolean permitCam = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        list = ListFragment.newInstance(1);
        detail = DetailFragment.newInstance();
        main = (FrameLayout) findViewById(R.id.activity_main);
        manager = getSupportFragmentManager();
        try {
            readData();
        }catch (SQLException e){
            Log.e(TAG, e+"============================");
        }

        list.setData(datas);
        setList();
        checkPermission();
    }

    // 권한 관련 체크
    @TargetApi(Build.VERSION_CODES.M)
    private void checkPermission() {
        // 버전체크를 통해 마시멜로보다 낮으면 런타임권한 체크를 하지 않는다.
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M) {
            if (PermissionControl.checkPermission(this, REQ_PERMISSION)) {
                init();
            }
        } else {
            init();

        }
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == REQ_PERMISSION){
            if( PermissionControl.onCheckResult(grantResults)){
                init();
            }else{
                //Toast.makeText(this, "권한을 허용하지 않으시면 프로그램을 실행할 수 없습니다.", Toast.LENGTH_LONG).show();
                //finish();
            }
        }
    }

    private void init(){
        /**
         *  Camera가 꺼져 있을 경우, Camera를 켜기 위한 팝업창 생성
         */
        permitCam = true;
    }



    public void readData() throws SQLException {
        // 데이터베이스 연결
        dbHelper = new DBHelper(this);
        // 테이블 연결
        memoDao = dbHelper.getMemoDao();

        // 메모를 새롭게 불러옴 (queryForAll)
        datas = memoDao.queryForAll();
        dbHelper.close();
    }

    // 목록 프래그먼트 FrameLayout 에 add
    private void setList(){
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.activity_main, list);
        transaction.commit();
    }

    @Override
    public void goDetail(){
        // 아래 goDetail(int position)메소드에서
        // detail.setArguments(bundle) 사용후 이 설정이 계속 유지됨.
        // Bundle을 0으로 설정해 줘야 Bundle값들이 전달되지 않는다.
        bundle = new Bundle(0);
        //bundle.putBoolean("permitCam", permitCam);
        detail.setArguments(bundle);
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.activity_main, detail);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void goDetail(int position) {
        //Log.i(TAG,"memo==========================="+datas.get(position).getMemo());
        bundle = new Bundle(7);
        bundle.putInt("id", datas.get(position).getId());
        bundle.putBoolean("load", true);
        bundle.putString("title",datas.get(position).getTitle());
        bundle.putString("contents",datas.get(position).getContents());
        bundle.putString("date", datas.get(position).getDate());
        bundle.putString("uri", datas.get(position).getUri());
        bundle.putBoolean("permitCam", permitCam);

        detail.setArguments(bundle);

        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.activity_main, detail);
        transaction.addToBackStack(null);
        transaction.commit();
    }


    @Override
    public void backToList() {
        super.onBackPressed();
    }

    @Override
    public void saveToList(Memo memo) throws SQLException {
        Log.i(TAG,"================ saveToList ================");
        Log.i(TAG,"= Id      : "+memo.getId());
        Log.i(TAG,"= Title   : "+memo.getTitle());
        Log.i(TAG,"= Content : "+memo.getContents());
        Log.i(TAG,"= fileUri : "+memo.getUri());
        Log.i(TAG,"============================================");

        // 데이터베이스 연결
        dbHelper = new DBHelper(this);
        // 테이블 연결
        memoDao = dbHelper.getDao(Memo.class);
        // 새 메모 생성
        memoDao.create(memo);
        // 메모를 새롭게 불러옴 (queryForAll)
        datas = memoDao.queryForAll();
        dbHelper.close();

        // Fragment내의 List값 update
        list.setData(datas);
        // 저장 후 메인 화면으로 복귀한다.
        super.onBackPressed();
        list.refreshAdapter();
    }

    @Override
    public void editToList(Memo memo) throws SQLException {
        int id = memo.getId();
        String title = memo.getTitle();
        String contents = memo.getContents();
        String date = memo.getDate();
        String uri = memo.getUri();
        Log.i(TAG,"================ editToList ================");
        Log.i(TAG,"= Title   : "+memo.getTitle());
        Log.i(TAG,"= Content : "+memo.getContents());
        Log.i(TAG,"= fileUri : "+memo.getUri());
        Log.i(TAG,"============================================");
        // 데이터베이스 연결
        dbHelper = dbHelper = new DBHelper(this);
        // 테이블 연결
        memoDao = dbHelper.getDao(Memo.class);
        // 변경할 레코드를 가져온다.
        memo = memoDao.queryForId(id);
        // 변경한 값을 입력한다.
        //memo.setId(id);
        memo.setTitle(title);
        memo.setContents(contents);
        memo.setDate(date);
        memo.setUri(uri);
        // 테이블에 반영한다.
        memoDao.update(memo);
        // 메모를 새롭게 불러옴 (queryForAll)
        datas = memoDao.queryForAll();
        dbHelper.close();

        // Fragment내의 List값 update
        list.setData(datas);
        // 저장 후 메인 화면으로 복귀한다.
        super.onBackPressed();
        list.refreshAdapter();
    }

    @Override
    public void delToList(Memo memo) throws SQLException {
        Log.i(TAG,"================ delToList- ================");
        Log.i(TAG,"= Title   : "+memo.getTitle());
        Log.i(TAG,"= Content : "+memo.getContents());
        Log.i(TAG,"= fileUri : "+memo.getUri());
        Log.i(TAG,"============================================");


        dbHelper = new DBHelper(this);
        memoDao = dbHelper.getDao(Memo.class);
        memoDao.deleteById(memo.getId());

        // 메모를 새롭게 불러옴 (queryForAll)
        datas = memoDao.queryForAll();
        dbHelper.close();

        // Fragment내의 List값 update
        list.setData(datas);
        // 저장 후 메인 화면으로 복귀한다.
        super.onBackPressed();
        list.refreshAdapter();
    }

    @Override
    public void delFromList(Memo memo) throws SQLException {
        //memo = datas.get(position);
        dbHelper = new DBHelper(this);
        memoDao = dbHelper.getDao(Memo.class);
        for(Memo memo1 : datas){
            if(memo1.isCheckbox()){
                Log.i(TAG,"================ delFromList- ================");
                Log.i(TAG,"= Title   : "+memo1.getTitle());
                Log.i(TAG,"= Content : "+memo1.getContents());
                Log.i(TAG,"= fileUri : "+memo1.getUri());
                Log.i(TAG,"============================================");
                memoDao.deleteById(memo1.getId());
            }
        }

        // 메모를 새롭게 불러옴 (queryForAll)
        datas = memoDao.queryForAll();
        dbHelper.close();

        // Fragment내의 List값 update
        list.setData(datas);
        // 저장 후 메인 화면으로 복귀한다.
        //super.onBackPressed();
        list.refreshAdapter();
    }

}