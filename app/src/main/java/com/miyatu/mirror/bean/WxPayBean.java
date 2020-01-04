package com.miyatu.mirror.bean;

import com.google.gson.annotations.SerializedName;

public class WxPayBean {

    /**
     * msg : 获取成功
     * status : 1
     * result : {"appid":"wx5a8bced094340e09","noncestr":"D2l34RsI3YclXjAE","package":"Sign=WXPay","partnerid":"1560764271","prepayid":"wx25163446842699d03ebf41fc1104193800","timestamp":1577262886,"sign":"95B73DC4AD917818563775CBF42FF109"}
     */

    private String msg;
    private int status;
    private ResultBean result;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public static class ResultBean {
        /**
         * appid : wx5a8bced094340e09
         * noncestr : D2l34RsI3YclXjAE
         * package : Sign=WXPay
         * partnerid : 1560764271
         * prepayid : wx25163446842699d03ebf41fc1104193800
         * timestamp : 1577262886
         * sign : 95B73DC4AD917818563775CBF42FF109
         */

        private String appid;
        private String noncestr;
        @SerializedName("package")
        private String packageX;
        private String partnerid;
        private String prepayid;
        private int timestamp;
        private String sign;

        public String getAppid() {
            return appid;
        }

        public void setAppid(String appid) {
            this.appid = appid;
        }

        public String getNoncestr() {
            return noncestr;
        }

        public void setNoncestr(String noncestr) {
            this.noncestr = noncestr;
        }

        public String getPackageX() {
            return packageX;
        }

        public void setPackageX(String packageX) {
            this.packageX = packageX;
        }

        public String getPartnerid() {
            return partnerid;
        }

        public void setPartnerid(String partnerid) {
            this.partnerid = partnerid;
        }

        public String getPrepayid() {
            return prepayid;
        }

        public void setPrepayid(String prepayid) {
            this.prepayid = prepayid;
        }

        public int getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(int timestamp) {
            this.timestamp = timestamp;
        }

        public String getSign() {
            return sign;
        }

        public void setSign(String sign) {
            this.sign = sign;
        }
    }
}
