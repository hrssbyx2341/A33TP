package com.example.x6.a33tp.Activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.x6.a33tp.Date.ResultDate;
import com.example.x6.a33tp.Date.StepCode;
import com.example.x6.a33tp.Date.TypeManager;
import com.example.x6.a33tp.R;

import java.io.IOException;

public class MainActivity extends Activity {
    private final String TAG = "MainActivity";
    private CheckBox y5,y7,y8,y10,k7232,k10232,k7485,k10485;
    private EditText uuid;
    private Button submit;
    private ResultDate resultDate;
    private TypeManager typeManager;
    private StepCode stepCode;
    private String type = null;
    private boolean isUUIDExited = false;
    private boolean isTYPEExited = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        try {
            if(resultDate.getUUID() != null) {
                uuid.setText(resultDate.getUUID());
                uuid.setEnabled(false);
                isUUIDExited = true;
            }
            if(resultDate.getType() != null){
                isTYPEExited = true;
                if(resultDate.getType().equals(TypeManager.STR_Y5)){
                    y5.setChecked(true);
                    checkBoxSetEnable(false);
                }else if(resultDate.getType().equals(TypeManager.STR_Y7)){
                    y7.setChecked(true);
                    checkBoxSetEnable(false);
                }
                else if(resultDate.getType().equals(TypeManager.STR_Y8)){
                    y8.setChecked(true);
                    checkBoxSetEnable(false);
                }
                else if(resultDate.getType().equals(TypeManager.STR_Y10)){
                    y10.setChecked(true);
                    checkBoxSetEnable(false);
                }
                else if(resultDate.getType().equals(TypeManager.STR_K10485)){
                    k10485.setChecked(true);
                    checkBoxSetEnable(false);
                }
                else if(resultDate.getType().equals(TypeManager.STR_K10232)){
                    k10232.setChecked(true);
                    checkBoxSetEnable(false);
                }
                else if(resultDate.getType().equals(TypeManager.STR_K7485)){
                    k7485.setChecked(true);
                    checkBoxSetEnable(false);
                }
                else if(resultDate.getType().equals(TypeManager.STR_K7232)){
                    k7232.setChecked(true);
                    checkBoxSetEnable(false);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(isTYPEExited && isUUIDExited){
            Intent intent = new Intent(MainActivity.this,Y7orY8orY10Activity.class); //跳转到调度界面
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            MainActivity.this.finish();
        }
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    if(uuid.getText().toString().equals("") || type == null){
                        Toast.makeText(MainActivity.this,"请输入识别码和选择产品型号，再进行测试",Toast.LENGTH_LONG).show();
                        return;
                    }
                    if(resultDate.getUUID() == null){
                        resultDate.setUUID(uuid.getText().toString());
                    }
                    if(resultDate.getType() == null){
                        resultDate.setTYPE(type);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent(MainActivity.this,Y7orY8orY10Activity.class); //跳转到调度界面
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                MainActivity.this.finish();
            }
        });
    }

    private void checkBoxSetEnable(boolean isEnable){
        y5.setEnabled(isEnable);
        y7.setEnabled(isEnable);
        y8.setEnabled(isEnable);
        y10.setEnabled(isEnable);
        k7232.setEnabled(isEnable);
        k10232.setEnabled(isEnable);
        k7485.setEnabled(isEnable);
        k10485.setEnabled(isEnable);
    }

    private void initView() {
        y5 = (CheckBox) findViewById(R.id.y5);
        y7 = (CheckBox) findViewById(R.id.y7);
        y8 = (CheckBox) findViewById(R.id.y8);
        y10 = (CheckBox) findViewById(R.id.y10);
        k7232 = (CheckBox) findViewById(R.id.k7232);
        k10232 = (CheckBox) findViewById(R.id.k10232);
        k7485 = (CheckBox) findViewById(R.id.k7485);
        k10485 = (CheckBox) findViewById(R.id.k10485);

        uuid = (EditText) findViewById(R.id.uuid);
        uuid.requestFocus();
        submit = (Button) findViewById(R.id.submit);

        typeManager = TypeManager.getTypeManager(this.getApplicationContext());
        stepCode = StepCode.getStepCodeClass(this.getApplicationContext());
        resultDate = ResultDate.getResultDate(this.getApplicationContext());
    }

    private void clearBox(){
        y5.setChecked(false);
        y7.setChecked(false);
        y8.setChecked(false);
        y10.setChecked(false);
        k7232.setChecked(false);
        k10232.setChecked(false);
        k7485.setChecked(false);
        k10485.setChecked(false);
    }
    public void boxClick(View v){
        clearBox();
        switch (v.getId()){
            case R.id.y5:
                y5.setChecked(true);
                type = TypeManager.STR_Y5;
                break;
            case R.id.y7:
                y7.setChecked(true);
                type = TypeManager.STR_Y7;
                break;
            case R.id.y8:
                y8.setChecked(true);
                type = TypeManager.STR_Y8;
                break;
            case R.id.y10:
                y10.setChecked(true);
                type = TypeManager.STR_Y10;
                break;
            case R.id.k7232:
                k7232.setChecked(true);
                type = TypeManager.STR_K7232;
                break;
            case R.id.k10232:
                k10232.setChecked(true);
                type = TypeManager.STR_K10232;
                break;
            case R.id.k7485:
                k7485.setChecked(true);
                type = TypeManager.STR_K7485;
                break;
            case R.id.k10485:
                k10485.setChecked(true);
                type = TypeManager.STR_K10485;
                break;
        }
    }

}
