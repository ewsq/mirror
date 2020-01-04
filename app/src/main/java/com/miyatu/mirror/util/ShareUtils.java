package com.miyatu.mirror.util;

import android.content.Context;
import android.graphics.Bitmap;

import com.hjq.toast.ToastUtils;
import com.miyatu.mirror.MyApp;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;

/**
 * create by: wangchao
 * 邮箱: 1064717519@qq.com
 */
public class ShareUtils {
    private Context context;

    private static IWXAPI iwxapi;
    /**
     * 分享网页类型至微信
     *
     * @param context 上下文
     * @param webUrl  网页的url
     * @param title   网页标题
     * @param content 网页描述
     * @param bitmap  位图
     */
    public static void shareToWX(Context context, String webUrl, String title, String content, Bitmap bitmap) {
        // 通过appId得到IWXAPI这个对象
        iwxapi = MyApp.api;

        // 检查手机或者模拟器是否安装了微信
        if (!iwxapi.isWXAppInstalled()) {
            ToastUtils.show("您还没有安装微信");
            return;
        }

        // 初始化一个WXWebpageObject对象
        WXWebpageObject webpageObject = new WXWebpageObject();
        // 填写网页的url
        webpageObject.webpageUrl = webUrl;

        // 用WXWebpageObject对象初始化一个WXMediaMessage对象
        WXMediaMessage msg = new WXMediaMessage(webpageObject);
        // 填写网页标题、描述、位图
        msg.title = title;
        msg.description = content;
        // 如果没有位图，可以传null，会显示默认的图片
        msg.setThumbImage(bitmap);

        // 构造一个Req
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        // transaction用于唯一标识一个请求（可自定义）
        req.transaction = "webpage";
        // 上文的WXMediaMessage对象
        req.message = msg;
        // SendMessageToWX.Req.WXSceneSession是分享到好友会话
        // SendMessageToWX.Req.WXSceneTimeline是分享到朋友圈
        req.scene = SendMessageToWX.Req.WXSceneSession;

        // 向微信发送请求
        iwxapi.sendReq(req);
    }

    public static void shareToWXFriendCircle(Context context, String webUrl, String title, String content, Bitmap bitmap) {
        // 通过appId得到IWXAPI这个对象
        iwxapi = MyApp.api;

        // 检查手机或者模拟器是否安装了微信
        if (!iwxapi.isWXAppInstalled()) {
            ToastUtils.show("您还没有安装微信");
            return;
        }

        // 初始化一个WXWebpageObject对象
        WXWebpageObject webpageObject = new WXWebpageObject();
        // 填写网页的url
        webpageObject.webpageUrl = webUrl;

        // 用WXWebpageObject对象初始化一个WXMediaMessage对象
        WXMediaMessage msg = new WXMediaMessage(webpageObject);
        // 填写网页标题、描述、位图
        msg.title = title;
        msg.description = content;
        // 如果没有位图，可以传null，会显示默认的图片
        msg.setThumbImage(bitmap);

        // 构造一个Req
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        // transaction用于唯一标识一个请求（可自定义）
        req.transaction = "webpage";
        // 上文的WXMediaMessage对象
        req.message = msg;
        // SendMessageToWX.Req.WXSceneSession是分享到好友会话
        // SendMessageToWX.Req.WXSceneTimeline是分享到朋友圈
        req.scene = SendMessageToWX.Req.WXSceneTimeline;

        // 向微信发送请求
        iwxapi.sendReq(req);
    }

//    //分享到QQ
//    public void shareToQQ(String title, String url, String imgUrl) {
//        final Bundle params = new Bundle();
//        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
//        params.putString(QQShare.SHARE_TO_QQ_TITLE, title);
//        params.putString(QQShare.SHARE_TO_QQ_SUMMARY, summary);
//        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, url);
//        params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, imgUrl);
//        params.putString(QQShare.SHARE_TO_QQ_APP_NAME, context.getString(R.string.app_name));
//        params.putString(QQShare.SHARE_TO_QQ_EXT_INT, QQShare.SHARE_TO_QQ_EXT_STR);
//        MyApp.mTencent.shareToQQ((Activity) context, params, new BaseUiListener(ThirdUtils.BaseUiListener.TYPE_SHARE_QQ));
//    }

}
