package com.example.x6.a33tp.Activity.FunctionActivity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.x6.a33tp.Activity.Y7orY8orY10Activity;
import com.example.x6.a33tp.Date.ResultDate;
import com.example.x6.a33tp.Date.StepCode;
import com.example.x6.a33tp.Function.GpioTest;
import com.example.x6.a33tp.R;

import java.io.IOException;

/**
 * Created by 24179 on 2018/5/9.
 */

public class GPIOActivity extends Activity {
    private Button nextTest, reEth;
    private TextView dispEth;
    private GpioTest gpioTest;
    private int input1,input2,output1,output2;
    private StepCode stepCode;
    private boolean isGpioPass = true;
    ResultDate resultDate;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gpio);
        nextTest = (Button) findViewById(R.id.nextTest);
        dispEth = (TextView) findViewById(R.id.dispEth);
        reEth = (Button) findViewById(R.id.reEth);
        stepCode = StepCode.getStepCodeClass(this.getApplicationContext());
        resultDate = ResultDate.getResultDate(this.getApplicationContext());
        reEth.setEnabled(false);
        nextTest.setEnabled(false);
        gpioTest = new GpioTest(new Handler());
        input1 = gpioTest.getGpioPin('E',2);
        input2 = gpioTest.getGpioPin('E',0);
        output1 = gpioTest.getGpioPin('E',3);
        output2 = gpioTest.getGpioPin('E',1);
        TestGpio();
        reEth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dispEth.setText("");
                TestGpio();
            }
        });
        nextTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    if (isGpioPass)
                        resultDate.WriteResultDate(ResultDate.GPIO,ResultDate.PASS,null);
                    else
                        resultDate.WriteResultDate(ResultDate.GPIO,ResultDate.UNPASS,null);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                stepCode.jumpActivity(StepCode.STEP_RTC,GPIOActivity.this.getApplicationContext(), Y7orY8orY10Activity.class);
                GPIOActivity.this.finish();
            }
        });
    }

    private void TestGpio(){
        dispEth.append("开始测试GPIO\n");
        gpioTest.setGpioIn(input1);
        gpioTest.setGpioHigh(output1);
        if(!gpioTest.isGpioGetHighValue(input1)){
            dispEth.append("GPIO I1 O1测试通过\n");
        }else{
            dispEth.append("GPIO I1 O1测试不通过\n");
            isGpioPass = false;
        }
        gpioTest.setGpioIn(input2);
        gpioTest.setGpioHigh(output2);
        if(!gpioTest.isGpioGetHighValue(input2)){
            dispEth.append("GPIO I2 O2测试通过\n");
        }else{
            dispEth.append("GPIO I2 O2测试不通过\n");
            isGpioPass = false;
        }
        nextTest.setEnabled(true);
        reEth.setEnabled(true);
    }
}
