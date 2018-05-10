package ss18.mc.positime;

import android.os.Handler;


/**
 * Created by marius on 10.05.2018.
 */

public class Timer {
    private int mTime;
    private int mInterval;
    private Handler mHandler = new Handler();
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            mTime+=1;
            mHandler.postDelayed(this,mInterval);
        }
    };

    public Timer(int interval) {
        this.mInterval = interval;
        this.mTime = 0;
    }

    public void startTimer(){
        mHandler.postDelayed(mRunnable,mInterval);
    }

    public void stopTimer(){
        mHandler.removeCallbacks(mRunnable);
    }
}
