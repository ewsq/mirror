package com.miyatu.mirror.bean;

public class AddMeasurementBean {

    /**
     * status : 1
     * msg : 添加成功
     * data : {"measure_id":12,"price":0.01}
     */

    private int status;
    private String msg;
    private DataBean data;

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

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * measure_id : 12
         * price : 0.01
         */

        private int measure_id;
        private double price;

        public int getMeasure_id() {
            return measure_id;
        }

        public void setMeasure_id(int measure_id) {
            this.measure_id = measure_id;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }
    }
}
