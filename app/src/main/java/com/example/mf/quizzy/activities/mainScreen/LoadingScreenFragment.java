package com.example.mf.quizzy.activities.mainScreen;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.mf.quizzy.R;
import com.example.mf.quizzy.util.RingBarWrapper;

import io.netopen.hotbitmapgg.library.view.RingProgressBar;

public class LoadingScreenFragment extends Fragment {

    interface PlayClickedListener {
        void onPlayButtonClicked();
    }

    private PlayClickedListener mListener;
    private View mView;
    private Button mStartButton;
    private int mLoadingTimeInSeconds;
    private RingProgressBar mLoadingBar;
    private RingBarWrapper mRingBarWrapper;


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
        mRingBarWrapper.start();

    }

    void setLoadingTimeInSeconds(int loadingTimeInSeconds) {
        mLoadingTimeInSeconds = loadingTimeInSeconds;
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

    private void prepareLoadingBar() {
        mLoadingBar = mView.findViewById(R.id.loading_progress_bar);
        mRingBarWrapper = new RingBarWrapper(mLoadingBar, mLoadingTimeInSeconds, new RingProgressBar.OnProgressListener() {
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
