package com.ouzhongiot.ozapp.others;

import android.content.Context;
import android.os.Environment;

import java.io.File;

/**
 * Created by liu on 2016/10/18.
 */
public class FileTool {


    /**
     * 添加文件目录
     *
     * @param context
     * @param p
     * @return
     */
    public static String getPath2(Context context, String p) {
        String path = null;
        boolean hasSDCard = Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
        String packageName = context.getPackageName();
        if (hasSDCard) {
            path = "/sdcard/" + packageName + "/" + p;
        } else {
            path = "/data/" + packageName + "/" + p;
        }
        File file = new File(path);
        boolean isExist = file.exists();
        if (!isExist) {
            file.mkdirs();
        }
        return file.getPath();
    }
}
