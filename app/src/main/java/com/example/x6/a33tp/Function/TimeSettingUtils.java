package com.example.x6.a33tp.Function;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.example.x6.a33tp.Date.TypeManager;

/**
 * Created by 24179 on 2018/5/17.
 */

public class TimeSettingUtils {
    private final String TAG = "TimeSettingUtils";
    private SuCommand suCommand;
    private Context context;
    private Handler handler;
    private DatePickerDialog datePickerDialog;
    private TimePickerDialog timePickerDialog;
    private int Year = 0, Month = 0, Day = 0, Hour = 0, Minute = 0;

    @RequiresApi(api = Build.VERSION_CODES.N)
    public TimeSettingUtils(Context context1, SuCommand suCommand1, final Handler handler1){
        this.suCommand = suCommand1;
        this.context = context1;
        this.handler = handler1;
        datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                Log.e(TAG,String.valueOf(i)+" "+String.valueOf(i1)+" "+String.valueOf(i2));
                Year = i;
                Month = i1;
                Day = i2;
                showTimePick(true);
            }
        },2018,1,1);
        datePickerDialog.setCanceledOnTouchOutside(false);
        datePickerDialog.setCancelable(false);
        timePickerDialog = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                Log.e(TAG,String.valueOf(i)+" "+String.valueOf(i1));
                Hour = i;
                Minute = i1;
                handler.sendEmptyMessage(0);
            }
        },0,0,true);
        timePickerDialog.setCanceledOnTouchOutside(false);
        timePickerDialog.setCancelable(false);
    }

    public void showDatePick(boolean isShowDatePick){
        if(isShowDatePick)
            datePickerDialog.show();
        else
            datePickerDialog.dismiss();
    }

    public void showTimePick(boolean isShowTimePick){
        if(isShowTimePick)
            timePickerDialog.show();
        else
            timePickerDialog.dismiss();
    }

    public String getTimeDate(){
        String result = "";
        if(Month <  10 && Month >= 0){
            result += "0"+String.valueOf(Month);
        }else{
            result += String.valueOf(Month);
        }

        if(Day < 10 && Day >= 0){
            result += "0"+String.valueOf(Day);
        }else{
            result += String.valueOf(Day);
        }

        if(Hour <10 && Hour >=0){
            result += "0"+String.valueOf(Hour);
        }else{
            result += String.valueOf(Hour);
        }

        if(Minute < 10 && Minute >=0){
            result += "0"+String.valueOf(Minute);
        } else{
            result += String.valueOf(Minute);
        }

        if(Year < 10 && Year >= 0){
            result += "0"+String.valueOf(Year);
        }else{
            result += String.valueOf(Year);
        }
        return result;
    }

    /**
     *
     * @param timeZone 时区字符串，默认为GMT
     * @return 时区设置是否成功
     */
    public boolean setTimeZone(String timeZone){
        String cmd = "";
        if(timeZone == null || timeZone.equals("")){
            timeZone = "GMT";
        }
        cmd = "setprop persist.sys.timezone "+timeZone;
        if(suCommand.execRootCmdSilent(cmd) == 0)
            return true;
        else
            return false;
    }

    /**
     *
     * @param time 时间格式 mmddhhmmyyyy
     * @return 时间设置是否成功
     */
    public boolean setTime(String time){
        String cmd = "";
        cmd = "busybox date time " +time;
        if(suCommand.execRootCmdSilent(cmd) == 0)
            return true;
        else
            return false;
    }

}
