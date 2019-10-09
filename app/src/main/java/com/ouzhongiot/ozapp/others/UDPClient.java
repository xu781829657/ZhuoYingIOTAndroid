package com.ouzhongiot.ozapp.others;

/**
 * Created by liu on 2016/5/15.
 */

import android.util.Log;

import com.ouzhongiot.ozapp.tools.LogTools;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class UDPClient {
    private static final int SERVER_PORT = 3001;
    private DatagramSocket dSocket = null;
    private String msg;
    private byte[] msg2 = new byte[11];
    DatagramPacket packet2;
    DatagramPacket packet;
    String string = "";

    /**
     * @param msg
     */
    public UDPClient(String msg) {
        this.msg = msg;
    }


    /**
     * 发送信息到服务器
     */
    public String send() {
        StringBuilder sb = new StringBuilder();
        final DatagramSocket socket;
        try {
            //使用InetAddress(Inet4Address).getByName把IP地址转换为网络地址
            InetAddress serverAddress = InetAddress.getByName("255.255.255.255");
            //创建DatagramSocket对象并指定一个端口号，注意，如果客户端需要接收服务器的返回数据,
            //还需要使用这个端口号来receive，所以一定要记住
            socket = new DatagramSocket();

            //Inet4Address serverAddress = (Inet4Address) Inet4Address.getByName("192.168.1.32");

            byte data[] = msg.getBytes();//把字符串str字符串转换为字节数组
            //创建一个DatagramPacket对象，用于发送数据。
            //参数一：要发送的数据  参数二：数据的长度  参数三：服务端的网络地址  参数四：服务器端端口号
            packet = new DatagramPacket(data, data.length, serverAddress, 3001);
            packet2 = new DatagramPacket(msg2, msg2.length);
            //把数据发送到服务端。
//            socket.send(packet);
            socket.setSoTimeout(10000);
            new Thread(new Runnable() {
                @Override
                public void run() {

                    try {
                        socket.receive(packet2);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }).start();
            Log.wtf("发送udp包", msg);
            socket.send(packet);
            int k;
            String hexK;
            String str;
            for (int j = 0; j < packet2.getLength(); j++) {
                k = 0xff & packet2.getData()[j];
                hexK = Integer.toHexString(k);
                str = ((k < 16) ? ("0" + hexK) : (hexK));
                string = string + str;

            }

            while (string.equals("") || string.substring(0, 1).equals("0")) {
                string = "";
                try {
                    Thread.sleep(1000);
                    Log.wtf("继续发送udp包", msg);
                    socket.send(packet);
                    int k2;
                    String hexK2;
                    String str2;
                    for (int j = 0; j < packet2.getLength(); j++) {
                        k2 = 0xff & packet2.getData()[j];
                        hexK2 = Integer.toHexString(k2);
                        str2 = ((k2 < 16) ? ("0" + hexK2) : (hexK2));
                        string = string + str2;
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
            Log.wtf("这个是收到的数据", string);

            socket.close();
        } catch (SocketException e) {
            LogTools.d("超时");
            e.printStackTrace();

        } catch (UnknownHostException e) {
            LogTools.d("超时");
            e.printStackTrace();
        } catch (IOException e) {
            LogTools.d("超时");
            e.printStackTrace();
        }

        return string;
    }
}