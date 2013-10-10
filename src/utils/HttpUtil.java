package utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author haopengujn@gmail.com
 */
public class HttpUtil {

    public static String httpPost(String urlStr, String params, String charSet) {
        HttpURLConnection httpConn=null;
        try {
            byte[] data=params.getBytes(charSet);
            URL url=new URL(urlStr);
            httpConn=(HttpURLConnection)url.openConnection();
            httpConn.setRequestMethod("POST");
            httpConn.setRequestProperty("ContentType", "application/x-www-form-urlencoded");
            httpConn.setRequestProperty("Content-Length", String.valueOf(data.length));
            httpConn.setDoInput(true);
            httpConn.setDoOutput(true);
            // System.setProperty("sun.net.client.defaultConnectTimeout", "30000");//jdk1.4换成这个,连接超时
            // System.setProperty("sun.net.client.defaultReadTimeout", "30000"); //jdk1.4换成这个,读操作超�?
            httpConn.setConnectTimeout(30000);// jdk 1.5换成这个,连接超时
            httpConn.setReadTimeout(30000);// jdk 1.5换成这个,读操作超�?
            httpConn.connect();
            OutputStream os1=httpConn.getOutputStream();
            os1.write(data);
            os1.flush();
            os1.close();
            return getResponseResult(httpConn, urlStr, charSet);
        } catch(Exception ex) {
            ex.printStackTrace();
        } finally {
            if(null != httpConn) {
                httpConn.disconnect();
            }
        }
        return null;
    }

    public static String httpGet(String urlStr, String params, String charSet) {
        HttpURLConnection httpConn=null;
        try {
            if(null != params && params.length() > 0) {
                if(urlStr.indexOf("?") == -1) {
                    urlStr+="?" + params;
                } else {
                    urlStr+="&" + params;
                }
            }
            URL url=new URL(urlStr);
            httpConn=(HttpURLConnection)url.openConnection();
            httpConn.setRequestMethod("GET");
            httpConn.setRequestProperty("ContentType", "application/x-www-form-urlencoded");
            httpConn.setDoInput(true);
            httpConn.setDoOutput(true);
            // System.setProperty("sun.net.client.defaultConnectTimeout", "30000");//jdk1.4换成这个,连接超时
            // System.setProperty("sun.net.client.defaultReadTimeout", "30000"); //jdk1.4换成这个,读操作超�?
            httpConn.setConnectTimeout(30000);// jdk 1.5换成这个,连接超时
            httpConn.setReadTimeout(30000);// jdk 1.5换成这个,读操作超�?
            httpConn.connect();
            return getResponseResult(httpConn, urlStr, charSet);
        } catch(Exception ex) {
            ex.printStackTrace();
        } finally {
            if(null != httpConn) {
                httpConn.disconnect();
            }
        }
        return null;
    }

    private static String getResponseResult(HttpURLConnection httpConn, String urlStr, String charSet) throws IOException {
        String res=null;
        int responseCode=httpConn.getResponseCode();
        if(HttpURLConnection.HTTP_OK == responseCode) {
            byte[] buffer=new byte[1024];
            int len=-1;
            InputStream is=httpConn.getInputStream();
            ByteArrayOutputStream bos=new ByteArrayOutputStream();
            while((len=is.read(buffer)) != -1) {
                bos.write(buffer, 0, len);
            }
            res=bos.toString(charSet);
            is.close();
            bos.close();
            // logger.error(urlStr + " Response Code:" + responseCode + " content:" + res);
        }
        return res;
    }
}
