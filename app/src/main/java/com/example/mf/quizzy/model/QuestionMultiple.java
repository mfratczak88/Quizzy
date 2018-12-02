package com.example.mf.quizzy.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class QuestionMultiple implements Question {
    private String mQuestionText;
    private int mTrueQuestionIndex;
    private List<Answer> mAnswers;

    protected QuestionMultiple(String questionText, List<String> answers) {
        this.mQuestionText = questionText;
        mAnswers = new ArrayList<>();
        for (int i = 0; i < answers.size(); i++) {
            mAnswers.add(new AnswerMultiple(i == answers.size() - 1, answers.get(i)));
        }
    }

    @Override
    public boolean isQuestionBoolean() {
        return false;
    }

    @Override
    public String getQuestionText() {
        return mQuestionText;
    }

    @Override
    public boolean isAnswerCorrect(String answerPicked) {
        return getCorrectAnswer().isCorrect();
    }

    @Override
    public ArrayList<String> getAllAnswers() {
        ArrayList<String> allAnswers = new ArrayList<>();
        for (Answer answer : mAnswers) {
            allAnswers.add(answer.getText());
        }
        return allAnswers;
    }

    @Override
    public String getCorrectAnswerText() {
        return getCorrectAnswer().getText();
    }

    private Answer getCorrectAnswer() {
        Iterator iterator = mAnswers.iterator();
        Answer answer;
        while (iterator.hasNext()) {
            answer = (Answer) iterator.next();
            if (answer.isCorrect()) {

                return answer;
            }
        }
        return null;
    }
}
