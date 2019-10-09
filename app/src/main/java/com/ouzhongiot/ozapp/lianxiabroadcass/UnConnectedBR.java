package com.ouzhongiot.ozapp.lianxiabroadcass;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class UnConnectedBR extends BroadcastReceiver {
    public UnConnectedBR() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.

        Toast.makeText(context,"当前无网络，正在重连....请稍后再试",Toast.LENGTH_LONG).show();

    }
}
