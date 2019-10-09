package com.ouzhongiot.ozapp.others;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

/**
 * Created by liu on 2016/6/7.
 */
public class LoginOutActivity extends FragmentActivity {

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            // TODO Auto-generated method stub
            super.onCreate(savedInstanceState);
            ActivityCollector.addActivity(this);
        }

        @Override
        protected void onDestroy() {
            // TODO Auto-generated method stub
            super.onDestroy();
            ActivityCollector.removeActivity(this);
        }
    }
