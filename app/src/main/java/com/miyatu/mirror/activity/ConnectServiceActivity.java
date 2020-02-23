package com.miyatu.mirror.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hjq.toast.ToastUtils;
import com.miyatu.mirror.PublicActivity;
import com.miyatu.mirror.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class ConnectServiceActivity extends PublicActivity {
    private ImageView iv_back;
    private TextView tvPhoneNumber;
    private ImageView ivQrCode;

    private Context context;
    //长按后显示的 Item
    final String[] items = new String[] { "保存图片"};

    public static void startActivity(Context context){
        Intent intent = new Intent(context, ConnectServiceActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_connect_service;
    }

    @Override
    protected void initView() {
        context = ConnectServiceActivity.this;

        iv_back = findViewById(R.id.iv_back);
        tvPhoneNumber = findViewById(R.id.tv_phone_number);
        ivQrCode = findViewById(R.id.iv_qr_code);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initEvent() {
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tvPhoneNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + tvPhoneNumber.getText().toString()));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        ivQrCode.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.qr_code, null);
                //弹出的“保存图片”的Dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        saveImageToGallery(context, bitmap);
                    }
                });
                builder.show();
                return true;
            }
        });
    }

    @Override
    protected void updateView() {

    }

    public static void saveImageToGallery(Context context, Bitmap bmp) {
        // 首先保存图片 创建文件夹
        File appDir = new File(Environment.getExternalStorageDirectory(), "shy");
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        //图片文件名称
        String fileName = "shy_"+System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 其次把文件插入到系统图库
        String path = file.getAbsolutePath();
        try {
            MediaStore.Images.Media.insertImage(context.getContentResolver(), path, fileName, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // 最后通知图库更新
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri uri = Uri.fromFile(file);
        intent.setData(uri);
        context.sendBroadcast(intent);
        ToastUtils.show("保存成功");
    }

}
