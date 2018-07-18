package dev.mevur.com.scrollview;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    private EndlessHorizontalScrollView scrollView;

    private Sensor accelerometer;
    private Sensor magnetic;

    private SensorManager sensorManager;
    private Sensor gyroScope;

    private float[] accelerometerValues = new float[3];
    private float[] magneticFieldValues = new float[3];

    private float north = -11113332;

    private long timestamp;

    private static final float NS2S = 1.0f / 1000000000.0f;
    private float[] angle = new float[3];

    private float oldAngely = -111111113;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        scrollView = findViewById(R.id.scrollView);

    }

    public void addView(View view) {
        scrollView.addImg();
    }




}
