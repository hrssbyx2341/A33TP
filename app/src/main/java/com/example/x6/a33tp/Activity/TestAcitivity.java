package com.example.x6.a33tp.Activity;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.util.Xml;

import com.example.x6.a33tp.Date.ResultDate;
import com.example.x6.a33tp.Date.StepCode;
import com.example.x6.a33tp.Date.TypeManager;
import com.example.x6.a33tp.Function.SuCommand;
import com.example.x6.a33tp.Function.TimeSettingUtils;
import com.example.x6.a33tp.R;

import org.apache.http.util.EncodingUtils;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by 24179 on 2018/5/10.
 */

public class TestAcitivity extends Activity {
    private final String TAG = "TestAcitivity";
    private ResultDate resultDate;
    private TypeManager typeManager;
    private StepCode stepCode;
    private TimeSettingUtils timeSettingUtils;
    private SuCommand suCommand;




    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    Log.e(TAG,timeSettingUtils.getTimeDate());
                    break;
            }
        }
    };
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.empty);
        suCommand = new SuCommand();
        timeSettingUtils = new TimeSettingUtils(this,suCommand,handler);
        timeSettingUtils.showDatePick(true);
    }

}
