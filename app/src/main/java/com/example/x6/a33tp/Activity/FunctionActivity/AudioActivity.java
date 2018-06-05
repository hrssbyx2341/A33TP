package com.example.x6.a33tp.Activity.FunctionActivity;

import android.app.Activity;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import com.example.x6.a33tp.Activity.Y7orY8orY10Activity;
import com.example.x6.a33tp.Date.ResultDate;
import com.example.x6.a33tp.Date.StepCode;
import com.example.x6.a33tp.Date.TypeManager;
import com.example.x6.a33tp.R;

import java.io.IOException;

/**
 * Created by 24179 on 2018/5/4.
 */

public class AudioActivity extends Activity {
    MediaPlayer mp = new MediaPlayer();
    private Button spkyes,spkno,hpyes,hpno,reTest,nextTest;
    private ResultDate resultDate;
    boolean isSpkTested = false,isHpTested = false;
    private StepCode stepCode;
    private TypeManager typeManager;
    boolean isSpkPass = false;
    boolean isHpPass = false;

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(isHpTested && isSpkTested){
                reTest.setEnabled(true);
                nextTest.setEnabled(true);
            }
        }
    };
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.audio);
        startRing();
        initView();
        resultDate = ResultDate.getResultDate(this.getApplicationContext());
        stepCode = StepCode.getStepCodeClass(this.getApplicationContext());
        typeManager = TypeManager.getTypeManager(this.getApplicationContext());

        reTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reTest.setEnabled(false);
                spkno.setEnabled(true);
                spkyes.setEnabled(true);
                hpno.setEnabled(true);
                hpyes.setEnabled(true);
                nextTest.setEnabled(false);
                isSpkTested = false;
                isHpTested = false;
                isHpPass = false;
                isSpkPass = false;
            }
        });
        nextTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mp.stop();
                try {
                    if(isSpkPass){
                        resultDate.WriteResultDate(ResultDate.SPEAK,ResultDate.PASS,null);
                    }else{
                        resultDate.WriteResultDate(ResultDate.SPEAK,ResultDate.UNPASS,null);
                    }
                    if(isHpPass){
                        resultDate.WriteResultDate(ResultDate.HEADPHONE,ResultDate.PASS,null);
                    }else{
                        resultDate.WriteResultDate(ResultDate.HEADPHONE,ResultDate.UNPASS,null);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                stepCode.jumpActivity(StepCode.STEP_RECORE, AudioActivity.this, Y7orY8orY10Activity.class);
                AudioActivity.this.finish();
            }
        });


    }

    public void auClick(View v){
        handler.sendEmptyMessage(0);
        switch (v.getId()){
            case R.id.spkno:
                spkyes.setEnabled(false);
                spkno.setEnabled(false);
                isSpkPass = false;
                isSpkTested = true;
                break;
            case R.id.spkyes:
                spkyes.setEnabled(false);
                spkno.setEnabled(false);
                isSpkPass = true;
                isSpkTested = true;
                break;
            case R.id.hpno:
                hpyes.setEnabled(false);
                hpno.setEnabled(false);
                isHpPass = false;
                isHpTested = true;
                break;
            case R.id.hpyes:
                hpyes.setEnabled(false);
                hpno.setEnabled(false);
                isHpPass = true;
                isHpTested = true;
                break;
        }
    }

    private void initView() {
        spkno = (Button) findViewById(R.id.spkno);
        spkyes = (Button) findViewById(R.id.spkyes);
        hpno = (Button) findViewById(R.id.hpno);
        hpyes = (Button) findViewById(R.id.hpyes);
        reTest = (Button) findViewById(R.id.reTest);
        nextTest = (Button) findViewById(R.id.nextTest);
        nextTest.setEnabled(false);
        reTest.setEnabled(false);

    }

    private void startRing(){
        try {
            mp.setDataSource(this, RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            mp.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mp.start();
        mp.setLooping(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mp.stop();
    }
}
