package views;

import android.animation.*;
import android.content.*;
import android.content.res.*;
import android.graphics.*;
import android.os.*;
import android.support.annotation.*;
import android.util.*;
import android.view.*;
import android.view.animation.*;

import java.util.*;

import kyvo.circlecheckview.*;

import static java.lang.Math.max;

/**
 * Created by Administrator on 2017/11/6.
 */

public class CircleCheckView extends View{

    ValueAnimator animator;
    private final String TAG = "test";
    private Context context;
    private Paint circlePaint;//外圈画笔
    private Paint pointPaint;//圆点画笔
    private Paint insideCirclePaint;//内圈画笔
    private Paint insideStokePaint;//内圈描边画笔
    private Paint textPaint;//文字画笔


    private boolean loadend;


    private int mWidth;
    private int mHeight;

    private int centerX;
    private int centerY;

    private float insideCircleRadius;

    private float mDotProgress;
    private float circleProgress;
    private LoadListener listener;


    private int bottomCircleScaleColor;
    private int topCircleScaleColor;
    private int topArcScaleColor;
    private int roundBallColor;
    private int insideCircleColor;
    private int textColor;
    private int bottomCircleScaleWidth;
    private int scaleHeight;
    private int topCircleScaleWidth;
    private int roundBallRadius;

    private boolean onClicking;
    //圆点半径

    private float radius;
    private float pointX;
    private float pointY;

    public CircleCheckView(Context context) {
        this(context,null);
    }

    public CircleCheckView(Context context, AttributeSet attrs) {
        this(context,attrs,0);
    }

    public CircleCheckView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        Resources res = context.getResources();
        TypedArray typedArray = context.obtainStyledAttributes(attrs,R.styleable.CircleProgressView,defStyleAttr,0);
        bottomCircleScaleColor = typedArray.getColor(R.styleable.CircleProgressView_bottom_circle_scale_color,res.getColor(R.color.grey));
        topCircleScaleColor = typedArray.getColor(R.styleable.CircleProgressView_top_circle_scale_color,Color.WHITE);
        topArcScaleColor = typedArray.getColor(R.styleable.CircleProgressView_top_arc_scale_color,res.getColor(R.color.yellow_circle));
        roundBallColor = typedArray.getColor(R.styleable.CircleProgressView_round_ball_color,Color.WHITE);
        insideCircleColor = typedArray.getColor(R.styleable.CircleProgressView_inside_circle_color,res.getColor(R.color.translucent));
        textColor = typedArray.getColor(R.styleable.CircleProgressView_text_color,res.getColor(R.color.yellow_circle));

        bottomCircleScaleWidth = typedArray.getDimensionPixelSize(R.styleable.CircleProgressView_bottom_circle_scale_width,Utils.dip2px(context,1));
        scaleHeight = typedArray.getDimensionPixelSize(R.styleable.CircleProgressView_scale_height,Utils.dip2px(context,10));
        topCircleScaleWidth = typedArray.getDimensionPixelSize(R.styleable.CircleProgressView_top_scale_width,Utils.dip2px(context,2));
        roundBallRadius = typedArray.getDimensionPixelSize(R.styleable.CircleProgressView_round_ball_radius,Utils.dip2px(context,3));
        typedArray.recycle();


        init();
    }

    public  static  interface LoadListener{
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
        circlePaint.setColor(bottomCircleScaleColor);
        circlePaint.setStyle(Paint.Style.FILL);
        circlePaint.setStrokeWidth(bottomCircleScaleWidth);

        pointPaint = new Paint();
        pointPaint.setAntiAlias(true);
        pointPaint.setColor(roundBallColor);
        pointPaint.setStyle(Paint.Style.FILL);

        insideCirclePaint = new Paint();
        insideCirclePaint.setAntiAlias(true);
        insideCirclePaint.setColor(context.getResources().getColor(R.color.grey));
        insideCirclePaint.setStrokeWidth(Utils.dip2px(context,1));
        insideCirclePaint.setStyle(Paint.Style.FILL);

        insideStokePaint = new Paint();
        insideStokePaint.setAntiAlias(true);
        insideStokePaint.setColor(context.getResources().getColor(R.color.grey));
        insideStokePaint.setStyle(Paint.Style.STROKE);
        insideStokePaint.setStrokeWidth(Utils.dip2px(context,1));

        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setStrokeWidth(2);
        textPaint.setTextSize(60);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setColor(Color.WHITE);


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
        centerX = mWidth/2;
        centerY = mHeight/2;

         radius  = mWidth/2;
        float a = (float) Math.cos(Math.PI * 30/180) * radius;
        float b = (float) (Math.sin(Math.PI * 30/180) * radius);
        pointX = radius - b;
        pointY = radius + a;

//        Log.e(TAG, "onMeasure: mWidth:" + mWidth + "   mHeight:" + mHeight);
        setMeasuredDimension(mWidth,mHeight);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        int startY = (mHeight - mWidth)/2;
//        Log.e(TAG, "onDraw: startRawY:" + startRawY);
        insideCircleRadius = mWidth/2.0f/10*7;
        int centerColor = context.getResources().getColor(R.color.grey_60);
        int edageColor = context.getResources().getColor(R.color.grey_0f);
        if(!onClicking){
            RadialGradient radialGradient = new RadialGradient(mWidth/2,mHeight/2,insideCircleRadius,centerColor,edageColor, Shader.TileMode.REPEAT);
            insideCirclePaint.setShader(radialGradient);
        }else {
            RadialGradient radialGradient = new RadialGradient(mWidth/2,mHeight/2,insideCircleRadius,edageColor,edageColor, Shader.TileMode.REPEAT);
            insideCirclePaint.setShader(radialGradient);
        }

        canvas.save();
        canvas.drawCircle(mWidth/2,mHeight/2,insideCircleRadius, insideStokePaint);
        canvas.restore();

        canvas.save();
        canvas.drawCircle(mWidth/2,mHeight/2,insideCircleRadius, insideCirclePaint);
        canvas.restore();


        textPaint.setTextSize(60);
        Rect bounds = new Rect();
        textPaint.getTextBounds((int)circleProgress + "分",0,1,bounds);
        Paint.FontMetrics fontMetrics = insideCirclePaint.getFontMetrics();
        canvas.save();
        canvas.drawText((int) circleProgress + "分",mWidth/2,mHeight/2 + (fontMetrics.bottom - fontMetrics.top)/2 - fontMetrics.bottom, textPaint);
        canvas.restore();
        textPaint.setTextSize(25);



        if(!loadend){
            canvas.save();
            textPaint.getTextBounds("正在加载...",0,7,bounds);
            Paint.FontMetrics fontMetrics2 = insideCirclePaint.getFontMetrics();
            float textY = Utils.dip2px(context,20) + fontMetrics.bottom - fontMetrics.top +mHeight/2 + (fontMetrics2.bottom - fontMetrics2.top)/2 - fontMetrics2.bottom;
            canvas.drawText("正在加载...",mWidth/2,textY, textPaint);

            for (int i = 0; i < 100 ; i++){
                if(circleProgress > i){
                    circlePaint.setColor(Color.WHITE);
                    circlePaint.setStrokeWidth(topCircleScaleWidth);
                }else {
                    circlePaint.setStrokeWidth(bottomCircleScaleWidth);
                    circlePaint.setColor(context.getResources().getColor(R.color.grey));
                }
                canvas.drawLine(mWidth/2.0f, startY > 0 ? mHeight - startY : mHeight ,mWidth/2.0f,startY > 0 ? mHeight -startY - scaleHeight : mHeight - scaleHeight,circlePaint);
                canvas.rotate(3.6f,mWidth/2,mHeight/2);
            }
            canvas.restore();

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
                canvas.save();
                canvas.rotate(mDotProgress*3.6f,mWidth/2,mHeight/2);
                canvas.drawCircle(mWidth/2.0f,startY > 0 ? startY + scaleHeight +  Utils.dip2px(context,6): scaleHeight + roundBallRadius*2,roundBallRadius,pointPaint);
                startDotAnimator();
                canvas.restore();
            }
        }else {
            canvas.save();
            textPaint.getTextBounds("点击优化",0,4,bounds);
            Paint.FontMetrics fontMetrics2 = insideCirclePaint.getFontMetrics();
            float textY = Utils.dip2px(context,20) + fontMetrics.bottom - fontMetrics.top +mHeight/2 + (fontMetrics2.bottom - fontMetrics2.top)/2 - fontMetrics2.bottom;
            canvas.drawText("点击优化",mWidth/2,textY, textPaint);
//            circlePaint.setStrokeWidth(Utils.dip2px(context,1));
            canvas.rotate(30f,mWidth/2,mHeight/2);
            for (int i = 0; i < 100 ; i++){
                if(circleProgress > i){
                    circlePaint.setStrokeWidth(topCircleScaleWidth);
                    circlePaint.setColor(context.getResources().getColor(R.color.yellow_circle));
                }else {
                    circlePaint.setStrokeWidth(bottomCircleScaleWidth);
                    circlePaint.setColor(context.getResources().getColor(R.color.grey));
                }
                canvas.drawLine(mWidth/2.0f, startY > 0 ? mHeight - startY : mHeight ,mWidth/2.0f,startY > 0 ? mHeight -startY - scaleHeight : mHeight - scaleHeight,circlePaint);
                canvas.rotate(3.0f,mWidth/2,mHeight/2);
            }
            canvas.restore();
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
        animator.setDuration(3000);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setRepeatMode(ValueAnimator.RESTART);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                // 设置小圆点的进度，并通知界面重绘
                mDotProgress = (Float) animation.getAnimatedValue();
//                Log.e(TAG, "onAnimationUpdate: mDotProgress:" + mDotProgress);
                invalidate();
            }
        });
        animator.start();
    }

    public void setProgress(final int progress){

        circleProgress = progress;
        if(circleProgress > 100){
            circleProgress = 100;
        }
        if(circleProgress < 0){
            circleProgress = 0;
        }
        if(Thread.currentThread() == Looper.getMainLooper().getThread()){

//            if(loadend){
//                Timer timer = new Timer();
//                TimerTask task = new TimerTask() {
//                    @Override
//                    public void run() {
//                        for(circleProgress = 0; circleProgress < progress;circleProgress++){
//                            try {
//                                Thread.sleep(10);
//                                postInvalidate();
//                            } catch (InterruptedException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    }
//                };
//                timer.schedule(task,0);
//            }else {
                invalidate();
//            }

        }else {
//            if(loadend){
//                for(circleProgress = 0; circleProgress < progress;circleProgress++){
//                    try {
//                        Thread.sleep(10);
//                        postInvalidate();
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }else {
                postInvalidate();
//            }

        }

    }


    public float getProgerss(int progress){
        return circleProgress;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        Log.i(TAG, "CircleCheckView : dispatchTouchEvent: " + event.getAction() );
        boolean result = super.dispatchTouchEvent(event);
        return result;
    }


    private float startRawX, startRawY;
    private float startX, startY;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.i(TAG, "CircleCheckView :onTouchEvent: " + event.getAction());
        boolean result = super.onTouchEvent(event);

        if(!loadend){
            return false;
        }

        float x = (int) event.getX();//相对于控件的坐标
        float y = event.getY();
        float rawX = event.getRawX();//相对于屏幕的坐标
        float rawY = event.getRawY();
        startRawX = rawX;
        startRawY = rawY;
        startX = x;
        startY = y;

        int distance = (int) Math.sqrt(Math.abs((startX - centerX)*(startX - centerX))+Math.abs((startY - centerY)*(startY - centerY)));
        Log.i(TAG, "ACTION_UP: startX:" + startX + "startY:" + startY + " centerX:" + centerX + " centerY:" + centerY + " pointX:" + pointX + " pointY:" + pointY);
//        θ=arctan[(y2-y0)/(x2-x0)]-arctan[(y1-y0)/(x1-x0)]；

//        Log.i(TAG, "onTouchEvent: jiaodu:" + jiaodu);
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                if(distance <= insideCircleRadius){
                    onClicking = true;
                }

                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                onClicking = false;
                invalidate();
                Log.i(TAG, "ACTION_UP: startX:" + startX + "startY:" + startY + " centerX:" + centerX + " centerY:" + centerY + " distance:" + distance + " insideCircleRadius:" + insideCircleRadius);
                if(distance <= insideCircleRadius){
                    if(onClickListener != null){
                        onClickListener.onClick(this);
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
//                Log.e(TAG, "onTouchEvent: ACTION_MOVE");
                if(distance > insideCircleRadius){
                    double originAngle = Math.atan((pointY-centerY)/(pointX-centerX)) * 180/Math.PI;
                    if(pointX - centerX < 0){
                        originAngle += 180;
                    }else if(pointX -centerX > 0 && pointY - centerY < 0){
                        originAngle += 360;
                    }


                    double gestureAngle = Math.atan((y-centerY)/(x-centerX)) * 180/Math.PI;
                    if(x - centerX < 0){
                        gestureAngle += 180;
                    }else if(x -centerX > 0 && y - centerY < 0){
                        gestureAngle += 360;
                    }

                    if(gestureAngle < originAngle){
                        gestureAngle += 360;
                    }

                    double angle = gestureAngle - originAngle;
                    setProgress((int) angle/3);
//                  弧度制×（180°/π）
//                  Log.i("wadexi", "onTouchEvent: jiaodu:" + jiaodu);
                    Log.i("wadexi", "onTouchEvent: originAngle:" + originAngle + "  gestureAngle:" + gestureAngle + "angle:" + angle);
                }

                break;
            case MotionEvent.ACTION_OUTSIDE:
                Log.e(TAG, "onTouchEvent: ACTION_OUTSIDE");

                break;
            case MotionEvent.ACTION_HOVER_ENTER:
                Log.e(TAG, "onTouchEvent: ACTION_HOVER_ENTER");

                break;
            case MotionEvent.ACTION_HOVER_MOVE:
                Log.e(TAG, "onTouchEvent: ACTION_HOVER_MOVE");

                break;
            case MotionEvent.ACTION_HOVER_EXIT:
                Log.e(TAG, "onTouchEvent: ACTION_HOVER_EXIT");
                break;
        }
        result = true;
        return true;
    }

    public interface OnViewClickListener{
        void onClick(View v);
    }

    public void setOnViewClikListener(OnViewClickListener listener){
        this.onClickListener = listener;
    }

    private OnViewClickListener onClickListener;
}
