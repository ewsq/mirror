package com.miyatu.mirror.bean;

import java.util.List;

public class RecordDetailsBean {

    /**
     * status : 1
     * msg : 查询成功
     * data : {"relative":"高手如云","gender":1,"height":"181.00","weight":"63.00","id":1,"user_id":2,"relative_id":4,"pics":null,"is_pay":1,"measure":[{"value":6,"name":"后腰高","intro":"中腰水平带沿后中线垂直贴量到低腰","unit":"cm","len":1},{"value":7,"name":"脚踝横向宽","intro":"在脚踝部最突出处的水平距离","unit":"cm","len":1}],"create_time":null,"update_time":null,"isdelete":0,"status":1}
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
         * relative : 高手如云
         * gender : 1
         * height : 181.00
         * weight : 63.00
         * id : 1
         * user_id : 2
         * relative_id : 4
         * pics : null
         * is_pay : 1
         * measure : [{"value":6,"name":"后腰高","intro":"中腰水平带沿后中线垂直贴量到低腰","unit":"cm","len":1},{"value":7,"name":"脚踝横向宽","intro":"在脚踝部最突出处的水平距离","unit":"cm","len":1}]
         * create_time : null
         * update_time : null
         * isdelete : 0
         * status : 1
         */

        private String relative;
        private int gender;
        private String height;
        private String weight;
        private int id;
        private int user_id;
        private int relative_id;
        private Object pics;
        private int is_pay;
        private Object create_time;
        private Object update_time;
        private int isdelete;
        private int status;
        private List<MeasureBean> measure;

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

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getUser_id() {
            return user_id;
        }

        public void setUser_id(int user_id) {
            this.user_id = user_id;
        }

        public int getRelative_id() {
            return relative_id;
        }

        public void setRelative_id(int relative_id) {
            this.relative_id = relative_id;
        }

        public Object getPics() {
            return pics;
        }

        public void setPics(Object pics) {
            this.pics = pics;
        }

        public int getIs_pay() {
            return is_pay;
        }

        public void setIs_pay(int is_pay) {
            this.is_pay = is_pay;
        }

        public Object getCreate_time() {
            return create_time;
        }

        public void setCreate_time(Object create_time) {
            this.create_time = create_time;
        }

        public Object getUpdate_time() {
            return update_time;
        }

        public void setUpdate_time(Object update_time) {
            this.update_time = update_time;
        }

        public int getIsdelete() {
            return isdelete;
        }

        public void setIsdelete(int isdelete) {
            this.isdelete = isdelete;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public List<MeasureBean> getMeasure() {
            return measure;
        }

        public void setMeasure(List<MeasureBean> measure) {
            this.measure = measure;
        }

        public static class MeasureBean {
            /**
             * value : 6
             * name : 后腰高
             * intro : 中腰水平带沿后中线垂直贴量到低腰
             * unit : cm
             * len : 1
             */

            private int value;
            private String name;
            private String intro;
            private String unit;
            private int len;

            public int getValue() {
                return value;
            }

            public void setValue(int value) {
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

            public int getLen() {
                return len;
            }

            public void setLen(int len) {
                this.len = len;
            }
        }
    }
}
