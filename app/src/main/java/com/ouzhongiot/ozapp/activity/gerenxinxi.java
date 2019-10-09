package com.ouzhongiot.ozapp.activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.Instrumentation;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.ouzhongiot.ozapp.OZApplication;
import com.ouzhongiot.ozapp.R;
import com.ouzhongiot.ozapp.others.ActivityCollector;
import com.ouzhongiot.ozapp.others.CircleImageView;
import com.ouzhongiot.ozapp.others.FilePost;
import com.ouzhongiot.ozapp.others.JacksonUtil;
import com.ouzhongiot.ozapp.others.LoginOutActivity;
import com.ouzhongiot.ozapp.others.Post;
import com.socks.cropimage.CropOption;
import com.socks.cropimage.CropOptionAdapter;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class gerenxinxi extends LoginOutActivity {

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private String address;
    private TextView tv_dizhi, tv_youxiang, tv_nicheng, tv_userSn, tv_shengri;
    private ImageView iv_xingbie;
    private String email, nicheng, xingbie;
    private static final int DATE_PICKER_ID = 1;
    private static final int PICK_FROM_CAMERA = 1;
    private static final int CROP_FROM_CAMERA = 2;
    private static final int PICK_FROM_FILE = 3;
    private CircleImageView mImageView;
    private Uri imgUri;
    private String touxiangbendi;
    private Socket socket;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        socket =((OZApplication)getApplication()).socket;
        setContentView(R.layout.activity_gerenxinxi);
        tv_dizhi = (TextView) findViewById(R.id.tv_dizhi);
        tv_youxiang = (TextView) findViewById(R.id.tv_youxiang);
        tv_nicheng = (TextView) findViewById(R.id.tv_nicheng);
        iv_xingbie = (ImageView) findViewById(R.id.iv_xingbie);
        tv_userSn = (TextView) findViewById(R.id.tv_userSn);
        tv_shengri = (TextView) findViewById(R.id.tv_shengri);
        mImageView = (CircleImageView) findViewById(R.id.gerenxinxi_touxiang);
        preferences = getSharedPreferences("data", MODE_PRIVATE);
        editor = preferences.edit();
        address = preferences.getString("address", "");
        email = preferences.getString("email", "");
        nicheng = preferences.getString("nickname", "");
        xingbie = preferences.getString("sex", "");
        //返回
        this.findViewById(R.id.iv_gerenxinxi_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBack();
            }
        });
        //Z币
        this.findViewById(R.id.zbi).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(gerenxinxi.this, zbi.class);
                startActivity(intent);
            }
        });
        //地址
        this.findViewById(R.id.dizhi).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(gerenxinxi.this, dizhi.class);
                startActivity(intent);

            }
        });

        //昵称
        this.findViewById(R.id.nicheng).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(gerenxinxi.this, nicheng.class);
                startActivity(intent);
            }
        });
        //邮箱
        this.findViewById(R.id.youxiang).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(gerenxinxi.this, youxiang.class);
                startActivity(intent);
            }
        });
        //退出登录
        this.findViewById(R.id.tuichuzhanghao).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                socket = null;
                    ((OZApplication)getApplication()).setSocket(socket);
                    ((OZApplication)getApplication()).setTCPconnect(false);

                editor.putString("username","");
                editor.putString("password","");
                editor.commit();
                Intent intent1 = new Intent(gerenxinxi.this,login.class);
//                intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent1);
                ActivityCollector.finishAll();
            }
        });
        //修改密码
        this.findViewById(R.id.gerenxinxi_xiugaimima).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* Intent intent = new Intent(gerenxinxi.this, forgetpassword.class);
                startActivity(intent);*/
                Intent intent = new Intent(gerenxinxi.this, ForgetPwdActivity.class);
                intent.putExtra(ForgetPwdActivity.PARAM_TYPE,"modify");
                startActivity(intent);
            }
        });
        //性别
        iv_xingbie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                switch (preferences.getString("sex", "")) {
                    case "0":
                        iv_xingbie.setImageResource(R.mipmap.girl);
                        editor.putString("sex", "2");
                        editor.commit();
                        break;
                    case "null":
                        iv_xingbie.setImageResource(R.mipmap.girl);
                        editor.putString("sex", "2");
                        editor.commit();
                        break;
                    case "1":
                        iv_xingbie.setImageResource(R.mipmap.girl);
                        editor.putString("sex", "2");
                        editor.commit();
                        break;
                    case "2":
                        iv_xingbie.setImageResource(R.mipmap.boy);
                        editor.putString("sex", "1");
                        editor.commit();
                        break;
                    default:
                        break;

                }
            }
        });

        this.findViewById(R.id.tv_shengri).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(DATE_PICKER_ID);
            }
        });
        if (!((OZApplication)getApplication()).getISTOUXIANGUPDATE()) {
            ImageLoader.ImageListener listener = ImageLoader.getImageListener(mImageView,
                    R.mipmap.test, R.mipmap.test);
            Log.wtf("头像地址", preferences.getString("headImageUrl", "null"));
            if (!preferences.getString("headImageUrl", "null").equals("null")) {
                ((OZApplication) getApplication()).getImageLoader().get(preferences.getString("headImageUrl", "null"), listener);
            }
        }
    }

    //日期选择器
    DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            tv_shengri.setText(year + "-" + monthOfYear + "-" + dayOfMonth);
            editor.putString("birthdate", year + "-" + monthOfYear + "-" + dayOfMonth);
            editor.commit();
        }
    };

    @Override
    @Deprecated
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_PICKER_ID:
                return new DatePickerDialog(this, onDateSetListener, 2014, 1, 11);//注意月份是从0开始的
        }
        return null;
    }


    @Override
    protected void onResume() {

        if (address.equals("null") || address.equals("")) {
        } else {
            tv_dizhi.setText(preferences.getString("address.xiancheng", "请设置地址") + preferences.getString("address.jiedao", ""));
        }

        if (email.equals("null") || email.equals("")) {
        } else {
            tv_youxiang.setText(preferences.getString("email", ""));
        }

        if (nicheng.equals("null") || nicheng.equals("")) {
        } else {
            tv_nicheng.setText(preferences.getString("nickname", ""));
        }
//性别
        if (xingbie.equals("null") || xingbie.equals("")||xingbie.equals("0")) {

            editor.putString("sex", "1");
        } else if (preferences.getString("sex", "").equals("1")) {
            iv_xingbie.setImageResource(R.mipmap.boy);
        } else if (preferences.getString("sex", "").equals("2")) {
            iv_xingbie.setImageResource(R.mipmap.girl);
        }

        tv_userSn.setText(preferences.getString("userSn", ""));

        if (!preferences.getString("birthdate", "").equals("null") || !preferences.getString("birthdate", "").equals("")) {
            tv_shengri.setText(preferences.getString("birthdate", ""));
        }


        if (((OZApplication)getApplication()).getISTOUXIANGUPDATE())
        {
            if (coldfanAindex.getLoacalBitmap(preferences.getString("touxiangbendiurl",Environment.getExternalStorageDirectory() + "/"
                    + "lianxiatouxiang" + ".jpg"))!=null){

                mImageView.setImageBitmap(coldfanAindex.getLoacalBitmap(preferences.getString("touxiangbendiurl", Environment.getExternalStorageDirectory() + "/"
                        + "lianxiatouxiang" + ".jpg")));
            }
        }



        super.onResume();
    }

    @Override
    protected void onDestroy() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String uriAPI = MainActivity.ip + "smarthome/user/modifyUserInfo";
                List<NameValuePair> params = new ArrayList<NameValuePair>();
//                params.add(new BasicNameValuePair("user.id", preferences.getString("userId", "")));
                params.add(new BasicNameValuePair("user.sn", preferences.getString("userSn", "")));
                params.add(new BasicNameValuePair("user.nickname", preferences.getString("nickname", "")));
                params.add(new BasicNameValuePair("user.sex", preferences.getString("sex", "")));
                params.add(new BasicNameValuePair("user.birthdate", preferences.getString("birthdate", "")));
                params.add(new BasicNameValuePair("user.email", preferences.getString("email", "")));
                String str = Post.dopost(uriAPI, params);
                Map<String, Object> map = JacksonUtil.deserializeJsonToObject(str, Map.class);
                String state = JacksonUtil.serializeObjectToJson(map.get("state"));
            }
        }).start();
        super.onDestroy();
    }


    //设置头像
    public void btnClick(View view) {
        new AlertDialog.Builder(gerenxinxi.this).setTitle("选择头像")
                .setPositiveButton("相册", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 方式1，直接打开图库，只能选择图库的图片
                        Intent i = new Intent(Intent.ACTION_PICK,
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        // 方式2，会先让用户选择接收到该请求的APP，可以从文件系统直接选取图片
                        Intent intent = new Intent(Intent.ACTION_GET_CONTENT,
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        intent.setType("image/*");
                        startActivityForResult(intent, PICK_FROM_FILE);

                    }
                }).setNegativeButton("拍照", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(
                        MediaStore.ACTION_IMAGE_CAPTURE);
                imgUri = Uri.fromFile(new File(Environment
                        .getExternalStorageDirectory(), "avatar_"
                        + String.valueOf(System.currentTimeMillis())
                        + ".jpg"));
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imgUri);
                startActivityForResult(intent, PICK_FROM_CAMERA);


            }
        }).create().show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case PICK_FROM_CAMERA:
                doCrop();
                break;
            case PICK_FROM_FILE:
                imgUri = data.getData();
                doCrop();
                break;
            case CROP_FROM_CAMERA:
                if (null != data) {
                    setCropImg(data);
                }

                break;
        }
    }
    private void doCrop() {
        final ArrayList<CropOption> cropOptions = new ArrayList<CropOption>();
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setType("image/*");
        List<ResolveInfo> list = getPackageManager().queryIntentActivities(
                intent, 0);
        int size = list.size();

        if (size == 0) {
            Toast.makeText(this, "can't find crop app", Toast.LENGTH_SHORT)
                    .show();
            return;
        } else {
            intent.setData(imgUri);
            intent.putExtra("outputX", 200);
            intent.putExtra("outputY", 200);
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            intent.putExtra("scale", true);
            intent.putExtra("return-data", true);

            // only one
            if (size == 1) {
                Intent i = new Intent(intent);
                ResolveInfo res = list.get(0);
                i.setComponent(new ComponentName(res.activityInfo.packageName,
                        res.activityInfo.name));
                startActivityForResult(i, CROP_FROM_CAMERA);
            } else {
                // many crop app
                for (ResolveInfo res : list) {
                    final CropOption co = new CropOption();
                    co.title = getPackageManager().getApplicationLabel(
                            res.activityInfo.applicationInfo);
                    co.icon = getPackageManager().getApplicationIcon(
                            res.activityInfo.applicationInfo);
                    co.appIntent = new Intent(intent);
                    co.appIntent
                            .setComponent(new ComponentName(
                                    res.activityInfo.packageName,
                                    res.activityInfo.name));
                    cropOptions.add(co);
                }

                CropOptionAdapter adapter = new CropOptionAdapter(
                        getApplicationContext(), cropOptions);

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("choose a app");
                builder.setAdapter(adapter,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int item) {
                                startActivityForResult(
                                        cropOptions.get(item).appIntent,
                                        CROP_FROM_CAMERA);
                            }
                        });

                builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {

                        if (imgUri != null) {
                            getContentResolver().delete(imgUri, null, null);
                            imgUri = null;
                        }
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();
            }
        }
    }

    /**
     * set the bitmap
     *
     * @param picdata
     */
    private void setCropImg(Intent picdata) {
        Bundle bundle = picdata.getExtras();
        if (null != bundle) {
            Bitmap mBitmap = bundle.getParcelable("data");
            mImageView.setImageBitmap(mBitmap);
            ((OZApplication)getApplication()).setISTOUXIANGUPDATE(true);
            touxiangbendi = System.currentTimeMillis() + "";
            saveBitmap(Environment.getExternalStorageDirectory() + "/"
                    + touxiangbendi + ".jpg", mBitmap);
            editor.putString("touxiangbendiurl", Environment.getExternalStorageDirectory() + "/"
                    + touxiangbendi + ".jpg");
            editor.putString("touxiangname",touxiangbendi);
            editor.commit();
            new Thread(new Runnable() {
                @Override
                public void run() {
//                    File file = new File(Environment.getExternalStorageDirectory() + "/"
//                            + touxiangbendi + ".jpg");
                    List<NameValuePair> params =  new ArrayList<NameValuePair>();
                    params.add(new BasicNameValuePair("files",Environment.getExternalStorageDirectory() + "/"+ touxiangbendi + ".jpg"));
                    params.add(new BasicNameValuePair("userSn", preferences.getString("userSn", "")));
                    String str =   FilePost.post(MainActivity.ip+"smarthome/user/uploadUserHeadphoto",params);
                    Map<String, Object> map = JacksonUtil.deserializeJsonToObject(str, Map.class);
                    if ( JacksonUtil.serializeObjectToJson(map.get("success")).equals("true"))
                    {
                        editor.putString("headImageUrl",JacksonUtil.serializeObjectToJson(map.get("data")));
                        editor.commit();
                    }

                }
            }).start();
        }
    }

    /**
     * save the crop bitmap
     *
     * @param fileName
     * @param mBitmap
     */
    public static void  saveBitmap(String fileName, Bitmap mBitmap) {
        File f = new File(fileName);
        FileOutputStream fOut = null;
        try {
            f.createNewFile();
            fOut = new FileOutputStream(f);
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 30, fOut);
            fOut.flush();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fOut.close();
//                Toast.makeText(gerenxinxi.this, "保存头像成功", Toast.LENGTH_SHORT).show();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }


    //点击返回上个页面
    public void onBack() {
        new Thread() {
            public void run() {
                try {
                    Instrumentation inst = new Instrumentation();
                    inst.sendKeyDownUpSync(KeyEvent.KEYCODE_BACK);
                } catch (Exception e) {
                    Log.e("Exception when onBack", e.toString());
                }
            }
        }.start();
    }
}
