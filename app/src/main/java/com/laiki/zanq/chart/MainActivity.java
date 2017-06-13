package com.laiki.zanq.chart;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;

import com.laiki.zanq.chart.view.PieChartView;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        int contentTop = getWindow().findViewById(Window.ID_ANDROID_CONTENT)
                .getTop();
        PieChartView view = (PieChartView) findViewById(R.id.view);
        view.setContentTop(contentTop);
    }
}
