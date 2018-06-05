package com.example.x6.a33tp.Function;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.StrictMode;
import android.util.Log;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by 24179 on 2018/4/27.
 */

public class Check4gNet {
    public static final int MOBIL_NET = 8888;
    public static final int WIFI_NET = 6666;
    public static final int ETH_NET = 6688;
    public static final int NO_LINK = 8866;
    private static int get_net_state(Context context){
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo dataNetworkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if(dataNetworkInfo.isConnected()){
            return MOBIL_NET;
        }else{
            NetworkInfo wifiNetworkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (wifiNetworkInfo.isConnected()){
                return WIFI_NET;
            }else{
                try {
                    NetworkInfo netNetworkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_ETHERNET);
                    if(netNetworkInfo.isConnected()){
                        return ETH_NET;
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
        return NO_LINK;
    }
    public static int isNetworkAvailable(Context context)
    {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null)
        {
            return NO_LINK;
        }
        else
        {
            NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();

            if (networkInfo != null && networkInfo.length > 0)
            {
                for (int i = 0; i < networkInfo.length; i++)
                {
                    if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED)
                    {
                        Log.e("网络情况","网络可用");
                        return get_net_state(context);
                    }
                }
            }
        }
        Log.e("网络情况","网络不可用");
        return NO_LINK;
    }

//    public static int isNetworkAvailable(Context context){
//        URL url;
//        HttpURLConnection conn = null;
//
//        try {
//            url = new URL("http://www.baidu.com");//你的服务器IP
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//            return NO_LINK;
//        }
//        try {
//            conn = (HttpURLConnection)url.openConnection();
//        } catch (IOException e) {
//            e.printStackTrace();
//            return NO_LINK;
//        }
//        conn.setConnectTimeout(1000*5);
//        conn.setReadTimeout(5*1000);
//        try {
//            int temp = conn.getResponseCode();
//            if(temp==200 || temp == 302){
//                Log.e("网络情况","网络可用");
//            }else{
//                Log.e("网络情况","网络不可用"+String.valueOf(temp));
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//            return NO_LINK;
//        }
//        return get_net_state(context);
//    }

    public static void setStrictMode() {
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectAll()
                .penaltyLog()
                .build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                .detectLeakedSqlLiteObjects()
                .detectLeakedClosableObjects()
                .penaltyLog()
                .penaltyDeath()
                .build());

    }
}
