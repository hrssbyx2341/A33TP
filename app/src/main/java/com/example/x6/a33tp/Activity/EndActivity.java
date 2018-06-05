package com.example.x6.a33tp.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.BoolRes;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.x6.a33tp.Activity.FunctionActivity.ETHActivity;
import com.example.x6.a33tp.Date.ResultDate;
import com.example.x6.a33tp.Date.SaveRtc;
import com.example.x6.a33tp.Function.MobileNetControl;
import com.example.x6.a33tp.Function.SuCommand;
import com.example.x6.a33tp.R;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.Vector;

/**
 * Created by 24179 on 2018/5/11.
 */

public class EndActivity extends Activity {
    private final String TAG = "EndActivity";
    private TextView showDate;
    private Button positave, negative;
    private SaveRtc saveRtc;
    private ResultDate resultDate;
    private SuCommand suCommand;
    private ProgressDialog progressDialog;
    private MobileNetControl mobileNetControl;


    private final String[] deviceTable = new String[]{
            ResultDate.SPEAK,ResultDate.HEADPHONE,ResultDate.RECORDER,
            ResultDate.TFCARD,ResultDate.USB,ResultDate.RTC,ResultDate.WIFI,
            ResultDate.BLUETOOTH,ResultDate.ETH,ResultDate.MOBILENET,ResultDate.TOUCH,
            ResultDate.SCREEN,ResultDate.UART,ResultDate.GPIO
    };

    private void showExitDialog01(String title,String message){
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("确定", null)
                .show();
    }
    private void showProgressDialog(String message){
        progressDialog.setTitle("提示...");
        progressDialog.setMessage(message);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
    }
    private void dissMissProgressDialog(){
        progressDialog.dismiss();
    }


    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case -1:
                    dissMissProgressDialog();
                    showExitDialog01("提示:","没有检测到U盘，请插入U盘");
                    break;
                case -2:
                    dissMissProgressDialog();
                    showExitDialog01("提示:","生成文件名失败");
                    break;
                case -3:
                    dissMissProgressDialog();
                    showExitDialog01("提示:","拷贝文件失败");
                    break;
                case -4:
                    dissMissProgressDialog();
                    showExitDialog01("提示:","写入总文件失败");
                    break;
                case 4:
                    dissMissProgressDialog();
                    showExitDialog01("提示:","生成测试文件成功");
                    positave.setEnabled(false);
                    break;
            }
        }
    };
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.end);
        saveRtc = new SaveRtc(this.getApplicationContext());
        resultDate = ResultDate.getResultDate(this.getApplicationContext());
        suCommand = new SuCommand();
        showDate = (TextView) findViewById(R.id.showDate);
        positave = (Button) findViewById(R.id.positave);
        negative = (Button) findViewById(R.id.negative);
        progressDialog = new ProgressDialog(EndActivity.this);
        mobileNetControl = new MobileNetControl();
        String str = null;
        try {
            str = saveRtc.ReadRTC();
            totalFileName = saveRtc.ReadRTC()+".txt";
        } catch (IOException e) {
            e.printStackTrace();
        }
        Vector<String> vector = suCommand.execRootCmd(SaveRtc.rtcCmd);
        try {
            for (String string: vector){
                if(str.equals(string)){
                    if(resultDate.readLine(ResultDate.RTC) == null)
                        resultDate.WriteResultDate(ResultDate.RTC,ResultDate.PASS,null);
                }else{
                    if(resultDate.readLine(ResultDate.RTC) == null)
                        resultDate.WriteResultDate(ResultDate.RTC,ResultDate.UNPASS,null);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            showDate.setText(resultDate.readResultDate().toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

        positave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showProgressDialog("正在生成测试报告文件...");
                startTestFile();
                Log.e(TAG,getFileName());
            }
        });
        negative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resultDate.deleteResultDate();
                saveRtc.deleteRTCDate();
                Intent intent = new Intent(EndActivity.this.getApplicationContext(),MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                EndActivity.this.finish();
            }
        });
    }

    private String countResultDate(){
        String result = "";
        try {
        for(String str:deviceTable){
            if(resultDate.readLine(str).equals(ResultDate.UNPASS)){
                result += str+"_";
            }
        }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    private boolean isPass(){
        boolean result = true;
        try {
            for(String str:deviceTable){
                if(resultDate.readLine(str).equals(ResultDate.UNPASS)){
                    result = false;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    private String getFileName(){
        String result = null;
        try {
            result = resultDate.getType() +"_"+resultDate.getUUID();
            String temp = null;
            temp = countResultDate();
            if(!temp.equals("")){
                result += "_"+temp+"unpass";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    private String totalFileName;
    private final String sourcePath = "/data/data/com.example.x6.a33tp/files/"+ResultDate.filePath;
    private final String USBDevPath ="/dev/block/sda1";
    private final String MountPath = "/mnt/media_rw/";
    private final String MountCmd = "mount -t vfat "+USBDevPath+" "+MountPath;
    private void startTestFile(){
        new Thread(){
            @Override
            public void run() {
                super.run();
                Looper.prepare();
                try {
                    mobileNetControl.openMobileNet();
                    String cmd = "ls /dev/block/sda1";
                    Log.e(TAG,cmd);
                    if(suCommand.execRootCmdSilent(cmd) == 0) { // 检测U盘是否插入
                        cmd = MountCmd;
                        Log.e(TAG,cmd);
                        suCommand.execRootCmdSilent(cmd);
//                        if (suCommand.execRootCmdSilent(cmd) == 0) {//挂载U盘
                            if (getFileName() != null) { //生成文件名
//                                cmd = "cp " + sourcePath + " " + MountPath + getFileName()+".txt";
                                cmd = "cp " + sourcePath + " " + MountPath+resultDate.getType()+"_"+resultDate.getUUID()+".txt";
                                Log.e(TAG,cmd+String.valueOf(suCommand.execRootCmdSilent(cmd)));
//                                Vector<String> vector = suCommand.execRootCmd("ls "+"/data/data/com.example.x6.a33tp/files/");
//                                for(String string:vector)
//                                    Log.e(TAG,string);
                                if (suCommand.execRootCmdSilent(cmd) == 0) { // 拷贝文件成功
                                    String str = countResultDate();
                                    if (str.equals("")) { //测试通过
                                        cmd = "echo \""+resultDate.getType() + "_" + resultDate.getUUID() + " PASS\"  >> " + MountPath + totalFileName;
                                        Log.e(TAG,cmd);
                                        if (suCommand.execRootCmdSilent(cmd) == 0) { //写入测试总文件成功
                                            handler.sendEmptyMessage(4);
                                        } else { //写入测试总文件失败
                                            handler.sendEmptyMessage(-4);
                                        }
                                    } else {
                                        cmd = "echo \""+resultDate.getType() + "_" + resultDate.getUUID() + " NOT PASS\"  >> " + MountPath + totalFileName;
                                        Log.e(TAG,cmd);
                                        if (suCommand.execRootCmdSilent(cmd) == 0) { //写入测试总文件成功
                                            handler.sendEmptyMessage(4);
                                        } else { //写入测试总文件失败
                                            handler.sendEmptyMessage(-4);
                                        }
                                    }
                                } else { //拷贝文件失败
                                    handler.sendEmptyMessage(-3);
                                }
                            } else { //生成文件名失败
                                handler.sendEmptyMessage(-2);
                            }
                    }else{
                        handler.sendEmptyMessage(-1);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
}
