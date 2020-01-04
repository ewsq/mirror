package com.miyatu.mirror.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.CountDownTimer;
import android.provider.Settings;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.miyatu.mirror.PublicActivity;
import com.miyatu.mirror.R;
import com.miyatu.mirror.util.CommonUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 首页welcome页面
 */
public class SplashActivity extends PublicActivity {
    private int mRequestCode = 0x11;
    private List<String> mPermissionList = new ArrayList<>();
    private String[] permissions = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.INTERNET,
            Manifest.permission.CAMERA};

    private Context context;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        context = SplashActivity.this;
        initPermissions();
    }

    @Override
    protected void initEvent() {

    }

    @Override
    protected void updateView() {

    }

//    private void initTimer() {
//        timer=new Timer();
//        userJson = PreferencesUtils.getString(this, PreferencesUtils.USERDATA, "");
//        timerTask=new TimerTask() {
//            @Override
//            public void run() {
//                if (userJson == null || userJson.equals("")) {
//                    toLoginActivity();
//                }
//                else {
//                    toHomeActivity();
//                }
//            }
//        };
//        timer.schedule(timerTask,1500);
//    }

    private void initTimer() {
        new CountDownTimer(2*1000,1000) {
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                HomeActivity.startActivity(SplashActivity.this);
                finish();
            }
        }.start();
    }

    private void toLoginActivity() {
        Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
    private void toHomeActivity() {
        Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
        startActivity(intent);
        finish(
        );
    }

    private void initPermissions() {
        // 版本判断。当手机系统大于 23 时，才有必要去判断权限是否获取
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (String permission : permissions) {
                if (ContextCompat.checkSelfPermission(this, permission) !=
                        PackageManager.PERMISSION_GRANTED) {
                    mPermissionList.add(permission);//添加还未授予的权限到mPermissionList中
                }
//                // 检查该权限是否已经获取
//                int i = ContextCompat.checkSelfPermission(this, permission);
//                // 权限是否已经 授权 GRANTED---授权  DINIED---拒绝
//                if (i != PackageManager.PERMISSION_GRANTED) {
//                    // 如果没有授予该权限，就去提示用户请求
//                    ActivityCompat.requestPermissions(this, permissions, 0);
//                }
            }
            //申请权限
            if (mPermissionList.size() > 0) {//有权限没有通过，需要申请
                ActivityCompat.requestPermissions(this, permissions, mRequestCode);
            } else {
                initTimer();
            }
        } else {
            initTimer();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean hasPermissionDismiss = false;//有权限没有通过
        if (mRequestCode == requestCode) {
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] == -1) {
                    hasPermissionDismiss = true;
                    break;
                }
            }
        }
        if (hasPermissionDismiss) {//如果有没有被允许的权限
            showPermissionDialog();
        } else {
            initTimer();
        }
    }

    AlertDialog mPermissionDialog;
    String mPackName = CommonUtils.getPackageName(context);

    private void showPermissionDialog() {
        if (mPermissionDialog == null) {
            mPermissionDialog = new AlertDialog.Builder(this)
                    .setMessage("已禁用权限，请手动授予")
                    .setPositiveButton("设置", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            cancelPermissionDialog();

                            Uri packageURI = Uri.parse("package:" + mPackName);
                            Intent intent = new Intent(Settings.
                                    ACTION_APPLICATION_DETAILS_SETTINGS, packageURI);
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //关闭页面或者做其他操作
                            cancelPermissionDialog();
                            finish();
                        }
                    })
                    .create();
        }
        mPermissionDialog.show();
    }

    private void cancelPermissionDialog() {
        mPermissionDialog.cancel();
    }

}
