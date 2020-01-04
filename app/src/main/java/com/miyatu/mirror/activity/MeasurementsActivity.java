package com.miyatu.mirror.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.myapplication.R;
import com.miyatu.mirror.MyApp;
import com.miyatu.mirror.bean.MeasurementBean;
import com.tozmart.tozisdk.activity.RxAppCompatActivity;
import com.tozmart.tozisdk.api.GetMeasurementsCallback;
import com.tozmart.tozisdk.constant.SDKCode;
import com.tozmart.tozisdk.entity.MeasurementEntity;
import com.tozmart.tozisdk.entity.MeasurementsData;
import com.tozmart.tozisdk.entity.Profile2ModelData;
import com.tozmart.tozisdk.entity.SdkResponse;
import com.tozmart.tozisdk.sdk.OneMeasureSDKLite;

import java.util.ArrayList;
import java.util.List;

public class MeasurementsActivity extends RxAppCompatActivity{
    private ProgressBar progressBar;

    private Profile2ModelData profile2ModelData;
    private String taskId;
    private int apiType;
    private boolean hasEdit;

    private String userName;
    private String height;
    private int relativeID;

    private List<MeasurementEntity> measurementEntityList = new ArrayList<MeasurementEntity>();
    private List<MeasurementBean> measurementBeanList = new ArrayList<>();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_measurements);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            taskId = bundle.getString("taskId", "");
            apiType = bundle.getInt("apiType");
            profile2ModelData = bundle.getParcelable("profile2ModelData");
            hasEdit = bundle.getBoolean("hasEdit");
            userName = bundle.getString("userName");
            height = bundle.getString("height");
            relativeID = bundle.getInt("relativeID");
        }

        progressBar = findViewById(R.id.progress_bar);

        if (apiType == MyApp.IMAGE2MEASURE) {
            getMeasurements();
        } else {
            if (hasEdit) {
                getMeasurementsByProfile();
            } else {
                getMeasurementsByTask();
            }
        }
    }

    private void getMeasurementsByTask() {
        OneMeasureSDKLite.getInstance().getMeasurementsByTask(MeasurementsActivity.this, taskId, new GetMeasurementsCallback() {
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
                    startActivity(new Intent(MeasurementsActivity.this, AddMeasurementActivity.class).putExtras(bundle));
                } else {
                    Toast.makeText(getApplicationContext(),sdkResponse.getServerStatusText() + "(" + sdkResponse.getServerStatusCode() + ")", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void getMeasurementsByProfile() {
        OneMeasureSDKLite.getInstance().getMeasurementsByProfile(MeasurementsActivity.this, profile2ModelData, new GetMeasurementsCallback() {
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
                    startActivity(new Intent(MeasurementsActivity.this, AddMeasurementActivity.class).putExtras(bundle));
                } else {
                    Toast.makeText(getApplicationContext(),
                            sdkResponse.getServerStatusText() + "(" + sdkResponse.getServerStatusCode() + ")", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void getMeasurements() {
        OneMeasureSDKLite.getInstance().getMeasurements(MeasurementsActivity.this, taskId, new GetMeasurementsCallback() {
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
                    startActivity(new Intent(MeasurementsActivity.this, AddMeasurementActivity.class).putExtras(bundle));
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(),
                            sdkResponse.getServerStatusText() + "(" + sdkResponse.getServerStatusCode() + ")", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
