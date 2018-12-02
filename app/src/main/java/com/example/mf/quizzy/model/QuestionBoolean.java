package com.example.mf.quizzy.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class QuestionBoolean implements Question {
    private UUID mId;
    private String mQuestionText;
    private List<Answer> mAnswers = new ArrayList<>();

    @Override
    public boolean isQuestionBoolean() {
        return true;
    }

    protected QuestionBoolean(String questionText, List<String> answers) {
        mId = UUID.randomUUID();
        mQuestionText = questionText;
        mAnswers.add(new AnswerBoolean(false, answers.get(0)));
        mAnswers.add(new AnswerBoolean(true, answers.get(1)));
    }

    protected UUID getId() {
        return mId;
    }

    @Override
    public String getQuestionText() {
        return mQuestionText;
    }

    @Override
    public boolean isAnswerCorrect(String answerPicked) {
        return answerPicked.equals(getCorrectAnswerText());
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
        AnswerBoolean answerBoolean;
        for (Answer answer : mAnswers) {
            if (answer.isCorrect()) {
                return answer.getText();
            }
        }
        return null;
    }
}
