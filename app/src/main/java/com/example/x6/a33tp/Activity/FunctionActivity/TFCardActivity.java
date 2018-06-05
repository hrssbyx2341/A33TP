package com.example.x6.a33tp.Activity.FunctionActivity;

import android.app.Activity;
import android.graphics.Color;
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
import com.example.x6.a33tp.Date.TypeManager;
import com.example.x6.a33tp.Function.StartApk;
import com.example.x6.a33tp.Function.SuCommand;
import com.example.x6.a33tp.R;

import java.io.IOException;

/**
 * Created by 24179 on 2018/5/5.
 */

public class TFCardActivity extends Activity {
    private final String cmd = "ls /dev/block/mmcblk1p1";
    private Button tfYes,tfNo,reTf,nextTest;
    private TextView displayTf;

    private ResultDate resultDate;
    private StepCode stepCode;
    private SuCommand suCommand;
    boolean isCheckPool = true;

    boolean isTFCardPass = false;


    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    displayTf.setText("检测到内存卡");
                    displayTf.setTextColor(Color.RED);
                    break;
                default:
                    displayTf.setText("没有检测到内存卡");
                    displayTf.setTextColor(Color.BLUE);
                    break;
            }
        }
    };
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tfcard);
        initView();
        startCheck();

        tfYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isTFCardPass = true;
                tfYes.setEnabled(false);
                tfNo.setEnabled(false);
                nextTest.setEnabled(true);
                reTf.setEnabled(true);
            }
        });
        tfNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isTFCardPass = false;
                tfYes.setEnabled(false);
                tfNo.setEnabled(false);
                nextTest.setEnabled(true);
                reTf.setEnabled(true);
            }
        });
        reTf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reTf.setEnabled(false);
                tfYes.setEnabled(true);
                tfNo.setEnabled(true);
                nextTest.setEnabled(false);
                isCheckPool = true;
                startCheck();
            }
        });
        nextTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isCheckPool = false;
                try {
                    if(isTFCardPass)
                        resultDate.WriteResultDate(ResultDate.TFCARD,ResultDate.PASS,null);
                    else
                        resultDate.WriteResultDate(ResultDate.TFCARD,ResultDate.UNPASS,null);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                stepCode.jumpActivity(StepCode.STEP_USB,TFCardActivity.this,Y7orY8orY10Activity.class);
                TFCardActivity.this.finish();
            }
        });

    }

    private void initView() {
        tfYes = (Button) findViewById(R.id.tfyes);
        tfNo = (Button) findViewById(R.id.tfno);
        reTf = (Button) findViewById(R.id.reTf);
        nextTest = (Button) findViewById(R.id.nextTest);
        displayTf = (TextView) findViewById(R.id.tfDisplay);
        nextTest.setEnabled(false);
        reTf.setEnabled(false);
        resultDate = ResultDate.getResultDate(this.getApplicationContext());
        stepCode = StepCode.getStepCodeClass(this.getApplicationContext());
        suCommand = new SuCommand();
    }


    private void startCheck(){
        new Thread(){
            @Override
            public void run() {
                Looper.prepare();
                while (isCheckPool) {
                    handler.sendEmptyMessage(suCommand.execRootCmdSilent(cmd));
                    try {
                        Thread.sleep(1*1000);
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
        isCheckPool = false;
    }
}
