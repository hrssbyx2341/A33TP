package com.example.x6.a33tp.Function;

import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.example.x6.a33tp.BroadcastReceive.WifiConnectChangeReceiver;

import java.util.List;

/**
 * Created by 24179 on 2018/5/7.
 */

public class WifiControl {
    private final String TAG = "WifiControl";
    WifiManager wifiManager;
    Handler handler;
    WifiConnectChangeReceiver wifiConnectChangeReceiver;

    public String getMsg(Message message){
        Bundle bundle = message.getData();
        return bundle.getString(WifiConnectChangeReceiver.WIFI_INFO_KEY);
    }

    public void registerNetworkConnectChangeReceiver(Context context,Handler handler) {
        IntentFilter filter = new IntentFilter();
        filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        filter.addAction(WifiManager.SUPPLICANT_CONNECTION_CHANGE_ACTION);
        filter.addAction(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION);
        wifiConnectChangeReceiver = new WifiConnectChangeReceiver(handler);
        context.getApplicationContext().registerReceiver(wifiConnectChangeReceiver, filter);
    }
    public void unregisterNetworkConnectChangeReceiver(Context context){
        context.unregisterReceiver(wifiConnectChangeReceiver);
    }

    public enum WifiCipherType{
        WIFICIPHER_WEP,
        WIFICIPHER_WPA,
        WIFICIPHER_NOPASS,
        WIFICIPHER_INVALID
    }

    public WifiControl(Context context){
        wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
    }
    public boolean isEnable() {
        return wifiManager.isWifiEnabled();
    }
    // 打开wifi功能
    public boolean WifiOpen() {
        boolean bRet = true;
        if (!wifiManager.isWifiEnabled()) {
            bRet = wifiManager.setWifiEnabled(true);
        }
        return bRet;
    }

    public void WifiConnect(String SSID, String Password,WifiCipherType type){
        WifiOpen();
        WifiConfiguration config = new WifiConfiguration();
        config.SSID = "\""+SSID+"\"";
        if(Password.equals(""))
            config.preSharedKey = null;
        else
            config.preSharedKey = "\""+Password+"\"";
        config.hiddenSSID = true;
        config.status = WifiConfiguration.Status.ENABLED;
        config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
        config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
        config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
        config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
        if(Password.equals(""))
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
        else
            config.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
        int netid = wifiManager.addNetwork(config);
        boolean b = wifiManager.enableNetwork(netid,true);
    }

    public void WifiClose(){
        if(isEnable())
            wifiManager.setWifiEnabled(false);
    }

}
