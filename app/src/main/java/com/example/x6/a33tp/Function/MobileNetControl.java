package com.example.x6.a33tp.Function;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;

/**
 * Created by 24179 on 2018/5/7.
 */

public class MobileNetControl {
    public MobileNetControl(){
        suCommand = new SuCommand();
    };
    private final static String COMMAND_AIRPLANE_ON = "settings put global airplane_mode_on 1 \n" +
            "am broadcast -a android.intent.action.AIRPLANE_MODE --ez state true\n";

    private final static String COMMAND_AIRPLANE_OFF = "settings put global airplane_mode_on 0 \n" +
            " am broadcast -a android.intent.action.AIRPLANE_MODE --ez state false\n ";
    private SuCommand suCommand;
    public void openMobileNet(){
        suCommand.execRootCmdSilent(COMMAND_AIRPLANE_OFF);
    }
    public void closeMobuleNet(){
        suCommand.execRootCmdSilent(COMMAND_AIRPLANE_ON);
    }
}
