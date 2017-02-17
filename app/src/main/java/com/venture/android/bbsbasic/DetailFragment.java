package com.venture.android.bbsbasic;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.venture.android.bbsbasic.domain.Memo;
import com.venture.android.bbsbasic.interfaces.DetailInterface;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.app.Activity.RESULT_OK;
import static com.google.android.gms.internal.zzs.TAG;


public class DetailFragment extends Fragment implements View.OnClickListener{

    private final int REQ_CAMERA     = 101;           // 카메라 권한 요청

    Context context = null;
    DetailInterface detailInterface = null;
    //int position = -1;
    //View view = null;
    private View view = null;

    // 사진정보 데이터 저장소
    //List<String> galleryData = new ArrayList<>();
    //RecyclerView recyclerView;
    Uri fileUri = null;
    Intent intent;

    ImageButton btnSave, btnCancel, btnDel, btnNew;
    ImageButton btnCamera;
    ImageView   imageView;
    EditText editMemo;
    EditText editTitle;
    int tmpId;
    boolean tmpLoad;
    String tmpTitle;
    String tmpContents;
    String tmpDate;
    Boolean tmpPermitCam;
    String tmpUri;


    public DetailFragment() {
        // Required empty public constructor
    }

    public static DetailFragment newInstance() {
        DetailFragment fragment = new DetailFragment();
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG,"================ onResume ==================");
        Log.i(TAG,"= Id   : "+tmpId);
        Log.i(TAG,"= Title : "+tmpTitle);
        Log.i(TAG,"= Content : "+tmpContents);
        Log.i(TAG,"= Uri : "+tmpUri);
        Log.i(TAG,"============================================");
        if(tmpId!=0 || tmpTitle !=null || tmpContents!=null ) {
            editMemo.setText(tmpContents);
            editTitle.setText(tmpTitle);
            if(tmpUri != null) {
                fileUri = Uri.parse(tmpUri);
                if (fileUri != null) {
                    // 글라이드로 이미지 세팅하면 자동으로 사이즈 조절
                    Glide.with(this)
                            .load(fileUri)
                            .override(150,150)
                            .into(imageView);
                }
            } else {
                imageView.setImageBitmap(null);
            }
        }
        else if(tmpId==0 && tmpTitle !=null || tmpContents!=null ){
            editMemo.setText("");
            editTitle.setText("");
            imageView.setImageBitmap(null);
        }
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extra = getArguments();

        if(getArguments()!=null){

            tmpLoad = extra.getBoolean("load");
            tmpTitle = extra.getString("title");
            tmpContents = extra.getString("contents");
            tmpDate = extra.getString("date");
            tmpId = extra.getInt("id");
            tmpPermitCam = extra.getBoolean("permitCam");
            tmpUri = extra.getString("uri");
        } else {

        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(view != null)
            return view;

        view      = inflater.inflate(R.layout.fragment_detail, container, false);
        btnSave   = (ImageButton) view.findViewById(R.id.btnSave);
        btnCancel = (ImageButton) view.findViewById(R.id.btnCancel);
        btnDel    = (ImageButton) view.findViewById(R.id.btnDel);
        btnCamera = (ImageButton) view.findViewById(R.id.btnCamera);
        btnNew    = (ImageButton) view.findViewById(R.id.btnNew);

        editMemo  = (EditText) view.findViewById(R.id.editMemo);
        editTitle = (EditText) view.findViewById(R.id.editTitle);
        imageView = (ImageView) view.findViewById(R.id.imageView);


        btnSave.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        btnDel.setOnClickListener(this);
        btnCamera.setOnClickListener(this);
        btnNew.setOnClickListener(this);

        return view;
    }

    // 버튼 비활성화 하기
    private void buttonDisable() {
        btnCamera.setEnabled(false);
    }

    private void buttonEnable() {
        btnCamera.setEnabled(true);
    }

    private void init(){
        // 권한 처리가 통과 되었을때만 버튼을 활성화 시켜준다.
        buttonEnable();
    }




    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        this.detailInterface = (DetailInterface) context;
    }



    @Override
    public void onClick(View v) {
        Memo memo;
        String string;

        switch (v.getId()){
            case R.id.btnSave:
                try {
                    // 새 메모를 위한 List 생성
                    memo = new Memo();

                    // 메모 제목
                    string = editTitle.getText().toString();
                    memo.setTitle(string);
                    // 메모 내용을 받아서 데이터베이스에 저장
                    string = editMemo.getText().toString();
                    memo.setContents(string);
                    // 날짜를 포맷에 맞춰 String 으로 생성
                    Date now = new Date();
                    SimpleDateFormat sdf;
                    //sdf = new SimpleDateFormat("yy/MM/dd hh:mm aa");
                    sdf = new SimpleDateFormat("yy/MM/dd");
                    memo.setDate(sdf.format(now));

                    // Uri저장
                    if(fileUri!=null) {
                        memo.setUri(fileUri.toString());
                    }


                    if(tmpId==0){
                        // 데이터베이스에 저장하고 List Fragment로 돌아간다.
                        detailInterface.saveToList(memo);
                    } else {
                        // 데이터베이스에 수정하고 List Fragment로 돌아간다.
                        memo.setId(tmpId);
                        detailInterface.editToList(memo);
                    }
                    inputClear();

                } catch (SQLException e) {
                    e.printStackTrace();
                }
                break;

            case R.id.btnCancel:
                inputClear();
                detailInterface.backToList();
                break;

            case R.id.btnDel:
                try {
                    memo = new Memo();
                    memo.setId(tmpId);
                    inputClear();
                    detailInterface.delToList(memo);
                }catch (SQLException e){
                    e.printStackTrace();
                }
                break;

            case R.id.btnNew:
                inputClear();
                break;

            case R.id.btnCamera:
                intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                // 롤리팝 이상 버전에서는 아래 코드를 반영해야 한다.
                // --- 카메라 촬영 후 미디어 컨텐트 uri 를 생성해서 외부저장소에 저장한다 ---
                if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ) {
                    ContentValues values = new ContentValues(1);
                    values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpg");
                    fileUri = getContext().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                }
                // --- 여기 까지 컨텐트 uri 강제세팅 ---

                tmpTitle = editTitle.getText().toString();
                tmpContents = editMemo.getText().toString();
                startActivityForResult(intent, REQ_CAMERA);
                break;

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.i("Camera","resultCode==============================="+resultCode);

        switch(requestCode) {
            case REQ_CAMERA :
                if (requestCode == REQ_CAMERA && resultCode == RESULT_OK) { // 사진 확인처리됨 RESULT_OK = -1
                    // 롤리팝 체크
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                        Log.i("Camera", "data.getData()===============================" + data.getData());
                        fileUri = data.getData();
                    }
                    Log.i("Camera", "fileUri===============================" + fileUri);
                    if (fileUri != null) {
                        // 글라이드로 이미지 세팅하면 자동으로 사이즈 조절
                        Glide.with(this)
                                .load(fileUri)
                                .override(150,150)
                                .into(imageView);
                    } else {
                      //  Toast.makeText(this, "사진파일이 없습니다", Toast.LENGTH_LONG).show();
                    }
                } else {
                    // resultCode 가 0이고 사진이 찍혔으면 uri 가 남는데
                    // uri 가 있을 경우 삭제처리...
                }
                break;
        }
    }

    public void inputClear() {
        tmpId = 0;
        editTitle.setText("");
        editMemo.setText("");
        imageView.setImageBitmap(null);
    }

}