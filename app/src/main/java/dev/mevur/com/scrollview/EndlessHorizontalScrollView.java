package dev.mevur.com.scrollview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class EndlessHorizontalScrollView extends HorizontalScrollView {
    private Context context;
    private int actualScrollRange;
    private int restScrollRange;

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
        restScrollRange = actualScrollRange - scrollX;
        System.out.printf("%4d %4d\n", restScrollRange, scrollX);
        if (restScrollRange < 50) {
            System.out.println("max");
            LinearLayout container = (LinearLayout) getChildAt(0);
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
}
