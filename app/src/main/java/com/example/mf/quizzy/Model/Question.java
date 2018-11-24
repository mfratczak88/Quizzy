package com.example.mf.quizzy.model;

import java.util.ArrayList;

interface Question {

    String getQuestionText();

    boolean isAnswerCorrect(String answerPicked);

    ArrayList<String> getAllAnswers();

    String getCorrectAnswerText();

    boolean isQuestionBoolean();
}
