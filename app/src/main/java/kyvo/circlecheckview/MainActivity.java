package kyvo.circlecheckview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.*;
import android.view.*;
import android.widget.*;

import java.util.*;

import views.*;

public class MainActivity extends AppCompatActivity {

    private final String TAG = "test";


    private SeekBar seekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        seekBar = (SeekBar) findViewById(R.id.seekBar);


        final CircleCheckView circleCheckView = (CircleCheckView) findViewById(R.id.circleCheckView);
        circleCheckView.setOnViewClikListener(new CircleCheckView.OnViewClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "优化", Toast.LENGTH_SHORT).show();
            }
        });
        circleCheckView.registerLoadListener(new CircleCheckView.LoadListener() {
            @Override
            public void loadEnd() {
                circleCheckView.setProgress(80);
            }
        });

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                int progress = 0;
                while (progress <= 100){
                    try {
                        Thread.sleep(30);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    circleCheckView.setProgress(progress);
                    progress++;
                }
            }
        };

        Timer timer = new Timer();
        timer.schedule(task,0);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                circleCheckView.setProgress(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        Log.i(TAG, "MainActivity : dispatchKeyEvent: " + event.getAction() );
        boolean result = super.dispatchTouchEvent(event);
        return result;
    }




    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.i(TAG, "MainActivity : onTouchEvent: " + event.getAction() );
        boolean result = super.onTouchEvent(event);
        return result;
    }
}
