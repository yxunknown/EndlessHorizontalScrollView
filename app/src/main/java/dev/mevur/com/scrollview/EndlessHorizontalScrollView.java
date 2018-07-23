package dev.mevur.com.scrollview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.view.menu.ListMenuItemView;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.Random;

public class EndlessHorizontalScrollView extends HorizontalScrollView
        implements SensorEventListener {
    private static final float NANO_SECOND_TO_SECOND = 1.0F / 1000000000.0F;
    private Context context;
    //sensor manager & orientation sensor
    private SensorManager sensorManager;
    private Sensor orientationSensor;

    private float pixelsPerDegree;
    private LinearLayout container;

    //get the width & height of current screen
    private DisplayMetrics displayMetrics;

    private LinearLayout.LayoutParams itemLayoutParams;



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
        //init sensor
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        //get accelerometer sensor and magnetic sensor
        orientationSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        //
        // although SensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION) is deprecated,
        // but the data of SensorManager.getOrientation(rotationMetrics, orientationValues),
        // orientationValues can not get a stable value.
        //

        if (null != orientationSensor) {
            //register sensor change event listener
            sensorManager.registerListener(this, orientationSensor,
                    SensorManager.SENSOR_DELAY_GAME);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        //disable touch to scroll
        return false;
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);


    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (null == displayMetrics) {
            // get current screen display metrics
            displayMetrics = new DisplayMetrics();
            getDisplay().getMetrics(displayMetrics);
            // get width & height at pixels of current screen
            int width = displayMetrics.widthPixels;
            int height = displayMetrics.heightPixels;
            // construct layout params of each child view of container
            itemLayoutParams = new LinearLayout.LayoutParams(width / 2, height);

            // every screen can display 30 degree range of data
            // each item can display 15 degree range of data
            // therefore, for 360 degree, need 360 / 15 = 24 items to display all data
        }
        // fill scroll view automatically
        if (0 == getChildCount()) {
            // current scroll view has no child
            // add a linear layout to scroll view
            LinearLayout linearLayout = new LinearLayout(context);
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            addView(linearLayout);
        } else if (!(getChildAt(0) instanceof LinearLayout)) {
            removeViewAt(0);
            LinearLayout linearLayout = new LinearLayout(context);
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            addView(linearLayout);
        }
        container = (LinearLayout) getChildAt(0);
        if (26 != container.getChildCount()) {
            // remove all view then to add 26 child views
            container.removeAllViews();
            for (int i = 0; i < 26; i++) {
                LinearLayout child = new LinearLayout(context);
                child.setOrientation(LinearLayout.VERTICAL);
                child.setLayoutParams(itemLayoutParams);
                Random random = new Random();

                child.setBackgroundColor(Color.rgb(random.nextInt(255),
                        random.nextInt(255),
                        random.nextInt(255)));
                container.addView(child);
            }
        }
        Drawable drawable = container.getChildAt(1).getBackground();
        container.getChildAt(25).setBackground(drawable);
        drawable = container.getChildAt(24).getBackground();
        container.getChildAt(0).setBackground(drawable);

        System.out.println(((LinearLayout) getChildAt(0)).getChildCount());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        pixelsPerDegree = (computeHorizontalScrollRange() - 1080) / 360.0f;
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
       if (Sensor.TYPE_ORIENTATION == event.sensor.getType()) {
           updateOrientation(event.values[0]);
       }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public void updateOrientation(double orientation) {
        int scrollTO = (int) (orientation * pixelsPerDegree);
        smoothScrollTo(scrollTO, 0);
    }
}
