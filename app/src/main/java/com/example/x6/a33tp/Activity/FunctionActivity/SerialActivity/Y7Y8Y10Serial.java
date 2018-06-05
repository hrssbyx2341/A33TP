package com.example.x6.a33tp.Activity.FunctionActivity.SerialActivity;

import android.app.Activity;
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
import com.example.x6.a33tp.Function.SerialControl;
import com.example.x6.a33tp.R;

import java.io.IOException;

/**
 * Created by 24179 on 2018/5/8.
 */

public class Y7Y8Y10Serial extends Activity {
    private TextView serial1,serial2,serial3,serial0,serial0_1,serial1_1,serial2_1,serial3_1;
    private Button nextTest,Serial3232no,Serial3485no;
    private StepCode stepCode;
    private ResultDate resultDate;
    private SerialControl serialControl;
    private int serialSendPack0 = 0,serialSendPack1 = 0,serialSendPack2 = 0,serialSendPack3 = 0;
    private int serialRecvPack0 = 0,serialRecvPack1 = 0,serialRecvPack2 = 0,serialRecvPack3 = 0;
    private int serialRecvWPack0 = 0,serialRecvWPack1 = 0,serialRecvWPack2 = 0,serialRecvWPack3 = 0;

    private boolean isSerialPass = true;

    private boolean isSerialTested1 = false,isSerialTested2 = false,isSerialTested0 = false;
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle bundle = msg.getData();
            switch (msg.what){
                case 0:
                    syncUI(0,bundle,serial0,serial0_1);
                    break;
                case 1:
                    syncUI(1,bundle,serial1,serial1_1);
                    break;
                case 2:
                    syncUI(2,bundle,serial2,serial2_1);
                    break;
                case 3:
                    syncUI(3,bundle,serial3,serial3_1);
                    break;
            }
        }
    };

    private int getSerialSendPack(int i){
        switch (i){
            case 0:
                return serialSendPack0;
            case 1:
                return serialSendPack1;
            case 2:
                return serialSendPack2;
            case 3:
                return serialSendPack3;
            default:
                return 0;
        }
    }

    private int getSerialRecvPack(int i){
        switch (i){
            case 0:
                return serialRecvPack0;
            case 1:
                return serialRecvPack1;
            case 2:
                return serialRecvPack2;
            case 3:
                return serialRecvPack3;
            default:
                return 0;
        }
    }

    private int getSerialRecvWPack(int i){
        switch (i){
            case 0:
                return serialRecvWPack0;
            case 1:
                return serialRecvWPack1;
            case 2:
                return serialRecvWPack2;
            case 3:
                return serialRecvWPack3;
            default:
                return 0;
        }
    }
    private void isSerialTested(int i,boolean tempbl){
        switch (i){
            case 0:
                isSerialTested0 = tempbl;
                break;
            case 1:
                isSerialTested1 = tempbl;
                break;
            case 2:
                isSerialTested2 = tempbl;
                break;
            case 3:
                break;
        }
    }
    private void syncNextTest(){
        if(isSerialTested0 && isSerialTested1 && isSerialTested2 )
            nextTest.setEnabled(true);
    }

    private void syncUI(int port, Bundle bundle,TextView textView,TextView textView1){
        switch (serialControl.getMsgFlag(bundle)){
            case SerialControl.SEND_START:
                textView.setText("串口"+String.valueOf(port)+"开始\n" +
                        "串口"+String.valueOf(port)+"发送"+String.valueOf(getSerialSendPack(port))+"帧数据\n"+
                        "串口"+String.valueOf(port)+"接收"+String.valueOf(getSerialRecvPack(port))+"帧正确数据\n"+
                        "串口"+String.valueOf(port)+"接收"+String.valueOf(getSerialRecvWPack(port))+"帧错误数据\n");
                break;
            case SerialControl.SEND_ONE_FPS:
                switch (port){
                    case 0:
                         serialSendPack0++;
                        break;
                    case 1:
                        serialSendPack1++;
                        break;
                    case 2:
                        serialSendPack2++;
                        break;
                    case 3:
                        serialSendPack3++;
                        break;
                }
                textView.setText("串口"+String.valueOf(port)+"开始\n" +
                        "串口"+String.valueOf(port)+"发送"+String.valueOf(getSerialSendPack(port))+"帧数据\n"+
                        "串口"+String.valueOf(port)+"接收"+String.valueOf(getSerialRecvPack(port))+"帧正确数据\n"+
                        "串口"+String.valueOf(port)+"接收"+String.valueOf(getSerialRecvWPack(port))+"帧错误数据\n");
                break;
            case SerialControl.RECV_ONE_FPS:
                switch (port){
                    case 0:
                        serialRecvPack0++;
                        break;
                    case 1:
                        serialRecvPack1++;
                        break;
                    case 2:
                        serialRecvPack2++;
                        break;
                    case 3:
                        serialRecvPack3++;
                        break;
                }
                textView.setText("串口"+String.valueOf(port)+"开始\n" +
                        "串口"+String.valueOf(port)+"发送"+String.valueOf(getSerialSendPack(port))+"帧数据\n"+
                        "串口"+String.valueOf(port)+"接收"+String.valueOf(getSerialRecvPack(port))+"帧正确数据\n"+
                        "串口"+String.valueOf(port)+"接收"+String.valueOf(getSerialRecvWPack(port))+"帧错误数据\n");
                break;
            case SerialControl.RECV_WRONG_FPS:
                switch (port){
                    case 0:
                        serialRecvWPack0++;
                        break;
                    case 1:
                        serialRecvWPack1++;
                        break;
                    case 2:
                        serialRecvWPack2++;
                        break;
                    case 3:
                        serialRecvWPack3++;
                        break;
                }
                textView.setText("串口"+String.valueOf(port)+"开始\n" +
                        "串口"+String.valueOf(port)+"发送"+String.valueOf(getSerialSendPack(port))+"帧数据\n"+
                        "串口"+String.valueOf(port)+"接收"+String.valueOf(getSerialRecvPack(port))+"帧正确数据\n"+
                        "串口"+String.valueOf(port)+"接收"+String.valueOf(getSerialRecvWPack(port))+"帧错误数据\n");
                break;
            case SerialControl.SEND_END:
                textView1.append("串口"+String.valueOf(port)+"发送数据结束\n");
                break;
            case SerialControl.RECV_END:
                textView1.append("串口"+String.valueOf(port)+"测试结束");
                if((float)getSerialRecvPack(port)/(float)getSerialSendPack(port) >= 0.15){
                    textView1.append("\n丢包率为 "+String.valueOf((1 - (float)getSerialRecvPack(port)/(float)getSerialSendPack(port))*100)
                            +"% \n错误率为 "+String.valueOf(((float)getSerialRecvWPack(port)/(float)getSerialSendPack(port))*100)+"%\n测试通过");
                    textView.setTextColor(Color.BLUE);
                    textView1.setTextColor(Color.BLUE);
                    isSerialTested(port,true);
                    syncNextTest();
                } else {
                    textView1.append("\n丢包率为 "+String.valueOf((1 - (float)getSerialRecvPack(port)/(float)getSerialSendPack(port))*100)
                            +"% \n错误率为 "+String.valueOf(((float)getSerialRecvWPack(port)/(float)getSerialSendPack(port))*100)+"%\n测试不通过");
                    textView.setTextColor(Color.RED);
                    textView1.setTextColor(Color.RED);
                    isSerialTested(port,true);
                    syncNextTest();
                    isSerialPass = false;
                }
                break;
        }
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.serial);
        initView();
        serialControl = new SerialControl(handler);
        serialControl.startSerialPort0();
        serialControl.startSerialPort1();
        serialControl.startSerialPort2();
        serialControl.startSerialPort3();

        Serial3232no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isSerialPass = false;
            }
        });

        Serial3485no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isSerialPass = false;
            }
        });

        nextTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                serialControl.stopSerialPort3();
                try {
                    if(isSerialPass)
                        resultDate.WriteResultDate(ResultDate.UART,ResultDate.PASS,null);
                    else
                        resultDate.WriteResultDate(ResultDate.UART,ResultDate.UNPASS,null);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                stepCode.jumpActivity(StepCode.STEP_GPIO,Y7Y8Y10Serial.this, Y7orY8orY10Activity.class);
                Y7Y8Y10Serial.this.finish();
            }
        });
    }

    private void initView() {
        serial0 = (TextView) findViewById(R.id.serial0);
        serial1 = (TextView) findViewById(R.id.serial1);
        serial2 = (TextView) findViewById(R.id.serial2);
        serial3 = (TextView) findViewById(R.id.serial3);
        serial0_1 = (TextView) findViewById(R.id.serial0_1);
        serial1_1 = (TextView) findViewById(R.id.serial1_1);
        serial2_1 = (TextView) findViewById(R.id.serial2_1);
        serial3_1 = (TextView) findViewById(R.id.serial3_1);
        nextTest = (Button) findViewById(R.id.nextTest);
        Serial3232no = (Button) findViewById(R.id.serial3_pass);
        Serial3485no = (Button) findViewById(R.id.serial3_unpass);


        nextTest.setEnabled(false);

        serial3_1.setText("此串口为232,485复用串口，会一直发送和接收数据。测试485需要接母板\n当发送帧数据和接收正确帧数据都在上涨的时候，表示串口测试通过");
        serial3_1.setTextColor(Color.BLACK);
        stepCode = StepCode.getStepCodeClass(Y7Y8Y10Serial.this.getApplicationContext());
        resultDate = ResultDate.getResultDate(this.getApplicationContext());
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        serialControl.stopSerialPort3();
    }
}
