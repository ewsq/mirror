package com.miyatu.mirror.bean;

public class UserDatabean {

    /**
     * status : 1
     * msg : 登录成功
     * data : {"id":2,"nickname":"大神","token":"645f446387388c2a99493bcda2e2b445","openid":null,"mobile":"18262616236","sex":0,"email":"1198095563@qq.com","birthday":"2019-12-19","head_pic":"/tmp/uploads/20191218/ddff723a0ba6f5752ef9bf3fa8ccb3ad.png","alipay":"","qq":"","last_login":"2019-12-19 19:21:43","relative":{"relative_id":4,"user_id":2,"relative":"大神","gender":1,"height":"174.00","weight":"68.00"}}
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
         * id : 2
         * nickname : 大神
         * token : 645f446387388c2a99493bcda2e2b445
         * openid : null
         * mobile : 18262616236
         * sex : 0
         * email : 1198095563@qq.com
         * birthday : 2019-12-19
         * head_pic : /tmp/uploads/20191218/ddff723a0ba6f5752ef9bf3fa8ccb3ad.png
         * money:"0.00"
         * alipay :
         * qq :
         * last_login : 2019-12-19 19:21:43
         * relative : {"relative_id":4,"user_id":2,"relative":"大神","gender":1,"height":"174.00","weight":"68.00"}
         */

        private int id;
        private String nickname;
        private String token;
        private Object openid;
        private String mobile;
        private int sex;
        private String email;
        private String birthday;
        private String head_pic;
        private String money;
        private String alipay;
        private String qq;
        private String last_login;
        private RelativeBean relative;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public Object getOpenid() {
            return openid;
        }

        public void setOpenid(Object openid) {
            this.openid = openid;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public int getSex() {
            return sex;
        }

        public void setSex(int sex) {
            this.sex = sex;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getBirthday() {
            return birthday;
        }

        public void setBirthday(String birthday) {
            this.birthday = birthday;
        }

        public String getHead_pic() {
            return head_pic;
        }

        public void setHead_pic(String head_pic) {
            this.head_pic = head_pic;
        }

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }

        public String getAlipay() {
            return alipay;
        }

        public void setAlipay(String alipay) {
            this.alipay = alipay;
        }

        public String getQq() {
            return qq;
        }

        public void setQq(String qq) {
            this.qq = qq;
        }

        public String getLast_login() {
            return last_login;
        }

        public void setLast_login(String last_login) {
            this.last_login = last_login;
        }

        public RelativeBean getRelative() {
            return relative;
        }

        public void setRelative(RelativeBean relative) {
            this.relative = relative;
        }

        public static class RelativeBean {
            /**
             * relative_id : 4
             * user_id : 2
             * relative : 大神
             * gender : 1
             * height : 174.00
             * weight : 68.00
             */

            private int relative_id;
            private int user_id;
            private String relative;
            private int gender;
            private String height;
            private String weight;

            public int getRelative_id() {
                return relative_id;
            }

            public void setRelative_id(int relative_id) {
                this.relative_id = relative_id;
            }

            public int getUser_id() {
                return user_id;
            }

            public void setUser_id(int user_id) {
                this.user_id = user_id;
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
}
