package com.miyatu.mirror.bean;

public class EditInfoBean {

    /**
     * status : 1
     * msg : 修改成功
     * data : {"head_pic":"public/user_thumb/20191224/2435a8fa905ecc5dfa9396643d6d023d.jpg","nickname":"000000","gender":"1","height":"176","weight":"68","birthday":"20200220"}
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
         * head_pic : public/user_thumb/20191224/2435a8fa905ecc5dfa9396643d6d023d.jpg
         * nickname : 000000
         * gender : 1
         * height : 176
         * weight : 68
         * birthday : 20200220
         */

        private String head_pic;
        private String nickname;
        private int gender;
        private String height;
        private String weight;
        private String birthday;

        public String getHead_pic() {
            return head_pic;
        }

        public void setHead_pic(String head_pic) {
            this.head_pic = head_pic;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
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

        public String getBirthday() {
            return birthday;
        }

        public void setBirthday(String birthday) {
            this.birthday = birthday;
        }
    }
}
