package com.ouzhongiot.ozapp.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;


/**
 * Created by liu on 2016/5/18.
 */
public class TCPCilent extends Service {

    private MyBinder myBinder = new MyBinder();
    @Override
    public IBinder onBind(Intent intent) {
        return myBinder;
    }

    public class MyBinder extends Binder{

        String message ="";
        //建立tcp连接
        //创建Socket
        public void Tcpconnect(){

        }
    }

}

