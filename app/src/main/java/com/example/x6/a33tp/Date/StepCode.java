package com.example.x6.a33tp.Date;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.x6.a33tp.Activity.Y7orY8orY10Activity;

import java.io.IOException;

/**
 * Created by 24179 on 2018/5/4.
 */

public class StepCode {

    private ResultDate resultDate;
    private Context context;
    private final String TAG = "StepCode";

    public static final int STEP_UUID = 0;
    public static final int STEP_TYPE = 1;
    public static final int STEP_AUDIO = 2;
    public static final int STEP_RECORE = 3;
    public static final int STEP_TFCRAD = 4;
    public static final int STEP_USB = 5;
    public static final int STEP_ETH = 6;
    public static final int STEP_WIFI = 7;
    public static final int STEP_4G = 8;
    public static final int STEP_BLUETOOTH = 9;
    public static final int STEP_TOUCH_DISP = 10;
    public static final int STEP_UART = 11;
    public static final int STEP_UART485 = 12;
    public static final int STEP_GPIO = 13;
    public static final int STEP_RTC = 14;


    private int step = -1;
    private static StepCode stepCode = null;
    private StepCode(Context context1){
        this.context = context1;
    }
    public static StepCode getStepCodeClass(Context context){
        if(stepCode == null) {
            stepCode = new StepCode(context);
        }
        return stepCode;
    }

    public void jumpActivity(int stepCode1, Context context, Class class1){
        Intent intent = new Intent(context,Y7orY8orY10Activity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public int getStepCode(){
        try {
            resultDate = ResultDate.getResultDate(context);
            this.step = resultDate.getLastStep();
        } catch (IOException e) {
            this.step = -1;
            e.printStackTrace();
        }
        Log.e(TAG,String.valueOf(step));
        return step;
    }

}
