package com.example.x6.a33tp.Function;

import android.app.TimePickerDialog;
import android.content.Context;

/**
 * Created by 24179 on 2018/5/18.
 */

public class MyTimePickDialog extends TimePickerDialog {
    public MyTimePickDialog(Context context, OnTimeSetListener listener, int hourOfDay, int minute, boolean is24HourView) {
        super(context, listener, hourOfDay, minute, is24HourView);
    }

    public MyTimePickDialog(Context context, int themeResId, OnTimeSetListener listener, int hourOfDay, int minute, boolean is24HourView) {
        super(context, themeResId, listener, hourOfDay, minute, is24HourView);
    }

    @Override
    protected void onStop() {

    }
}
