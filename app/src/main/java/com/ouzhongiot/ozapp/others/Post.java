package com.ouzhongiot.ozapp.others;

import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.List;

/**
 * Created by liu on 2016/5/10.
 */
public class Post {
    public static String dopost(String url,List<NameValuePair> params){
        String strResult="请求失败";
        HttpPost httpRequest = new HttpPost(url);
        try {
          /* 添加请求参数到请求对象*/
            httpRequest.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
          /*发送请求并等待响应*/
            HttpResponse httpResponse = new DefaultHttpClient().execute(httpRequest);
          /*若状态码为200 ok*/
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
            /*读返回数据*/
                strResult = EntityUtils.toString(httpResponse.getEntity());
                Log.wtf("返回的数据",strResult);
            } else {
                Log.wtf("请求服务器","Error Response: " + httpResponse.getStatusLine().toString());
            }
        } catch (ClientProtocolException e) {
            Log.wtf("请求服务器","Error Response: 1" +e.getMessage().toString());
            e.printStackTrace();
        } catch (IOException e) {
            Log.wtf("请求服务器", "Error Response: 2" + e.getMessage().toString());
            e.printStackTrace();
        } catch (Exception e) {
            Log.wtf("请求服务器","Error Response: 3" +e.getMessage().toString());
            e.printStackTrace();
        }
       return strResult;
    }
}
