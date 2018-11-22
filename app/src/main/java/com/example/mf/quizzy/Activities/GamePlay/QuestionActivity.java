package com.example.mf.quizzy.Activities.GamePlay;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import com.example.mf.quizzy.Activities.Results.ResultsActivity;
import com.example.mf.quizzy.App;
import com.example.mf.quizzy.Listeners.*;
import com.example.mf.quizzy.Model.Model;
import com.example.mf.quizzy.Model.ModelFactory;
import com.example.mf.quizzy.R;

import java.util.HashMap;

import io.netopen.hotbitmapgg.library.view.RingProgressBar;

public class QuestionActivity extends AppCompatActivity implements AnswerShownListener {
    private HashMap<Integer, String> mAnswerHistory = new HashMap<>();
    private int mCorrectAnswers;
    private int mQuestionsCounter;
    private Model mModel;
    private Handler mCountDownHandler;
    private int mTimeProgress = 0;
    private RingProgressBar mRingProgressBar;
    private static Thread mCountDownThread;

    public QuestionActivity() {
        mModel = ModelFactory.getFactory().getModel();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        mRingProgressBar = findViewById(R.id.id_progress_bar);
        setActionBar();
        addFragmentManagers();
        initializeCountDown();
    }

    private void setActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar == null) {
            return;
        }
        actionBar.setTitle(R.string.game_play_action_bar_text);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                goToMain();
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    private void goToMain(){
        startActivity(App.getInstance().getMainIntent(this));
    }

    private void addFragmentManagers() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragmentQuestionContent = fetchFragmentQuestionContent();
        Fragment fragmentQuestionAnswers = fetchFragmentAnswers();

        if (fragmentQuestionContent == null) {
            fragmentQuestionContent = new QuestionContentFragment();
            fragmentManager.beginTransaction()
                    .add(R.id.frame_question_content, fragmentQuestionContent)
                    .commit();
        }

        if (fragmentQuestionAnswers == null) {
            fragmentQuestionAnswers = AnswersFragmentFactory.getInstance().getFragment();
            fragmentManager.beginTransaction()
                    .add(R.id.frame_question_answers, fragmentQuestionAnswers)
                    .commit();
        }
    }

    private Fragment fetchFragmentQuestionContent() {
        return getSupportFragmentManager().findFragmentById(R.id.frame_question_content);
    }

    private Fragment fetchFragmentAnswers() {
        return getSupportFragmentManager().findFragmentById(R.id.frame_question_answers);
    }

    private void initializeCountDown() {
        createCountDownHandler();
        initializeProgressBar();
        createCountDownThread();
        mCountDownThread.start();
    }

    private void createCountDownThread() {
        mCountDownThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    for (int i = 0; i < 200; i++) { // 20 seconds
                        Thread.sleep(100);
                        mCountDownHandler.sendEmptyMessage(0);
                    }
                } catch (InterruptedException e) {

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

    private void initializeProgressBar() {
        mRingProgressBar.setOnProgressListener(new RingProgressBar.OnProgressListener() {
            @Override
            public void progressToComplete() {
                onTimeOut();
            }
        });
    }

    public static Intent newIntent(Context context) {
        return new Intent(context, QuestionActivity.class);
    }

    @Override
    public void onAnswerGiven(String answerGiven, boolean wasItCorrect) {
        onAnswerShown();
        addAnswerToAnswersHistory(answerGiven);
        if (wasItCorrect) {
            incrementCorrectAnswers();
        }
    }

    @Override
    public void onAnswerShown() {
        incrementQuestionCounter();
        moveToNextQuestion();
        resetCounter();
        initializeCountDown();
    }

    @Override
    public void stopClock() {
        mCountDownThread.interrupt();
    }

    private void addAnswerToAnswersHistory(String answerGiven) {
        mAnswerHistory.put(mQuestionsCounter, answerGiven);
    }

    private void incrementQuestionCounter() {
        mQuestionsCounter++;
    }

    private void incrementCorrectAnswers() {
        mCorrectAnswers++;
    }

    private void replaceFragments() {
        Fragment fragmentQuestionContent = new QuestionContentFragment();
        Fragment fragmentQuestionAnswers = AnswersFragmentFactory.getInstance().getFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_question_content, fragmentQuestionContent)
                .replace(R.id.frame_question_answers, fragmentQuestionAnswers)
                .commit();
    }

    private void moveToNextQuestion() {
        if (mModel.getCurrentQuestionManager().isItLastQuestion()) {
            displayResults();
        }
        setNextQuestion();
        replaceFragments();
    }

    private void resetCounter() {
        mCountDownThread.interrupt();
        mTimeProgress = 0;
        mRingProgressBar.setProgress(mTimeProgress);
    }

    private void setNextQuestion() {
        mModel.getCurrentQuestionManager().setNextQuestion();
    }

    private void displayResults() {
        startActivity(ResultsActivity.newIntent(this, mCorrectAnswers, mQuestionsCounter));
    }

    private void onTimeOut() {
        //todo add something better here
        try {
            ((TimeOutListener) fetchFragmentAnswers()).onTimeOut();
        } catch (Exception e) {
            Log.d(getClass().toString(), e.toString());
        }
    }

    @Override
    public void onBackPressed() {
        goToMain();
    }
}
