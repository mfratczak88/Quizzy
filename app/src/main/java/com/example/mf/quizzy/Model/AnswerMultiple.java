package com.example.mf.quizzy.Model;

class AnswerMultiple implements Answer {
    boolean mCorrect;
    String mText;

    public AnswerMultiple(boolean correct, String text) {
        mCorrect = correct;
        mText = text;
    }

    @Override
    public String getText() {
        return mText;
    }

    public boolean isCorrect() {
        return mCorrect;
    }
}
