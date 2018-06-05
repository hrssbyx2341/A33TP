package com.example.x6.a33tp.Activity.FunctionActivity;

import android.app.Activity;
import android.app.ProgressDialog;
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

import com.example.x6.a33tp.Activity.MainActivity;
import com.example.x6.a33tp.Activity.Y7orY8orY10Activity;
import com.example.x6.a33tp.Date.NetInfoManager;
import com.example.x6.a33tp.Date.ResultDate;
import com.example.x6.a33tp.Date.StepCode;
import com.example.x6.a33tp.Function.MobileNetControl;
import com.example.x6.a33tp.Function.NetConnectTest;
import com.example.x6.a33tp.Function.SuCommand;
import com.example.x6.a33tp.Function.WifiControl;
import com.example.x6.a33tp.R;

import java.io.IOException;
import java.util.Vector;

/**
 * Created by 24179 on 2018/5/5.
 */

public class ETHActivity extends Activity {
    private final String TAG="ETHActivity";
    private SuCommand suCommand;
    private Button nextTest, reEth;
    private TextView dispEth;
    public String ethStr = "";
    boolean isEthOutTime = true;
    private ResultDate resultDate;
    private WifiControl wifiControl;
    private MobileNetControl mobileNetControl;
    private ProgressDialog progressDialog;
    private StepCode stepCode;
    private boolean isETHPass = false;

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    dispEth.append("开始测试以太网\n搜索以太网设备：");
                    break;
                case 1:
                    dispEth.append("找到以太网设备\n启动以太网设备：");
                    break;
                case 2:
                    dispEth.append("以太网设备启动成功\n以太网正在获取IP：");
                    reEth.setEnabled(true);
                    break;
                case 3:
                    dispEth.append("以太网获取IP成功\n正在进行PING测试：");
                    break;
                case -1:
                    dispEth.append("找不到以太网设备,请进行下一项测试\n以太网功能有故障");
                    nextTest.setEnabled(true);
                    dispEth.setTextColor(Color.RED);
                    isETHPass = false;
                    break;
                case -2:
                    dispEth.append("以太网设备启动失败\n以太网功能有故障");
                    nextTest.setEnabled(true);
                    dispEth.setTextColor(Color.RED);
                    isETHPass = false;
                    break;
                case -3:
                    dispEth.append("以太网获取IP失败,请检查网线后重新测试，或者进入下一项测试\n以太网功能有故障");
                    nextTest.setEnabled(true);
                    dispEth.setTextColor(Color.RED);
                    isETHPass = false;
                    break;
                case 4:
                    dispEth.append("PING测试成功\n以太网测试完成：功能正常\n");
                    dispEth.setTextColor(Color.BLUE);
                    isEthOutTime = false;
                    nextTest.setEnabled(true);
                    reEth.setEnabled(true);
                    isETHPass = true;
                    break;
                case -4:
                    dispEth.append("PING测试失败\n以太网测试完成：功能正常\n");
                    dispEth.setTextColor(Color.RED);
                    isEthOutTime = false;
                    nextTest.setEnabled(true);
                    reEth.setEnabled(true);
                    isETHPass = false;
                    break;
                case -5:
                    stopEth();
                    break;
                case 5:
                    progressDialog.dismiss();
                    startETH();
                    startOutTime();
                    break;
            }
        }
    };
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.eth);
        nextTest = (Button) findViewById(R.id.nextTest);
        dispEth = (TextView) findViewById(R.id.dispEth);
        reEth = (Button) findViewById(R.id.reEth);
        nextTest.setEnabled(false);
        reEth.setEnabled(false);
        suCommand = new SuCommand();
        resultDate = ResultDate.getResultDate(this.getApplicationContext());
        progressDialog = new ProgressDialog(ETHActivity.this);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setTitle("提示：");
        progressDialog.setMessage("正在关闭其他网络...");
        progressDialog.show();
        closeOthreNet();
        reEth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextTest.setEnabled(false);
                dispEth.setText("");
                dispEth.setTextColor(Color.BLACK);
                startETH();
                isEthOutTime = true;
                startOutTime();
            }
        });

        nextTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopEth();
                try {
                    if(isETHPass)
                        resultDate.WriteResultDate(ResultDate.ETH,ResultDate.PASS,null);
                    else
                        resultDate.WriteResultDate(ResultDate.ETH,ResultDate.UNPASS,null);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                stepCode = StepCode.getStepCodeClass(ETHActivity.this.getApplicationContext());
                stepCode.jumpActivity(StepCode.STEP_WIFI,ETHActivity.this, Y7orY8orY10Activity.class);
                ETHActivity.this.finish();
            }
        });
    }


    private void closeOthreNet(){
        new Thread(){
            @Override
            public void run() {
                Looper.prepare();
                wifiControl = new WifiControl(ETHActivity.this.getApplicationContext());
                mobileNetControl = new MobileNetControl();
                mobileNetControl.closeMobuleNet();
                wifiControl.WifiClose();
                handler.sendEmptyMessage(5);
                Looper.loop();
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
                Looper.loop();
            }
        }.start();
    }

    private String getGatway(){
        String cmd = "getprop | grep dhcp."+ethStr+".gateway";
        Vector<String> vector = suCommand.execRootCmd(cmd);
        cmd = "dhcp."+ethStr+".gateway[: [";
        for(String str: vector){
            cmd = str.substring(cmd.length()+1,str.length()-1);
        }
        return cmd;
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
                Looper.loop();
            }
        }.start();
    }

    public void startETH(){
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
//                            try {
//                                Thread.sleep(1*1000);
//                            } catch (InterruptedException e) {
//                                e.printStackTrace();
//                            }
                            cmd = "busybox ping "+ NetInfoManager.ETHgateWay+" -w 1 -I "+ethStr;
                            Log.e(TAG,"ping 命令 :"+cmd);
                            if(0 == suCommand.execRootCmdSilent(cmd)){
                                handler.sendEmptyMessage(4);
                            }else{
                                handler.sendEmptyMessage(-4);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopEth();
    }
}
