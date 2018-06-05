package com.example.x6.gpioctl;

import android.util.Log;

import java.io.DataOutputStream;

/**
 * Created by X6 on 2017/9/22.
 */

public class GpioUtils {
    private static final String TAG = "GpioCtl";
    private static Process su;
    private static DataOutputStream os;
    private static GpioUtils gpioUtils = null;

	public static final int GPIO_DIRECTION_IN = 1;
    public static final int GPIO_DIRECTION_OUT = 0;
    public static final int GPIO_VALUE_HIGH = 1;
    public static final int GPIO_VALUE_LOW = 0;
	
    private  GpioUtils(){
        int tag ;
        Chmod();
        for (tag = 0; tag < 3; ){
            if(Open() < 0){
                Log.e("GPIO","节点打开失败尝试再一次打开:"+"["+String.valueOf(tag+1)+"]");
                tag++;
                try {
                    Thread.sleep(1*1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }else{
                break;
            }
        }
    }

    public synchronized static GpioUtils getGpioUtils(){
        if( gpioUtils == null){
            gpioUtils = new GpioUtils();
        }
        return gpioUtils;
    }

    /*
      改变文件权限
     */
    private void Chmod(){
        try {
            su = Runtime.getRuntime().exec("su");
            os = new DataOutputStream(su.getOutputStream());
        } catch(Exception e) {
            e.printStackTrace();
        }
        try{
            os.writeBytes("chmod 777 /dev/sunxi_gpio\n");
            os.flush();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public int getGpioPin(char ioGround, int ioNum){
        return ((ioGround - 'A')*32 +ioNum);
    }

    public void close(){
        gpioUtils.Close();
        gpioUtils = null;
    }

    private native static int Open();
    public native static int setGpioDirection(int io, int isIn);
    public native static int gpioGetValue(int io);
    public native static int gpioSetValue(int io, int value);
    private native void Close();

    static {
        System.loadLibrary("Ioctl");
    }

}
