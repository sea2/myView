package com.ispring.canvasdemo;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

import com.ispring.canvasdemo.ui.ButtonProgress;
import com.ispring.canvasdemo.ui.CircleProgress;
import com.ispring.canvasdemo.ui.ClockView;
import com.ispring.canvasdemo.ui.Progress2View;

public class ProgressActivity extends Activity {

    private com.ispring.canvasdemo.ui.Progress2View progress2;
    private Button btn_clock_stop;
    private ClockView clock_view;
    private Button btn_clock_stop2;
    private Button btn_clock_stop3;
    private ClockView clockview;
    private Button btnclockstop;
    private Button btnclockstop2;
    private Button btnclockstop3;
    private ButtonProgress btnpg;
    private com.ispring.canvasdemo.ui.CircleProgress circleprogress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress);
        this.circleprogress = (CircleProgress) findViewById(R.id.circle_progress);
        this.progress2 = (Progress2View) findViewById(R.id.progress2);
        ButtonProgress btn_pg = (ButtonProgress) findViewById(R.id.btn_pg);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                progress2.setProgressInt(250);

                circleprogress.setProgressint(0.8f);
            }
        }, 3000);

        progress2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progress2.setProgressInt(270);
            }
        });


        btn_clock_stop = (Button) findViewById(R.id.btn_clock_stop);
        btn_clock_stop2 = (Button) findViewById(R.id.btn_clock_stop2);
        btn_clock_stop3 = (Button) findViewById(R.id.btn_clock_stop3);
        clock_view = (ClockView) findViewById(R.id.clock_view);

        btn_clock_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clock_view.cancel();
            }
        });
        btn_clock_stop2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clock_view.stop();
            }
        });

        btn_clock_stop3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clock_view.restart();
            }
        });
    }


}
