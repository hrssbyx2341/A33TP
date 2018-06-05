package com.example.x6.a33tp.Activity.FunctionActivity.SerialActivity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.x6.a33tp.Activity.Y7orY8orY10Activity;
import com.example.x6.a33tp.Date.ResultDate;
import com.example.x6.a33tp.Date.StepCode;
import com.example.x6.a33tp.Function.SerialControl;
import com.example.x6.a33tp.R;
import com.example.x6.serial.SerialPort;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by 24179 on 2018/5/19.
 */

public class Serial485Test extends Activity {
    private final String TAG = "MainActivity";

    private Button yes,no,nextTest,reTest;
    private boolean isPass = false;
    private ResultDate resultDate;
    private StepCode stepCode;
    private SerialControl serialControl;
    private TextView display;
    private int serialSendPack3 = 0, serialRecvPack3 = 0,serialRecvWPack3 = 0;

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle bundle = msg.getData();
            switch (msg.what){
                case 3:
                    syncUI(3,bundle,display);
                    break;
            }
        }
    };

    private void syncUI(int port, Bundle bundle,TextView textView){
        switch (serialControl.getMsgFlag(bundle)){
            case SerialControl.SEND_START:
                textView.setText("串口"+String.valueOf(port)+"开始\n" +
                        "串口"+String.valueOf(port)+"发送"+String.valueOf(serialSendPack3)+"帧数据\n"+
                        "串口"+String.valueOf(port)+"接收"+String.valueOf(serialRecvPack3)+"帧正确数据\n"+
                        "串口"+String.valueOf(port)+"接收"+String.valueOf(serialRecvWPack3)+"帧错误数据\n");
                break;
            case SerialControl.SEND_ONE_FPS:
                serialSendPack3++;
                textView.setText("串口"+String.valueOf(port)+"开始\n" +
                        "串口"+String.valueOf(port)+"发送"+String.valueOf(serialSendPack3)+"帧数据\n"+
                        "串口"+String.valueOf(port)+"接收"+String.valueOf(serialRecvPack3)+"帧正确数据\n"+
                        "串口"+String.valueOf(port)+"接收"+String.valueOf(serialRecvWPack3)+"帧错误数据\n");
                break;
            case SerialControl.RECV_ONE_FPS:
                serialRecvPack3++;
                textView.setText("串口"+String.valueOf(port)+"开始\n" +
                        "串口"+String.valueOf(port)+"发送"+String.valueOf(serialSendPack3)+"帧数据\n"+
                        "串口"+String.valueOf(port)+"接收"+String.valueOf(serialRecvPack3)+"帧正确数据\n"+
                        "串口"+String.valueOf(port)+"接收"+String.valueOf(serialRecvWPack3)+"帧错误数据\n");
                break;
            case SerialControl.RECV_WRONG_FPS:
                serialRecvWPack3++;
                textView.setText("串口"+String.valueOf(port)+"开始\n" +
                        "串口"+String.valueOf(port)+"发送"+String.valueOf(serialSendPack3)+"帧数据\n"+
                        "串口"+String.valueOf(port)+"接收"+String.valueOf(serialRecvPack3)+"帧正确数据\n"+
                        "串口"+String.valueOf(port)+"接收"+String.valueOf(serialRecvWPack3)+"帧错误数据\n");
                break;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.serial485);
        yes = (Button) findViewById(R.id.recoyes);
        no = (Button) findViewById(R.id.recono);
        nextTest = (Button) findViewById(R.id.nextTest);
        reTest = (Button) findViewById(R.id.reTest);
        display = (TextView) findViewById(R.id.display);
        nextTest.setEnabled(false);
        reTest.setEnabled(false);
        resultDate = ResultDate.getResultDate(this.getApplicationContext());
        stepCode = StepCode.getStepCodeClass(this.getApplicationContext());
        serialControl = new SerialControl(handler);

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isPass = true;
                yes.setEnabled(false);
                no.setEnabled(false);
                reTest.setEnabled(true);
                nextTest.setEnabled(true);
            }
        });

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isPass = false;
                yes.setEnabled(false);
                no.setEnabled(false);
                nextTest.setEnabled(true);
                reTest.setEnabled(true);
            }
        });

        reTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                yes.setEnabled(true);
                no.setEnabled(true);
                nextTest.setEnabled(false);
                reTest.setEnabled(false);
            }
        });

        nextTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                serialControl.stopSerialPort3();
                try {
                    if(isPass)
                        resultDate.WriteResultDate(ResultDate.UART485,ResultDate.PASS,null);
                    else
                        resultDate.WriteResultDate(ResultDate.UART485,ResultDate.UNPASS,null);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                stepCode.jumpActivity(StepCode.STEP_GPIO,Serial485Test.this, Y7orY8orY10Activity.class);
                Serial485Test.this.finish();
            }
        });

        serialControl.startSerialPort3();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        serialControl.stopSerialPort3();
    }
}
