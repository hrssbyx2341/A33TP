package com.example.x6.a33tp.Date;

import android.content.ContentResolver;
import android.content.Context;
import android.provider.Settings;
import android.util.Log;

import com.example.x6.a33tp.Activity.FunctionActivity.BlueToothActivity;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by 24179 on 2018/5/4.
 */

public class ResultDate {
    private final String TAG="ResultDate";

    private Context context = null;
    private FileInputStream fileInputStream = null;
    private FileOutputStream fileOutputStream = null;

    public final static String filePath = "tempFile.txt";


    public final static String UUID = "唯一识别码";
    public final static String TYPE = "型号";
    public final static String SPEAK = "外放";
    public final static String HEADPHONE = "耳机";
    public final static String RECORDER = "录音";
    public final static String TFCARD = "TF卡";
    public final static String USB = "USB";
//    public final static String KEY = "按键";
    public final static String RTC = "RTC(实时时钟)";
    public final static String WIFI = "WIFI(无线网)";
    public final static String BLUETOOTH = "蓝牙";
    public final static String ETH = "以太网";
    public final static String MOBILENET = "移动网络";
    public final static String TOUCH = "触摸屏";
    public final static String SCREEN = "显示屏";
    public final static String UART = "232串口";
    public final static String UART485 = "485串口";
    public final static String GPIO = "GPIO";

    public final static String PASS = "通过";
    public final static String UNPASS = "不通过";
    public final static String UNSUPPORT = "不支持";
    public final static String REASON = "未知";

    public final static String[] DEVICE_TABLE = new String[]{
            UUID,TYPE,SPEAK,RECORDER,TFCARD,USB,ETH,WIFI,MOBILENET,BLUETOOTH,TOUCH,UART,UART485,GPIO,RTC
    };
    public int getLastStep() throws IOException {
        int result = -1;
            int i = 0;
            for(i = 0; i < DEVICE_TABLE.length; i++) {
                if (readLine(DEVICE_TABLE[i]) == null){
                    result = i;
                    Log.e(TAG,String.valueOf(i));
                    break;
                }
                if(i == DEVICE_TABLE.length-1){
                    result = DEVICE_TABLE.length;
                }
            }
            return result;
    }


    public StringBuffer readResultDate() throws IOException {
        fileOutputStream = context.openFileOutput(filePath,Context.MODE_APPEND|Context.MODE_WORLD_READABLE);
        fileInputStream = context.openFileInput(filePath);
        StringBuffer result = new StringBuffer();
        StringBuilder sb = new StringBuilder("");
        if (fileInputStream != null){
            BufferedReader d = new BufferedReader(new InputStreamReader(fileInputStream));
            while (d.ready()) {
                result.append(d.readLine()+"\n");
            }
            fileInputStream.close();
            fileOutputStream.close();
        }
        return result;
    }
/*
    功能：根据设备名字读取他的内容
    参数：item 要查找的设备名
    返回值：成功返回查找到的内容字符串，失败返回null
 */
    public String readLine(String item) throws IOException {
        fileOutputStream = context.openFileOutput(filePath,Context.MODE_APPEND|Context.MODE_WORLD_READABLE);
        fileInputStream = context.openFileInput(filePath);
        String result = null;
        StringBuilder sb = new StringBuilder("");
            if (fileInputStream != null){
                BufferedReader d = new BufferedReader(new InputStreamReader(fileInputStream));
                while (d.ready()) {
                    String str = d.readLine();
                    if(str.indexOf(item) != -1){
                        result = str.substring(str.indexOf(item)+item.length()+1,str.length());
                    }
                }
                fileInputStream.close();
                fileOutputStream.close();
            }
            return result;
    };
    /*
    功能：写入设备测试结果
    参数：device：所测试的设备；result：测试结果；reason：原因
    返回值：-1 写入失败，0 写入成功
 */
    public int WriteResultDate(String device, String result, String reason) throws IOException {
        fileOutputStream = context.openFileOutput(filePath,Context.MODE_APPEND|Context.MODE_WORLD_READABLE);
        fileInputStream = context.openFileInput(filePath);
        String str = getResultString(device,result,reason);
        if(str == null){
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
    public void deleteResultDate(){
        context.deleteFile(filePath);
    }


    public String getType() throws IOException {
        return readLine(TYPE);
    }

    /*
    功能：生成测试结果字符串
    参数：device：所测试的设备；result：测试结果；reason：原因
    返回值：成功返回生成的字符串，失败返回null；
     */
    public String getResultString(String device, String result, String reason){
        if(device.equals(UUID)){
            return device+"="+result+"\n";
        }else if(device.equals(TYPE)){
            return device+"="+result+"\n";
        }else if(result.equals(PASS))
            return device+"="+result+"\n";
        else if(result.equals(UNPASS))
            return device+"="+result+"\n";
        else if(result.equals(UNSUPPORT))
            return device+"="+result+"\n";
        else
            return null;
    }

    private final String uuidKey = "uuidKeyPrivate";
    private boolean isUUIDExit(){
        if (Settings.System.getString(context.getContentResolver(), uuidKey) == null
                || Settings.System.getString(context.getContentResolver(), uuidKey).equals("")){
            return false;
        }else
            return true;
    }
    public int writeUUID(String uuid) throws IOException {
        Settings.System.putString(context.getContentResolver(), uuidKey, uuid);
        return 0;
    }
//    public String getUUID() throws IOException {
//        if(isUUIDExit())
//            return Settings.System.getString(context.getContentResolver(), uuidKey);
//        else
//            return null;
//    }
    public String getUUID() throws IOException {
        return readLine(UUID);
    }
    public int setUUID(String uuid) throws IOException {
//        writeUUID(uuid);
        return WriteResultDate(UUID,uuid,null);
    }
    public int setTYPE(String type) throws IOException {
        if(type.equals(TypeManager.STR_K10485)||type.equals(TypeManager.STR_K10232)
                ||type.equals(TypeManager.STR_K7485)||type.equals(TypeManager.STR_K7232)
                ||type.equals(TypeManager.STR_Y10)||type.equals(TypeManager.STR_Y5)
                ||type.equals(TypeManager.STR_Y7)||type.equals(TypeManager.STR_Y8)) {
            return WriteResultDate(TYPE, type, null);
        }
        else
            return -1;
    }

    private static ResultDate resultDate = null;
    private ResultDate(Context context1){
        this.context = context1;
    }


    public static ResultDate getResultDate(Context context1){
        if (resultDate == null)
            return new ResultDate(context1);
        return resultDate;
    }
}
