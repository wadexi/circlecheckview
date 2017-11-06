package kyvo.circlecheckview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.*;

import views.*;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        final CircleCheckView circleCheckView = (CircleCheckView) findViewById(R.id.circleCheckView);
//        circleCheckView.startDotAnimator();
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
        timer.schedule(task,1000*1);


    }
}