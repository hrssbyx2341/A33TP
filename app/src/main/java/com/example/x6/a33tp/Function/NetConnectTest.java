package com.example.x6.a33tp.Function;

import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by 24179 on 2018/5/5.
 */

public class NetConnectTest {
    public static final int NO_LINK = 8866;
    public static final int LINK_OK = 6668;
    public static int isNetworkAvailable(Context context){
        URL url;
        HttpURLConnection conn = null;
        try {
            url = new URL("http://www.baidu.com");//你的服务器IP
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return NO_LINK;
        }
        try {
            conn = (HttpURLConnection)url.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
            return NO_LINK;
        }
        conn.setConnectTimeout(1000*5);
        conn.setReadTimeout(5*1000);
        try {
            int temp = conn.getResponseCode();
            if(temp==200 || temp == 302){
                Log.e("网络情况","网络可用");
            }else{
                Log.e("网络情况","网络不可用"+String.valueOf(temp));
            }
        } catch (IOException e) {
            e.printStackTrace();
            return NO_LINK;
        }
        return LINK_OK;
    }
}
