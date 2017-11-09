package views;

import android.content.*;
import android.os.*;
import android.support.annotation.*;
import android.util.*;
import android.view.*;
import android.widget.*;

/**
 * Created by Administrator on 2017/11/8.
 */

public class CustomRelativeLayout extends RelativeLayout{


    public final String TAG = "test";

    public CustomRelativeLayout(Context context) {
        super(context);
    }

    public CustomRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CustomRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        Log.i(TAG, "CustomRelativeLayout : dispatchTouchEvent: " + event.getAction() );
        boolean result = super.dispatchTouchEvent(event);
        return result;
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        Log.i(TAG, "CustomRelativeLayout :  onInterceptTouchEvent: " + ev.getAction() );
        boolean result = super.onInterceptTouchEvent(ev);
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.i(TAG, "CustomRelativeLayout :  onTouchEvent: " + event.getAction() );
        boolean result = super.onTouchEvent(event);
        return result;
    }
}
