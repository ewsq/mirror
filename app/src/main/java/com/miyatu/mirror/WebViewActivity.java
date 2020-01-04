package com.miyatu.mirror;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;


public class WebViewActivity extends PublicActivity {
    private WebView webView;
    private LinearLayout back;

    String content;

    public static void startActivity(Context context, String content){
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra("content", content);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_webview;
    }

    @Override
    protected void initView() {
        webView = findViewById(R.id.webView);
        back = findViewById(R.id.back);
    }

    @Override
    protected void initData() {
        content = getIntent().getStringExtra("content");

        String[] str = content.split("<img src=\"");
        String imgUrl = str[0];
        for (int i = 1; i < str.length; i ++) {
            str[i] = "http://mojing.meyatu.com" + str[i];
            imgUrl = imgUrl + "<img style=\"max-width:100%;height:auto\" src=\"" + str[i];
        }
        webView.getSettings().setDefaultTextEncodingName("UTF -8");
        webView.getSettings().setTextZoom(85);        //设置文字大小百分比
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        webView.setLayoutParams(layoutParams);
        webView.loadData(imgUrl, "text/html; charset=UTF-8", null);
    }

    @Override
    protected void initEvent() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void updateView() {

    }
}
