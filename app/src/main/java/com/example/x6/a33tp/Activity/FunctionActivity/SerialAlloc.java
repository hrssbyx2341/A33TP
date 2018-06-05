package com.example.x6.a33tp.Activity.FunctionActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.x6.a33tp.Activity.FunctionActivity.SerialActivity.K7K10232Serial;
import com.example.x6.a33tp.Activity.FunctionActivity.SerialActivity.K7K10485Serial;
import com.example.x6.a33tp.Activity.FunctionActivity.SerialActivity.Y5Serial;
import com.example.x6.a33tp.Activity.FunctionActivity.SerialActivity.Y7Y8Y10Serial;
import com.example.x6.a33tp.Activity.Y7orY8orY10Activity;
import com.example.x6.a33tp.Date.TypeManager;
import com.example.x6.a33tp.R;

/**
 * Created by 24179 on 2018/5/8.
 */

public class SerialAlloc extends Activity {
    private TypeManager typeManager;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.empty);
        typeManager = TypeManager.getTypeManager(this.getApplicationContext());
        switch (typeManager.getProductType()){
            case TypeManager.Y7:
            case TypeManager.Y8:
            case TypeManager.Y10:
                jumpActivity(K7K10232Serial.class);
                break;
            case TypeManager.K7232:
            case TypeManager.K10232:
                jumpActivity(K7K10232Serial.class);
                break;
            case TypeManager.K7485:
            case TypeManager.K10485:
                jumpActivity(K7K10232Serial.class);
                break;
            case TypeManager.Y5:
                jumpActivity(Y5Serial.class);
                break;
        }
    }
    private void jumpActivity(Class class1){
        Intent intent = new Intent(SerialAlloc.this,class1);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        SerialAlloc.this.finish();
    }
}
