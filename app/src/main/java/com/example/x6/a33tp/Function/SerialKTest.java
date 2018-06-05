package com.example.x6.a33tp.Function;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.example.x6.serial.SerialPort;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by 24179 on 2018/5/9.
 */

public class SerialKTest {
    private final String TAG = "SerialControl";
    private final String ttyS0 = "ttyS0";
    private final String ttyS1 = "ttyS1";
    private final String ttyS2 = "ttyS2";
    private final String ttyS3 = "ttyS3";

    private Handler handler;
    private SerialPort serialPort0,serialPort1,serialPort2,serialPort3;
    private InputStream serialInputStream0, serialInputStream1,serialInputStream2,serialInputStream3;
    private OutputStream serialOutputStream0, serialOutputStream1,serialOutputStream2,serialOutputStream3;
    private boolean isSerailSend0 = false, isSerailSend1 = false, isSerailSend2 = false, isSerailSend3 = false;
    private boolean isSerial1Rece0 = false, isSerial1Rece1 = false,isSerial1Rece2 = false,isSerial1Rece3 = false;
    private byte[] bytes = new byte[]{0x01,0x02,0x03,0x04,0x05,0x01,1,2,3,1,5,1,32,5,1,23,5,2,1,3,4,21,3,4,1,32,1,34,14,2,13,4,1,4,3,2,4,1};
    private long lastTimeM ;
    private long timeOut = 200;
    private int sendTime = 1200;


    private int testTimes = 5;
    public void setTestTimes(int testTimes1){
        this.testTimes = testTimes1;
    }

    public SerialKTest(Handler handler1){
        this.handler = handler1;
        try {
            serialPort0 = new SerialPort(new File("/dev/ttyS0"),115200,0,30);
            serialPort1 = new SerialPort(new File("/dev/ttyS1"),115200,0,30);
            serialPort2 = new SerialPort(new File("/dev/ttyS2"),115200,0,30);
            serialPort3 = new SerialPort(new File("/dev/ttyS3"),115200,0,30);
            serialInputStream0 = serialPort0.getInputStream();
            serialInputStream1 = serialPort1.getInputStream();
            serialInputStream2 = serialPort2.getInputStream();
            serialInputStream3 = serialPort3.getInputStream();
            serialOutputStream0 = serialPort0.getOutputStream();
            serialOutputStream1 = serialPort1.getOutputStream();
            serialOutputStream2 = serialPort2.getOutputStream();
            serialOutputStream3 = serialPort3.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public final static int SEND_START = 0;
    public final static int SEND_ONE_FPS = 1;
    public final static int SEND_END = 2;
    public final static int RECV_START = 3;
    public final static int RECV_ONE_FPS = 4;
    public final static int RECV_END = 5;
    public final static int RECV_OUT_TIME = 6;
    public final static int RECV_WRONG_FPS = 7;

    private static String FLAG_KEY = "FLAG_KEY";
    private void sendMsgFlag(int what, int flag){
        Message message = new Message();
        message.what = what;
        Bundle bundle = new Bundle();
        bundle.putInt(FLAG_KEY,flag);
        message.setData(bundle);
        handler.sendMessage(message);
    }

    public int getMsgFlag(Bundle bundle){
        return bundle.getInt(FLAG_KEY);
    }

    /*
    串口0
     */

    public void stopSerialPort0(){
        isSerial1Rece0 = false;
        isSerailSend0 = false;
        serialPort0.close();
    }

    public void startSerialPort0(){
        isSerial1Rece0 = true;
        isSerailSend0 = true;
        serialPort0Recv();
        serialPort0Send();
    }
    public void serialPort0Send(){
        new Thread(){
            @Override
            public void run() {
                super.run();
                Looper.prepare();
                if(isSerailSend0) {
                    sendMsgFlag(0,SEND_START);
                    for (int i = 0; i < testTimes; i++) {
                        try {
                            Thread.sleep(sendTime);
                            String str = "";
                            for (byte tempbyte : bytes) {
                                str += String.format("0x%x ", tempbyte);
                            }
                            Log.e(ttyS0, "Serial port 0 SEND:-->" + str);
                            serialOutputStream0.write(bytes);
                            sendMsgFlag(0,SEND_ONE_FPS);
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    sendMsgFlag(0,SEND_END);
                    isSerailSend0 = false;
                }
            }
        }.start();
    }


    public void serialPort0Recv(){
        new Thread(){
            @Override
            public void run() {
                super.run();
                Looper.prepare();
                byte[] recvbyte0 = new byte[1024];
                while(isSerial1Rece0){
                    int length = 0;
                    lastTimeM = System.currentTimeMillis();
                    int j = 0;
                    while ( isSerial1Rece0 && System.currentTimeMillis() - lastTimeM <= timeOut && serialInputStream0 != null){
                        try {
                            if(serialInputStream0.available()>0 == false){
                                if(j >= 5 && !isSerailSend0){
                                    sendMsgFlag(0,RECV_END);
                                    stopSerialPort0(); //这里标志串口短发送结束
                                }
                                j++;
                                continue;
                            }else{
                                Thread.sleep(20);
                            }
                            int size = 0;
                            size = serialInputStream0.read(recvbyte0);
                            if(size > 0){
                                length += size;
                            }else{
                                Log.e(ttyS0,"serial port 0 receive time out");
                                sendMsgFlag(0,RECV_OUT_TIME);
                                continue;
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    String str = "";
                    boolean isRight = true;
                    if(length > 0) {
                        for (int i = 0; i < 38; i++) { // 验证收到的这一帧信息是否正确
                            str += String.format("0x%x ", recvbyte0[i]);
                            if (recvbyte0[i] != bytes[i])
                                isRight = false;
                        }
                        Log.i(ttyS0, "Serial port 0 RECV:<--" + str);
                        if (isRight) {
                            Log.e(ttyS0, "串口接收数据正常");
                            sendMsgFlag(0,RECV_ONE_FPS);
                        }else{
                            sendMsgFlag(0,RECV_WRONG_FPS);
                        }
                    }
                }
            }
        }.start();
    }
   /*
    串口1
     */




    public void stopSerialPort1(){
        isSerial1Rece1 = false;
        isSerailSend1 = false;
        serialPort1.close();
    }

    public void startSerialPort1(){
        isSerial1Rece1 = true;
        isSerailSend1 = true;
        serialPort1Recv();
        serialPort1Send();
    }
    public void serialPort1Send(){
        new Thread(){
            @Override
            public void run() {
                super.run();
                Looper.prepare();
                if(isSerailSend1) {
                    sendMsgFlag(1,SEND_START);
                    for (int i = 0; i < testTimes; i++) {
                        try {
                            Thread.sleep(sendTime);
                            String str = "";
                            for (byte tempbyte : bytes) {
                                str += String.format("0x%x ", tempbyte);
                            }
                            Log.e(ttyS1, "Serial port 1 SEND:-->" + str);
                            serialOutputStream1.write(bytes);
                            sendMsgFlag(1,SEND_ONE_FPS);
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    sendMsgFlag(1,SEND_END);
                    isSerailSend1 = false;
                }
            }
        }.start();
    }


    public void serialPort1Recv(){
        new Thread(){
            @Override
            public void run() {
                super.run();
                Looper.prepare();
                byte[] recvbyte0 = new byte[1024];
                while(isSerial1Rece1){
                    int length = 0;
                    lastTimeM = System.currentTimeMillis();
                    int j = 0;
                    while ( isSerial1Rece1 && System.currentTimeMillis() - lastTimeM <= timeOut && serialInputStream1 != null){
                        try {
                            if(serialInputStream1.available()>0 == false){
                                if(j >= 5 && !isSerailSend1){
                                    sendMsgFlag(1,RECV_END);
                                    stopSerialPort1(); //这里标志串口短发送结束
                                }
                                j++;
                                continue;
                            }else{
                                Thread.sleep(20);
                            }
                            int size = 0;
                            size = serialInputStream1.read(recvbyte0);
                            if(size > 0){
                                length += size;
                            }else{
                                Log.e(ttyS1,"serial port 1 receive time out");
                                sendMsgFlag(0,RECV_OUT_TIME);
                                continue;
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    String str = "";
                    boolean isRight = true;
                    if(length > 0) {
                        for (int i = 0; i < 38; i++) { // 验证收到的这一帧信息是否正确
                            str += String.format("0x%x ", recvbyte0[i]);
                            if (recvbyte0[i] != bytes[i])
                                isRight = false;
                        }
                        Log.i(ttyS1, "Serial port 1 RECV:<--" + str);
                        if (isRight) {
                            Log.e(ttyS1, "串口接收数据正常");
                            sendMsgFlag(1,RECV_ONE_FPS);
                        }else{
                            sendMsgFlag(1,RECV_WRONG_FPS);
                        }
                    }
                }
            }
        }.start();
    }
    /*
    串口2
     */
    public void stopSerialPort2(){
        isSerial1Rece2 = false;
        isSerailSend2 = false;
        serialPort2.close();
    }

    public void startSerialPort2(){
        isSerial1Rece2 = true;
        isSerailSend2 = true;
        serialPort2Recv();
        serialPort2Send();
    }
    public void serialPort2Send(){
        new Thread(){
            @Override
            public void run() {
                super.run();
                Looper.prepare();
                if(isSerailSend2) {
                    sendMsgFlag(2,SEND_START);
                    for (int i = 0; i < testTimes; i++) {
                        try {
                            Thread.sleep(sendTime);
                            String str = "";
                            for (byte tempbyte : bytes) {
                                str += String.format("0x%x ", tempbyte);
                            }
                            Log.e(ttyS2, "Serial port 2 SEND:-->" + str);
                            serialOutputStream2.write(bytes);
                            sendMsgFlag(2,SEND_ONE_FPS);
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    sendMsgFlag(2,SEND_END);
                    isSerailSend2 = false;
                }
            }
        }.start();
    }


    public void serialPort2Recv(){
        new Thread(){
            @Override
            public void run() {
                super.run();
                Looper.prepare();
                byte[] recvbyte0 = new byte[1024];
                while(isSerial1Rece2){
                    int length = 0;
                    lastTimeM = System.currentTimeMillis();
                    int j = 0;
                    while ( isSerial1Rece2 && System.currentTimeMillis() - lastTimeM <= timeOut && serialInputStream2 != null){
                        try {
                            if(serialInputStream2.available()>0 == false){
                                if(j >= 5 && !isSerailSend2){
                                    sendMsgFlag(2,RECV_END);
                                    stopSerialPort2(); //这里标志串口短发送结束
                                }
                                j++;
                                continue;
                            }else{
                                Thread.sleep(20);
                            }
                            int size = 0;
                            size = serialInputStream2.read(recvbyte0);
                            if(size > 0){
                                length += size;
                            }else{
                                Log.e(ttyS2,"serial port 2 receive time out");
                                sendMsgFlag(2,RECV_OUT_TIME);
                                continue;
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    String str = "";
                    boolean isRight = true;
                    if(length > 0) {
                        for (int i = 0; i < 38; i++) { // 验证收到的这一帧信息是否正确
                            str += String.format("0x%x ", recvbyte0[i]);
                            if (recvbyte0[i] != bytes[i])
                                isRight = false;
                        }
                        Log.i(ttyS2, "Serial port 2 RECV:<--" + str);
                        if (isRight) {
                            Log.e(ttyS2, "串口接收数据正常");
                            sendMsgFlag(2,RECV_ONE_FPS);
                        }else{
                            sendMsgFlag(2,RECV_WRONG_FPS);
                        }
                    }
                }
            }
        }.start();
    }
    /*
    串口3
     */
    public void stopSerialPort3(){
        isSerial1Rece3 = false;
        isSerailSend3 = false;
        serialPort3.close();
    }

    public void startSerialPort3(){
        isSerial1Rece3 = true;
        isSerailSend3 = true;
        serialPort3Recv();
        serialPort3Send();
    }
    public void serialPort3Send(){
        new Thread(){
            @Override
            public void run() {
                super.run();
                Looper.prepare();
                while (isSerailSend3) {
                    sendMsgFlag(3,SEND_START);
                    for (int i = 0; i < testTimes; i++) {
                    try {
                        Thread.sleep(2*1000); //485串口 2s发送一下
                        String str = "";
                        for (byte tempbyte : bytes) {
                            str += String.format("0x%x ", tempbyte);
                        }
                        Log.e(ttyS3, "Serial port 3 SEND:-->" + str);
                        serialOutputStream3.write(bytes);
                        sendMsgFlag(3,SEND_ONE_FPS);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    }
                    sendMsgFlag(3,SEND_END);
                    isSerailSend3 = false;
                }
            }
        }.start();
    }


    public void serialPort3Recv(){
        new Thread(){
            @Override
            public void run() {
                super.run();
                Looper.prepare();
                byte[] recvbyte0 = new byte[1024];
                while(isSerial1Rece3){
                    int length = 0;
                    lastTimeM = System.currentTimeMillis();
                    int j = 0;
                    while ( isSerial1Rece3 && System.currentTimeMillis() - lastTimeM <= timeOut && serialInputStream3 != null){
                        try {
                            if(serialInputStream3.available()>0 == false){
                                if(j >= 5 && !isSerailSend3){
                                    sendMsgFlag(3,RECV_END);
                                    stopSerialPort3(); //这里标志串口短发送结束
                                }
                                j++;
                                continue;
                            }else{
                                Thread.sleep(20);
                            }
                            int size = 0;
                            size = serialInputStream3.read(recvbyte0);
                            if(size > 0){
                                length += size;
                            }else{
                                Log.e(ttyS3,"serial port 3 receive time out");
                                sendMsgFlag(3,RECV_OUT_TIME);
                                continue;
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    String str = "";
                    boolean isRight = true;
                    if(length > 0) {
                        for (int i = 0; i < 38; i++) { // 验证收到的这一帧信息是否正确
                            str += String.format("0x%x ", recvbyte0[i]);
                            if (recvbyte0[i] != bytes[i])
                                isRight = false;
                        }
                        Log.i(ttyS3, "Serial port 3 RECV:<--" + str);
                        if (isRight) {
                            Log.e(ttyS3, "串口接收数据正常");
                            sendMsgFlag(3,RECV_ONE_FPS);
                        }else{
                            sendMsgFlag(3,RECV_WRONG_FPS);
                        }
                    }
                }
            }
        }.start();
    }
}
