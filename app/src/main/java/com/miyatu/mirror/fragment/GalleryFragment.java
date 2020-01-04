package com.miyatu.mirror.fragment;


import android.os.Build;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.miyatu.mirror.MyApp;
import com.miyatu.mirror.PublicFragment;
import com.miyatu.mirror.activity.CourseListBean;

import java.lang.reflect.Method;


/**
 * 帮拍教程的fragment
 */
public class GalleryFragment extends PublicFragment {

//    private VideoView videoView = null;             //Videoview(帮拍教程)
    private ImageView cover = null;                //预览图片
    private TextView tvTitle;
    private TextView tvSimple;
    private WebView webView;
    private ImageView iconVideo;

    private CourseListBean.DataBean dataBean;

    public GalleryFragment(CourseListBean.DataBean dataBean){
        this.dataBean = dataBean;
    }

    @Override
    protected int getLayout() {
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
        return R.layout.galleryfragment;
    }

    @Override
    protected void initView(View view) {
//        videoView = view.findViewById(R.id.videoview_helptakephoto);
        webView = view.findViewById(R.id.webView);
        cover = view.findViewById(R.id.iv_videoview_preview);
        tvTitle = view.findViewById(R.id.tv_title);
        tvSimple = view.findViewById(R.id.tv_simple);
        iconVideo = view.findViewById(R.id.iconVideo);

        initWebView();
    }

    @Override
    protected void initData() {
        tvTitle.setText(dataBean.getTitle());
        tvSimple.setText(dataBean.getSimple());
        Glide.with(this).load(MyApp.imageUrl + dataBean.getCover()).into(cover);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
    }

    @Override
    protected void initEvent() {
//        videoView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                cover.setVisibility(View.GONE);
//
//                //网络视频
//                String videoUrl = MyApp.imageUrl + dataBean.getVideo();
//                Uri uri = Uri.parse( videoUrl );
//                //设置视频控制器
//                videoView.setMediaController(new MediaController(getActivity()));
//                //设置视频路径
//                videoView.setVideoURI(uri);
//                //开始播放视频
//                videoView.start();
//            }
//        });
        cover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cover.setVisibility(View.GONE);
                iconVideo.setVisibility(View.GONE);

                webView.loadUrl(MyApp.imageUrl + dataBean.getVideo());
            }
        });
    }

    @Override
    protected void updateView() {

    }

    private void initWebView() {
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        try {
            if (Build.VERSION.SDK_INT >= 16) {
                Class<?> clazz = webView.getSettings().getClass();
                Method method = clazz.getMethod("setAllowUniversalAccessFromFileURLs", boolean.class);
                if (method != null) {
                    method.invoke(webView.getSettings(), true);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        webView.getSettings().setPluginState(WebSettings.PluginState.ON);
        webView.getSettings().setDomStorageEnabled(true);// 必须保留，否则无法播放优酷视频，其他的OK

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webView.getSettings().setMixedContentMode(webView.getSettings().MIXED_CONTENT_ALWAYS_ALLOW);
        }
    }

}
