package com.miyatu.mirror.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
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
import com.tozmart.tozisdk.constant.PhotoType;
import com.tozmart.tozisdk.constant.SDKCode;
import com.tozmart.tozisdk.entity.CameraAngle;
import com.tozmart.tozisdk.entity.ErrorWarn;
import com.tozmart.tozisdk.entity.ProcessData;
import com.tozmart.tozisdk.entity.SdkResponse;
import com.tozmart.tozisdk.sdk.OneMeasureSDKLite;
import com.tozmart.tozisdk.view.CameraView;

public class SideCameraActivity extends RxAppCompatActivity {
    public static final int REQUEST_GALLERY = 9162;

    private ImageView ivHelp;
    private ImageView ivTakePhoto;
    private ImageView ivPhotoAlbum;
    private CameraView cameraView;
    private TextView tvUserName;
    private TextView tvUserHeight;
    private TextView tvCannotTakePhoto;

    private PopupWindow popupWindow;

    private String taskId;
    private CameraAngle cameraAngle;
    private Bitmap bitmap;
    private ProgresDialog progresDialog;

    private int cameraFacing;

    private int apiType;

    private String userName;
    private String height;
    private int relativeID;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_side_camera);

        initPopWindow();

        ivHelp = findViewById(R.id.ivHelp);
        ivTakePhoto = findViewById(R.id.ivTakePhoto);
        ivPhotoAlbum = findViewById(R.id.ivPhotoAlbum);
        cameraView = findViewById(R.id.cameraView);
        tvUserName = findViewById(R.id.tvUserName);
        tvUserHeight = findViewById(R.id.tvUserHeight);
        tvCannotTakePhoto = findViewById(R.id.tvCannotTakePhoto);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            taskId = bundle.getString("taskId", "");
            apiType = bundle.getInt("apiType");
            userName = bundle.getString("userName");
            height = bundle.getString("height");
            relativeID = bundle.getInt("relativeID");
            cameraFacing = getIntent().getExtras().getInt("cameraFacing");
        }

        tvUserName.setText(userName);
        tvUserHeight.setText(height + "cm");

        switch (cameraFacing) {
            case MyApp.FACING_BACK:
                cameraView.setFacing(CameraKit.FACING_BACK);
                break;
            case MyApp.FACING_FRONT:
                cameraView.setFacing(CameraKit.FACING_FRONT);
                break;
        }


        progresDialog = new ProgresDialog(this);

        if (cameraView.lackRequiredSensors()) {
            Toast.makeText(this, "lack sensors", Toast.LENGTH_SHORT).show();
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
        ivTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraView.captureImage(new CameraView.ImageCallback() {
                    @Override
                    public void onImage(CameraView view, byte[] jpeg) {
                        bitmap = BitmapFactory.decodeByteArray(jpeg, 0, jpeg.length);
                        processImage();
                    }
                });
            }
        });
        ivPhotoAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraView.openGalleryFromActivity(SideCameraActivity.this, REQUEST_GALLERY);
            }
        });
    }

    private void initPopWindow() {
        View view = LayoutInflater.from(SideCameraActivity.this).inflate(R.layout.pop_take_photo_help,null);
        int width = ScreenUtils.getScreenWidth(SideCameraActivity.this);
        int height = ScreenUtils.getScreenHeight(SideCameraActivity.this);
        popupWindow = new PopupWindow(view, width*3/5, height/3);
        popupWindow.setFocusable(true);
        popupWindow.setTouchable(true);
        popupWindow.setOutsideTouchable(false);
        popupWindow.setClippingEnabled(false);
    }

    private void processImage() {
        if (progresDialog != null) {
            progresDialog.show();
        }
        OneMeasureSDKLite.getInstance().processImage(SideCameraActivity.this, bitmap, PhotoType.SIDE, cameraAngle, taskId, new ProcessCallback() {
            @Override
            public void onResponse(SdkResponse sdkResponse, String taskId, ProcessData processData) {
                if (progresDialog != null) {
                    progresDialog.dismiss();
                }
                if (sdkResponse.getServerStatusCode().equals(SDKCode.SERVER_SUCCESS)) {

                    if (processData != null && processData.getImageProcessFeedback() != null) {
                        if (processData.getImageProcessFeedback().getSideImageErrors().size() != 0) {
                            StringBuilder stringBuilder = new StringBuilder();
                            for (ErrorWarn error : processData.getImageProcessFeedback().getSideImageErrors()) {
                                stringBuilder.append(error.getContent());
                                stringBuilder.append("\n");
                            }
                            Toast.makeText(SideCameraActivity.this, stringBuilder.toString(), Toast.LENGTH_LONG).show();
                            return;
                        }
                    }

                    Bundle bundle = new Bundle();
                    bundle.putString("taskId", taskId);
                    bundle.putInt("apiType", apiType);
                    bundle.putString("userName", userName);
                    bundle.putString("height", height);
                    bundle.putInt("relativeID", relativeID);

                    switch (apiType) {
                        case MyApp.PROFILE2MEASURE:
                            startActivity(new Intent(SideCameraActivity.this, ProfileActivity.class).putExtras(bundle));
                            break;
                        case MyApp.IMAGE2MEASURE:
                            startActivity(new Intent(SideCameraActivity.this, MeasurementsActivity.class).putExtras(bundle));
                            break;
                        default:
                            break;
                    }

//                    startActivity(new Intent(SideCameraActivity.this, ProfileActivity.class).putExtras(bundle));

                    finish();

                    if (bitmap != null && !bitmap.isRecycled()) {
                        bitmap.recycle();
                    }
                } else {
                    Toast.makeText(SideCameraActivity.this, sdkResponse.getServerStatusText(), Toast.LENGTH_LONG).show();
                }
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
                    processImage();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
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
