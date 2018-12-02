package com.example.mf.quizzy.util;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import io.netopen.hotbitmapgg.library.view.RingProgressBar;

public class RingBarWrapper {
    private RingProgressBar mRingProgressBar;
    private int mHowManySeconds;
    private Thread mCountDownThread;
    private Handler mCountDownHandler;
    private int mTimeProgress = 0;
    private int mThreadSleepTimeInMilliseconds;

    public RingBarWrapper(RingProgressBar ringProgressBar, int howManySeconds, RingProgressBar.OnProgressListener onCompleteListener) {
        mRingProgressBar = ringProgressBar;
        mRingProgressBar.setOnProgressListener(onCompleteListener);
        mHowManySeconds = howManySeconds;
        mThreadSleepTimeInMilliseconds = (howManySeconds * 1000) / 100; // assuming progress bar should progress 1% at the time
        createCountDownHandler();
        createCountDownThread();
    }


    public void start() {
        mCountDownThread.start();
    }

    private void createCountDownThread() {
        mCountDownThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    for (int i = 0; i < 100; i++) {
                        Thread.sleep(mThreadSleepTimeInMilliseconds);
                        mCountDownHandler.sendEmptyMessage(0);
                    }
                } catch (InterruptedException e) {
                    Log.d(getClass().toString(), "Interrupted ring bar");
                }
            }
        });
    }

    private void createCountDownHandler() {
        mCountDownHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if (msg.what == 0) {
                    if (mTimeProgress < 100) {
                        mTimeProgress++;
                        mRingProgressBar.setProgress(mTimeProgress);
                    }
                }
                return true;
            }
        });
    }
}
