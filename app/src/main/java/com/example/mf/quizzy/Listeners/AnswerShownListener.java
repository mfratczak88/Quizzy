package com.example.mf.quizzy.Listeners;

public interface AnswerShownListener {
    void onAnswerGiven(String answerGiven, boolean wasItCorrect);
    void onAnswerShown();
    void stopClock();
}
