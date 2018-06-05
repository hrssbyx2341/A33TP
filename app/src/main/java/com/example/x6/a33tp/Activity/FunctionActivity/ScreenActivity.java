package com.example.x6.a33tp.Activity.FunctionActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.x6.a33tp.R;

/**
 * Created by X6 on 2017/9/8.
 */

public class ScreenActivity extends AppCompatActivity {
    private Button checkscreen;
    private int pageNum = 7;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        full_screen();
        setContentView(R.layout.screen);
        checkscreen = (Button) findViewById(R.id.checkscreen);
        checkscreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                full_screen();
                switch (pageNum){
                    case 0:
                        Intent intent = new Intent(ScreenActivity.this.getApplicationContext(),ScreenTotalActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        ScreenActivity.this.finish();
                        break;
                    case 1:
                        checkscreen.setBackgroundResource(R.drawable.screen_test_o);
                        break;
                    case 2:
                        checkscreen.setBackgroundResource(R.drawable.screen_test_t);
                        break;
                    case 3:
                        checkscreen.setBackgroundColor(Color.parseColor("#ff0000"));
                        break;
                    case 4:
                        checkscreen.setBackgroundColor(Color.parseColor("#00ff00"));
                        break;
                    case 5:
                        checkscreen.setBackgroundColor(Color.parseColor("#0000ff"));
                        break;
                    case 6:
                        checkscreen.setBackgroundColor(Color.parseColor("#ffffff"));
                        break;
                    case 7:
                        checkscreen.setBackgroundColor(Color.parseColor("#000000"));
                        break;
                    default:
                        break;
                }
                pageNum--;
            }
        });
    }


    public void full_screen(){
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE
        );
    }
}
