package com.example.x6.a33tp.Date;

import android.content.Context;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by 24179 on 2018/5/11.
 */

public class SaveRtc {
    private Context context = null;
    private FileInputStream fileInputStream = null;
    private FileOutputStream fileOutputStream = null;
    private final String rtcFilePath = "rtc.txt";

    public static final String rtcCmd = "date +\"%Y-%m-%d\"";

    public SaveRtc(Context context1){
        this.context = context1;
    }
    public int WriteRTC(String date) throws IOException {
        fileOutputStream = context.openFileOutput(rtcFilePath,Context.MODE_APPEND|Context.MODE_WORLD_READABLE);
        fileInputStream = context.openFileInput(rtcFilePath);
        String str = "RTC="+date+"\n";
        if(date == null){
            fileInputStream.close();
            fileOutputStream.close();
            return -1;
        }else{
            fileOutputStream.write(str.getBytes());
            fileOutputStream.flush();
            fileInputStream.close();
            fileOutputStream.close();
            return 0;
        }
    }

    public String ReadRTC() throws IOException {
        fileOutputStream = context.openFileOutput(rtcFilePath,Context.MODE_APPEND|Context.MODE_WORLD_READABLE);
        fileInputStream = context.openFileInput(rtcFilePath);
        String result = null;
        StringBuilder sb = new StringBuilder("");
        if (fileInputStream != null){
            BufferedReader d = new BufferedReader(new InputStreamReader(fileInputStream));
            while (d.ready()) {
                String str = d.readLine();
                if(str.indexOf("RTC=") != -1){
                    result = str.substring(str.indexOf("RTC=")+"RTC=".length(),str.length());
                }
            }
            fileInputStream.close();
            fileOutputStream.close();
        }
        return result;
    }

    public void deleteRTCDate(){
        context.deleteFile(rtcFilePath);
    }

}
