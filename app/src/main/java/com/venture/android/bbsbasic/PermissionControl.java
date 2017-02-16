package com.venture.android.bbsbasic;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.os.Build;

/**
 * 권한 처리를 담당하는 클래스
 *
 * 권한 변경시 PERMISSION ARRAY의 값만 변경해 주면 된다.
 *
 */

public class PermissionControl {
    // 요청할 권한 목록
    public final static String PERMISSION_ARRAY[] = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE
            , Manifest.permission.CAMERA
    };


    // Runtime Permission 관련 부분은 안드로이드 6.0 마시멜로 버전 이후부터 사용 가능
    // 따라서 6.0 이전 버전들은 @TargetApi코드를 통해 컴파일을 하더라도 무시하는 것으로 처리된다.
    // 1.1 권한체크
    @TargetApi(Build.VERSION_CODES.M)
    public static boolean checkPermission(MainActivity activity, int req_permission) {

        // 1.1 런타임 권한체크
        boolean permCheck = true;
        for(String perm : PERMISSION_ARRAY) {
            if(activity.checkSelfPermission(perm) != PackageManager.PERMISSION_GRANTED){
                permCheck = false;
                break;
            }
        }

        // 1.2 퍼미션이 모두 true이면 그냥 프로그램 실행
        if( permCheck ){
            return true;
        } else {
            // 1.3 시스템에 권한요청
            activity.requestPermissions(PERMISSION_ARRAY, req_permission);
            return false;
        }
    }

    // 권한체크 후 콜백처리
    public static boolean onCheckResult(int[] grantResults) {
        boolean checkResult = true;
        // 권한처리 결과값을 반복문을 돌면서 확인한 후 하나라도 승인되지 않았다면 false를 리턴해준다
        for(int result : grantResults) {
            if(result != PackageManager.PERMISSION_GRANTED){
                checkResult = false;
                break;
            }
        }
        return checkResult;
    }
}
