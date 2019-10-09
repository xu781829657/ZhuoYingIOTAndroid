package com.ouzhongiot.ozapp.tools;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import com.ouzhongiot.ozapp.constant.SpConstant;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class FileTool {

    /*
     * 在SD卡上创建文件
     */
    public static File createSDFile(String filepath) throws IOException {
        File file = new File(filepath);
        file.createNewFile();
        return file;
    }

    /*
     * 在SD卡上创建目录
     */
    public static File createSDDir(String filepath) {
        File dir = new File(filepath);
        dir.mkdir();
        return dir;
    }

    /*
     * 判断SD卡上文件夹是否存在
     */
    public static boolean isFileExist(String filepath) {
        File file = new File(filepath);
        return file.exists();
    }

    /**
     * 判断文件夹是否存在不存在创建文件夹
     *
     * @param filepath
     * @return
     */
    public static boolean isFolderExists(String filepath) {
        File file = new File(filepath);
        if (!file.exists()) {
            if (file.mkdir()) {
                // 资源目录创建成功
                return true;
            } else
                return false;
        }
        return true;
    }

    /**
     * 获取图片的存储目录，在有sd卡的情况下为“/sdcard/apps_images/本应用包名/cach/images”
     * 没有sd的情况下为“/data/data/本应用包名/cach/images/”
     *
     * @param context
     * @return本地图片存储目录
     */
    public static String getPath(Context context) {
        String path = null;
        boolean hasSDCard = Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);// 判断是否有sd卡
        String packageName = context.getPackageName() + "/cach/images/";
        if (hasSDCard) {
            path = "/sdcard/apps_images/" + packageName;
        } else {
            path = "/data/data/" + packageName;
        }
        File file = new File(path);
        boolean isExist = file.exists();
        if (!isExist) {
            file.mkdirs();
        }
        return file.getPath();
    }

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

    /**
     * 将一个InputStream里面的数据写入到SD卡中
     *
     * @param path
     * @param fileName
     * @param input
     * @return
     */
    public static File write2SDFromInput(String path, String fileName,
                                         InputStream input) {
        File file = null;
        OutputStream output = null;
        try {
            // 创建目录
            createSDDir(path);
            // 创建文件
            file = createSDFile(path + fileName);
            // 创建输出流
            output = new FileOutputStream(file);
            // 创建缓冲区
            byte buffer[] = new byte[4 * 1024];
            // 写入数据
            int bufferLength = 0;
            while ((bufferLength = input.read(buffer)) != -1) {
                output.write(buffer, 0, bufferLength);
            }
            // 清空缓存
            output.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                // 关闭输出流
                output.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    /**
     * 复制单个文件
     *
     * @param oldPath String 原文件路径 如：c:/fqf.txt
     * @param newPath String 复制后的路径 如：f:/fqf.txt
     */
    public void copyFile(String oldPath, String newPath) {
        try {
            int bytesum = 0;
            int byteread = 0;
            File oldfile = new File(oldPath);
            if (oldfile.exists()) {
                InputStream inStream = new FileInputStream(oldPath);
                FileOutputStream fs = new FileOutputStream(newPath);
                byte[] buffer = new byte[1444];
                int length;
                while ((byteread = inStream.read(buffer)) != -1) {
                    bytesum += byteread; // 字节数文件大小
                    System.out.println(bytesum);
                    fs.write(buffer, 0, byteread);
                }
                inStream.close();
            }
        } catch (Exception e) {
            System.out.println("复制单个文件操作出错");
            e.printStackTrace();

        }
    }

    // 请求头像转化为文件
    public static File picToFile(Context context, Bitmap bmp) {
        if (bmp == null)
            return null;
        //下面的方法：手机上就会存最新更改的图片
//        File file = new File(Environment.getExternalStorageDirectory(),
//                "tmp.png");
//        if (file.exists()) {
//            file.delete();
//        }
        //下面的方法：图片不覆盖，所有修改过的图片都会存下来
        String fileName = SpData.getInstance(context).getData(SpConstant.HEAD_FILE_NAME_TIME).toString() + ".png";
        File file = new File(Environment.getExternalStorageDirectory(), fileName);
        try {
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return file;
    }

    // 请求头像转化为文件
    public static File feedbackPicToFile(Context context, Bitmap bmp) {
        if (bmp == null)
            return null;
        //下面的方法：图片不覆盖，所有修改过的图片都会存下来
        File file = new File(Environment.getExternalStorageDirectory(), String.valueOf(System.currentTimeMillis()) + ".png");
        try {
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return file;
    }


    /**
     * 将图片转化为文件(生成随机名字)
     *
     * @param bmp
     * @return
     */
    public static File BitmapToFile(Bitmap bmp) {
        if (bmp == null)
            return null;
        File file = new File(checkDir("images"),
                getRandomFileName() + ".png");
        if (file.exists()) {
            file.delete();
        }

        try {
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    /**
     * 生成随机文件名：当前年月日时分秒+五位随机数
     *
     * @return
     */
    public static String getRandomFileName() {
        SimpleDateFormat simpleDateFormat;
        simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        Date date = new Date();
        String str = simpleDateFormat.format(date);
        Random random = new Random();
        int rannum = (int) (random.nextDouble() * (99999 - 10000 + 1)) + 10000;// 获取5位随机数
        return rannum + str;// 当前时间
    }

    /**
     * 组装路径
     *
     * @param path
     * @return
     */
    private static String checkDir(String path) {
        // 设置根目录
        File root = Environment.getExternalStorageDirectory();
        String paths = root.getAbsolutePath() + File.separator + "Android" + File.separator + "data" + File.separator + "com.palmcity.android.wifi" + File.separator;
        // 判断文件是否存在
        File file = new File(paths + File.separator + path + File.separator);
        if (!file.exists()) {
            file.mkdirs();
        }
        return file.getPath();
    }

    /**
     * File转成Bitmap
     * @param name
     * @return
     */
    public static Bitmap fileToBitmapStr(String name) {
        try {
            FileInputStream fis = new FileInputStream(name);
            return BitmapFactory.decodeStream(fis);  ///把流转化为Bitmap图片

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * File转成Bitmap
     * @param file
     * @return
     */
    public static Bitmap fileToBitmapFile(File file){
        FileInputStream is = null;
        try {
            is = new FileInputStream(file);
            return BitmapFactory.decodeStream(is);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;

    }


}
