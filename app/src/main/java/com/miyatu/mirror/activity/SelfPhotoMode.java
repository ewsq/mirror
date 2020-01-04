package com.miyatu.mirror.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.miyatu.mirror.R;

import java.io.IOException;

public class SelfPhotoMode extends AppCompatActivity implements View.OnClickListener {
    private ImageView iv_self_takephoto = null;
    private ImageView iv_self_question = null;
    private ImageView selfphoto_back = null;
    private TextView tv_selfphoto_name = null;
    private TextView tv_selfphoto_height = null;

    private SurfaceView surfaceview = null;
    private Camera camera;
    private Dialog dialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_self_photo_mode);
        RequestPermission();
        showDialog();
        iv_self_takephoto = findViewById(R.id.iv_self_takephoto);
        iv_self_takephoto.setOnClickListener(this);
        iv_self_question = findViewById(R.id.iv_self_question);
        iv_self_question.setOnClickListener(this);
        selfphoto_back = findViewById(R.id.iv_selfphoto_back);
        selfphoto_back.setOnClickListener(this);
        surfaceview = findViewById(R.id.selfmode_surfaceview);

        tv_selfphoto_name = findViewById(R.id.tv_selfphoto_name);
        tv_selfphoto_height = findViewById(R.id.tv_selfphoto_height);

        SurfaceHolder holder = surfaceview.getHolder();
        holder.setFixedSize(176, 155);// 设置分辨率
        holder.setKeepScreenOn(true);
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        // SurfaceView只有当activity显示到了前台，该控件才会被创建     因此需要监听surfaceview的创建
        holder.addCallback(new MySelfSurfaceCallback());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (camera != null) {
            camera.stopPreview();
            camera.setPreviewCallback(null);
            camera.release();
            camera = null;
        }
    }

    /**
     * 对话框提示
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void showDialog() {
        // 构建dialog显示的view布局
        View view_dialog = getLayoutInflater().from(this).inflate(R.layout.selfphototipcontent, null);
        ImageView iv_cancel = view_dialog.findViewById(R.id.iv_selfphoto_cancel);
        if (dialog == null) {
            // 创建AlertDialog对象
            dialog = new AlertDialog.Builder(this)
                    .create();
            dialog.show();
            // 设置点击不可取消
            dialog.setCancelable(false);

            // 获取Window对象
            Window window = dialog.getWindow();
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            // 设置Window尺寸
            WindowManager.LayoutParams lp = window.getAttributes();
            int heightPixels = this.getResources().getDisplayMetrics().heightPixels;
            int widthPixels = this.getResources().getDisplayMetrics().widthPixels;
            lp.width = widthPixels * 4 / 5;
            lp.height = heightPixels / 2;
            window.setAttributes(lp);
            // 设置显示视图内容
            window.setContentView(view_dialog);
        } else {
            dialog.show();
        }
        iv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
//                Toast.makeText(SelfPhotoMode.this, "点击了取消提示", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_self_takephoto: {
//                Toast.makeText(this, "点击了拍照", Toast.LENGTH_SHORT).show();
                takepicture(v);
                break;
            }
            case R.id.iv_self_question: {
//                Toast.makeText(this, "点击了问题", Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.iv_selfphoto_back: {
                finish();
//                Toast.makeText(this, "点击了返回", Toast.LENGTH_SHORT).show();
                break;
            }
        }

    }

    /**
     * 监听surfaceview的创建
     *
     * @author Administrator
     * Surfaceview只有当activity显示到前台，该空间才会被创建
     */
    private final class MySelfSurfaceCallback implements SurfaceHolder.Callback {

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width,
                                   int height) {
            // TODO Auto-generated method stub

        }

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            // TODO Auto-generated method stub

            try {
                // 当surfaceview创建就去打开相机
                camera = Camera.open(1);
                camera.setDisplayOrientation(90);
                camera.setPreviewDisplay(surfaceview.getHolder());
                // 开启预览
                camera.startPreview();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            // TODO Auto-generated method stub
            if (camera != null) {
                camera.release();
                camera = null;
            }
        }

    }

    public void autofoucs(View v) {
        // 自动对焦由硬件来完成
    }

    public void takepicture(View v) {
        /*
         * shutter:快门被按下
         * raw:相机所捕获的原始数据
         * jpeg:相机处理的数据
         */
        camera.takePicture(null, null, new MySelfPictureCallback());
    }

    void RequestPermission() {
        if (ContextCompat.checkSelfPermission(SelfPhotoMode.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(SelfPhotoMode.this, new String[]{Manifest.permission.CAMERA}, 1);
        }
    }


    private final class MySelfPictureCallback implements Camera.PictureCallback {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            // TODO Auto-generated method stub
//            Toast.makeText(SelfPhotoMode.this, "拍照完成 data[]:" + data, Toast.LENGTH_SHORT).show();
            // 在拍照的时候相机是被占用的,拍照之后需要重新预览

            tv_selfphoto_name.setText("王二");
            tv_selfphoto_height.setText("191cm");
            camera.startPreview();
        }

    }
}
