package com.ouzhongiot.ozapp.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ouzhongiot.ozapp.OZApplication;
import com.ouzhongiot.ozapp.R;
import com.ouzhongiot.ozapp.base.BaseHomeActivity;
import com.ouzhongiot.ozapp.constant.SpConstant;
import com.ouzhongiot.ozapp.constant.UrlConstant;
import com.ouzhongiot.ozapp.http.PostParamTools;
import com.ouzhongiot.ozapp.tools.FileTool;
import com.ouzhongiot.ozapp.tools.LogTools;
import com.ouzhongiot.ozapp.tools.SpData;
import com.ouzhongiot.ozapp.tools.ToastTools;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author hxf
 * @date 创建时间: 2017/5/22
 * @Description 意见反馈
 */

public class FeedBackActivity extends BaseHomeActivity implements View.OnClickListener {
    private LinearLayout llayout_back;
    private TextView tv_back_behind;//返回键后面
    private TextView tv_title;//标题
    private EditText edt_content;
    private TextView tv_word_count;//输入的字体个数
    private ImageView[] img_pics = new ImageView[4];
    private Button btn_commit;//提交

    private static final String IMAGE_UNSPECIFIED = "image/*";
    private static final int PHOTO_ZOOM = 2; // 缩放
    private static final int NONE = 0;
    private static final int PHOTO_RESOULT = 3;// 结果

    private int position = 0;
    private Bitmap photos[];
    private File[] allFiles;


    @Override
    public int addContentView() {
        return R.layout.activity_feedback;
    }

    @Override
    public void initView() {
        llayout_back = (LinearLayout) findViewById(R.id.llayout_back);
        tv_back_behind = (TextView) findViewById(R.id.txt_back_behind);
        tv_title = (TextView) findViewById(R.id.txt_head_content);
        edt_content = (EditText) findViewById(R.id.edt_feedback_content);
        tv_word_count = (TextView) findViewById(R.id.tv_feedback_word_count);
        img_pics[0] = (ImageView) findViewById(R.id.img_feedback_pic1);
        img_pics[1] = (ImageView) findViewById(R.id.img_feedback_pic2);
        img_pics[2] = (ImageView) findViewById(R.id.img_feedback_pic3);
        img_pics[3] = (ImageView) findViewById(R.id.img_feedback_pic4);
        btn_commit = (Button) findViewById(R.id.btn_feedback_commit);


    }

    @Override
    public void initValue() {
        OZApplication.getInstance().addActivity(this);
        tv_back_behind.setText(getString(R.string.txt_about_product));
        tv_title.setText(getString(R.string.txt_feedback));

        edt_content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (LogTools.debug) {
                    LogTools.e("输入的文字个数->" + s.toString().length());
                }
                tv_word_count.setText(String.valueOf(s.toString().length()));

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        photos = new Bitmap[4];
        allFiles = new File[4];
        setClick();


    }

    public void setClick() {
        llayout_back.setOnClickListener(this);
        img_pics[0].setOnClickListener(this);
        img_pics[1].setOnClickListener(this);
        img_pics[2].setOnClickListener(this);
        img_pics[3].setOnClickListener(this);
        btn_commit.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.llayout_back:
                //返回
                OZApplication.getInstance().finishActivity();
                break;
            case R.id.img_feedback_pic1:
                position = 0;
                showAlbum();
                break;
            case R.id.img_feedback_pic2:
                position = 1;
                showAlbum();
                break;
            case R.id.img_feedback_pic3:
                position = 2;
                showAlbum();
                break;
            case R.id.img_feedback_pic4:
                position = 3;
                showAlbum();
                break;
            case R.id.btn_feedback_commit:
                //提交
                verify();
                break;
        }
    }

    public void showAlbum() {
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                IMAGE_UNSPECIFIED);
        startActivityForResult(intent, PHOTO_ZOOM);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == NONE)
            return;
        // 读取相册缩放图片
        if (requestCode == PHOTO_ZOOM) {
            startPhotoZoom(data.getData());
        }
        // 处理结果
        if (requestCode == PHOTO_RESOULT) {
            Bundle extras = data.getExtras();
            if (extras != null) {
                photos[position] = extras.getParcelable("data");
                img_pics[position].setImageBitmap(photos[position]);
                img_pics[position + 1].setVisibility(View.VISIBLE);

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                photos[position].compress(Bitmap.CompressFormat.JPEG, 100, stream);// (0-100)压缩文件
                //将bitmap转化为文件
                allFiles[position] = FileTool.feedbackPicToFile(FeedBackActivity.this, photos[position]);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");// 调用Android系统自带的一个图片剪裁页面,
        intent.setDataAndType(uri, IMAGE_UNSPECIFIED);
        intent.putExtra("crop", "true");// 进行修剪
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, PHOTO_RESOULT);
    }

    public void verify() {
        if (edt_content.getText().toString().trim().isEmpty()) {
            ToastTools.show(this, "请输入内容");
        } else {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("feedback.userSn", SpData.getInstance(FeedBackActivity.this).getData(SpConstant.LOGIN_USERSN).toString());
                    params.put("feedback.content", edt_content.getText().toString().trim());

                    Map<String, File[]> files = new HashMap<String, File[]>();
                    //判断用户是否上传了图片
                    if (allFiles[0] != null) {
                        files.put("files", allFiles);
                    }
                    //请求接口
                    try {
                        String result = PostParamTools.postFiles(UrlConstant.ADD_FEEDBACK, params, files);
                        if (LogTools.debug) {
                            LogTools.d("添加反馈返回数据->" + result);
                        }

                        JSONObject object = null;
                        try {
                            object = new JSONObject(result);
                            int state = object.getInt("state");
                            if (state == 0) {
                                //成功
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        ToastTools.show(FeedBackActivity.this, "提交成功");
                                        OZApplication.getInstance().finishActivity();
                                    }
                                });
                            } else if (state == 1) {
                                //参数异常
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        ToastTools.show(FeedBackActivity.this, "参数异常");
                                    }
                                });
                            } else if (state == 2) {
                                //失败
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        ToastTools.show(FeedBackActivity.this, getString(R.string.fail_txt));
                                    }
                                });

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }

            ).start();

        }

    }

}
