package com.example.mf.quizzy.Activities.MainScreen;


import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.mf.quizzy.R;

import io.netopen.hotbitmapgg.library.view.RingProgressBar;

public class LoadingScreenFragment extends Fragment {

    interface PlayClickedListener {
        void onPlayButtonClicked();
    }

    private PlayClickedListener mListener;
    private View mView;
    private Button mStartButton;
    private Handler mCountDownHandler;
    private int mTimeProgress = 0;
    private RingProgressBar mLoadingBar;
    private static Thread mCountDownThread;

    @Override
    public void onAttach(Context context) {
        try {
            mListener = (PlayClickedListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException("Class must implement the PlayClickedListener interface");
        }
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.loading_layout, container, false);
        setOnClickStartButtonListener();
        prepareLoadingBar();
        return mView;
    }

    @Override
    public void onResume() {
        super.onResume();
        startLoadingBar();
    }

    private void setOnClickStartButtonListener() {
        mStartButton = mView.findViewById(R.id.start_button);
        mStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onPlayButtonClicked();
            }
        });
    }

    private void startLoadingBar() {
        mCountDownThread.start();

    }

    private void prepareLoadingBar() {
        setLoadingBar();
        createCountDownHandler();
        initializeProgressBar();
        createCountDownThread();
    }

    private void setLoadingBar() {
        mLoadingBar = mView.findViewById(R.id.loading_progress_bar);
    }

    // todo: encapsulate this ringbar in a separate class
    private void createCountDownThread() {
        mCountDownThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    for (int i = 0; i < 100; i++) {
                        Thread.sleep(100);
                        mCountDownHandler.sendEmptyMessage(0);
                    }
                } catch (InterruptedException e) {
                    Log.d(getClass().toString(), "Interupted ringbar");
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
                        mLoadingBar.setProgress(mTimeProgress);
                    }
                }
                return true;
            }
        });
    }

    private void initializeProgressBar() {
        mLoadingBar.setOnProgressListener(new RingProgressBar.OnProgressListener() {
            @Override
            public void progressToComplete() {
                showStartButton();
            }
        });
    }

    private void showStartButton() {
        mLoadingBar.setVisibility(View.INVISIBLE);
        mStartButton.setVisibility(View.VISIBLE);
    }

}
