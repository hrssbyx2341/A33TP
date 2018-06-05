package com.example.x6.a33tp.Activity.FunctionActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.x6.a33tp.Activity.MainActivity;
import com.example.x6.a33tp.Activity.Y7orY8orY10Activity;
import com.example.x6.a33tp.Date.StepCode;
import com.example.x6.a33tp.Date.TypeManager;
import com.example.x6.a33tp.R;
import com.example.x6.serial.SerialPort;

import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by 24179 on 2018/5/8.
 */

public class UartActivity extends Activity {
    private final String TAG = "UartActivity";
    private TypeManager typeManager;
    private SerialPort[] ttySPorts = new SerialPort[4];
    private OutputStream[] ttySOutputs = new OutputStream[4];
    private InputStream[] ttySInputs = new InputStream[4];
    private boolean[] isTesting = new boolean[]{false,false,false,false};
    private byte[] bytes = new byte[]{0x00,0x01,0x02,0x03,0x04,0x05,0x06,0x07,0x08};
    Handler handler;
    private long serialOutTime = 2*100;
    private long startTime;
    private final String SRlengthKey = "SRlengthKey";
    private final String SRFlagKey = "SRFlagKey";
    private final String serialDisplay = "串口";
    private final String serialRecvDate = "接收数据字节数：";
    private final String serialSendDate = "发送数据字节：";

    private int[] serialSendSize = new int[4];
    private int[] serialRecvSize = new int[4];

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {



        super.onCreate(savedInstanceState);
        for(int i=0; i < 4; i++){
            try {
                ttySPorts[i] = new SerialPort(new File("/dev/ttyS"+String.valueOf(i)),115200,0,50);
                ttySInputs[i] = ttySPorts[i].getInputStream();
                ttySOutputs[i] = ttySPorts[i].getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        typeManager = TypeManager.getTypeManager(this.getApplicationContext());
        switch (typeManager.getProductType()){
            case TypeManager.Y7:
            case TypeManager.Y8:
            case TypeManager.Y10:
                LinearLayout totalLinelayout = new LinearLayout(this);
                totalLinelayout.setOrientation(LinearLayout.VERTICAL);
                LinearLayout[] sonLineLayout = new LinearLayout[4];
                LinearLayout.LayoutParams sonLineLayoutParam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                sonLineLayoutParam.setLayoutDirection(LinearLayout.HORIZONTAL);

                LinearLayout.LayoutParams totalLineLayoutParam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT,1);
                totalLineLayoutParam.setLayoutDirection(LinearLayout.VERTICAL);

                final TextView[] textViews = new TextView[5];
                final Button[][] buttons = new Button[4][2];
                int i=0, j =0;
                for(i = 0; i < 4; i++){
                    sonLineLayout[i] = new LinearLayout(this);
                    sonLineLayout[i].setOrientation(LinearLayout.HORIZONTAL);
                    textViews[i] = new TextView(this);
                    textViews[i].setText(serialDisplay + String.valueOf(i)
                            +" "+serialRecvDate+ String.valueOf(0)
                            +" "+serialSendDate+ String.valueOf(0));
                    textViews[i].setTextColor(Color.BLUE);
                    textViews[i].setTextSize(30);
                    sonLineLayout[i].addView(textViews[i]);
                    for(j = 0; j < 2; j++){
                        Log.e(TAG,"buttons"+String.valueOf(i)+" "+String.valueOf(j));
                        buttons[i][j] = new Button(this);
                        buttons[i][j].setTextSize(50);
                        if(j == 0) {
                            buttons[i][j].setText("通过");
                            sonLineLayout[i].addView(buttons[i][j]);
                        }else if(j == 1) {
                            buttons[i][j].setText("不通过");
                            sonLineLayout[i].addView(buttons[i][j]);
                        }
                    }
                    totalLinelayout.addView(sonLineLayout[i],sonLineLayoutParam);
                }
                textViews[4] = new TextView(this);
                textViews[4].setTextSize(50);
                textViews[4].setText("串口测试说明，串口主要是收发数据，在插上正确完好的工装时，串口的接收数据字节数和发送数据字节数都在增长则串口测试通过，否则不通过。串口测试是依次测试的！");
                totalLinelayout.addView(textViews[4]);
                this.addContentView(totalLinelayout,totalLineLayoutParam);
                handler = new Handler(){
                    @Override
                    public void handleMessage(Message msg) {
                        Bundle bundle = msg.getData();
                        switch (msg.what){
                            case 0:
                                if (getSRFlag(bundle)){
                                    serialRecvSize[msg.what] += getMsg(bundle);
                                }else if(!getSRFlag(bundle)){
                                    serialSendSize[msg.what] += getMsg(bundle);
                                }
                                textViews[msg.what].setText(serialDisplay + String.valueOf(msg.what)
                                        +" "+serialRecvDate+ String.valueOf(serialRecvSize[msg.what])
                                        +" "+serialSendDate+ String.valueOf(serialSendSize[msg.what]));
                                break;
                            case 1:
                                if (getSRFlag(bundle)){
                                    serialRecvSize[msg.what] += getMsg(bundle);
                                }else if(!getSRFlag(bundle)){
                                    serialSendSize[msg.what] += getMsg(bundle);
                                }
                                textViews[msg.what].setText(serialDisplay + String.valueOf(msg.what)
                                        +" "+serialRecvDate+ String.valueOf(serialRecvSize[msg.what])
                                        +" "+serialSendDate+ String.valueOf(serialSendSize[msg.what]));
                                break;
                            case 2:
                                if (getSRFlag(bundle)){
                                    serialRecvSize[msg.what] += getMsg(bundle);
                                }else if(!getSRFlag(bundle)){
                                    serialSendSize[msg.what] += getMsg(bundle);
                                }
                                textViews[msg.what].setText(serialDisplay + String.valueOf(msg.what)
                                        +" "+serialRecvDate+ String.valueOf(serialRecvSize[msg.what])
                                        +" "+serialSendDate+ String.valueOf(serialSendSize[msg.what]));
                                break;
                            case 3:
                                if (getSRFlag(bundle)){
                                    serialRecvSize[msg.what] += getMsg(bundle);
                                }else if(!getSRFlag(bundle)){
                                    serialSendSize[msg.what] += getMsg(bundle);
                                }
                                textViews[msg.what].setText(serialDisplay + String.valueOf(msg.what)
                                        +" "+serialRecvDate+ String.valueOf(serialRecvSize[msg.what])
                                        +" "+serialSendDate+ String.valueOf(serialSendSize[msg.what]));
                                break;
                        }
                    }
                };
                startTest(0);

                break;
            case TypeManager.K7232:
            case TypeManager.K10232:
                break;
            case TypeManager.K7485:
            case TypeManager.K10485:
                break;
            case TypeManager.Y5:
                break;
        }
    }

    private void startTest(int serialPort){
        chooseSerialPort(serialPort);
        test232recv();
        test232send();
    }
    private void stopTest(int serialPort){
        isTesting[serialPort] = false;
    }
    private void chooseSerialPort(int j){
        for(int i = 0; i < 4; i++){
            if(i == j)
                isTesting[i] = true;
            else
                isTesting[i] = false;
        }
    }

    private void testTempSend(){
        new Thread(){
            @Override
            public void run() {
                super.run();
                Looper.prepare();

                Looper.loop();
            }
        }.start();
    }
    private void testTempReceive(){
        new Thread(){
            @Override
            public void run() {
                super.run();
                Looper.prepare();
                int i = 0;
                while (!isTesting[i]) //选择测试串口
                    i++;
                if (isTesting[i]) {
                    int length = 0;
                    for (int j = 0; j < 9; j++) {
                        try {
                            ttySOutputs[i].write(bytes);
                            ttySOutputs[i].flush();
                            Thread.sleep(100);
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        length = bytes.length;
                        sendMsg(i, length, false);
                        Looper.loop();
                    }
                }
            }
        }.start();
    }

    private void test232send(){
        new Thread(){
            @Override
            public void run() {
                super.run();
                Looper.prepare();
                int i = 0;
                while (!isTesting[i]) //选择测试串口
                    i++;
                while (isTesting[i]) {
                    int length = 0;
                    for (int j = 0; j < 9; j++) {
                        try {
                            ttySOutputs[i].write(bytes);
                            ttySOutputs[i].flush();
                            Thread.sleep(100);
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        length = bytes.length;
                        sendMsg(i,length,false);
                    }


                }
                Looper.loop();
            }
        }.start();
    }

    private void sendMsg(int what, int length, boolean isReceive){
        Message message = new Message();
        message.what = what;
        Bundle bundle = new Bundle();
        bundle.putInt(SRlengthKey,length);
        bundle.putBoolean(SRFlagKey,isReceive);
        message.setData(bundle);
        handler.sendMessage(message);
    }

    private boolean getSRFlag(Bundle bundle){
        return bundle.getBoolean(SRFlagKey);
    }

    private int getMsg(Bundle bundle){
        return bundle.getInt(SRlengthKey);
    }

    private void test232recv(){
        new Thread(){
            @Override
            public void run() {
                super.run();
                Looper.prepare();
                int i = 0;
                while (!isTesting[i]) //选择测试串口
                    i++;
                while(isTesting[i]){
                    int size;
                    byte[] recvbyte = new byte[1024];
                    startTime = System.currentTimeMillis();
                    int length = 0;
//                    System.currentTimeMillis() - startTime < serialOutTime &&
                    while (  ttySInputs[i] != null){
                        try {
                            size = ttySInputs[i].read(recvbyte);
                            if(size > 0){
                                sendMsg(i,size,true);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                }
                Looper.loop();
            }
        }.start();
    }
}
