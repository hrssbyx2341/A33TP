package com.example.x6.a33tp.Activity.FunctionActivity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.x6.a33tp.Activity.Y7orY8orY10Activity;
import com.example.x6.a33tp.Date.ResultDate;
import com.example.x6.a33tp.Date.StepCode;
import com.example.x6.a33tp.Date.TypeManager;
import com.example.x6.a33tp.Function.PointerLocationView;
import com.example.x6.a33tp.R;

import java.io.IOException;

/**
 * Created by Administrator on 2018/5/7.
 */

public class ScreenTotalActivity extends Activity {
    private final String TAG = "ScreenTotalActivity";
    PointerLocationView pointerLocationView;
    private static final int MAX_TOUCHPOINTS = 10;
    private TextView[] textViews;
    private Button badpointyes,badpointno,touchyes,touchno,nextTest,reTest;
    private boolean isbadPointTested = false, isTouchTested = false;
    private StepCode stepCode;
    private ResultDate resultDate;
    private boolean isBadPointPass = false;
    private boolean isTouchPass = false;

    int sourceHeight;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_total);
        textViews = new TextView[MAX_TOUCHPOINTS];
        for(int i = 0; i < 10; i++){
            textViews[i] = (TextView) findViewById(R.id.point1+i);
        }
        initView();
        badpointyes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isBadPointPass = true;
                badpointno.setEnabled(false);
                badpointyes.setEnabled(false);
                isbadPointTested = true;
                if(isTouchTested && isbadPointTested) {
                    nextTest.setEnabled(true);
                    reTest.setEnabled(true);
                }
            }
        });
        badpointno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isBadPointPass = false;
                badpointyes.setEnabled(false);
                badpointno.setEnabled(false);
                isbadPointTested = true;
                if(isTouchTested && isbadPointTested) {
                    nextTest.setEnabled(true);
                    reTest.setEnabled(true);
                }
            }
        });
        touchno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isTouchPass = false;
                touchyes.setEnabled(false);
                touchno.setEnabled(false);
                isTouchTested = true;
                if(isTouchTested && isbadPointTested) {
                    nextTest.setEnabled(true);
                    reTest.setEnabled(true);
                }
            }
        });
        touchyes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isTouchPass = true;
                touchyes.setEnabled(false);
                touchno.setEnabled(false);
                isTouchTested = true;
                if(isTouchTested && isbadPointTested) {
                    nextTest.setEnabled(true);
                    reTest.setEnabled(true);
                }
            }
        });
        nextTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if(isBadPointPass)
                        resultDate.WriteResultDate(ResultDate.SCREEN,ResultDate.PASS,null);
                    else
                        resultDate.WriteResultDate(ResultDate.SCREEN,ResultDate.UNPASS,null);
                    if(isTouchPass)
                        resultDate.WriteResultDate(ResultDate.TOUCH,ResultDate.PASS,null);
                    else
                        resultDate.WriteResultDate(ResultDate.TOUCH,ResultDate.UNPASS,null);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                stepCode.jumpActivity(StepCode.STEP_UART,ScreenTotalActivity.this.getApplicationContext(), Y7orY8orY10Activity.class);
                ScreenTotalActivity.this.finish();
            }
        });
        reTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reTest.setEnabled(false);
                nextTest.setEnabled(false);
                badpointno.setEnabled(true);
                badpointyes.setEnabled(true);
                touchyes.setEnabled(true);
                touchno.setEnabled(true);
            }
        });
    }

    private void initView() {
        badpointno = (Button) findViewById(R.id.badpointno);
        badpointyes = (Button) findViewById(R.id.badpointyes);
        touchno = (Button) findViewById(R.id.touchno);
        touchyes = (Button) findViewById(R.id.touchyes);
        nextTest = (Button) findViewById(R.id.nextTest);
        stepCode = StepCode.getStepCodeClass(ScreenTotalActivity.this.getApplicationContext());
        resultDate = ResultDate.getResultDate(this.getApplicationContext());
        reTest = (Button) findViewById(R.id.reTest);
        reTest.setEnabled(false);

        nextTest.setEnabled(false);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int pointerCount=event.getPointerCount();
        if (pointerCount>MAX_TOUCHPOINTS){
            pointerCount=MAX_TOUCHPOINTS;
        }
            if (event.getAction()==MotionEvent.ACTION_UP){
//                当手离开屏幕是，清屏
                for(TextView textView1 : textViews)
                    textView1.setText("");
            }else {
                for(int i=0;i<pointerCount;i++){
                    int id=event.getPointerId(i);
                    int x=(int) event.getX(i);
                    int y=(int) event.getY(i);
                    textViews[id].setText("手指"+String.valueOf(id)+": "+"X: "+String.valueOf(x)+" Y: "+String.valueOf(y) );
//                    Log.e(TAG,"手指"+String.valueOf(id)+": "+"X: "+String.valueOf(x)+" Y: "+String.valueOf(y));
                }
            }
        return super.onTouchEvent(event);
    }

    public void full_screen(){
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE
        );
    }
}
