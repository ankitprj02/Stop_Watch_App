package in.ankitprj.stopwatch;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.button.MaterialButton;

import java.text.MessageFormat;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    TextView textView;
    MaterialButton reset,start,stop;
    int milliSeconds,seconds,minutes;
    long millisecond,startTime,timeBuff,updateTime=0L;
    Handler handler;
    private LottieAnimationView lottieAnimationView;
    private boolean isPlaying = false;

    private  final  Runnable runnable= new Runnable() {
        @Override
        public void run() {
            millisecond = SystemClock.uptimeMillis() - startTime;
            updateTime = timeBuff+millisecond;
            seconds=(int)(updateTime/1000);
            minutes = seconds/60;
            seconds=seconds%60;
            milliSeconds=(int) (updateTime%1000);

            textView.setText(MessageFormat.format("{0}:{1}:{2}",
                    String.format(Locale.getDefault(),"%02d",minutes),
                    String.format(Locale.getDefault(), "%02d", seconds),
                    String.format(Locale.getDefault(), "%03d", milliSeconds)  // Correctly formatted with %03d
            ));
            handler.postDelayed(this,0);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.textview);
        reset = findViewById(R.id.resetBtn);
        start = findViewById(R.id.startBtn);

        handler = new Handler(Looper.getMainLooper());
        lottieAnimationView = findViewById(R.id.animationView);


        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (start.getText().toString().equals("START")) {
                    startWatch(); // Call start function
                    start.setText("PAUSE"); // Change button text to "PAUSE"
                } else if (start.getText().toString().equals("PAUSE")) {
                    pauseWatch(); // Call pause function
                    start.setText("START"); // Change button text to "START"
                }
            }
        });




        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pauseWatch();
                start.setText("START"); // Change button text to "START"
                millisecond=0L;
                startTime=0L;
                timeBuff=0L;
                updateTime=0L;
                seconds=0;
                minutes=0;
                milliSeconds=0;
                textView.setText("00:00:000");
                lottieAnimationView.pauseAnimation();
            }
        });

    }

    private void startWatch() {
        startTime = SystemClock.uptimeMillis();
        handler.postDelayed(runnable,0);
        lottieAnimationView.playAnimation();
    }

    private void pauseWatch(){
        timeBuff +=millisecond;
        handler.removeCallbacks(runnable);
        lottieAnimationView.pauseAnimation();

    }
}