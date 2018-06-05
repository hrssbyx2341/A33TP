package com.example.x6.a33tp.Activity.FunctionActivity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import com.example.x6.a33tp.Activity.MainActivity;
import com.example.x6.a33tp.Activity.Y7orY8orY10Activity;
import com.example.x6.a33tp.Date.ResultDate;
import com.example.x6.a33tp.Date.StepCode;
import com.example.x6.a33tp.Date.TypeManager;
import com.example.x6.a33tp.Function.StartApk;
import com.example.x6.a33tp.R;

import java.io.IOException;

/**
 * Created by 24179 on 2018/5/4.
 */

public class RecoderActivity extends Activity {
    private Button recoYes,recoNo,reReco, nextTest;
    private ResultDate resultDate;
    private StepCode stepCode;
    private StartApk startApk;
    private boolean isRecorderPass = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recoder);
        initView();
        startApk = new StartApk();
        startApk.startPro("录音机",RecoderActivity.this);
        recoYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isRecorderPass = true;
                nextTest.setEnabled(true);
                recoNo.setEnabled(false);
                recoYes.setEnabled(false);
            }
        });
        recoNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isRecorderPass = false;
                nextTest.setEnabled(true);
                recoNo.setEnabled(false);
                recoYes.setEnabled(false);
            }
        });
        reReco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startApk.startPro("录音机",RecoderActivity.this);
                recoNo.setEnabled(true);
                recoYes.setEnabled(true);
                nextTest.setEnabled(false);
            }
        });
        nextTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if(isRecorderPass)
                        resultDate.WriteResultDate(ResultDate.RECORDER, ResultDate.PASS,null);
                    else
                        resultDate.WriteResultDate(ResultDate.RECORDER, ResultDate.UNPASS,null);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                stepCode.jumpActivity(StepCode.STEP_TFCRAD, RecoderActivity.this, Y7orY8orY10Activity.class);
                RecoderActivity.this.finish();
            }
        });

    }

    private void initView() {
        recoNo = (Button) findViewById(R.id.recono);
        recoYes = (Button) findViewById(R.id.recoyes);
        reReco = (Button) findViewById(R.id.reReco);
        nextTest = (Button) findViewById(R.id.nextTest);
        nextTest.setEnabled(false);

        resultDate = ResultDate.getResultDate(this.getApplicationContext());
        stepCode = StepCode.getStepCodeClass(RecoderActivity.this.getApplicationContext());
    }
}
