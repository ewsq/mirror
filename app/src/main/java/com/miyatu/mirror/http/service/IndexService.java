package com.miyatu.mirror.http.service;

import com.miyatu.mirror.http.api.IndexApi;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import rx.Observable;

public interface IndexService {
    //登录
    @FormUrlEncoded
    @POST(IndexApi.LOGIN)
    Observable<String> login(@FieldMap Map<String,Object> params);

    //微信登录绑定手机号
    @FormUrlEncoded
    @POST(IndexApi.BIND_PHONE)
    Observable<String> bindPhone(@FieldMap Map<String,Object> params);

    //发送验证码
    @FormUrlEncoded
    @POST(IndexApi.SEND_SMS)
    Observable<String> sendSms(@FieldMap Map<String,Object> params);

    //注册
    @FormUrlEncoded
    @POST(IndexApi.REGISTER)
    Observable<String> register(@FieldMap Map<String,Object> params);

    //注册
    @FormUrlEncoded
    @POST(IndexApi.FORGET_PASSWORD)
    Observable<String> forgetPassword(@FieldMap Map<String,Object> params);

    //添加问题反馈
    @FormUrlEncoded
    @POST(IndexApi.ADD_QUESTION)
    Observable<String> addQuestion(@FieldMap Map<String,Object> params);

    //消息列表
    @FormUrlEncoded
    @POST(IndexApi.MESSAGE_LIST)
    Observable<String> messageList(@FieldMap Map<String,Object> params);

    //消息详情
    @FormUrlEncoded
    @POST(IndexApi.MESSAGE_INFO)
    Observable<String> messageInfo(@FieldMap Map<String,Object> params);

    //修改用户信息（包括头像)
    @Multipart
    @POST(IndexApi.EDIT_INFO)
    Observable<String> editPortrait(@PartMap Map<String, RequestBody> bodyMap, @Part MultipartBody.Part part);

    //修改用户信息(不包括头像）
    @Multipart
    @POST(IndexApi.EDIT_INFO)
    Observable<String> editPortrait(@PartMap Map<String, RequestBody> bodyMap);

    //获取协议
    @FormUrlEncoded
    @POST(IndexApi.GET_PROTOCOL)
    Observable<String> getProtocol(@FieldMap Map<String,Object> params);

    //量体数据列表
    @FormUrlEncoded
    @POST(IndexApi.INDEX)
    Observable<String> index(@FieldMap Map<String,Object> params);

    //量体结果发送到邮箱
    @FormUrlEncoded
    @POST(IndexApi.SEND_TO_MAILBOX)
    Observable<String> sendToMailBox(@FieldMap Map<String,Object> params);

    //量体数据详情
    @FormUrlEncoded
    @POST(IndexApi.MEASURE_INFO)
    Observable<String> measureInfo(@FieldMap Map<String,Object> params);

    //删除量体数据
    @FormUrlEncoded
    @POST(IndexApi.DEL_MEASURE)
    Observable<String> delMeasure(@FieldMap Map<String,Object> params);

    //获取量体关系列表
    @FormUrlEncoded
    @POST(IndexApi.RELATIVE_LIST)
    Observable<String> relativeList(@FieldMap Map<String,Object> params);

    //修改量体关系账户
    @FormUrlEncoded
    @POST(IndexApi.EDIT_RELATIVE)
    Observable<String> editRelative(@FieldMap Map<String,Object> params);

    //添加量体关系账户
    @FormUrlEncoded
    @POST(IndexApi.ADD_RELATIVE)
    Observable<String> addRelative(@FieldMap Map<String,Object> params);

    //删除量体关系账户
    @FormUrlEncoded
    @POST(IndexApi.DEL_RELATIVE)
    Observable<String> delRelative(@FieldMap Map<String,Object> params);

    //获取教程列表
    @FormUrlEncoded
    @POST(IndexApi.COURSE_LIST)
    Observable<String> courseList(@FieldMap Map<String,Object> params);

    //添加量体数据
    @FormUrlEncoded
    @POST(IndexApi.ADD_MEASURE)
    Observable<String> addMeasure(@FieldMap Map<String,Object> params);

    //支付宝支付
    @FormUrlEncoded
    @POST(IndexApi.ALI_PAY)
    Observable<String> aliPay(@FieldMap Map<String,Object> params);

    //微信支付
    @FormUrlEncoded
    @POST(IndexApi.WX_PAY)
    Observable<String> wxPay(@FieldMap Map<String,Object> params);

    //余额支付
    @FormUrlEncoded
    @POST(IndexApi.PAY)
    Observable<String> pay(@FieldMap Map<String,Object> params);

    //账户余额
    @FormUrlEncoded
    @POST(IndexApi.MY_BALANCE)
    Observable<String> myBalance(@FieldMap Map<String,Object> params);

    //查询消费明细
    @FormUrlEncoded
    @POST(IndexApi.QUERY_CONSUMPTION_DETAILS)
    Observable<String> queryConsumptionDetails(@FieldMap Map<String,Object> params);
}
