package com.miyatu.mirror.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class AccountBindBean {

    /**
     * status : 1
     * msg : 查询成功
     * data : [{"id":4,"relative":"大神","gender":1,"height":"100.23","weight":"100.05"},{"id":5,"relative":"大神本人","gender":1,"height":"175.00","weight":"68.00"}]
     */

    private int status;
    private String msg;
    private List<DataBean> data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean implements Parcelable {
        /**
         * id : 4
         * relative : 大神
         * gender : 1
         * height : 100.23
         * weight : 100.05
         */

        private int id;
        private String relative;
        private int gender;
        private String height;
        private String weight;
        private boolean isSelect;

        protected DataBean(Parcel in) {
            id = in.readInt();
            relative = in.readString();
            gender = in.readInt();
            height = in.readString();
            weight = in.readString();
            isSelect = in.readByte() != 0;
        }

        public DataBean() {

        }

        public static final Creator<DataBean> CREATOR = new Creator<DataBean>() {
            @Override
            public DataBean createFromParcel(Parcel in) {
                return new DataBean(in);
            }

            @Override
            public DataBean[] newArray(int size) {
                return new DataBean[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeInt(id);
            parcel.writeString(relative);
            parcel.writeInt(gender);
            parcel.writeString(height);
            parcel.writeString(weight);
            parcel.writeByte((byte) (isSelect ? 1 : 0));
        }

        public boolean isSelect() {
            return isSelect;
        }

        public void setSelect(boolean select) {
            isSelect = select;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getRelative() {
            return relative;
        }

        public void setRelative(String relative) {
            this.relative = relative;
        }

        public int getGender() {
            return gender;
        }

        public void setGender(int gender) {
            this.gender = gender;
        }

        public String getHeight() {
            return height;
        }

        public void setHeight(String height) {
            this.height = height;
        }

        public String getWeight() {
            return weight;
        }

        public void setWeight(String weight) {
            this.weight = weight;
        }
    }
}
