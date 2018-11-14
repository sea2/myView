package com.ispring.canvasdemo.rain;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.ispring.canvasdemo.R;

public class RainActivity extends AppCompatActivity {

    private RainView rainView;
    private Button btnCake;
    private Button btnDog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_rain);
        rainView = (RainView) findViewById(R.id.testView);
        btnCake = (Button) findViewById(R.id.btn_cake);
        btnDog = (Button) findViewById(R.id.btn_dog);
        btnCake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rainView.setImgResId(R.mipmap.cake);
                rainView.start(true);
            }
        });
        btnDog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rainView.setImgResId(R.mipmap.dog);
                rainView.start(true);
            }
        });


    }
}
