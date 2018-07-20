package dev.mevur.com.scrollview;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.vr.sdk.widgets.pano.VrPanoramaView;

public class MainActivity extends AppCompatActivity
        implements SensorEventListener {
    private static final float NANO_SECOND_TO_SECOND = 1.0F / 1000000000.0F;
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

//    private double oldAngely = -111111113;

    private double oldAngelx = -111111113;
    private double oldAngely = -111111113;
    private double oldAngelz = -111111113;

    private double newAngelx = -111111113;
    private double newAngely = -111111113;
    private double newAngelz = -111111113;

    private Sensor orientationSensor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        scrollView = findViewById(R.id.scrollView);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        gyroScope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        orientationSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        sensorManager.registerListener(this, orientationSensor, SensorManager.SENSOR_DELAY_GAME);
//        if (null != gyroScope) {
//            sensorManager.registerListener(this, gyroScope,
//                    SensorManager.SENSOR_DELAY_GAME);
//        }
//        VrPanoramaView vrPanoramaView = new VrPanoramaView(this);
    }

    public void addView(View view) {
        scrollView.addImg();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        int pixelsPerDegree = scrollView.getMaxScroll() / 360;
        //<editor-fold desc="gyroscope sensor">
        if (Sensor.TYPE_GYROSCOPE == event.sensor.getType()) {
            //handle gyroscope sensor data here
            if (0 != timestamp) {
                float dt = (event.timestamp - timestamp) * NS2S;
                float[] radValues = new float[3];
                radValues[0] = event.values[0] * dt;
                radValues[1] = event.values[1] * dt;
                radValues[2] = event.values[2] * dt;

                double newAngelX = radValues[0] * 180 / Math.PI;
                double newAngelY = radValues[1] * 180 / Math.PI;
                double newAngelZ = radValues[1] * 180 / Math.PI;
                if (newAngelx == -111111113) {
                    newAngelx = newAngelX;
                    newAngely = newAngelY;
                    newAngelz = newAngelZ;
                    if (oldAngelx == -111111113) {
                        oldAngelx = newAngelX;
                        oldAngely = newAngelY;
                        oldAngelz = newAngelZ;
                    }
                } else {
                    // dx 仰俯
                    // dy 竖直旋转
                    // dz 水平旋转
                    double dx = newAngelx - oldAngelx;
                    double dy = newAngely - oldAngely;
                    double dz = newAngelz - oldAngelz;
                    if (Math.abs(dy * pixelsPerDegree) > 0) {


//                    System.out.println(dy);
                        // when rotation detected, scroll the content
                        // UP ROTATION
//                    smoothScrollBy((int) (dy * pixelsPerDegree), 0);
                        // HORIZONTAL ROTATION
                        scrollView.smoothScrollBy((int) (dy * pixelsPerDegree), 0);
                        oldAngely = newAngely;
                        oldAngelz = newAngelz;
                        oldAngelx = newAngelx;
                        newAngelz = -111111113;
                        newAngely = -111111113;
                        newAngelx = -111111113;
                    } else {
                        newAngelx += newAngelX;
                        newAngely += newAngelY;
                        newAngelz += newAngelZ;
                    }
                }
            }
            timestamp = event.timestamp;
        }
        //</editor-fold>
        if (Sensor.TYPE_ORIENTATION == event.sensor.getType()) {
            float direction = event.values[0];
            scrollView.smoothScrollTo((int) (direction * pixelsPerDegree), 0);
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
