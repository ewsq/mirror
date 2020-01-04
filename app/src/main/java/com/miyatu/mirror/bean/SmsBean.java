package com.miyatu.mirror.bean;

public class SmsBean {

    /**
     * status : 1
     * msg : 短信发送成功
     * data : {"session_id":"rnq8ep15mqjt69pskrd7evroj0"}
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
         * session_id : rnq8ep15mqjt69pskrd7evroj0
         */

        private String session_id;

        public String getSession_id() {
            return session_id;
        }

        public void setSession_id(String session_id) {
            this.session_id = session_id;
        }
    }
}
