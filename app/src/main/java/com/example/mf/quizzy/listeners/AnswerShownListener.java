package com.example.mf.quizzy.listeners;

public interface AnswerShownListener {
    void onAnswerGiven(String answerGiven, boolean wasItCorrect);
    void onAnswerShown();
    void stopClock();
}
