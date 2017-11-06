package views;

import android.animation.*;
import android.content.*;
import android.graphics.*;
import android.os.*;
import android.support.annotation.*;
import android.util.*;
import android.view.*;
import android.view.animation.*;

import static java.lang.Math.log;
import static java.lang.Math.max;

/**
 * Created by Administrator on 2017/11/6.
 */

public class CircleCheckView extends View{

    ValueAnimator animator;
    private final String TAG = "CircleCheckView";
    private Context context;
    private Paint circlePaint;
    private Paint pointPaint;
    private int circleColor = Color.WHITE;
    private int pointColor = Color.WHITE;

    private boolean loadend;
    private int offset = 8;

    private float lineHeight;
    private float lineWidth;

    private int mWidth;
    private int mHeight;

    private float mDotProgress;
    private float circleProgress;
    private float arcProgress;
    private LoadListener listener;


//    private int defaultMinWidth;
//    private int defaultMinHeight;

    public CircleCheckView(Context context) {
        this(context,null);
    }

    public CircleCheckView(Context context, AttributeSet attrs) {
        this(context,attrs,0);
    }

    public CircleCheckView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    public  static interface LoadListener{
        void loadEnd();
    }
    public void registerLoadListener(LoadListener listener){
        this.listener = listener;
    }


    private void init() {
//        defaultMinHeight = Utils.dip2px(context,80);
//        defaultMinWidth = Utils.dip2px(context,80);
        circlePaint = new Paint();
        circlePaint.setAntiAlias(true);
        circlePaint.setColor(circleColor);
        circlePaint.setStyle(Paint.Style.FILL);

        lineWidth = Utils.dip2px(context,2);
        lineHeight = Utils.dip2px(context,10);
        circlePaint.setStrokeWidth(lineWidth);

        pointPaint = new Paint();
        pointPaint.setAntiAlias(true);
        pointPaint.setColor(pointColor);
        pointPaint.setStyle(Paint.Style.FILL);
        pointPaint.setStrokeWidth(lineWidth);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CircleCheckView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.context = context;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int minWidth = getSuggestedMinimumWidth();
        int minHeight = getSuggestedMinimumHeight();

        mWidth = resolveSize(minWidth,widthMeasureSpec);
        mHeight = resolveSize(minHeight,heightMeasureSpec);
        Log.e(TAG, "onMeasure: mWidth:" + mWidth + "   mHeight:" + mHeight);
        setMeasuredDimension(mWidth,mHeight);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int startY = (mHeight - mWidth)/2;
        Log.e(TAG, "onDraw: startY:" + startY);
//        for (int i = 0; i < 100; i++){
//            if(circleProgress > i){
//                circlePaint.setColor(Color.BLUE);
//            }else {
//                circlePaint.setColor(Color.WHITE);
//            }
//            canvas.drawLine(mWidth/2.0f, startY > 0 ? startY : 0 ,mWidth/2.0f,startY > 0 ? startY + lineHeight : lineHeight,circlePaint);
//            canvas.rotate(3.6f,mWidth/2,mHeight/2);
//        }

        if(!loadend){

            for (int i = 0; i < 100 ; i++){
                if(circleProgress > i){
                    circlePaint.setColor(Color.BLUE);
                }else {
                    circlePaint.setColor(Color.WHITE);
                }
                canvas.drawLine(mWidth/2.0f, startY > 0 ? mHeight - startY : mHeight ,mWidth/2.0f,startY > 0 ? mHeight -startY - lineHeight : mHeight - lineHeight,circlePaint);
                canvas.rotate(3.6f,mWidth/2,mHeight/2);
            }

            if(circleProgress == 100){
                circleProgress = 0;
                if(animator != null){
                    loadend = true;
//                    animator.cancel();
                    animator.end();
                }
                invalidate();
                listener.loadEnd();
            }else {
                canvas.rotate(mDotProgress*3.6f,mWidth/2,mHeight/2);
                canvas.drawCircle(mWidth/2.0f,startY > 0 ? startY + lineHeight*2 : lineHeight*2,Utils.dip2px(context,3),pointPaint);
                startDotAnimator();
            }
        }else {
//            circlePaint.setStrokeWidth(Utils.dip2px(context,1));
            canvas.rotate(45f,mWidth/2,mHeight/2);
            for (int i = 0; i < 100 ; i++){
                if(circleProgress > i){
                    circlePaint.setColor(Color.BLUE);
                }else {
                    circlePaint.setColor(Color.WHITE);
                }
                canvas.drawLine(mWidth/2.0f, startY > 0 ? mHeight - startY : mHeight ,mWidth/2.0f,startY > 0 ? mHeight -startY - lineHeight : mHeight - lineHeight,circlePaint);
                canvas.rotate(2.7f,mWidth/2,mHeight/2);
            }
//            invalidate();
        }

    }

    /**
     * 启动小圆点旋转动画
     */
    private void startDotAnimator() {
        if(animator != null && animator.isRunning()){
            return;
        }
        animator = ValueAnimator.ofFloat(0, 100);
        animator.setDuration(1500);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setRepeatMode(ValueAnimator.RESTART);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                // 设置小圆点的进度，并通知界面重绘
                mDotProgress = (Float) animation.getAnimatedValue();
                Log.e(TAG, "onAnimationUpdate: mDotProgress:" + mDotProgress);
                invalidate();
            }
        });
        animator.start();
    }

    public void setProgress(int progress){

        circleProgress = progress;
        if(Thread.currentThread() == Looper.getMainLooper().getThread()){
            invalidate();
        }else {
            postInvalidate();
        }

    }


    public float getProgerss(int progress){
        return circleProgress;
    }




}
