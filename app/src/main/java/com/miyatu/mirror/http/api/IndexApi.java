package com.miyatu.mirror.http.api;

import com.miyatu.mirror.http.service.IndexService;
import com.wzgiceman.rxretrofitlibrary.retrofit_rx.Api.BaseApi;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Retrofit;
import rx.Observable;

public class IndexApi extends BaseApi {
    private Map<String, Object> params;
    private Map<String, RequestBody> bodyMap;
    private List<MultipartBody.Part> parts;

    private File headImage;
    private String headImageUri = "";
    private String token = "";
    private String nickName = "";
    private String gender = "";
    private String height = "";
    private String weight = "";
    private String birthday = "";

    public static final String LOGIN = "index/login";                           //登录
    public static final String BIND_PHONE = "index/bindWX";                     //微信登录绑定手机号
    public static final String SEND_SMS = "index/sendSMS";                      //发送验证码
    public static final String REGISTER = "index/register";                     //注册
    public static final String FORGET_PASSWORD = "index/forgetPwd";             //忘记密码
    public static final String ADD_QUESTION = "index/addQuestion";              //添加问题反馈
    public static final String MESSAGE_LIST = "index/messageList";              //消息列表
    public static final String MESSAGE_INFO = "index/messageInfo";              //消息详情
    public static final String EDIT_INFO = "index/editInfo";                    //修改用户信息
    public static final String GET_PROTOCOL = "index/getProtocol";              //获取协议
    public static final String SEND_TO_MAILBOX = "index/getMeasureData";        //量体结果发送到邮箱

    public static final String RELATIVE_LIST = "index/relativeList";            //获取量体关系列表
    public static final String EDIT_RELATIVE = "index/editRelative";            //修改量体关系账户
    public static final String ADD_RELATIVE = "index/addRelative";              //添加量体关系账户
    public static final String DEL_RELATIVE = "index/delRelative";              //删除量体关系户

    public static final String COURSE_LIST = "index/courseList";                //获取教程列表

    public static final String INDEX = "index/index";                           //量体数据列表
    public static final String ADD_MEASURE = "index/addMeasure";                //添加量体数据
    public static final String MEASURE_INFO = "index/measureInfo";              //量体数据详情
    public static final String DEL_MEASURE = "index/delMeasure";              //删除量体数据

    public static final String WX_PAY = "extra/wxPay";                          //微信支付
    public static final String ALI_PAY = "extra/aliPay";                        //支付宝支付
    public static final String PAY = "extra/Pay";                               //余额支付
    public static final String MY_BALANCE = "index/getBalance";                 //账户余额

    public IndexApi(String mothed) {
        setShowProgress(false);
        setMothed(mothed);
        setCancel(false);
        setCache(false);
        setCookieNetWorkTime(60);
        setCookieNoNetWorkTime(24 * 60 * 60);
    }

    public void setBodyMap(Map<String, RequestBody> bodyMap) {
        this.bodyMap = bodyMap;
    }

    public void setParts(List<MultipartBody.Part> parts) {
        this.parts = parts;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }

    public void setHeadImage(File file) {
        this.headImage = file;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setHeadImageUri(String uri) {
        this.headImageUri = uri;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    @Override
    public Observable getObservable(Retrofit retrofit) {
        IndexService service = retrofit.create(IndexService.class);
        if(getMothed().equals(LOGIN)){
            return service.login(params);
        }
        if(getMothed().equals(BIND_PHONE)){
            return service.bindPhone(params);
        }
        if (getMothed().equals(SEND_SMS)) {
            return service.sendSms(params);
        }
        if (getMothed().equals(REGISTER)) {
            return service.register(params);
        }
        if (getMothed().equals(FORGET_PASSWORD)) {
            return service.forgetPassword(params);
        }
        if (getMothed().equals(ADD_QUESTION)) {
            return service.addQuestion(params);
        }
        if (getMothed().equals(MESSAGE_LIST)) {
            return service.messageList(params);
        }
        if (getMothed().equals(MESSAGE_INFO)) {
            return service.messageInfo(params);
        }
        if (getMothed().equals(EDIT_INFO)) {
            RequestBody strBodyToken = RequestBody.create(MediaType.parse("text/plain"), token);
            RequestBody strBodyName = RequestBody.create(MediaType.parse("text/plain"), nickName);
            RequestBody strBodyGender = RequestBody.create(MediaType.parse("text/plain"), gender);
            RequestBody strBodyHeight = RequestBody.create(MediaType.parse("text/plain"), height);
            RequestBody strBodyWeight = RequestBody.create(MediaType.parse("text/plain"), weight);
            RequestBody strBodyBirthday = RequestBody.create(MediaType.parse("text/plain"), birthday);
            bodyMap = new HashMap<>();
            bodyMap.put("token", strBodyToken);
            bodyMap.put("nickname", strBodyName);
            bodyMap.put("gender", strBodyGender);
            bodyMap.put("height", strBodyHeight);
            bodyMap.put("weight", strBodyWeight);
            bodyMap.put("birthday", strBodyBirthday);
            if (headImage != null) {
                RequestBody fileBody = RequestBody.create(MediaType.parse("multipart/form-data"), headImage);
                MultipartBody.Part part = MultipartBody.Part.createFormData("head_pic", headImage.getName(), fileBody);
                return service.editPortrait(bodyMap, part);
            }
            else {
                RequestBody strBodyImageUri = RequestBody.create(MediaType.parse("text/plain"), headImageUri);
                bodyMap.put("head_pic", strBodyImageUri);
                return service.editPortrait(bodyMap);
            }
        }
        if (getMothed().equals(GET_PROTOCOL)) {
            return service.getProtocol(params);
        }
        if (getMothed().equals(INDEX)) {
            return service.index(params);
        }
        if (getMothed().equals(RELATIVE_LIST)) {
            return service.relativeList(params);
        }
        if (getMothed().equals(SEND_TO_MAILBOX)) {
            return service.sendToMailBox(params);
        }
        if(getMothed().equals(EDIT_RELATIVE)) {
            return service.editRelative(params);
        }
        if(getMothed().equals(ADD_RELATIVE)) {
            return service.addRelative(params);
        }
        if(getMothed().equals(DEL_RELATIVE)) {
            return service.delRelative(params);
        }
        if (getMothed().equals(COURSE_LIST)) {
            return service.courseList(params);
        }
        if (getMothed().equals(ADD_MEASURE)) {
            return service.addMeasure(params);
        }
        if (getMothed().equals(ALI_PAY)) {
            return service.aliPay(params);
        }
        if (getMothed().equals(WX_PAY)) {
            return service.wxPay(params);
        }
        if (getMothed().equals(PAY)) {
            return service.pay(params);
        }
        if (getMothed().equals(DEL_MEASURE)) {
            return service.delMeasure(params);
        }
        if (getMothed().equals(MEASURE_INFO)) {
            return service.measureInfo(params);
        }
        if (getMothed().equals(MY_BALANCE)) {
            return service.myBalance(params);
        }
        return null;
    }
}