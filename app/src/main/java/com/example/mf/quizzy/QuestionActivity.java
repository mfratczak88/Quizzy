package com.example.mf.quizzy;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.mf.quizzy.Listeners.onAnsweredQuestionListener;
import com.example.mf.quizzy.Model.Model;
import com.example.mf.quizzy.Model.ModelFactory;
import com.example.mf.quizzy.Model.QuestionBank;

import java.lang.reflect.Method;
import java.util.HashMap;

public class QuestionActivity extends AppCompatActivity implements onAnsweredQuestionListener {
    private Fragment mFragmentQuestionContent, mFragmentQuestionAnswers;
    private HashMap<Integer, String> mAnswerHistory = new HashMap<>();
    private int mCorrectAnswers;
    private int mQuestionsCounter;
    private Model mModel;

    public QuestionActivity(){
        mModel = ModelFactory.getFactory().getModel();
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment mFragmentQuestionContent = fragmentManager.findFragmentById(R.id.frame_question_content);
        Fragment mFragmentQuestionAnswers = fragmentManager.findFragmentById(R.id.frame_question_answers);

        if (mFragmentQuestionContent == null) {
            mFragmentQuestionContent = new QuestionContentFragment();

            fragmentManager.beginTransaction()
                    .add(R.id.frame_question_content, mFragmentQuestionContent)
                    .commit();
        }

        if (mFragmentQuestionAnswers == null) {
            mFragmentQuestionAnswers = AnswersFragmentFactory.getInstance().getFragment();

            fragmentManager.beginTransaction()
                    .add(R.id.frame_question_answers, mFragmentQuestionAnswers)
                    .commit();
        }
    }

    public static Intent newIntent(Context context) {
        return new Intent(context, QuestionActivity.class);
    }

    @Override
    public void onAnswerGiven(String answerGiven, boolean wasItCorrect) {
        incrementQuestionCounter();
        addAnswerToAnswersHistory(answerGiven);
        if (wasItCorrect) {
            onCorrectAnswer();
            return;
        }
        onIncorrectAnswer();
    }

    private void onCorrectAnswer() {
        incrementCorrectAnswers();
        moveToNextQuestion();
    }

    private void onIncorrectAnswer() {
        moveToNextQuestion();
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
        mFragmentQuestionContent = new QuestionContentFragment();
        mFragmentQuestionAnswers = AnswersFragmentFactory.getInstance().getFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_question_content, mFragmentQuestionContent)
                .replace(R.id.frame_question_answers, mFragmentQuestionAnswers)
                .commit();
    }

    private void moveToNextQuestion() {
        if (mModel.getCurrentQuestionManager().isItLastQuestion()) {
            displayResults();
        }
        setNextQuestion();
        replaceFragments();
    }

    private void setNextQuestion() {
        mModel.getCurrentQuestionManager().setNextQuestion();
    }

    private void displayResults() {
        startActivity(ResultsActivity.newIntent(this, mCorrectAnswers, mQuestionsCounter));
    }
}
