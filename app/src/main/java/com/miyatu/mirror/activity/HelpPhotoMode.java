package com.miyatu.mirror.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Build;
import android.provider.MediaStore;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.myapplication.R;
import com.miyatu.mirror.PublicActivity;

import java.io.IOException;

public class HelpPhotoMode extends PublicActivity implements View.OnClickListener {
    private ImageView iv_help_takephoto = null;
    private ImageView iv_help_question = null;
    private ImageView helpphoto_back = null;
    private ImageView iv_help_choose = null;
    private TextView tv_helpphoto_name = null;
    private TextView tv_helpphoto_height = null;

    private SurfaceView surfaceview = null;
    private Camera camera;
    private Dialog dialog = null;

    public static void startActivity(Context context){
        Intent intent = new Intent(context, HelpPhotoMode.class);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_help_photo_mode;
    }

    @Override
    protected void initView() {
        RequestPermission();

        iv_help_takephoto = findViewById(R.id.iv_help_takephoto);
        iv_help_takephoto.setOnClickListener(this);
        iv_help_question = findViewById(R.id.iv_help_question);
        iv_help_question.setOnClickListener(this);
        iv_help_choose = findViewById(R.id.iv_help_choose);
        iv_help_choose.setOnClickListener(this);
        helpphoto_back = findViewById(R.id.helpphoto_back);
        helpphoto_back.setOnClickListener(this);
        surfaceview = findViewById(R.id.helpmode_surfaceview);

        tv_helpphoto_name = findViewById(R.id.tv_helpphoto_name);
        tv_helpphoto_height = findViewById(R.id.tv_helpphoto_height);

        SurfaceHolder holder = surfaceview.getHolder();
        holder.setFixedSize(176, 155);// 设置分辨率
        holder.setKeepScreenOn(true);
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        // SurfaceView只有当activity显示到了前台，该控件才会被创建     因此需要监听surfaceview的创建
        holder.addCallback(new MyHelpSurfaceCallback());
    }

    @Override
    protected void initData() {
    }

    @Override
    protected void initEvent() {

    }

    @Override
    protected void updateView() {

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_help_takephoto: {
//                Toast.makeText(this, "点击了拍照", Toast.LENGTH_SHORT).show();
                takepicture(v);
                break;
            }
            case R.id.iv_help_question: {
//                Toast.makeText(this, "点击了问题", Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.iv_help_choose: {
//                Toast.makeText(this, "点击了选择图片", Toast.LENGTH_SHORT).show();
                Intent intent2 = new Intent();

                if (Build.VERSION.SDK_INT < 19) {
                    intent2.setAction(Intent.ACTION_GET_CONTENT);
                    intent2.setType("image/*");
                } else {
                    intent2 = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent2.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                }
                startActivityForResult(intent2, 1);
                break;
            }
            case R.id.helpphoto_back: {
                finish();
//                Toast.makeText(this, "点击了返回", Toast.LENGTH_SHORT).show();
                break;
            }
        }
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
     * 监听surfaceview的创建
     * @author Administrator
     * Surfaceview只有当activity显示到前台，该空间才会被创建
     */
    private final class MyHelpSurfaceCallback implements SurfaceHolder.Callback {

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            // TODO Auto-generated method stub

        }

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            // TODO Auto-generated method stub
            try {
                // 当surfaceview创建就去打开相机
                camera = Camera.open(0);
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
        camera.takePicture(null, null, new MyHelpPictureCallback());
    }

    void RequestPermission() {
        if (ContextCompat.checkSelfPermission(HelpPhotoMode.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(HelpPhotoMode.this, new String[]{Manifest.permission.CAMERA}, 1);
        }
    }


    private final class MyHelpPictureCallback implements Camera.PictureCallback {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            // TODO Auto-generated method stub
//            Toast.makeText(HelpPhotoMode.this, "拍照完成 data[]:" + data, Toast.LENGTH_SHORT).show();
            // 在拍照的时候相机是被占用的,拍照之后需要重新预览

            tv_helpphoto_name.setText("王二");
            tv_helpphoto_height.setText("131cm");
            camera.startPreview();
        }
    }

//    OneMeasureSDKLite.getInstance().getMeasurementsByTask(HelpPhotoMode.this, taskId, new GetMeasurementsCallback() {
//        @Override
//        public void onResponse(SdkResponse sdkResponse, MeasurementsData measurementsData) {
//
//        }
//    });


}
