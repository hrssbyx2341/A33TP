package com.example.x6.a33tp.Activity.FunctionActivity;

import android.app.Activity;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import com.example.x6.a33tp.Activity.Y7orY8orY10Activity;
import com.example.x6.a33tp.Date.ResultDate;
import com.example.x6.a33tp.Date.StepCode;
import com.example.x6.a33tp.Function.SuCommand;
import com.example.x6.a33tp.R;

import java.io.IOException;

/**
 * Created by 24179 on 2018/5/5.
 */

public class USBActivity extends Activity {
    private Button usbyes,usbno, reUsb, nextTest;

    private ResultDate resultDate;
    private StepCode stepCode;
    private boolean isUSBPass = false;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.usb);
        initView();

        usbno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reUsb.setEnabled(true);
                usbno.setEnabled(false);
                usbyes.setEnabled(false);
                nextTest.setEnabled(true);
                isUSBPass = false;
            }
        });
        usbyes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reUsb.setEnabled(true);
                usbno.setEnabled(false);
                usbyes.setEnabled(false);
                nextTest.setEnabled(true);
                isUSBPass = true;
            }
        });
        reUsb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reUsb.setEnabled(false);
                usbno.setEnabled(true);
                usbyes.setEnabled(true);
                nextTest.setEnabled(false);
            }
        });
        nextTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if(isUSBPass)
                        resultDate.WriteResultDate(ResultDate.USB,ResultDate.PASS,null);
                    else
                        resultDate.WriteResultDate(ResultDate.USB,ResultDate.UNPASS,null);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                stepCode.jumpActivity(StepCode.STEP_ETH,USBActivity.this, Y7orY8orY10Activity.class);
                USBActivity.this.finish();
            }
        });
    }

    private void initView() {
        usbno = (Button) findViewById(R.id.usbno);
        usbyes = (Button) findViewById(R.id.usbyes);
        reUsb = (Button) findViewById(R.id.reUsb);
        nextTest = (Button) findViewById(R.id.nextTest);
        nextTest.setEnabled(false);

        reUsb.setEnabled(false);
        resultDate = ResultDate.getResultDate(this.getApplicationContext());
        stepCode = StepCode.getStepCodeClass(USBActivity.this.getApplicationContext());
    }
}
