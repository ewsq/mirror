package com.miyatu.mirror.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class MeasurementBean implements Parcelable {
    private String value;
    private String name;
    private String intro;
    private String unit;

    public MeasurementBean(){}


    protected MeasurementBean(Parcel in) {
        value = in.readString();
        name = in.readString();
        intro = in.readString();
        unit = in.readString();
    }

    public static final Creator<MeasurementBean> CREATOR = new Creator<MeasurementBean>() {
        @Override
        public MeasurementBean createFromParcel(Parcel in) {
            return new MeasurementBean(in);
        }

        @Override
        public MeasurementBean[] newArray(int size) {
            return new MeasurementBean[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(value);
        parcel.writeString(name);
        parcel.writeString(intro);
        parcel.writeString(unit);
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public static Creator<MeasurementBean> getCREATOR() {
        return CREATOR;
    }
}
