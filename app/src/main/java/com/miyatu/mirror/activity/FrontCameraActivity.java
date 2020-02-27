package com.miyatu.mirror.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.camerakit.CameraKit;
import com.miyatu.mirror.Custom.ProgresDialog;
import com.miyatu.mirror.MyApp;
import com.miyatu.mirror.R;
import com.miyatu.mirror.util.ScreenUtils;
import com.tozmart.tozisdk.activity.RxAppCompatActivity;
import com.tozmart.tozisdk.api.ProcessCallback;
import com.tozmart.tozisdk.constant.Gender;
import com.tozmart.tozisdk.constant.Language;
import com.tozmart.tozisdk.constant.PhotoType;
import com.tozmart.tozisdk.constant.SDKCode;
import com.tozmart.tozisdk.constant.Unit;
import com.tozmart.tozisdk.entity.CameraAngle;
import com.tozmart.tozisdk.entity.ErrorWarn;
import com.tozmart.tozisdk.entity.ProcessData;
import com.tozmart.tozisdk.entity.SdkResponse;
import com.tozmart.tozisdk.sdk.OneMeasureSDKLite;
import com.tozmart.tozisdk.view.CameraView;

public class FrontCameraActivity extends RxAppCompatActivity {
    public static final int REQUEST_GALLERY = 9162;

    private ImageView ivHelp;
    private ImageView ivTakePhoto;
    private ImageView ivPhotoAlbum;
    private CameraView cameraView;
    private TextView tvUserName;
    private TextView tvUserHeight;
    private TextView tvCannotTakePhoto;

    private PopupWindow popupWindow;

    private String userName;
    private int gender;
    private int height;
    private int weight;
    private int relativeID;

    private CameraAngle cameraAngle;
    private Bitmap bitmap;
    private ProgresDialog progresDialog;

    private int cameraFacing;

    public static void startActivity(Context context, String userName, int gender, int height, int weight, int relativeID){
        Intent intent = new Intent(context, FrontCameraActivity.class);
        intent.putExtra("userName", userName);
        intent.putExtra("gender", gender);
        intent.putExtra("height", height);
        intent.putExtra("weight", weight);
        intent.putExtra("relativeID", relativeID);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_front_camera);

        new OneMeasureSDKLite.Builder()
                .withActivity(FrontCameraActivity.this)
                .setAppKey("281474788982530048191216143600")
                .setAppSecret("44666d8b931c5882cb7ddade3e668b19")
                .setName(getIntent().getExtras().getString("userName"))
                .setGender(getIntent().getExtras().getInt("gender") == 1? Gender.MALE : Gender.FEMALE)
                .setHeight(Float.parseFloat(getIntent().getExtras().getString("height")))
                .setWeight(Float.parseFloat(getIntent().getExtras().getString("weight")))
                .setUserId(getIntent().getExtras().getString("relativeID"))
                .setLanguage(Language.CHINESE)
                .setUnit(Unit.METRIC)
                .build();


        initPopWindow();

        ivHelp = findViewById(R.id.ivHelp);
        ivTakePhoto = findViewById(R.id.ivTakePhoto);
        ivPhotoAlbum = findViewById(R.id.ivPhotoAlbum);
        cameraView = findViewById(R.id.cameraView);
        tvUserName = findViewById(R.id.tvUserName);
        tvUserHeight = findViewById(R.id.tvUserHeight);
        tvCannotTakePhoto = findViewById(R.id.tvCannotTakePhoto);

        progresDialog = new ProgresDialog(this);

        cameraFacing = getIntent().getExtras().getInt("cameraFacing");
        switch (cameraFacing) {
            case MyApp.FACING_BACK:
                cameraView.setFacing(CameraKit.FACING_BACK);
                break;
            case MyApp.FACING_FRONT:
                cameraView.setFacing(CameraKit.FACING_FRONT);
                break;
        }

        initSensorListener();

        tvUserName.setText(getIntent().getExtras().getString("userName"));
        tvUserHeight.setText(getIntent().getExtras().getString("height") + "cm");

        ivHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);
            }
        });

        ivTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraView.captureImage(new CameraView.ImageCallback() {
                    @Override
                    public void onImage(CameraView view, byte[] jpeg) {
                        bitmap = BitmapFactory.decodeByteArray(jpeg, 0, jpeg.length);
                        processImage(false);
                    }
                });
            }
        });

        ivPhotoAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraView.openGalleryFromActivity(FrontCameraActivity.this, REQUEST_GALLERY);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent result) {
        super.onActivityResult(requestCode, resultCode, result);
        if (requestCode == REQUEST_GALLERY && resultCode == RESULT_OK) {
            if (result.getData() != null) {
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), result.getData());
                    processImage(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void initSensorListener() {
        // 在获取传感器相关数据之前必须先检测传感器是否合格
        if (cameraView.lackRequiredSensors()) {
            Toast.makeText(this, "此手机没有检测到相关传感器", Toast.LENGTH_SHORT).show();
        } else {
            cameraView.setOnSensorListener(new CameraView.OnSensorListener() {
                @Override
                public void onSensorOk() {
                    ivTakePhoto.setVisibility(View.VISIBLE);
                    tvCannotTakePhoto.setVisibility(View.GONE);
                }
                @Override
                public void onSensorError() {
                    ivTakePhoto.setVisibility(View.GONE);
                    tvCannotTakePhoto.setVisibility(View.VISIBLE);
                }
                @Override
                public void onSensorAngle(float sensorFB, float sensorLR) {
                    cameraAngle = new CameraAngle(sensorFB, sensorLR);
                }
            });
        }
    }

    private void initPopWindow() {
        View view = LayoutInflater.from(FrontCameraActivity.this).inflate(R.layout.pop_front_take_photo_help,null);
        int width = ScreenUtils.getScreenWidth(FrontCameraActivity.this);
        int height = ScreenUtils.getScreenHeight(FrontCameraActivity.this);
        popupWindow = new PopupWindow(view, width*4/5, height/2);
        popupWindow.setFocusable(true);
        popupWindow.setTouchable(true);
        popupWindow.setOutsideTouchable(false);
        popupWindow.setClippingEnabled(false);
    }

    private void processImage(boolean selectFromGallery) {
        if (progresDialog != null) {
            progresDialog.show();
        }
        if (selectFromGallery) {
            cameraAngle = new CameraAngle(0, 0);
        }
        OneMeasureSDKLite.getInstance().processImage(FrontCameraActivity.this, bitmap, PhotoType.FRONT, cameraAngle, null, new ProcessCallback() {
            @Override
            public void onResponse(SdkResponse sdkResponse, String taskId, ProcessData processData) {

                if (progresDialog != null) {
                    progresDialog.dismiss();
                }
                if (sdkResponse.getServerStatusCode().equals(SDKCode.SERVER_SUCCESS)) {

                    if (processData != null && processData.getImageProcessFeedback() != null) {
                        if (processData.getImageProcessFeedback().getFrontImageErrors().size() != 0) {
                            StringBuilder stringBuilder = new StringBuilder();
                            for (ErrorWarn error : processData.getImageProcessFeedback().getFrontImageErrors()) {
                                stringBuilder.append(error.getContent());
                                stringBuilder.append("\n");
                            }
                            Toast.makeText(FrontCameraActivity.this, stringBuilder.toString(), Toast.LENGTH_LONG).show();
                            return;
                        }
                    }

                    Bundle bundle = new Bundle();
                    bundle.putString("taskId", taskId);
                    if (getIntent().getExtras() != null) {
                        bundle.putInt("apiType", getIntent().getExtras().getInt("apiType"));
                        bundle.putString("userName", getIntent().getExtras().getString("userName"));
                        bundle.putString("height", getIntent().getExtras().getString("height"));
                        bundle.putInt("relativeID", getIntent().getExtras().getInt("relativeID"));
                        bundle.putInt("cameraFacing", getIntent().getExtras().getInt("cameraFacing"));
                    }
//                    bundle.putString("userName", userName);
//                    bundle.putInt("gender", gender);
//                    bundle.putInt("height", height);
//                    bundle.putInt("weight", weight);
//                    bundle.putInt("relativeID", relativeID);
                    startActivity(new Intent(FrontCameraActivity.this, SideCameraActivity.class).putExtras(bundle));
                    // if next activity is Camera activity, pls call cameraView.onStop();, otherwise camera can not be opened.
                    cameraView.onStop();

                    finish();

                    if (bitmap != null && !bitmap.isRecycled()) {
                        bitmap.recycle();
                    }
                } else {
                    Toast.makeText(FrontCameraActivity.this, sdkResponse.getServerStatusText(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        try {
            cameraView.onStart();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            cameraView.onResume();
        } catch (Exception e) {
            e.printStackTrace();
        }
        cameraView.registerSensor();
    }

    @Override
    public void onPause() {
        try {
            cameraView.onPause();
        } catch (Exception e) {
            e.printStackTrace();
        }
        cameraView.unregisterSensor();
        super.onPause();
    }

    @Override
    public void onStop() {
        try {
            cameraView.onStop();
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onStop();
    }

    @Override
    public void onDestroy() {
        if (bitmap != null && !bitmap.isRecycled()) {
            bitmap.recycle();
        }
        super.onDestroy();
    }

}
