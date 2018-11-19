package com.example.mf.quizzy.Activities.GamePlay;

import android.support.v4.app.Fragment;

import com.example.mf.quizzy.Model.Model;
import com.example.mf.quizzy.Model.ModelFactory;

public class AnswersFragmentFactory {
    private static final int QUESTION_BOOLEAN = 0;
    private static final int QUESTION_MULTIPLE = 1;
    private static AnswersFragmentFactory sInstance;
    private Model mModel = ModelFactory.getFactory().getModel();

    public static AnswersFragmentFactory getInstance() {
        if (sInstance == null)
            sInstance = new AnswersFragmentFactory();

        return sInstance;
    }

    public Fragment getFragment() {
        return getQuestionType() == QUESTION_MULTIPLE ? new AnswerMultipleFragment() : new AnswerBooleanFragment();
    }

    private int getQuestionType() {
        if (mModel.getCurrentQuestionManager().isQuestionBoolean())
            return QUESTION_BOOLEAN;

        return QUESTION_MULTIPLE;
    }
}
