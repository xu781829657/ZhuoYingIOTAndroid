package com.ouzhongiot.ozapp.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.ouzhongiot.ozapp.R;
import com.ouzhongiot.ozapp.activity.AboutProductActivity;
import com.ouzhongiot.ozapp.activity.AboutUsActivity;
import com.ouzhongiot.ozapp.activity.MessageNotificationActivity;
import com.ouzhongiot.ozapp.activity.MessageNotificationActivity2;
import com.ouzhongiot.ozapp.activity.UserInfoActivity;
import com.ouzhongiot.ozapp.base.BaseHomeFragment;
import com.ouzhongiot.ozapp.constant.SpConstant;
import com.ouzhongiot.ozapp.others.CircleImageView;
import com.ouzhongiot.ozapp.others.DataCleanManager;
import com.ouzhongiot.ozapp.tools.IconfontTools;
import com.ouzhongiot.ozapp.tools.SpData;
import com.ouzhongiot.ozapp.tools.ToastTools;

/**
 * @author hxf
 * @date 创建时间: 2017/4/13
 * @Description 个人中心
 */

public class PersonalFragment extends BaseHomeFragment implements View.OnClickListener {
    private LinearLayout llayout_back;
    private TextView tv_title;//标题
    private LinearLayout llayout_userinfo;//用户信息
    private LinearLayout llayout_message_notification;//消息通知
    private LinearLayout llayout_about_product;//关于产品
    private LinearLayout llayout_about_us;//关于我们
    private LinearLayout llayout_clean_cache;//清除缓存
    private CircleImageView circle_avatar;//头像
    private TextView tv_nickname;//昵称
    private ImageView img_sex;//性别
    private TextView font_red_hot;//消息通知红点
    private TextView tv_current_cache;//当前缓存
    private boolean flag_sys_noti = false;

    private DisplayImageOptions options;

    private final int SYS_NOTI_REQUEST_CODE = 1000;
    private final int USER_INFO_REQUEST_CODE = 1001;


    @Override
    public int getLayoutResID() {
        return R.layout.fragment_personal;
    }

    @Override
    public void initView() {
        llayout_back = (LinearLayout) mView.findViewById(R.id.llayout_back);
        tv_title = (TextView) mView.findViewById(R.id.txt_head_content);
        llayout_userinfo = (LinearLayout) mView.findViewById(R.id.llayout_personal_userinfo);
        llayout_message_notification = (LinearLayout) mView.findViewById(R.id.llayout_personal_message);
        font_red_hot = (TextView) mView.findViewById(R.id.font_message_dot);
        llayout_about_product = (LinearLayout) mView.findViewById(R.id.llayout_personal_about_product);
        llayout_about_us = (LinearLayout) mView.findViewById(R.id.llayout_personal_about_us);
        llayout_clean_cache = (LinearLayout) mView.findViewById(R.id.llayout_personal_clean_cache);
        tv_current_cache = (TextView) mView.findViewById(R.id.tv_personal_current_cache);
        circle_avatar = (CircleImageView) mView.findViewById(R.id.img_personal_avatar);
        tv_nickname = (TextView) mView.findViewById(R.id.tv_personal_username);
        img_sex = (ImageView) mView.findViewById(R.id.img_personal_sex);
    }

    @Override
    public void initValue() {
        llayout_back.setVisibility(View.GONE);
        tv_title.setText(getString(R.string.txt_personal));
        font_red_hot.setTypeface(IconfontTools.getTypeface(getActivity()));
        //显示头像、昵称、性别
        options = new DisplayImageOptions.Builder().cacheInMemory(true)
                .cacheOnDisk(true).bitmapConfig(Bitmap.Config.RGB_565)
                .showImageForEmptyUri(R.mipmap.test)
                .showImageOnFail(R.mipmap.test).build();
        if (!SpData.getInstance(getActivity()).getData(SpConstant.LOGIN_HEADURL).toString().equals("null")) {
            ImageLoader.getInstance().displayImage(SpData.getInstance(getActivity()).getData(SpConstant.LOGIN_HEADURL).toString(), circle_avatar, options);
        }
        if (!SpData.getInstance(getActivity()).getData(SpConstant.LOGIN_NICKNAME).toString().equals("null")) {
            tv_nickname.setText(SpData.getInstance(getActivity()).getData(SpConstant.LOGIN_NICKNAME).toString());
        }
        if (SpData.getInstance(getActivity()).getData(SpConstant.LOGIN_SEX).toString().equals("1")) {
            //男
            img_sex.setImageResource(R.mipmap.sex_nan);
        } else if (SpData.getInstance(getActivity()).getData(SpConstant.LOGIN_SEX).toString().equals("2")) {
            //女
            img_sex.setImageResource(R.mipmap.sex_nv);
        }

        //统计当前缓存
        countCurrentCache();
        //设置点击事件
        setClick();

    }

    public void setClick() {
        llayout_userinfo.setOnClickListener(this);
        llayout_message_notification.setOnClickListener(this);
        llayout_about_product.setOnClickListener(this);
        llayout_about_us.setOnClickListener(this);
        llayout_clean_cache.setOnClickListener(this);

    }

    //统计当前缓存
    public void countCurrentCache() {
        try {
            tv_current_cache.setText(getString(R.string.txt_personal_current_cache) + DataCleanManager.getTotalCacheSize(getActivity()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.llayout_personal_userinfo:
                //用户信息
                startActivityForResult(new Intent(getActivity(), UserInfoActivity.class), USER_INFO_REQUEST_CODE);
                break;
            case R.id.llayout_personal_message:
                //消息通知
                Intent meIntent = new Intent(getActivity(), MessageNotificationActivity2.class);
                meIntent.putExtra(MessageNotificationActivity.PARAM_SYSTEM_NOTI_FLAG, flag_sys_noti);
                startActivityForResult(meIntent, SYS_NOTI_REQUEST_CODE);
                break;
            case R.id.llayout_personal_about_product:
                //关于产品
                startActivity(new Intent(getActivity(), AboutProductActivity.class));
                break;
            case R.id.llayout_personal_about_us:
                //关于我们
                startActivity(new Intent(getActivity(), AboutUsActivity.class));
                break;
            case R.id.llayout_personal_clean_cache:
                //清除缓存
                if (tv_current_cache.getText().toString().trim().equals(getString(R.string.txt_personal_current_cache) + "0K") || tv_current_cache.getText().toString().trim().equals(getString(R.string.txt_personal_current_cache) + "0.0Mb")) {
                    //没有缓存
                    ToastTools.show(getActivity(), getString(R.string.txt_none_cache_hint));
                } else {
                    showCleanDialog();
                }
                break;
        }

    }

    //弹出是否清空缓存的窗口
    public void showCleanDialog() {
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        RelativeLayout layout = (RelativeLayout) layoutInflater.inflate(
                R.layout.dialog_clean_cache, null);
        final Dialog dialog = new AlertDialog.Builder(getActivity()).create();
        dialog.show();
        dialog.getWindow().setContentView(layout);
        Button btn_cancle = (Button) layout.findViewById(R.id.btn_clean_cache_cancle);
        btn_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //取消
                dialog.cancel();
            }
        });
        Button btn_sure = (Button) layout.findViewById(R.id.btn_clean_cache_sure);
        btn_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //清除
                dialog.cancel();
                DataCleanManager.clearAllCache(getActivity());
                tv_current_cache.setText(getString(R.string.txt_personal_current_cache) + "0.0Mb");
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SYS_NOTI_REQUEST_CODE) {
            if (resultCode == getActivity().RESULT_OK) {
                //红点消失
                font_red_hot.setVisibility(View.GONE);
                flag_sys_noti = false;
            }
        } else if (requestCode == USER_INFO_REQUEST_CODE) {
            //用户信息
            if (resultCode == getActivity().RESULT_OK) {
                if (data.getBooleanExtra("avatar", false)) {
                    //修改了头像
                    ImageLoader.getInstance().displayImage(SpData.getInstance(getActivity()).getData(SpConstant.LOGIN_HEADURL).toString(), circle_avatar, options);
                }
                if (data.getBooleanExtra("name", false)) {
                    //修改了昵称
                    tv_nickname.setText(SpData.getInstance(getActivity()).getData(SpConstant.LOGIN_NICKNAME).toString());

                }
                if (data.getBooleanExtra("sex", false)) {
                    //修改了性别
                    if (SpData.getInstance(getActivity()).getData(SpConstant.LOGIN_SEX).toString().equals("1")) {
                        //男
                        img_sex.setImageResource(R.mipmap.sex_nan);
                    } else if (SpData.getInstance(getActivity()).getData(SpConstant.LOGIN_SEX).toString().equals("2")) {
                        //女
                        img_sex.setImageResource(R.mipmap.sex_nv);
                    }

                }
            }

        }
    }

}
