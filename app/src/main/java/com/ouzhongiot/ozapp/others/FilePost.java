package com.ouzhongiot.ozapp.others;

import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by liu on 2016/5/21.
 */
public class FilePost {



    public static  String  post(String url, List<NameValuePair> nameValuePairs) {
        HttpClient httpClient = new DefaultHttpClient();
        HttpContext localContext = new BasicHttpContext();
        HttpPost httpPost = new HttpPost(url);
        String  strResult = "";
        try {
            MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);

            for(int index=0; index < nameValuePairs.size(); index++) {
                if(nameValuePairs.get(index).getName().equalsIgnoreCase("files")) {
                    // If the key equals to "image", we use FileBody to transfer the data
                    entity.addPart(nameValuePairs.get(index).getName(), new FileBody(new File(nameValuePairs.get(index).getValue())));
                } else {
                    // Normal string data
                    entity.addPart(nameValuePairs.get(index).getName(), new StringBody(nameValuePairs.get(index).getValue()));
                }
            }

            httpPost.setEntity(entity);

            HttpResponse response = httpClient.execute(httpPost, localContext);
            if (response.getStatusLine().getStatusCode() == 200) {
            /*读返回数据*/
                strResult = EntityUtils.toString(response.getEntity());
                Log.wtf("返回的数据", strResult);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return strResult;
    }
}
