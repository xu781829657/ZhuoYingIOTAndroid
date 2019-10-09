package com.ouzhongiot.ozapp.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.ouzhongiot.ozapp.OZApplication;
import com.ouzhongiot.ozapp.R;
import com.ouzhongiot.ozapp.base.BaseHomeActivity;
import com.ouzhongiot.ozapp.constant.SpConstant;
import com.ouzhongiot.ozapp.constant.UrlConstant;
import com.ouzhongiot.ozapp.http.ConnectDataTask;
import com.ouzhongiot.ozapp.http.HcNetWorkTask;
import com.ouzhongiot.ozapp.http.PostParamTools;
import com.ouzhongiot.ozapp.others.CircleImageView;
import com.ouzhongiot.ozapp.tools.FileTool;
import com.ouzhongiot.ozapp.tools.IconfontTools;
import com.ouzhongiot.ozapp.tools.LogTools;
import com.ouzhongiot.ozapp.tools.SpData;
import com.ouzhongiot.ozapp.tools.SystemTools;
import com.ouzhongiot.ozapp.tools.ToastTools;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import cn.qqtheme.framework.picker.DatePicker;
import cn.qqtheme.framework.util.ConvertUtils;

/**
 * @author hxf
 * @date 创建时间: 2017/4/13
 * @Description 用户信息
 */
public class UserInfoActivity extends BaseHomeActivity implements View.OnClickListener, ConnectDataTask.OnResultDataLintener {
    private LinearLayout llayout_back;
    private TextView icon_back;//返回
    private TextView tv_back_behind;//返回键后面
    private TextView tv_title;//标题
    private LinearLayout llayout_avatar;//头像
    private CircleImageView img_avatar;
    private LinearLayout llayout_nickname;//昵称
    private TextView tv_nickname;//昵称
    private LinearLayout llayout_sex;//性别
    private TextView tv_sex;//性别
    private LinearLayout llayout_birthday;//生日
    private TextView tv_birthday;//性别

    private LinearLayout llayout_address;//我的地址
    private LinearLayout llayout_id;//我的ID
    private TextView tv_id;//ID

    private LinearLayout llayout_modify_pwd;//修改密码
    private LinearLayout llayout_email;//我的邮箱
    private TextView tv_email;//邮箱

    private Button btn_exit;//退出登录

    private Socket socket;

    private static final String IMAGE_UNSPECIFIED = "image/*";
    private static final int PHOTO_ZOOM = 2; // 缩放
    private static final int PHOTO_GRAPH = 1;// 拍照
    private static final int NONE = 0;
    private static final int PHOTO_RESOULT = 3;// 结果
    private Bitmap photo;
    private File tempdate;// 上传图片

    private Boolean isModifyAvatar = false;//是否修改了头像
    private Boolean isModifyName = false;//是否修改了昵称
    private Boolean isModifySex = false;//是否修改了性别

    private final int SET_NAME_REQUEST_CODE = 1002;
    private final int SET_EMAIL_REQUEST_CODE = 1003;


    private int sexTag = 0;//性别标识（男1 女2）
    private String select_date;//用户选择的生日
    private String user_date;//用户自己的生日
    private boolean isHasAddress = false;//标识是否有地址

    private JSONObject addressObject;


    @Override
    public int addContentView() {
        return R.layout.activity_user_info;
    }

    @Override
    public void initView() {
        llayout_back = (LinearLayout) findViewById(R.id.llayout_back);
        icon_back = (TextView) findViewById(R.id.font_head_back);
        tv_back_behind = (TextView) findViewById(R.id.txt_back_behind);
        tv_title = (TextView) findViewById(R.id.txt_head_content);
        llayout_avatar = (LinearLayout) findViewById(R.id.llayout_userinfo_avatar);
        img_avatar = (CircleImageView) findViewById(R.id.img_userinfo_avatar);
        llayout_nickname = (LinearLayout) findViewById(R.id.llayout_userinfo_nickname);
        tv_nickname = (TextView) findViewById(R.id.tv_userinfo_nickname);
        llayout_sex = (LinearLayout) findViewById(R.id.llayout_userinfo_sex);
        tv_sex = (TextView) findViewById(R.id.tv_userinfo_sex);
        llayout_birthday = (LinearLayout) findViewById(R.id.llayout_userinfo_birthday);
        tv_birthday = (TextView) findViewById(R.id.tv_userinfo_birthday);

        llayout_address = (LinearLayout) findViewById(R.id.llayout_userinfo_address);
        llayout_id = (LinearLayout) findViewById(R.id.llayout_userinfo_id);
        tv_id = (TextView) findViewById(R.id.tv_userinfo_id);

        llayout_modify_pwd = (LinearLayout) findViewById(R.id.llayout_userinfo_modify_pwd);
        llayout_email = (LinearLayout) findViewById(R.id.llayout_userinfo_email);
        tv_email = (TextView) findViewById(R.id.tv_userinfo_email);

        btn_exit = (Button) findViewById(R.id.btn_userinfo_exit);

    }

    @Override
    public void initValue() {
        OZApplication.getInstance().addActivity(this);
        icon_back.setTypeface(IconfontTools.getTypeface(this));
        tv_back_behind.setText(getString(R.string.txt_personal));
        tv_title.setText(getString(R.string.txt_personal_userinfo));
        socket = ((OZApplication) getApplication()).socket;
        //显示头像
        if (!SpData.getInstance(this).getData(SpConstant.LOGIN_HEADURL).toString().equals("null")) {
            ImageLoader.getInstance().displayImage(SpData.getInstance(this).getData(SpConstant.LOGIN_HEADURL).toString(), img_avatar);
        }
        //显示昵称
        if (!SpData.getInstance(this).getData(SpConstant.LOGIN_NICKNAME).toString().equals("null")) {
            tv_nickname.setText(SpData.getInstance(this).getData(SpConstant.LOGIN_NICKNAME).toString());
        }
        //性别
        if (SpData.getInstance(this).getData(SpConstant.LOGIN_SEX).toString().equals("1")) {
            //男
            tv_sex.setText("男");
        } else if (SpData.getInstance(this).getData(SpConstant.LOGIN_SEX).toString().equals("2")) {
            //女
            tv_sex.setText("女");
        }
        //生日
        if (!SpData.getInstance(this).getData(SpConstant.LOGIN_BIRTHDATE).toString().equals("null")) {
            user_date = SpData.getInstance(this).getData(SpConstant.LOGIN_BIRTHDATE).toString();
            tv_birthday.setText(user_date);
        }
        tv_id.setText(SpData.getInstance(this).getData(SpConstant.LOGIN_USERSN).toString());
        //邮箱
        if (!SpData.getInstance(this).getData(SpConstant.LOGIN_EMAIL).toString().equals("null")) {
            tv_email.setText(SpData.getInstance(this).getData(SpConstant.LOGIN_EMAIL).toString());
        }

        //设置点击事件
        setClick();

    }

    public void setClick() {
        llayout_back.setOnClickListener(this);
        llayout_avatar.setOnClickListener(this);
        llayout_nickname.setOnClickListener(this);
        llayout_sex.setOnClickListener(this);
        llayout_birthday.setOnClickListener(this);
        llayout_address.setOnClickListener(this);
        llayout_id.setOnClickListener(this);
        llayout_modify_pwd.setOnClickListener(this);
        llayout_email.setOnClickListener(this);
        btn_exit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.llayout_back:
                //返回
                onBackPressed();
                break;
            case R.id.llayout_userinfo_avatar:
                //头像
                setAvatarDiaglog();
                break;
            case R.id.llayout_userinfo_nickname:
                //昵称
                Intent intent = new Intent(this, ModifyNicknameActivity.class);
                intent.putExtra(ModifyNicknameActivity.PARAM_TAG, "nickname");
                startActivityForResult(intent, SET_NAME_REQUEST_CODE);
                break;
            case R.id.llayout_userinfo_sex:
                //性别
                setSexDiaglog();
                break;
            case R.id.llayout_userinfo_birthday:
                //生日
                onYearMonthDayPicker();
                break;
            case R.id.llayout_userinfo_address:
                //我的地址
                startActivity(new Intent(this, MineAddressActivity.class));
                break;
            case R.id.llayout_userinfo_id:
                //我的ID
                break;
            case R.id.llayout_userinfo_modify_pwd:
                //修改密码
                startActivity(new Intent(this, ResetPwdActivity.class));
                break;
            case R.id.llayout_userinfo_email:
                //我的邮箱
                Intent emailIntent = new Intent(this, ModifyNicknameActivity.class);
                emailIntent.putExtra(ModifyNicknameActivity.PARAM_TAG, "email");
                startActivityForResult(emailIntent, SET_EMAIL_REQUEST_CODE);
                break;
            case R.id.btn_userinfo_exit:
                //退出登录
                logout();
                break;
        }

    }

    // 显示设置性别的对话框
    public void setSexDiaglog() {
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        RelativeLayout layout = (RelativeLayout) layoutInflater.inflate(
                R.layout.dialog_set_sex, null);
        final Dialog dialog = new AlertDialog.Builder(this).create();
        dialog.show();
        dialog.getWindow().setContentView(layout);
        RadioButton rbtn_nan = (RadioButton) layout
                .findViewById(R.id.rbtn_sex_nan);
        RadioButton rbtn_nv = (RadioButton) layout
                .findViewById(R.id.rbtn_sex_nv);
        if (SpData.getInstance(UserInfoActivity.this).getData(SpConstant.LOGIN_SEX).toString().equals("1")) {
            //男
            rbtn_nan.setChecked(true);
            rbtn_nv.setChecked(false);

        } else if (SpData.getInstance(UserInfoActivity.this).getData(SpConstant.LOGIN_SEX).toString().equals("2")) {
            //女
            rbtn_nv.setChecked(true);
            rbtn_nan.setChecked(false);
        }
        // 男
        rbtn_nan.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                sexTag = 1;
                new HcNetWorkTask(UserInfoActivity.this, UserInfoActivity.this, 1).doPost(UrlConstant.MODIFY_USERINFO, null, postParams(1).getBytes());
                dialog.dismiss();
            }
        });

        // 女
        rbtn_nv.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                sexTag = 2;
                new HcNetWorkTask(UserInfoActivity.this, UserInfoActivity.this, 1).doPost(UrlConstant.MODIFY_USERINFO, null, postParams(1).getBytes());
                dialog.dismiss();
            }
        });

    }

    /**
     * post参数
     *
     * @param code
     * @return
     */
    private String postParams(int code) {
        Map<String, String> params = new HashMap<String, String>();
        if (code == 1) {
            // 修改性别
            params.put("user.sn", SpData.getInstance(this).getData(SpConstant.LOGIN_USERSN).toString());
            params.put("user.sex", String.valueOf(sexTag));
            if (LogTools.debug) {
                LogTools.i("修改性别参数->" + params.toString());
            }
            return PostParamTools.wrapParams(params);
        } else if (code == 3) {
            //设置生日
            params.put("user.sn", SpData.getInstance(this).getData(SpConstant.LOGIN_USERSN).toString());
            params.put("user.birthdate", select_date);
            if (LogTools.debug) {
                LogTools.i("生日参数->" + params.toString());
            }
            return PostParamTools.wrapParams(params);

        }
        return "";
    }

    //退出登录
    public void logout() {
        ((OZApplication) getApplication()).setLogin(false);
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        socket = null;
        ((OZApplication) getApplication()).setSocket(socket);
        ((OZApplication) getApplication()).setTCPconnect(false);

        SpData.getInstance(this).putData(SpConstant.LOGIN_PWD, "");
        SpData.getInstance(this).putData(SpConstant.LOGIN_USERNAME, "");
//        SpData.getInstance(this).putData(SpConstant.LAST_TIME_OPEN_TIME, "");

        //跳转到登录页
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        OZApplication.getInstance().finishAllActivity();

    }


    // 显示更改头像对话框
        public void setAvatarDiaglog() {
            LayoutInflater layoutInflater = LayoutInflater.from(this);
            RelativeLayout layout = (RelativeLayout) layoutInflater.inflate(
                    R.layout.dialog_edit_avatar, null);
            final Dialog dialog = new AlertDialog.Builder(this).create();
            dialog.show();
            dialog.getWindow().setContentView(layout);
            TextView fromAblum = (TextView) layout
                    .findViewById(R.id.avatar_from_album);
            // 相册
            fromAblum.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    Intent intent = new Intent(Intent.ACTION_PICK, null);
                    intent.setDataAndType(
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                            IMAGE_UNSPECIFIED);
                    startActivityForResult(intent, PHOTO_ZOOM);
                    dialog.dismiss();
                }
            });
            TextView camera_tv = (TextView) layout.findViewById(R.id.avatar_from_camera);
            // 相机
            camera_tv.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    String filePath = FileTool.getPath2(UserInfoActivity.this,
                            "OzAppOld");
                    //调试得到的值：/sdcard/com.ozapp.international/OzApp
                    String filename = filePath + "/avatar.jpg";
                    intent.putExtra(MediaStore.EXTRA_OUTPUT,
                            Uri.fromFile(new File(filename)));
                    startActivityForResult(intent, PHOTO_GRAPH);
                    dialog.dismiss();
                }
            });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == NONE)
            return;
        // 拍照
        if (requestCode == PHOTO_GRAPH) {
            // 设置文件保存路径
            String filePath = FileTool.getPath2(UserInfoActivity.this, "OzAppOld");
            String filename = filePath + "/avatar.jpg";
            startPhotoZoom(Uri.fromFile(new File(filename)));
        }

        // 读取相册缩放图片
        if (requestCode == PHOTO_ZOOM) {
            startPhotoZoom(data.getData());
        }
        // 处理结果
        if (requestCode == PHOTO_RESOULT) {
            Bundle extras = data.getExtras();
            if (extras != null) {
                photo = extras.getParcelable("data");
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                photo.compress(Bitmap.CompressFormat.JPEG, 100, stream);// (0-100)压缩文件
                //把当前的时间戳保存下来
                SpData.getInstance(UserInfoActivity.this).putData(SpConstant.HEAD_FILE_NAME_TIME, String.valueOf(System.currentTimeMillis()));
                tempdate = FileTool.picToFile(UserInfoActivity.this, photo);// 将bitmap转换为文件
                // 上传头像
                final Map<String, String> params = new HashMap<String, String>();
                params.put("userSn", SpData.getInstance(UserInfoActivity.this)
                        .getData(SpConstant.LOGIN_USERSN).toString());
                final Map<String, File> files = new HashMap<String, File>();
                files.put("files", tempdate);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String result = PostParamTools.post(UrlConstant.UPLOAD_AVATAR, params, files);
                            if (LogTools.debug) {
                                LogTools.d("上传头像返回数据->" + result);
                            }
                            try {
                                JSONObject object = new JSONObject(result);
                                int state = object.getInt("state");
                                if (state == 0) {
                                    //成功
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            img_avatar.setImageBitmap(photo);
                                        }
                                    });
//                                    http://112.124.48.212/image/headphoto/100000003/1493975371594.png
                                    SpData.getInstance(UserInfoActivity.this).putData(SpConstant.LOGIN_HEADURL, object.getString("data"));
                                    isModifyAvatar = true;

                                } else if (state == 1) {
                                    //失败
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            ToastTools.show(UserInfoActivity.this, getString(R.string.fail_txt));
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
                }).start();


            }
        }
        if (requestCode == SET_NAME_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                //设置昵称成功
                tv_nickname.setText(SpData.getInstance(UserInfoActivity.this).getData(SpConstant.LOGIN_NICKNAME).toString());
                isModifyName = true;
            }
        }
        if (requestCode == SET_EMAIL_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                //绑定邮箱成功
                tv_email.setText(SpData.getInstance(UserInfoActivity.this).getData(SpConstant.LOGIN_EMAIL).toString());
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


    @Override
    public void onBackPressed() {
        if (isModifyAvatar || isModifyName || isModifySex) {
            Intent data = new Intent();
            data.putExtra("avatar", isModifyAvatar);
            data.putExtra("name", isModifyName);
            data.putExtra("sex", isModifySex);
            setResult(RESULT_OK, data);
        }
        OZApplication.getInstance().finishActivity();
    }

    @Override
    public void onResult(String result, int code) {
        if (!result.isEmpty()) {
            try {
                JSONObject object = new JSONObject(result);
                int state = object.getInt("state");
                if (code == 1) {
                    //修改性别
                    if (state == 0) {
                        //修改成功
                        isModifySex = true;
                        if (sexTag == 1) {
                            tv_sex.setText("男");
                            SpData.getInstance(this).putData(SpConstant.LOGIN_SEX, 1);
                        } else if (sexTag == 2) {
                            tv_sex.setText("女");
                            SpData.getInstance(this).putData(SpConstant.LOGIN_SEX, 2);
                        }

                    } else if (state == 1) {
                        ToastTools.show(this, "参数异常");

                    } else if (state == 2) {
                        ToastTools.show(this, "修改失败");

                    }

                } else if (code == 3) {
                    //设置生日
                    if (state == 0) {
                        tv_birthday.setText(select_date);
                        SpData.getInstance(this).putData(SpConstant.LOGIN_BIRTHDATE, select_date);

                    } else if (state == 1) {
                        ToastTools.show(this, "参数异常");

                    } else if (state == 2) {
                        ToastTools.show(this, "修改失败");

                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }

    public void onYearMonthDayPicker() {
        final DatePicker picker = new DatePicker(this);
        picker.setCanceledOnTouchOutside(true);
        picker.setUseWeight(true);
        picker.setTopPadding(ConvertUtils.toPx(this, 20));
        picker.setRangeEnd(2050, 1, 11);
        picker.setRangeStart(1950, 8, 29);
        if (user_date == null) {
            //还没有设置过生日，默认是今天的日期
            String date = SystemTools.getYearMonDay();
            picker.setSelectedItem(Integer.valueOf(date.split("-")[0]), Integer.valueOf(date.split("-")[1]), Integer.valueOf(date.split("-")[2]));
        } else {
            picker.setSelectedItem(Integer.valueOf(user_date.split("-")[0]), Integer.valueOf(user_date.split("-")[1]), Integer.valueOf(user_date.split("-")[2]));
        }

        picker.setOnDatePickListener(new DatePicker.OnYearMonthDayPickListener() {
            @Override
            public void onDatePicked(String year, String month, String day) {
                //选择好之后
//                ToastTools.show(UserInfoActivity.this, year + "-" + month + "-" + day);
                select_date = year + "-" + month + "-" + day;
                new HcNetWorkTask(UserInfoActivity.this, UserInfoActivity.this, 3).doPost(UrlConstant.MODIFY_USERINFO, null, postParams(3).getBytes());

            }
        });
        picker.setOnWheelListener(new DatePicker.OnWheelListener() {
            @Override
            public void onYearWheeled(int index, String year) {
                picker.setTitleText(year + "-" + picker.getSelectedMonth() + "-" + picker.getSelectedDay());
            }

            @Override
            public void onMonthWheeled(int index, String month) {
                picker.setTitleText(picker.getSelectedYear() + "-" + month + "-" + picker.getSelectedDay());
            }

            @Override
            public void onDayWheeled(int index, String day) {
                picker.setTitleText(picker.getSelectedYear() + "-" + picker.getSelectedMonth() + "-" + day);
            }
        });
        picker.show();
    }
}
