package com.example.x6.a33tp.Activity.FunctionActivity;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.x6.a33tp.Activity.Y7orY8orY10Activity;
import com.example.x6.a33tp.Date.ResultDate;
import com.example.x6.a33tp.Date.StepCode;
import com.example.x6.a33tp.R;

import java.io.IOException;

/**
 * Created by Administrator on 2018/5/8.
 */

public class BlueToothActivity extends Activity{
    private TextView dispWifi;
    private Button wifiyes,wifino,withoutWifi,nextTest;
    private StepCode stepCode;
    private ResultDate resultDate;
    private BluetoothAdapter mBluetoothAdapter;
    private Boolean isCheckBlt = true;
    private String BLTInfo = null;

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    dispWifi.setText("正在启动蓝牙...");
                    break;
                case 1:
                    dispWifi.setText("蓝牙启动超时");
                    BLTInfo = ResultDate.UNPASS;
                    wifino.setEnabled(true);
                    withoutWifi.setEnabled(true);
                    wifiyes.setEnabled(false);
                    break;
                case 2:
                    dispWifi.setText("蓝牙启动成功");
                    BLTInfo = ResultDate.PASS;
                    wifino.setEnabled(false);
                    withoutWifi.setEnabled(false);
                    wifiyes.setEnabled(true);
                    break;
            }
        }
    };
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bluetooth);
        dispWifi = (TextView) findViewById(R.id.dispWifi);
        initView();
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter(); //获取蓝牙适配器实例
        BlueToothCheck();
        wifiyes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BLTInfo = ResultDate.PASS;
                dispWifi.setText("蓝牙功能正常");
                isCheckBlt = false;
                nextTest.setEnabled(true);
                wifino.setEnabled(false);
                wifiyes.setEnabled(false);
                withoutWifi.setEnabled(false);
            }
        });
        wifino.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BLTInfo = ResultDate.UNPASS;
                dispWifi.setText("蓝牙功能有故障");
                isCheckBlt = false;
                nextTest.setEnabled(true);
                wifino.setEnabled(false);
                wifiyes.setEnabled(false);
                withoutWifi.setEnabled(true);
            }
        });
        withoutWifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BLTInfo = ResultDate.UNSUPPORT;
                dispWifi.setText("此设备不支持蓝牙");
                isCheckBlt = false;
                nextTest.setEnabled(true);
                wifino.setEnabled(true);
                wifiyes.setEnabled(false);
                withoutWifi.setEnabled(false);
            }
        });
        nextTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBluetoothAdapter.disable();//关闭蓝牙
                isCheckBlt = false;
                try {
                    resultDate.WriteResultDate(ResultDate.BLUETOOTH,BLTInfo,null);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                stepCode = StepCode.getStepCodeClass(BlueToothActivity.this.getApplicationContext());
                stepCode.jumpActivity(StepCode.STEP_TOUCH_DISP,BlueToothActivity.this.getApplicationContext(), Y7orY8orY10Activity.class);
                BlueToothActivity.this.finish();
            }
        });

    }

    public void BlueToothCheck(){
        new Thread(){
            @Override
            public void run() {
                super.run();
                Looper.prepare();
                long lastTime = System.currentTimeMillis();
                handler.sendEmptyMessage(0); //启动蓝牙
                if (!mBluetoothAdapter.isEnabled()) {
                     mBluetoothAdapter.enable(); //打开蓝牙
                }
                while (!mBluetoothAdapter.isEnabled() && isCheckBlt){
                    if(System.currentTimeMillis() - lastTime >= 8*1000){
                        handler.sendEmptyMessage(1);//打开蓝牙超时
                    }
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                if(mBluetoothAdapter.isEnabled())
                    handler.sendEmptyMessage(2); //打开蓝牙成功
            }
        }.start();
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBluetoothAdapter.disable();//关闭蓝牙
        isCheckBlt = false;
    }
}
