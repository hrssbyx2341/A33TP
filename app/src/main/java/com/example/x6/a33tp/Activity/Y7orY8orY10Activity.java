package com.example.x6.a33tp.Activity;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import com.example.x6.a33tp.Activity.FunctionActivity.AudioActivity;
import com.example.x6.a33tp.Activity.FunctionActivity.BlueToothActivity;
import com.example.x6.a33tp.Activity.FunctionActivity.ETHActivity;
import com.example.x6.a33tp.Activity.FunctionActivity.GPIOActivity;
import com.example.x6.a33tp.Activity.FunctionActivity.MobileNetActivity;
import com.example.x6.a33tp.Activity.FunctionActivity.RTCActivity;
import com.example.x6.a33tp.Activity.FunctionActivity.RecoderActivity;
import com.example.x6.a33tp.Activity.FunctionActivity.ScreenActivity;
import com.example.x6.a33tp.Activity.FunctionActivity.SerialActivity.Serial485Test;
import com.example.x6.a33tp.Activity.FunctionActivity.SerialAlloc;
import com.example.x6.a33tp.Activity.FunctionActivity.TFCardActivity;
import com.example.x6.a33tp.Activity.FunctionActivity.USBActivity;
import com.example.x6.a33tp.Activity.FunctionActivity.WifiActivity;
import com.example.x6.a33tp.Date.ResultDate;
import com.example.x6.a33tp.Date.StepCode;
import com.example.x6.a33tp.Date.TypeManager;
import com.example.x6.a33tp.Function.StartApk;
import com.example.x6.a33tp.R;

import java.io.IOException;

/**
 * Created by 24179 on 2018/5/4.
 */

public class Y7orY8orY10Activity extends Activity {
    private final String TAG = "Y7orY8orY10Activity";
    private StepCode stepCode;
    MediaPlayer mp = new MediaPlayer();
    private Button nextTest;
    private StartApk startApk;
    private TypeManager typeManager;
    ResultDate resultDate;

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){

            }
        }
    };


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.empty);
        stepCode = StepCode.getStepCodeClass(this.getApplicationContext());
        nextTest = (Button) findViewById(R.id.nextTest);
        nextTest.setEnabled(false);
        startApk = new StartApk();
        typeManager = TypeManager.getTypeManager(this.getApplicationContext());
        resultDate = ResultDate.getResultDate(this.getApplicationContext());


        nextTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                jumpActivity(Y7orY8orY10Activity.class);
            }
        });

        switch (stepCode.getStepCode()){
            case StepCode.STEP_AUDIO:
                jumpActivity(AudioActivity.class);
                break;
            case StepCode.STEP_ETH:
                jumpActivity(ETHActivity.class);
                break;
            case StepCode.STEP_4G:
                jumpActivity(MobileNetActivity.class);
                break;
            case StepCode.STEP_WIFI:
                jumpActivity(WifiActivity.class);
                break;
            case StepCode.STEP_BLUETOOTH:
                jumpActivity(BlueToothActivity.class);
                break;
            case StepCode.STEP_GPIO:
                if(typeManager.getProductType() == TypeManager.K7232 ||
                        typeManager.getProductType() == TypeManager.K7485 ||
                        typeManager.getProductType() == TypeManager.K10232 ||
                        typeManager.getProductType() == TypeManager.K10485){
                    jumpActivity(GPIOActivity.class);
                }else{
                    try {
                        resultDate.WriteResultDate(ResultDate.GPIO,ResultDate.UNSUPPORT,null);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    stepCode.jumpActivity(StepCode.STEP_RTC,this.getApplicationContext(),Y7orY8orY10Activity.class);
                    this.finish();
                }
                break;
            case StepCode.STEP_RTC:
                jumpActivity(RTCActivity.class);
                break;
            case StepCode.STEP_TFCRAD:
                jumpActivity(TFCardActivity.class);
                break;
            case StepCode.STEP_TOUCH_DISP:
                jumpActivity(ScreenActivity.class);
                break;
            case StepCode.STEP_UART:
                jumpActivity(SerialAlloc.class);
                break;
            case StepCode.STEP_UART485:
                if(typeManager.getProductType() == TypeManager.Y5 ||
                        typeManager.getProductType() == TypeManager.Y7 ||
                        typeManager.getProductType() == TypeManager.Y8 ||
                        typeManager.getProductType() == TypeManager.Y10){
                    jumpActivity(Serial485Test.class);
                }else{
                    try {
                        resultDate.WriteResultDate(ResultDate.UART485,ResultDate.UNSUPPORT,null);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    stepCode.jumpActivity(StepCode.STEP_GPIO,this.getApplicationContext(),Y7orY8orY10Activity.class);
                    this.finish();
                }
                break;
            case StepCode.STEP_USB:
                jumpActivity(USBActivity.class);
                break;
            case StepCode.STEP_RECORE:
                jumpActivity(RecoderActivity.class);
                break;
            case 15:
                jumpActivity(EndActivity.class);
                break;

        }
    }

    private void jumpActivity(Class class1){
        Intent intent = new Intent(Y7orY8orY10Activity.this,class1);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        Y7orY8orY10Activity.this.finish();
    }

}
