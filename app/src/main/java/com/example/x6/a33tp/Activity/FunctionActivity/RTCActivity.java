package com.example.x6.a33tp.Activity.FunctionActivity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.x6.a33tp.Activity.EndActivity;
import com.example.x6.a33tp.Activity.Y7orY8orY10Activity;
import com.example.x6.a33tp.Date.NetInfoManager;
import com.example.x6.a33tp.Date.ResultDate;
import com.example.x6.a33tp.Date.SaveRtc;
import com.example.x6.a33tp.Date.StepCode;
import com.example.x6.a33tp.Function.MobileNetControl;
import com.example.x6.a33tp.Function.MyDatePickDialog;
import com.example.x6.a33tp.Function.MyTimePickDialog;
import com.example.x6.a33tp.Function.SuCommand;
import com.example.x6.a33tp.Function.TimeSettingUtils;
import com.example.x6.a33tp.Function.WifiControl;
import com.example.x6.a33tp.R;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Vector;

/**
 * Created by 24179 on 2018/5/9.
 */

public class RTCActivity extends Activity {
    private final String TAG="ETHActivity";
    private SuCommand suCommand;
    private Button nextTest, reEth;
    private TextView dispEth;
    public String ethStr = "";
    boolean isEthOutTime = true;
    private WifiControl wifiControl;
    private MobileNetControl mobileNetControl;
    private StepCode stepCode;
    private String date = "";
    private SaveRtc saveRtc;
    private MyDatePickDialog datePickerDialog;
    private MyTimePickDialog timePickerDialog;
    private int Year = 0, Month = 0, Day = 0, Hour = 0, Minute = 0;


    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    dispEth.append("开始同步时间...\n");
                    break;
                case 1:

                    break;
                case 2:

                    break;
                case 3:
                    break;
                case -1:
                    dispEth.append("以太网连接失败\n");
                    reEth.setEnabled(true);
                    dispEth.setTextColor(Color.RED);
                    nextTest.setEnabled(true);
                    break;
                case -2:
                    dispEth.append("以太网连接失败\n");
                    reEth.setEnabled(true);
                    dispEth.setTextColor(Color.RED);
                    nextTest.setEnabled(true);
                    break;
                case -3:
                    dispEth.append("以太网连接失败\n");
                    reEth.setEnabled(true);
                    dispEth.setTextColor(Color.RED);
                    nextTest.setEnabled(true);
                    break;
                case 4:
                    dispEth.append("写入时间成功\n");
                    isEthOutTime = false;
                    reEth.setEnabled(true);
                    nextTest.setEnabled(false);
                    break;
                case -4:
                    dispEth.append("同步时间失败\n");
                    reEth.setEnabled(true);
                    dispEth.setTextColor(Color.RED);
                    isEthOutTime = false;
                    reEth.setEnabled(true);
                    nextTest.setEnabled(true);
                    break;
                case -5:
                    stopEth();
                    break;
                case -6:
                    try {
                        saveRtc.WriteRTC("1995-09-26");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    dispEth.append("RTC（实时时间）写入失败\nRTC测试不通过\n请拔掉网线，并且断电重启设备。重启后再启动本测试程序，进行后续操作");
                    dispEth.setTextColor(Color.RED);
                    nextTest.setEnabled(false);
                    break;
                case 5:
                    dispEth.append("同步时间成功\n当前时间："+date+"\n"
                            +"请拔掉网线，并且断电重启设备。重启后再启动本测试程序，进行后续操作");
                    dispEth.setTextColor(Color.BLUE);
                    nextTest.setEnabled(true);
                    break;
                case -7:
                    dispEth.append("设置时区失败\n");
                    dispEth.setTextColor(Color.RED);
                    nextTest.setEnabled(true);
                    reEth.setEnabled(true);
                    break;
            }
        }
    };
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rtc);
        saveRtc = new SaveRtc(this.getApplicationContext());

        try {
            if(saveRtc.ReadRTC() != null){
                Intent intent = new Intent(RTCActivity.this.getApplicationContext(), EndActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                RTCActivity.this.finish();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        nextTest = (Button) findViewById(R.id.nextTest);
        dispEth = (TextView) findViewById(R.id.dispEth);
        reEth = (Button) findViewById(R.id.reEth);
        nextTest.setEnabled(false);
        reEth.setEnabled(false);
        suCommand = new SuCommand();

        closeOthreNet();
        startNetTimeSetting();
        startOutTime();


        datePickerDialog =  new MyDatePickDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                Log.e(TAG,String.valueOf(i)+" "+String.valueOf(i1)+" "+String.valueOf(i2));
                Year = i;
                Month = i1;
                Day = i2;
                showTimePick(true);
            }
        },2018,0,0);
        datePickerDialog.setCanceledOnTouchOutside(false);
        datePickerDialog.setCancelable(false);
        timePickerDialog =  new MyTimePickDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                Log.e(TAG,String.valueOf(i)+" "+String.valueOf(i1));
                Hour = i;
                Minute = i1;
                startManualTiming();
            }
        },0,0,true);
        timePickerDialog.setCanceledOnTouchOutside(false);
        timePickerDialog.setCancelable(false);

        reEth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveRtc.deleteRTCDate();
                reEth.setEnabled(false);
                nextTest.setEnabled(false);
                dispEth.setText("");
                dispEth.setTextColor(Color.BLACK);
                startNetTimeSetting();
                isEthOutTime = true;
                startOutTime();
            }
        });

        nextTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveRtc.deleteRTCDate();
                dispEth.setText("");
                dispEth.setTextColor(Color.BLACK);
                showDatePick(true);
            }
        });
    }


    private void closeOthreNet(){
        new Thread(){
            @Override
            public void run() {
                Looper.prepare();
                wifiControl = new WifiControl(RTCActivity.this.getApplicationContext());
                mobileNetControl = new MobileNetControl();
                mobileNetControl.closeMobuleNet();
                wifiControl.WifiClose();
            }
        }.start();
    }

    private void getEthStr(){
        Vector<String> vector = null;
        vector = suCommand.execRootCmd("netcfg");
        for (String str : vector){
            int index = 0;
            if(-1 != (index = str.indexOf("eth"))) {
                ethStr = str.substring(index,4);
            }
        }
    }
    private void startOutTime(){
        new Thread(){
            @Override
            public void run() {
                Looper.prepare();
                long tempTime = System.currentTimeMillis();
                while (isEthOutTime){
                    if(System.currentTimeMillis() - tempTime >= 3*1000 && isEthOutTime){
                        handler.sendEmptyMessage(-5);
                        isEthOutTime = false;
                    }
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

    private void stopEth(){
        new Thread(){
            @Override
            public void run() {
                Looper.prepare();
                Vector<String> vector = null;
                String cmd = "netcfg "+ethStr+" down";
                if(0 == suCommand.execRootCmdSilent(cmd))
//                    handler.sendEmptyMessage(2); //以太网关闭成功
                    Log.e(TAG,"以太网关闭成功");
                else
//                    handler.sendEmptyMessage(-2); //以太网关闭失败
                    Log.e(TAG,"以太网关闭失败");
            }
        }.start();
    }

    public void startNetTimeSetting(){
        new Thread(){
            @Override
            public void run() {
                Looper.prepare();
                Vector<String> vector = null;
                handler.sendEmptyMessage(0); // 开始
                getEthStr();
                if(ethStr.equals("")) {
                    handler.sendEmptyMessage(-1); //没有以太网设备节点
                }else {
                    handler.sendEmptyMessage(1); //获取到以太网设备节点
                    String cmd = "netcfg "+ethStr+" up";
                    if(0 == suCommand.execRootCmdSilent(cmd)) {
                        handler.sendEmptyMessage(2); //以太网启动成功
                        cmd = "netcfg "+ethStr+" dhcp";
                        if(0 == suCommand.execRootCmdSilent(cmd)) {
                            handler.sendEmptyMessage(3);  //获取IP成功
                            cmd = "busybox rdate -s "+NetInfoManager.NTPServerOIp;
                            if(0 == suCommand.execRootCmdSilent(cmd)){
                                handler.sendEmptyMessage(4); //获取网络时间成功
                                cmd = "busybox hwclock -w";
                                if(0 == suCommand.execRootCmdSilent(cmd)){ //实时时间写入成功
                                    cmd = "date";
                                    vector = suCommand.execRootCmd(cmd);
                                    for(String str: vector)
                                        date = str;
                                    vector = suCommand.execRootCmd(SaveRtc.rtcCmd);
                                    try {
                                        for(String str: vector)
                                            saveRtc.WriteRTC(str);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    handler.sendEmptyMessage(5);
                                }else{
                                    handler.sendEmptyMessage(-6);
                                }

                            }else {
                                handler.sendEmptyMessage(-4); //同步时间失败
                            }
                        }else {
                            handler.sendEmptyMessage(-3); //获取IP失败
                        }
                    }else {
                        handler.sendEmptyMessage(-2); //以太网启动失败
                    }
                }
            }
        }.start();
    }
    private void startManualTiming(){
        new Thread(){
            @Override
            public void run() {
                Looper.prepare();
                Vector<String> vector = null;
                String cmd = "";
                handler.sendEmptyMessage(0); // 开始
                cmd = "setprop persist.sys.timezone GMT";
                if(0 == suCommand.execRootCmdSilent(cmd)) {
                    cmd = "busybox date " + getTimeDate();
                    Log.e(TAG, cmd);
                    if (0 == suCommand.execRootCmdSilent(cmd)) {
                        handler.sendEmptyMessage(4); //手动写入时间成功
                        cmd = "busybox hwclock -w";
                        if (0 == suCommand.execRootCmdSilent(cmd)) { //实时时间写入成功
                            cmd = "date";
                            vector = suCommand.execRootCmd(cmd);
                            for (String str : vector)
                                date = str;
                            vector = suCommand.execRootCmd(SaveRtc.rtcCmd);
                            try {
                                for (String str : vector)
                                    saveRtc.WriteRTC(str);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            handler.sendEmptyMessage(5);
                        } else {
                            handler.sendEmptyMessage(-6);
                        }
                    } else {
                        handler.sendEmptyMessage(-4); //同步时间失败
                    }
                }else{ //设置时区失败
                    handler.sendEmptyMessage(-7);
                }
            }
        }.start();
    }


    public void showDatePick(boolean isShowDatePick){
        if(isShowDatePick)
            datePickerDialog.show();
        else
            datePickerDialog.dismiss();
    }

    public void showTimePick(boolean isShowTimePick){
        if(isShowTimePick)
            timePickerDialog.show();
        else
            timePickerDialog.dismiss();
    }

    public String getTimeDate(){
        String result = "";
        Month += 1;
        if(Month <  10 && Month >= 0){
            result += "0"+String.valueOf(Month);
        }else{
            result += String.valueOf(Month);
        }

        if(Day < 10 && Day >= 0){
            result += "0"+String.valueOf(Day);
        }else{
            result += String.valueOf(Day);
        }

        if(Hour <10 && Hour >=0){
            result += "0"+String.valueOf(Hour);
        }else{
            result += String.valueOf(Hour);
        }

        if(Minute < 10 && Minute >=0){
            result += "0"+String.valueOf(Minute);
        } else{
            result += String.valueOf(Minute);
        }

        if(Year < 10 && Year >= 0){
            result += "0"+String.valueOf(Year);
        }else{
            result += String.valueOf(Year);
        }
        return result;
    }
}
