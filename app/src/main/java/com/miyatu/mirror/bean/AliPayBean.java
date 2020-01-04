package com.miyatu.mirror.bean;

public class AliPayBean {

    /**
     * status : 1
     * msg : 签名成功
     * result : alipay_sdk=alipay-sdk-php-20161101&app_id=2019102868717463&biz_content=%7B%22out_trade_no%22%3A%22O20191225163955458%22%2C%22total_amount%22%3A%220.01%22%2C%22product_code%22%3A%22QUICK_MSECURITY_PAY%22%2C%22subject%22%3A%22%5Cu5347%5Cu7ea7%5Cu4f1a%5Cu5458%22%7D&charset=utf-8&format=json&method=alipay.trade.app.pay¬ify_url=http%3A%2F%2Fmojing.meyatu.com%2Findex.php%2Findex%2FExtra%2FnewalipayNotify&sign_type=RSA2×tamp=2019-12-25+16%3A39%3A55&version=1.0&sign=Xndo9dyxKwvTrxGXJXcBI1Jv8MMCXIh65CbjYQEt2eqjR2tqxktD8KZonSGGgOHdhoDHRjCWNGtGkENyDwfQNUB%2FXdA9wjhB2JmG4ftTeAe1gurW23%2FC5WH1V4uHKoz%2FKHO6YFIEJEASvndTfISXTlDnnNK1z0A5Nw%2BzudKhEnZnqi2p9%2FfucVgp6Dx0YEPcSDweeThtXxeHQiLEbly1fXK5h5KSqth0ynZgXf%2FmIzOzWPJTe325oNU3wnbFcQi2mQVqk4vAQW9B6V8V8hNiA6WtMasZ1uenGsEDCsli%2FjiUYF%2Bm%2BOs8BzSjLte5VZ%2BjZKWOTF8QdSsacLCJ9puGcw%3D%3D
     */

    private int status;
    private String msg;
    private String result;

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

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
