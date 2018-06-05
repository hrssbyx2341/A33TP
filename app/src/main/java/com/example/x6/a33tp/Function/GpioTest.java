package com.example.x6.a33tp.Function;

import android.os.Handler;

import com.example.x6.gpioctl.GpioUtils;

/**
 * Created by 24179 on 2018/5/9.
 */

public class GpioTest {
    Handler handler;
    GpioUtils gpioUtils;
    public GpioTest(Handler handler1){
        this.handler = handler1;
        gpioUtils = GpioUtils.getGpioUtils();
    }
    public int getGpioPin(char ioGround, int ioNum){
        return gpioUtils.getGpioPin(ioGround,ioNum);
    }
    public void setTestGpioPort(int inputGpio, int outputGpio){

    }


    public boolean isGpioGetHighValue(int gpioNum){
        setGpioIn(gpioNum);
        if (gpioUtils.gpioGetValue(gpioNum) == 0)
            return false;
        else
            return true;
    };
    public int setGpioLow(int gpioNum){
        setGpioOut(gpioNum);
        return setGpioValues(gpioNum,true);
    }
    public int setGpioHigh(int gpioNum){
        setGpioOut(gpioNum);
        return setGpioValues(gpioNum,false);
    }
    public int setGpioOut(int gpioNum){
        return setGpioDirection(gpioNum,false);
    }
    public int setGpioIn(int gpioNum){
        return setGpioDirection(gpioNum,true);
    }
    private int setGpioValues(int gpioNum, boolean isLow){
        if(isLow)
            return gpioUtils.gpioSetValue(gpioNum,0);
        else
            return gpioUtils.gpioSetValue(gpioNum,1);
    }
    private int setGpioDirection(int gpioNum, boolean isIn){
        if(isIn)
            return gpioUtils.setGpioDirection(gpioNum,1);
        else
            return gpioUtils.setGpioDirection(gpioNum,0);
    }

}
