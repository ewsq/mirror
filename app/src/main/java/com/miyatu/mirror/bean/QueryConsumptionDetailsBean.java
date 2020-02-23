package com.miyatu.mirror.bean;

import java.util.List;

public class QueryConsumptionDetailsBean {

    /**
     * status : 1
     * msg : 获取成功
     * data : [{"id":68,"user_id":3,"order_sn":"O20200102175014127","third_order_no":"20200102220014524814","item_type":2,"item_id":15,"item_name":"幸福小XU","total_amount":"0.01","discount":100,"discount_amount":"0.00","ex_preferential":"0.00","preferential":"0.00","amount":"0.01","pay_status":1,"pay_type":1,"pay_money":"0.01","pay_time":"2020-01-03 18:19:05","refound_money":"0.00","refound_time":"1970-01-01 08:00:00","role_type":1,"role_id":3,"create_time":"2020-01-02 17:50:14","update_time":"2020-01-03 18:19:05"}]
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

    public static class DataBean {
        /**
         * id : 68
         * user_id : 3
         * order_sn : O20200102175014127
         * third_order_no : 20200102220014524814
         * item_type : 2
         * item_id : 15
         * item_name : 幸福小XU
         * total_amount : 0.01
         * discount : 100
         * discount_amount : 0.00
         * ex_preferential : 0.00
         * preferential : 0.00
         * amount : 0.01
         * pay_status : 1
         * pay_type : 1
         * pay_money : 0.01
         * pay_time : 2020-01-03 18:19:05
         * refound_money : 0.00
         * refound_time : 1970-01-01 08:00:00
         * role_type : 1
         * role_id : 3
         * create_time : 2020-01-02 17:50:14
         * update_time : 2020-01-03 18:19:05
         */

        private int id;
        private int user_id;
        private String order_sn;
        private String third_order_no;
        private int item_type;
        private int item_id;
        private String item_name;
        private String total_amount;
        private int discount;
        private String discount_amount;
        private String ex_preferential;
        private String preferential;
        private String amount;
        private int pay_status;
        private int pay_type;
        private String pay_money;
        private String pay_time;
        private String refound_money;
        private String refound_time;
        private int role_type;
        private int role_id;
        private String create_time;
        private String update_time;

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

        public String getOrder_sn() {
            return order_sn;
        }

        public void setOrder_sn(String order_sn) {
            this.order_sn = order_sn;
        }

        public String getThird_order_no() {
            return third_order_no;
        }

        public void setThird_order_no(String third_order_no) {
            this.third_order_no = third_order_no;
        }

        public int getItem_type() {
            return item_type;
        }

        public void setItem_type(int item_type) {
            this.item_type = item_type;
        }

        public int getItem_id() {
            return item_id;
        }

        public void setItem_id(int item_id) {
            this.item_id = item_id;
        }

        public String getItem_name() {
            return item_name;
        }

        public void setItem_name(String item_name) {
            this.item_name = item_name;
        }

        public String getTotal_amount() {
            return total_amount;
        }

        public void setTotal_amount(String total_amount) {
            this.total_amount = total_amount;
        }

        public int getDiscount() {
            return discount;
        }

        public void setDiscount(int discount) {
            this.discount = discount;
        }

        public String getDiscount_amount() {
            return discount_amount;
        }

        public void setDiscount_amount(String discount_amount) {
            this.discount_amount = discount_amount;
        }

        public String getEx_preferential() {
            return ex_preferential;
        }

        public void setEx_preferential(String ex_preferential) {
            this.ex_preferential = ex_preferential;
        }

        public String getPreferential() {
            return preferential;
        }

        public void setPreferential(String preferential) {
            this.preferential = preferential;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public int getPay_status() {
            return pay_status;
        }

        public void setPay_status(int pay_status) {
            this.pay_status = pay_status;
        }

        public int getPay_type() {
            return pay_type;
        }

        public void setPay_type(int pay_type) {
            this.pay_type = pay_type;
        }

        public String getPay_money() {
            return pay_money;
        }

        public void setPay_money(String pay_money) {
            this.pay_money = pay_money;
        }

        public String getPay_time() {
            return pay_time;
        }

        public void setPay_time(String pay_time) {
            this.pay_time = pay_time;
        }

        public String getRefound_money() {
            return refound_money;
        }

        public void setRefound_money(String refound_money) {
            this.refound_money = refound_money;
        }

        public String getRefound_time() {
            return refound_time;
        }

        public void setRefound_time(String refound_time) {
            this.refound_time = refound_time;
        }

        public int getRole_type() {
            return role_type;
        }

        public void setRole_type(int role_type) {
            this.role_type = role_type;
        }

        public int getRole_id() {
            return role_id;
        }

        public void setRole_id(int role_id) {
            this.role_id = role_id;
        }

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }

        public String getUpdate_time() {
            return update_time;
        }

        public void setUpdate_time(String update_time) {
            this.update_time = update_time;
        }
    }
}
