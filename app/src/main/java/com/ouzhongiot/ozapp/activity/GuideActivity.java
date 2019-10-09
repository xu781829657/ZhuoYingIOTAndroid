package com.ouzhongiot.ozapp.activity;

import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ouzhongiot.ozapp.OZApplication;
import com.ouzhongiot.ozapp.R;
import com.ouzhongiot.ozapp.base.BaseHomeActivity;

import java.util.ArrayList;

/**
 * @author hxf
 * @date 创建时间: 2017/1/3
 * @Description 引导页
 */

public class GuideActivity extends BaseHomeActivity {
    private ViewPager viewPager;
    private ViewGroup group;// 包含所有点

    ArrayList<View> list;// 承载所有页面
    ImageView[] dotImageViews;


    @Override
    public int addContentView() {
        return R.layout.activity_guide;
    }

    @Override
    public void initView() {
        viewPager = (ViewPager) findViewById(R.id.guide_viewpager);
        group = (ViewGroup) findViewById(R.id.dot_guide_group);

    }

    @Override
    public void initValue() {
        OZApplication.getInstance().addActivity(this);
        getSharedPreferences("data", MODE_PRIVATE).edit().putBoolean("isFirst", false).commit();
        getSharedPreferences("data", MODE_PRIVATE).edit().putBoolean("isGuideAddMachine", true).commit();
        // 初始化布局
        initLayout();
        //设置点击事件
        setClick();

        viewPager.setAdapter(new MyAdapter());
        viewPager.setOnPageChangeListener(new MyListener(group));

    }

    public void initLayout() {
        LayoutInflater inflater = getLayoutInflater();
        list = new ArrayList<View>();
        // 3张图片
        list.add(inflater.inflate(R.layout.item_guide1, null));
        list.add(inflater.inflate(R.layout.item_guide2, null));
        list.add(inflater.inflate(R.layout.item_guide3, null));
        // 3个圆点
        dotImageViews = new ImageView[list.size()];
        for (int i = 0; i < list.size(); i++) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    40, 40);
            params.setMargins(15, 0, 0, 25);
            ImageView dot = new ImageView(GuideActivity.this);
            dot.setLayoutParams(params);
            dotImageViews[i] = dot;
            if (i == 0) {
                // 默认进入程序后第一张图片被选中;
                dotImageViews[i].setBackgroundResource(R.mipmap.guide1_dot1);
            } else {
                dotImageViews[i]
                        .setBackgroundResource(R.mipmap.guide_dot_gray);
            }
            group.addView(dot);
        }

    }

    // 设置点击事件
    public void setClick() {
        ((Button) list.get(2).findViewById(R.id.guide_end))
                .setOnClickListener(new View.OnClickListener() {

                    public void onClick(View arg0) {
//                        startActivity(new Intent(GuideActivity.this, login.class));
                        startActivity(new Intent(GuideActivity.this, LoginActivity.class));
                        OZApplication.getInstance().finishActivity();
                    }
                });

    }

    class MyAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public int getItemPosition(Object object) {
            return super.getItemPosition(object);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(list.get(position));
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(list.get(position));
            return list.get(position);
        }
    }

    class MyListener implements ViewPager.OnPageChangeListener {
        private ViewGroup group;

        public MyListener(ViewGroup group) {
            super();
            this.group = group;
        }

        @Override
        public void onPageScrolled(int position, float positionOffset,
                                   int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            for (int i = 0; i < dotImageViews.length; i++) {
                if (i == position) {
                    if (position == 0) {
                        dotImageViews[0]
                                .setBackgroundResource(R.mipmap.guide1_dot1);
                    } else if (position == 1) {
                        dotImageViews[1]
                                .setBackgroundResource(R.mipmap.guide2_dot2);
                    } else if (position == 2) {
                        dotImageViews[2]
                                .setBackgroundResource(R.mipmap.guide3_dot3);
                    }
                } else {
                    dotImageViews[i]
                            .setBackgroundResource(R.mipmap.guide_dot_gray);
                }
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

}

