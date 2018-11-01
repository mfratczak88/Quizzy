package com.example.mf.quizzy.Model;

import java.util.ArrayList;
import java.util.List;

interface Question {

    String getQuestionText();

    boolean isAnswerCorrect(String answerPicked);

    ArrayList<String> getAllAnswers();

    String getCorrectAnswerText();

    boolean isQuestionBoolean();
}
