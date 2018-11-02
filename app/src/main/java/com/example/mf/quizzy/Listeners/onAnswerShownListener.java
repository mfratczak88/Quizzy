package com.example.mf.quizzy.Listeners;

public interface onAnswerShownListener {
    void onAnswerGiven(String answerGiven, boolean wasItCorrect);
    void onAnswerShown();
    void stopClock();
}
