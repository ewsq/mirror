package com.miyatu.mirror.bean;

import java.util.List;

public class MyVolumeRecordFragmentBean {


    /**
     * status : 1
     * msg : 查询成功
     * data : [{"relative":"高手如云","gender":1,"height":"181.00","id":11,"user_id":3,"relative_id":6,"pics":"","measure":[{"value":6,"name":"后腰高","intro":"中腰水平带沿后中线垂直贴量到低腰","unit":"cm","len":1},{"value":7,"name":"脚踝横向宽","intro":"在脚踝部最突出处的水平距离","unit":"cm","len":1},{"value":10,"name":"脖颈横向宽","intro":"在脖颈部最突出处的水平距离","unit":"cm","len":1},{"value":10,"name":"膝盖横向宽","intro":"在膝部最突出处的水平距离","unit":"cm","len":1},{"value":11,"name":"脚踝横向厚","intro":"在脚踝部最突出处的前后水平距离","unit":"cm","len":1},{"value":11,"name":"膝盖横向厚","intro":"在膝部最突出处的前后水平距离","unit":"cm","len":1}],"is_pay":1,"create_time":1577688819,"update_time":1577688819,"isdelete":0,"status":1,"price":"0.01"}]
     * extra : {"page":1,"pageSize":20,"total_page":1}
     */

    private int status;
    private String msg;
    private ExtraBean extra;
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

    public ExtraBean getExtra() {
        return extra;
    }

    public void setExtra(ExtraBean extra) {
        this.extra = extra;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class ExtraBean {
        /**
         * page : 1
         * pageSize : 20
         * total_page : 1
         */

        private int page;
        private int pageSize;
        private int total_page;

        public int getPage() {
            return page;
        }

        public void setPage(int page) {
            this.page = page;
        }

        public int getPageSize() {
            return pageSize;
        }

        public void setPageSize(int pageSize) {
            this.pageSize = pageSize;
        }

        public int getTotal_page() {
            return total_page;
        }

        public void setTotal_page(int total_page) {
            this.total_page = total_page;
        }
    }

    public static class DataBean {
        /**
         * relative : 高手如云
         * gender : 1
         * height : 181.00
         * id : 11
         * user_id : 3
         * relative_id : 6
         * pics :
         * measure : [{"value":6,"name":"后腰高","intro":"中腰水平带沿后中线垂直贴量到低腰","unit":"cm","len":1},{"value":7,"name":"脚踝横向宽","intro":"在脚踝部最突出处的水平距离","unit":"cm","len":1},{"value":10,"name":"脖颈横向宽","intro":"在脖颈部最突出处的水平距离","unit":"cm","len":1},{"value":10,"name":"膝盖横向宽","intro":"在膝部最突出处的水平距离","unit":"cm","len":1},{"value":11,"name":"脚踝横向厚","intro":"在脚踝部最突出处的前后水平距离","unit":"cm","len":1},{"value":11,"name":"膝盖横向厚","intro":"在膝部最突出处的前后水平距离","unit":"cm","len":1}]
         * is_pay : 1
         * create_time : 1577688819
         * update_time : 1577688819
         * isdelete : 0
         * status : 1
         * price : 0.01
         */

        private String relative;
        private int gender;
        private String height;
        private int id;
        private int user_id;
        private int relative_id;
        private String pics;
        private int is_pay;
        private int create_time;
        private int update_time;
        private int isdelete;
        private int status;
        private String price;
        private boolean isSelect = false;
        private List<MeasureBean> measure;

        public boolean isSelect() {
            return isSelect;
        }

        public void setSelect(boolean select) {
            isSelect = select;
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

        public String getPics() {
            return pics;
        }

        public void setPics(String pics) {
            this.pics = pics;
        }

        public int getIs_pay() {
            return is_pay;
        }

        public void setIs_pay(int is_pay) {
            this.is_pay = is_pay;
        }

        public int getCreate_time() {
            return create_time;
        }

        public void setCreate_time(int create_time) {
            this.create_time = create_time;
        }

        public int getUpdate_time() {
            return update_time;
        }

        public void setUpdate_time(int update_time) {
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

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
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
