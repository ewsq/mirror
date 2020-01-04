package com.miyatu.mirror.activity;

import android.content.Intent;
import android.graphics.PointF;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.miyatu.mirror.Custom.AnimatedPathImageView;
import com.miyatu.mirror.bean.MeasurementBean;
import com.miyatu.mirror.util.ScreenUtils;
import com.tozmart.tozisdk.activity.RxAppCompatActivity;
import com.tozmart.tozisdk.api.GetMeasurementsCallback;
import com.tozmart.tozisdk.api.GetProfileCallback;
import com.tozmart.tozisdk.constant.SDKCode;
import com.tozmart.tozisdk.entity.ErrorWarn;
import com.tozmart.tozisdk.entity.MeasurementEntity;
import com.tozmart.tozisdk.entity.MeasurementsData;
import com.tozmart.tozisdk.entity.Profile2ModelData;
import com.tozmart.tozisdk.entity.SdkResponse;
import com.tozmart.tozisdk.sdk.OneMeasureSDKLite;
import com.tozmart.tozisdk.utils.BitmapHolder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ProfileActivity extends RxAppCompatActivity {
    private static final int REQUEST_EDIT_OUTLINE = 10000;

    ProgressBar progressBar;
    LinearLayout content;
    AnimatedPathImageView frontView;
    AnimatedPathImageView sideView;
    TextView info;
    Button nextBtn;

    private List<MeasurementEntity> measurementEntityList = new ArrayList<MeasurementEntity>();
    private List<MeasurementBean> measurementBeanList = new ArrayList<>();

    private Profile2ModelData mProfile2ModelData;
    private boolean hasEdit;// 是否编辑过轮廓
    private String taskId;
    private int apiType;

    private String userName;
    private String height;
    private int relativeID;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent result) {
        super.onActivityResult(requestCode, resultCode, result);
        if (requestCode == REQUEST_EDIT_OUTLINE && resultCode == RESULT_OK) {
            mProfile2ModelData = result.getExtras().getParcelable("profile2ModelData");
            hasEdit = result.getExtras().getBoolean("hasEdit");
            drawFrontOutline();
            drawSideOutline();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            taskId = bundle.getString("taskId", "");
            apiType = bundle.getInt("apiType");
            userName = bundle.getString("userName");
            height = bundle.getString("height");
            relativeID = bundle.getInt("relativeID");
        }

        progressBar = findViewById(R.id.progress_bar);
        content = findViewById(R.id.content);
        frontView = findViewById(R.id.frontView);
        sideView = findViewById(R.id.sideView);
        info = findViewById(R.id.info);
        nextBtn = findViewById(R.id.next_btn);
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                measurement();
                Bundle bundle = new Bundle();
                bundle.putParcelable("profile2ModelData", mProfile2ModelData);
                bundle.putInt("apiType", apiType);
                bundle.putBoolean("hasEdit", hasEdit);
                bundle.putString("taskId", taskId);
                bundle.putString("userName", userName);
                bundle.putString("height", height);
                bundle.putInt("relativeID", relativeID);
                startActivity(new Intent(ProfileActivity.this, MeasurementsActivity.class).putExtras(bundle));
            }
        });

        getImagesProfile();

//        frontView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Bundle bundle = new Bundle();
//                bundle.putParcelable("profile2ModelData", mProfile2ModelData);
//                bundle.putInt("whichSide", FRONT);
//                startActivityForResult(new Intent(ProfileActivity.this, EditOutlineActivity.class).putExtras(bundle), REQUEST_EDIT_OUTLINE);
//            }
//        });
//        sideView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Bundle bundle = new Bundle();
//                bundle.putParcelable("profile2ModelData", mProfile2ModelData);
//                bundle.putInt("whichSide", SIDE);
//                startActivityForResult(new Intent(ProfileActivity.this, EditOutlineActivity.class).putExtras(bundle), REQUEST_EDIT_OUTLINE);
//            }
//        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        BitmapHolder.recycle();
    }

    private void getImagesProfile() {
        OneMeasureSDKLite.getInstance().getProfile(ProfileActivity.this, taskId, new GetProfileCallback() {
            @Override
            public void onResponse(SdkResponse sdkResponse, Profile2ModelData profile2ModelData) {
                mProfile2ModelData = profile2ModelData;
                progressBar.setVisibility(View.GONE);
                content.setVisibility(View.VISIBLE);
                if (sdkResponse.getServerStatusCode().equals(SDKCode.SERVER_SUCCESS)) {
                    StringBuilder error = new StringBuilder();
                    if (profile2ModelData.getImageProcessFeedback() != null) {
                        if (profile2ModelData.getImageProcessFeedback().getFrontImageErrors() != null &&
                                profile2ModelData.getImageProcessFeedback().getFrontImageErrors().size() != 0) {
                            for (ErrorWarn errorWarn : profile2ModelData.getImageProcessFeedback().getFrontImageErrors()) {
                                error.append(errorWarn.getContent());
                                error.append("\n");
                            }
                        }
                        if (profile2ModelData.getImageProcessFeedback().getSideImageErrors() != null &&
                                profile2ModelData.getImageProcessFeedback().getSideImageErrors().size() != 0) {
                            for (ErrorWarn errorWarn : profile2ModelData.getImageProcessFeedback().getSideImageErrors()) {
                                error.append(errorWarn.getContent());
                                error.append("\n");
                            }
                        }
                    }
                    if (!TextUtils.isEmpty(error.toString())) {
                        info.setText(error.toString());
                        return;
                    }

                    BitmapHolder.recycle();
                    BitmapHolder.setFrontBitmap(profile2ModelData.getFrontProcessedBitmap());
                    BitmapHolder.setSideBitmap(profile2ModelData.getSideProcessedBitmap());
                    drawFrontOutline();
                    drawSideOutline();
                    nextBtn.setVisibility(View.VISIBLE);

                } else {
                    info.setText(sdkResponse.getServerStatusText());
                }
            }
        });
    }

    private void drawFrontOutline() {
        float bmapWidth = BitmapHolder.getFrontBitmap().getWidth();
        float bmapHeight = BitmapHolder.getFrontBitmap().getHeight();
        float ratio = bmapHeight / bmapWidth;
        int viewHeight = Math.round(ScreenUtils.getScreenWidth(ProfileActivity.this) / 2.f * ratio);
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) frontView.getLayoutParams();
        lp.height = viewHeight;
        frontView.setLayoutParams(lp);

        frontView.setImageBitmap(BitmapHolder.getFrontBitmap());

        ArrayList<PointF> frontPointsOnView = new ArrayList<>();
        float frontScale = viewHeight / bmapHeight;
        for (PointF p : mProfile2ModelData.getFrontAllPoints()) {
            frontPointsOnView.add(new PointF(p.x * frontScale, p.y * frontScale));
        }

        if (mProfile2ModelData.getfLooseIdx() != null) {
            frontView.setPath(frontPointsOnView, Arrays.asList(mProfile2ModelData.getfLooseIdx()));
        } else {
            frontView.setPath(frontPointsOnView, null);
        }
    }

    private void drawSideOutline() {
        float bmapWidth = BitmapHolder.getSideBitmap().getWidth();
        float bmapHeight = BitmapHolder.getSideBitmap().getHeight();
        float ratio = bmapHeight / bmapWidth;
        int viewHeight = Math.round(ScreenUtils.getScreenWidth(ProfileActivity.this) / 2.f * ratio);
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) sideView.getLayoutParams();
        lp.height = viewHeight;
        sideView.setLayoutParams(lp);

        sideView.setImageBitmap(BitmapHolder.getSideBitmap());

        ArrayList<PointF> sidePointsOnView = new ArrayList<>();
        float sideScale = viewHeight / bmapHeight;
        for (PointF p : mProfile2ModelData.getSideAllPoints()) {
            sidePointsOnView.add(new PointF(p.x * sideScale, p.y * sideScale));
        }

        if (mProfile2ModelData.getsLooseIdx() != null) {
            sideView.setPath(sidePointsOnView, Arrays.asList(mProfile2ModelData.getsLooseIdx()));
        } else {
            sideView.setPath(sidePointsOnView, null);
        }
    }

    private void measurement() {
        OneMeasureSDKLite.getInstance().getMeasurementsByTask(ProfileActivity.this, taskId, new GetMeasurementsCallback() {
            @Override
            public void onResponse(SdkResponse sdkResponse, final MeasurementsData measurementsData) {
                if (sdkResponse.getServerStatusCode().equals(SDKCode.SERVER_SUCCESS)) {
                    for (MeasurementEntity measurementEntity : measurementsData.getMeasurementEntities()) {
                        measurementEntityList.add(measurementEntity);
                    }
                    for (int i = 0; i < measurementEntityList.size(); i ++) {
                        MeasurementBean bean = new MeasurementBean();
                        bean.setValue(measurementEntityList.get(i).getMeaValue());
                        bean.setIntro(measurementEntityList.get(i).getSizeIntro());
                        bean.setName(measurementEntityList.get(i).getSizeName());
                        bean.setUnit(measurementEntityList.get(i).getUnit());
                        measurementBeanList.add(bean);
                    }
                    Bundle bundle = new Bundle();
                    bundle.putString("userName", userName);
                    bundle.putString("height", height);
                    bundle.putInt("relativeID", relativeID);
                    bundle.putParcelableArrayList("measurementBeanList", (ArrayList<? extends Parcelable>) measurementBeanList);
                    System.out.println("####step SIZE=" + measurementBeanList.size());
                    startActivity(new Intent(ProfileActivity.this, AddMeasurementActivity.class).putExtras(bundle));
                } else {
                    Toast.makeText(getApplicationContext(),sdkResponse.getServerStatusText() + "(" + sdkResponse.getServerStatusCode() + ")", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
