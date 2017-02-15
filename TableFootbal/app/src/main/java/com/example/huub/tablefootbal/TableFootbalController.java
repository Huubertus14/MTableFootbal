package com.example.huub.tablefootbal;

import android.app.Activity;
import android.content.Context;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class TableFootbalController extends Activity {

    public SensorManager sensorManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //Sensor shit
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        //Test Commit
        setContentView(new GamePanel(this, sensorManager));

    }
}
