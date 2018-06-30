package ss18.mc.positime.utils;

import android.os.Handler;

public class Timer {
    private int mTime;
    public int mInterval;
    public Handler mHandler;
    private Runnable mRunnable;

    public Timer(int interval) {
        mRunnable = new Runnable() {
            @Override
            public void run() {
                mTime+=1;
                mHandler.postDelayed(this,mInterval);
            }
        };
        mHandler = new Handler();
        mInterval = interval;
        mTime = 0;
    }

    public Timer(int interval, Runnable func){
        mRunnable = func;
        mInterval = interval;
        mHandler = new Handler();
        mTime = 0;
    }

    public void setTime(int time){
        this.mTime = time;
    }

    public int getTime(){
        return this.mTime;
    }

    public void startTimer(){
        mHandler.postDelayed(mRunnable,mInterval);
    }

    public void stopTimer(){
        mHandler.removeCallbacks(mRunnable);
    }
}