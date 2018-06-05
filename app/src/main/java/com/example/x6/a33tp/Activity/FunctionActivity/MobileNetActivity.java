package com.example.x6.a33tp.Activity.FunctionActivity;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.x6.a33tp.Activity.Y7orY8orY10Activity;
import com.example.x6.a33tp.Date.ResultDate;
import com.example.x6.a33tp.Date.StepCode;
import com.example.x6.a33tp.Function.Check4gNet;
import com.example.x6.a33tp.Function.MobileNetControl;
import com.example.x6.a33tp.R;

import java.io.IOException;

/**
 * Created by 24179 on 2018/5/7.
 */

public class MobileNetActivity extends Activity {
    private TextView netLongStat;
    private Button mobYes, mobNo, withoutMob,nextTest;
    private boolean isCheckMobileNet = true;
    private Context context;
    private MobileNetControl mobileNetControl;
    private ResultDate resultDate;
    private StepCode stepCode;
    private String MobNetInfo = null;

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case Check4gNet.ETH_NET:
                    netLongStat.setText("当前是4G网络测试，请关闭以太网");
                    netLongStat.setTextColor(Color.RED);
                    break;
                case Check4gNet.MOBIL_NET:
                    netLongStat.setText("当前网络----4G, 正常");
                    MobNetInfo = ResultDate.PASS;
                    mobYes.setEnabled(true);
                    mobNo.setEnabled(false);
                    withoutMob.setEnabled(false);
                    netLongStat.setTextColor(Color.GREEN);
                    break;
                case Check4gNet.NO_LINK:
                    netLongStat.setText("当前无网络连接");
                    netLongStat.setTextColor(Color.RED);
                    break;
                case Check4gNet.WIFI_NET:
                    netLongStat.setText("当前是4G网络测试，请关闭WIFI");
                    netLongStat.setTextColor(Color.RED);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mobile_net);
        netLongStat = (TextView) findViewById(R.id.dispMobileNet);
        initView();
        mobYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MobNetInfo = ResultDate.PASS;
                mobNo.setEnabled(false);
                mobYes.setEnabled(false);
                withoutMob.setEnabled(false);
                nextTest.setEnabled(true);
            }
        });
        mobNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MobNetInfo = ResultDate.UNPASS;
                mobNo.setEnabled(false);
                mobYes.setEnabled(false);
                withoutMob.setEnabled(true);
                nextTest.setEnabled(true);
            }
        });
        withoutMob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MobNetInfo = ResultDate.UNSUPPORT;
                mobNo.setEnabled(true);
                mobYes.setEnabled(false);
                withoutMob.setEnabled(false);
                nextTest.setEnabled(true);
            }
        });
        nextTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isCheckMobileNet = false;
                try {
                    resultDate.WriteResultDate(ResultDate.MOBILENET,MobNetInfo,null);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                stepCode.jumpActivity(StepCode.STEP_BLUETOOTH,MobileNetActivity.this.getApplicationContext(), Y7orY8orY10Activity.class);
                MobileNetActivity.this.finish();
            }
        });
        this.context = MobileNetActivity.this.getApplicationContext();
        mobileNetControl = new MobileNetControl();
        check4G();
    }

    private void initView() {
        mobNo = (Button) findViewById(R.id.mobuleNetno);
        mobYes = (Button) findViewById(R.id.mobuleNetyes);
        nextTest = (Button) findViewById(R.id.nextTest);
        withoutMob = (Button) findViewById(R.id.withoutMobileNet);

        nextTest.setEnabled(false);
        mobYes.setEnabled(false);
        resultDate = ResultDate.getResultDate(this.getApplicationContext());
        stepCode = StepCode.getStepCodeClass(MobileNetActivity.this.getApplicationContext());
    }

    private void check4G(){
        new Thread(){
            @Override
            public void run() {
                super.run();
                mobileNetControl.openMobileNet();
                while (isCheckMobileNet){
                    handler.sendEmptyMessage(Check4gNet.isNetworkAvailable(context));
                    try {
//                        Thread.sleep(10*1000);
                        Thread.sleep(1*1500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isCheckMobileNet = false;
    }
}
