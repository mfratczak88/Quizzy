package com.example.mf.quizzy.Model;

public class AnswerBoolean implements Answer {
    boolean mCorrect;
    String mText;

    public AnswerBoolean(boolean correct, String text) {
        mCorrect = correct;
        mText = text;
    }

    public boolean isCorrect() {
        return mCorrect;
    }

    @Override
    public String getText() {
        return mText;
    }
}
