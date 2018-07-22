package dev.mevur.com.scrollview;

import android.content.Context;
import android.graphics.Canvas;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
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
    private float pixelsPerDegree;
    private LinearLayout container;



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
//        setEnabled(true);
//        setOverScrollMode(OVER_SCROLL_NEVER);
//        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
//        gyroScopeSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
//        sensorManager.registerListener(this, gyroScopeSensor, SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        //disable touch to scroll
//        return super.onTouchEvent(ev);
        return false;
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
    }

    public void addImg() {
//        LinearLayout container = (LinearLayout) getChildAt(0);
//        ImageView imageView = new ImageView(context);
//        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
//                ViewGroup.LayoutParams.WRAP_CONTENT);
//        imageView.setLayoutParams(lp);
//        imageView.setImageResource(R.drawable.test);
//        container.addView(imageView);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        actualScrollRange = computeHorizontalScrollRange() - computeHorizontalScrollExtent();
        restScrollRange = actualScrollRange;

        pixelsPerDegree = (computeHorizontalScrollRange() - 1080) / 360.0f;

        container = (LinearLayout) getChildAt(0);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;
        for (int i = 0; i < container.getChildCount(); i++) {
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(width / 2, height);
            container.getChildAt(i).setLayoutParams(lp);

        }

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        pixelsPerDegree = (computeHorizontalScrollRange() - 1080) / 360.0f;
    }

    @Override
    protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX, boolean clampedY) {
      super.onOverScrolled(scrollX, scrollY, clampedX, clampedY);
      //implement endless scroll
//        restScrollRange = actualScrollRange - scrollX;
//        System.out.println(scrollX);
        LinearLayout container = (LinearLayout) getChildAt(0);
//        System.out.printf("%4d %4d\n", restScrollRange, scrollX);
        System.out.println(scrollX);
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
        System.out.println();
        return computeHorizontalScrollRange();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        //there is where the view die
//        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        pixelsPerDegree = computeHorizontalScrollRange() / 360;
        if (Sensor.TYPE_GYROSCOPE == event.sensor.getType()) {
            //handle gyroscope sensor data here
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public void updateOrientation(float orientation) {
        int scrollTO = (int) (orientation * pixelsPerDegree);
        System.out.println("degree: " + orientation + ",   scroll to:" + scrollTO +
                ",    dpp:" + pixelsPerDegree + ",   total:" + computeHorizontalScrollRange());
        smoothScrollTo(scrollTO, 0);
    }
}
