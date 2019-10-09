package com.ouzhongiot.ozapp.http;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;
import java.util.UUID;
/**
 * 
 * @Description: post请求工具类
 * @author: hxf
 * @date: 2016-6-21 下午5:19:22
 */
public class PostParamTools {
	/**
	 * post包装参数
	 * 
	 * @param params
	 * @return
	 */
	public static String wrapParams(Map<String, String> params) {
		StringBuilder sb = new StringBuilder();
		for (Map.Entry<String, String> entry : params.entrySet()) {
			sb.append(entry.getKey()).append("=");
			try {
				sb.append(URLEncoder.encode(entry.getValue(), "utf-8"));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			sb.append("&");
		}
		sb.deleteCharAt(sb.length() - 1);
		return sb.toString();
	}

	/**
	 * 文件转化为字节数组
	 * 
	 * @param file
	 * @return
	 */
	public static byte[] getBytesFromFile(File file) {
		byte[] ret = null;
		try {
			if (file == null) {
				// log.error("helper:the file is null!");
				return null;
			}
			FileInputStream in = new FileInputStream(file);
			ByteArrayOutputStream out = new ByteArrayOutputStream(4096);
			byte[] b = new byte[4096];
			int n;
			while ((n = in.read(b)) != -1) {
				out.write(b, 0, n);
			}
			in.close();
			out.close();
			ret = out.toByteArray();
		} catch (IOException e) {
			// log.error("helper:get bytes from file process error!");
			e.printStackTrace();
		}
		return ret;
	}
	  /**
     * 使用HttpURLConnection通过POST方式提交请求，并上传文件。
     *
     * @param actionUrl  访问的url
     * @param textParams 文本类型的POST参数(key:value)
     * @param filePaths  文件路径的集合
     * @return 服务器返回的数据，出现异常时返回 null
     */
    public static String postWithFiles(String actionUrl, Map<String, String> textParams, List<String> filePaths) {
        try {
            final String BOUNDARY = UUID.randomUUID().toString();
            final String PREFIX = "--";
            final String LINE_END = "\r\n";

            final String MULTIPART_FROM_DATA = "multipart/form-data";
            final String CHARSET = "UTF-8";

            URL uri = new URL(actionUrl);
            HttpURLConnection conn = (HttpURLConnection) uri.openConnection();

            //缓存大小
            conn.setChunkedStreamingMode(1024 * 1024 * 64);
            //超时
            conn.setReadTimeout(10 * 1000);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");

            conn.setRequestProperty("connection", "keep-alive");
            conn.setRequestProperty("Charset", "UTF-8");
            conn.setRequestProperty("Content-Type", MULTIPART_FROM_DATA + ";boundary=" + BOUNDARY);

            // 拼接文本类型的参数
            StringBuilder textSb = new StringBuilder();
            if (textParams != null) {
                for (Map.Entry<String, String> entry : textParams.entrySet()) {
                    textSb.append(PREFIX).append(BOUNDARY).append(LINE_END);
                    textSb.append("Content-Disposition: form-data; name=\"" + entry.getKey() + "\"" + LINE_END);
                    textSb.append("Content-Type: text/plain; charset=" + CHARSET + LINE_END);
                    textSb.append("Content-Transfer-Encoding: 8bit" + LINE_END);
                    textSb.append(LINE_END);
                    textSb.append(entry.getValue());
                    textSb.append(LINE_END);
                }
            }

            DataOutputStream outStream = new DataOutputStream(conn.getOutputStream());
            outStream.write(textSb.toString().getBytes());

            //参数POST方式
            //outStream.write("userId=1&cityId=26".getBytes());

            // 发送文件数据
            if (filePaths != null) {
                for (String file : filePaths) {
                    StringBuilder fileSb = new StringBuilder();
                    fileSb.append(PREFIX).append(BOUNDARY).append(LINE_END);
                    fileSb.append("Content-Disposition: form-data; name=\"file[]\"; filename=\"" +// php后台一定要改成 file[]否则只能接收到一张图片
                            file.substring(file.lastIndexOf("/") + 1) + "\"" + LINE_END);
                    fileSb.append("Content-Type: application/octet-stream; charset=" + CHARSET + LINE_END);
                    fileSb.append(LINE_END);
                    outStream.write(fileSb.toString().getBytes());

                    InputStream is = new FileInputStream(file);
                    byte[] buffer = new byte[1024 * 8];
                    int len;
                    while ((len = is.read(buffer)) != -1) {
                        outStream.write(buffer, 0, len);
                    }

                    is.close();
                    outStream.write(LINE_END.getBytes());
                }
            }

            // 请求结束标志
            outStream.write((PREFIX + BOUNDARY + PREFIX + LINE_END).getBytes());
            outStream.flush();

            // 得到响应码
            int responseCode = conn.getResponseCode();
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), CHARSET));

            StringBuilder resultSb = null;
            String line;
            if (responseCode == 200) {
                resultSb = new StringBuilder();
                while ((line = br.readLine()) != null) {
                    resultSb.append(line).append("\n");
                }
            }

            br.close();
            outStream.close();
            conn.disconnect();

            return resultSb == null ? null : resultSb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 通过拼接的方式构造请求内容，实现参数传输以及文件传输
     *
     * @param url    访问的服务器URL
     * @param params 普通参数
     * @param files  文件参数
     * @return
     * @throws IOException
     */
    public static String post(String url, Map<String, String> params, Map<String, File> files)
            throws IOException {
        String BOUNDARY = UUID.randomUUID().toString();
        String PREFIX = "--", LINEND = "\r\n";
        String MULTIPART_FROM_DATA = "multipart/form-data";
        String CHARSET = "UTF-8";


        URL uri = new URL(url);
        HttpURLConnection conn = (HttpURLConnection) uri.openConnection();
        conn.setReadTimeout(10 * 1000); // 缓存的最长时间
        conn.setDoInput(true);// 允许输入
        conn.setDoOutput(true);// 允许输出
        conn.setUseCaches(false); // 不允许使用缓存
        conn.setRequestMethod("POST");
        conn.setRequestProperty("connection", "keep-alive");
        conn.setRequestProperty("Charsert", "UTF-8");
        conn.setRequestProperty("Content-Type", MULTIPART_FROM_DATA + ";boundary=" + BOUNDARY);


        // 首先组拼文本类型的参数
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            sb.append(PREFIX);
            sb.append(BOUNDARY);
            sb.append(LINEND);
            sb.append("Content-Disposition: form-data; name=\"" + entry.getKey() + "\"" + LINEND);
            sb.append("Content-Type: text/plain; charset=" + CHARSET + LINEND);
            sb.append("Content-Transfer-Encoding: 8bit" + LINEND);
            sb.append(LINEND);
            sb.append(entry.getValue());
            sb.append(LINEND);
        }


        DataOutputStream outStream = new DataOutputStream(conn.getOutputStream());
        outStream.write(sb.toString().getBytes());
        // 发送文件数据
        if (files != null)
            for (Map.Entry<String, File> file : files.entrySet()) {
                StringBuilder sb1 = new StringBuilder();
                sb1.append(PREFIX);
                sb1.append(BOUNDARY);
                sb1.append(LINEND);
                //name是post中传参的键 filename是文件的名称
                sb1.append("Content-Disposition: form-data; name=\"" + file.getKey() + "\"; filename=\""
                        + file.getValue().getName() + "\"" + LINEND);
                sb1.append("Content-Type: text/plain; charset=" + CHARSET + LINEND);
                sb1.append(LINEND);
                outStream.write(sb1.toString().getBytes());

                InputStream is = new FileInputStream(file.getValue());
                byte[] buffer = new byte[1024];
                int len = 0;
                while ((len = is.read(buffer)) != -1) {
                    outStream.write(buffer, 0, len);
                }
                is.close();
                outStream.write(LINEND.getBytes());
            }

        // 请求结束标志
        byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINEND).getBytes();
        outStream.write(end_data);
        outStream.flush();
        // 得到响应码
        int res = conn.getResponseCode();
        InputStream in = conn.getInputStream();
        StringBuilder sb2 = new StringBuilder();
        if (res == 200) {
            int ch;
            while ((ch = in.read()) != -1) {
                sb2.append((char) ch);
            }
        }
        outStream.close();
        conn.disconnect();
        return sb2.toString();
    }

    /**
     * @param url
     * @param params
     * @param files  文件列表
     * @return
     * @throws IOException
     */
    public static String postFiles(String url, Map<String, String> params, Map<String, File[]> files)
            throws IOException {
        String BOUNDARY = UUID.randomUUID().toString();
        String PREFIX = "--", LINEND = "\r\n";
        String MULTIPART_FROM_DATA = "multipart/form-data";
        String CHARSET = "UTF-8";


        URL uri = new URL(url);
        HttpURLConnection conn = (HttpURLConnection) uri.openConnection();
        conn.setReadTimeout(10 * 1000); // 缓存的最长时间
        conn.setDoInput(true);// 允许输入
        conn.setDoOutput(true);// 允许输出
        conn.setUseCaches(false); // 不允许使用缓存
        conn.setRequestMethod("POST");
        conn.setRequestProperty("connection", "keep-alive");
        conn.setRequestProperty("Charsert", "UTF-8");
        conn.setRequestProperty("Content-Type", MULTIPART_FROM_DATA + ";boundary=" + BOUNDARY);


        // 首先组拼文本类型的参数
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            sb.append(PREFIX);
            sb.append(BOUNDARY);
            sb.append(LINEND);
            sb.append("Content-Disposition: form-data; name=\"" + entry.getKey() + "\"" + LINEND);
            sb.append("Content-Type: text/plain; charset=" + CHARSET + LINEND);
            sb.append("Content-Transfer-Encoding: 8bit" + LINEND);
            sb.append(LINEND);
            sb.append(entry.getValue());
            sb.append(LINEND);
        }


        DataOutputStream outStream = new DataOutputStream(conn.getOutputStream());
        outStream.write(sb.toString().getBytes());
        // 发送文件数据
        if (files.size() > 0)
            for (Map.Entry<String, File[]> file : files.entrySet()) {
                for (int i = 0; i < file.getValue().length; i++) {
                    if (file.getValue()[i] != null) {
                        StringBuilder sb1 = new StringBuilder();
                        sb1.append(PREFIX);
                        sb1.append(BOUNDARY);
                        sb1.append(LINEND);
                        //name是post中传参的键 filename是文件的名称
                        sb1.append("Content-Disposition: form-data; name=\"" + file.getKey() + "\"; filename=\""
                                + file.getValue()[i].getName() + "\"" + LINEND);
                        sb1.append("Content-Type: text/plain; charset=" + CHARSET + LINEND);
                        sb1.append(LINEND);
                        outStream.write(sb1.toString().getBytes());


                        InputStream is = new FileInputStream(file.getValue()[i]);
                        byte[] buffer = new byte[1024];
                        int len = 0;
                        while ((len = is.read(buffer)) != -1) {
                            outStream.write(buffer, 0, len);
                        }
                        is.close();
                        outStream.write(LINEND.getBytes());
                    }
                }

            }

        // 请求结束标志
        byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINEND).getBytes();
        outStream.write(end_data);
        outStream.flush();
        // 得到响应码
        int res = conn.getResponseCode();
        InputStream in = conn.getInputStream();
        StringBuilder sb2 = new StringBuilder();
        if (res == 200) {
            int ch;
            while ((ch = in.read()) != -1) {
                sb2.append((char) ch);
            }
        }
        outStream.close();
        conn.disconnect();
        return sb2.toString();
    }

}
