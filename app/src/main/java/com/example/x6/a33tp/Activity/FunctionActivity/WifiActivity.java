package com.example.x6.a33tp.Activity.FunctionActivity;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.x6.a33tp.Activity.Y7orY8orY10Activity;
import com.example.x6.a33tp.BroadcastReceive.WifiConnectChangeReceiver;
import com.example.x6.a33tp.Date.NetInfoManager;
import com.example.x6.a33tp.Date.ResultDate;
import com.example.x6.a33tp.Date.StepCode;
import com.example.x6.a33tp.Function.WifiControl;
import com.example.x6.a33tp.R;

import java.io.IOException;

/**
 * Created by 24179 on 2018/5/7.
 */

public class WifiActivity extends Activity {
    private TextView dispWifi;
    private Button wifiyes,wifino,withoutWifi,nextTest;
    private StepCode stepCode;
    private ResultDate resultDate;
    private String WifiInfo = null;


    WifiControl wifiControl;

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    dispWifi.append(wifiControl.getMsg(msg)+"\n");
                    break;
                case 1:
                    dispWifi.append(wifiControl.getMsg(msg)+"\n");
                    dispWifi.setTextColor(Color.BLUE);
                    wifiyes.setEnabled(true);
                    wifino.setEnabled(false);
                    withoutWifi.setEnabled(false);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wifi);
        dispWifi = (TextView) findViewById(R.id.dispWifi);
        initView();
        wifiyes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WifiInfo = ResultDate.PASS;
                nextTest.setEnabled(true);
                wifino.setEnabled(false);
                wifiyes.setEnabled(false);
                withoutWifi.setEnabled(false);
            }
        });
        wifino.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WifiInfo = ResultDate.UNPASS;
                nextTest.setEnabled(true);
                wifino.setEnabled(false);
                wifiyes.setEnabled(false);
                withoutWifi.setEnabled(false);
            }
        });
        withoutWifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WifiInfo = ResultDate.UNSUPPORT;
                nextTest.setEnabled(true);
                wifino.setEnabled(false);
                wifiyes.setEnabled(false);
                withoutWifi.setEnabled(false);
            }
        });
        nextTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wifiControl.unregisterNetworkConnectChangeReceiver(WifiActivity.this.getApplicationContext());
                wifiControl.WifiClose();
                try {
                    resultDate.WriteResultDate(ResultDate.WIFI,WifiInfo,null);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                stepCode = StepCode.getStepCodeClass(WifiActivity.this.getApplicationContext());
                stepCode.jumpActivity(StepCode.STEP_4G,WifiActivity.this.getApplicationContext(), Y7orY8orY10Activity.class);
                WifiActivity.this.finish();
            }
        });

        wifiControl = new WifiControl(WifiActivity.this.getApplicationContext());
        wifiControl.registerNetworkConnectChangeReceiver(WifiActivity.this.getApplicationContext(),handler);
        connectWifi();
    }

    private void initView() {
        wifiyes = (Button) findViewById(R.id.wifiyes);
        wifino = (Button) findViewById(R.id.wifino);
        withoutWifi = (Button) findViewById(R.id.withoutWifi);
        nextTest = (Button) findViewById(R.id.nextTest);

        wifiyes.setEnabled(false);
        nextTest.setEnabled(false);
        resultDate = ResultDate.getResultDate(this.getApplicationContext());
    }

    private void connectWifi(){
        new Thread(){
            @Override
            public void run() {
                Looper.prepare();
                wifiControl.WifiOpen();
                while (!wifiControl.isEnable()){
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                wifiControl.WifiConnect(NetInfoManager.wifiSSid,NetInfoManager.wifiPasswork, WifiControl.WifiCipherType.WIFICIPHER_WPA);
//                wifiControl.WifiConnect("ChinaNet-YYtA","12345hrs", WifiControl.WifiCipherType.WIFICIPHER_WPA);
                handler.sendEmptyMessage(0);
            }
        }.start();
    }
}
