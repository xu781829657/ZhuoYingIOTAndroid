package com.ouzhongiot.ozapp.activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ouzhongiot.ozapp.OZApplication;
import com.ouzhongiot.ozapp.R;
import com.ouzhongiot.ozapp.base.BaseHomeActivity;
import com.ouzhongiot.ozapp.constant.SpConstant;
import com.ouzhongiot.ozapp.fragment.MymachineFragment;
import com.ouzhongiot.ozapp.fragment.PersonalFragment;
import com.ouzhongiot.ozapp.tools.SpData;

/**
 * @author hxf
 * @date 创建时间: 2017/4/20
 * @Description 首页
 */

public class MyMachineActivity extends BaseHomeActivity implements View.OnClickListener {
    private LinearLayout llayout_my_machine;//设备列表
    private ImageView img_my_machine;
    private TextView tv_my_machine;
    private LinearLayout llayout_personal;//个人中心
    private ImageView img_personal;
    private TextView tv_personal;
    private MymachineFragment mymachineFragment;//设备列表
    private PersonalFragment personalFragment;//个人中心
    private Fragment currentFragment;// 当前的Fragment
    private ImageView img_guide_add_machine;

    public static boolean isGuide = false;

    @Override
    public int addContentView() {
        return R.layout.activity_my_machine;
    }

    @Override
    public void initView() {
        llayout_my_machine = (LinearLayout) findViewById(R.id.tab_llayout_my_machine);
        llayout_personal = (LinearLayout) findViewById(R.id.tab_llayout_personal);
        img_my_machine = (ImageView) findViewById(R.id.tab_img_my_machine);
        img_personal = (ImageView) findViewById(R.id.tab_img_personal);
        tv_my_machine = (TextView) findViewById(R.id.tab_tv_my_machine);
        tv_personal = (TextView) findViewById(R.id.tab_tv_personal);
        img_guide_add_machine = (ImageView) findViewById(R.id.img_guide_add_machine_1);

    }

    @Override
    public void initValue() {
        OZApplication.getInstance().addActivity(this);
        mymachineFragment = new MymachineFragment();
        currentFragment = mymachineFragment;
        getSupportFragmentManager().beginTransaction()
                .add(R.id.current_fragment, currentFragment)
                .commitAllowingStateLoss();
        Intent intentreconnected = new Intent();
        intentreconnected.setAction("reconnect");
        sendBroadcast(intentreconnected);
        //设置点击事件
        setClick();
        //显示引导页
        if (getSharedPreferences("data", MODE_PRIVATE).getBoolean("isGuideAddMachine", false)) {
            img_guide_add_machine.setVisibility(View.VISIBLE);
            getSharedPreferences("data", MODE_PRIVATE).edit().putBoolean("isGuideAddMachine", false).commit();
            isGuide = true;
            img_guide_add_machine.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //消失
                    img_guide_add_machine.setVisibility(View.GONE);
                }
            });

        }

    }

    //设置点击事件
    public void setClick() {
        llayout_my_machine.setOnClickListener(this);
        llayout_personal.setOnClickListener(this);

    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onClick(View v) {
        Fragment fragment = null;
        switch (v.getId()) {
            case R.id.tab_llayout_my_machine:
                //设备列表
                img_my_machine.setImageResource(R.mipmap.my_machine_selected);
                tv_my_machine.setTextColor(getResources().getColor(R.color.main_theme_color));
                img_personal.setImageResource(R.mipmap.personal_normal);
                tv_personal.setTextColor(getResources().getColor(R.color.nine_txt_color));
                //切换到mymachineFragment
                if (mymachineFragment == null) {
                    mymachineFragment = new MymachineFragment();
                }
                fragment = mymachineFragment;
                break;
            case R.id.tab_llayout_personal:
                //个人中心
                img_personal.setImageResource(R.mipmap.personal_selected);
                tv_personal.setTextColor(getResources().getColor(R.color.main_theme_color));
                img_my_machine.setImageResource(R.mipmap.my_machine_normal);
                tv_my_machine.setTextColor(getResources().getColor(R.color.nine_txt_color));
                //切换到personalFragment
                if (personalFragment == null) {
                    personalFragment = new PersonalFragment();
                }
                fragment = personalFragment;
                break;
        }
        if (fragment != null) {
            switchContent(currentFragment, fragment);
        }
    }

    /**
     * 从当前页面调到另外一个页面中去
     *
     * @param from 当前fragment
     * @param to   跳转的fragment
     */
    public void switchContent(Fragment from, Fragment to) {
        if (currentFragment != to) {
            currentFragment = to;
            FragmentTransaction transaction = getSupportFragmentManager()
                    .beginTransaction();
            if (!to.isAdded()) {
                transaction.hide(from).add(R.id.current_fragment, to)
                        .commitAllowingStateLoss();// 隐藏当前的fragment，add到Activity中
            } else {
                transaction.hide(from).show(to).commitAllowingStateLoss();// 隐藏当前的fragment，显示下一个
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (SpData.getInstance(this).getData(SpConstant.MACHINE_LIST_UPDATE).toString().equals("yes")) {
            if (mymachineFragment != null) {
                mymachineFragment.requestMyMachineList();
            }
            SpData.getInstance(this).putData(SpConstant.MACHINE_LIST_UPDATE, "no");
        }

    }
}
