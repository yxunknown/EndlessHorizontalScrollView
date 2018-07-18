package dev.mevur.com.scrollview;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class EndlessHorizontalScrollView extends HorizontalScrollView
        implements SensorEventListener{
    private static final float NANO_SECOND_TO_SECOND = 1.0F / 1000000000.0F;
    private static final int MAGINIFY_FACTOR = 1000;
    private Context context;
    private int actualScrollRange;
    private int restScrollRange;
    private Sensor gyroScopeSensor;
    private SensorManager sensorManager;
    private long timestamp;
    private float angelx;
    private float angely;
    private float angelz;
    private int pixelsPerDegree;



    public EndlessHorizontalScrollView(Context context) {
        super(context, null);
        init(context);
    }

    public EndlessHorizontalScrollView(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
        init(context);
    }

    public EndlessHorizontalScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        setEnabled(false);
        setOverScrollMode(OVER_SCROLL_NEVER);
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        gyroScopeSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        sensorManager.registerListener(this, gyroScopeSensor, SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        //disable touch to scroll
        return false;
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        actualScrollRange = computeHorizontalScrollRange() - computeHorizontalScrollExtent();
        restScrollRange = actualScrollRange;
        System.out.println(actualScrollRange);
    }

    public void addImg() {
        LinearLayout container = (LinearLayout) getChildAt(0);
        ImageView imageView = new ImageView(context);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        imageView.setLayoutParams(lp);
        imageView.setImageResource(R.drawable.test);
        container.addView(imageView);
    }

    @Override
    protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX, boolean clampedY) {
      super.onOverScrolled(scrollX, scrollY, clampedX, clampedY);
      //implement endless scroll
//        restScrollRange = actualScrollRange - scrollX;
        LinearLayout container = (LinearLayout) getChildAt(0);
//        System.out.printf("%4d %4d\n", restScrollRange, scrollX);
        if (scrollX > container.getChildAt(0).getWidth()) {
            System.out.println("view changed");
            View v = container.getChildAt(0);
            container.removeView(v);
            container.addView(v);
            restScrollRange += v.getWidth();
            scrollTo(scrollX - v.getWidth(), 0);
        }
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
//        System.out.println("l = [" + l + "], t = [" + t + "], oldl = [" + oldl + "], oldt = [" + oldt + "]");
    }

    public int getMaxScroll() {
        return computeHorizontalScrollRange();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        //there is where the view die
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        pixelsPerDegree = computeHorizontalScrollRange() / 360;
        if (Sensor.TYPE_GYROSCOPE == event.sensor.getType()) {
            //handle gyroscope sensor data here
            if (0 != timestamp) {
                float dt = (event.timestamp - timestamp) * NANO_SECOND_TO_SECOND;
                float[] radValues = new float[3];
                radValues[0] = event.values[0] * dt;
                radValues[1] = event.values[1] * dt;
                radValues[2] = event.values[2] * dt;

                float newAngelX = (float) Math.toDegrees(radValues[0]);
                float newAngelY = (float) Math.toDegrees(radValues[1]);
                float newAngelZ = (float) Math.toDegrees(radValues[2]);

                // dx 仰俯
                // dy 竖直旋转
                // dz 水平旋转

                if (0 != angelx || 0 != angely || 0 != angelz) {
                    float dx = (newAngelX - angelx) * MAGINIFY_FACTOR;
                    float dy = (newAngelY - angely) * MAGINIFY_FACTOR;
                    float dz = (newAngelZ - angelz) * MAGINIFY_FACTOR;

                    System.out.println(dy + " " + dz);
                    // when rotation detected, scroll the content
                    // UP ROTATION
                    smoothScrollBy((int) (dy * pixelsPerDegree), 0);
                    // HORIZONTAL ROTATION
                    smoothScrollBy((int) (dz * pixelsPerDegree), 0);
                }
                angelx = newAngelX;
                angely = newAngelY;
                angelz = newAngelZ;
            }
            timestamp = event.timestamp;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
