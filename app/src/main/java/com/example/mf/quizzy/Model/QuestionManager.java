package com.example.mf.quizzy.Model;

import java.util.ArrayList;

public interface QuestionManager {
    String getQuestionText();

    void setNextQuestion();

    boolean isItLastQuestion();

    boolean isQuestionBoolean();

    ArrayList<String> getAllAnswers();

    String getCorrectAnswer();

    String getCategoryName();
}
