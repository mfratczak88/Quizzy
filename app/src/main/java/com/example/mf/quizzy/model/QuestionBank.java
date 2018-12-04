package com.example.mf.quizzy.model;

import com.example.mf.quizzy.App;
import com.example.mf.quizzy.exceptions.QuestionManagerDataLoadException;
import com.example.mf.quizzy.listeners.DataLoadingListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class QuestionBank implements Model {
    private static Model sInstance;
    private Map<String, String> mCategories;
    private QuestionManagerImpl mQuestionManager;
    private DataLoadingListener mListener;
    private List<QuestionManagerImpl> mManagers = new ArrayList<>();

    public static Model getInstance() {
        if (sInstance == null) {
            sInstance = new QuestionBank();
        }
        return sInstance;
    }

    private QuestionBank() {
        setCategoriesNumbers();
    }

    @Override
    public void reloadCurrentData() throws QuestionManagerDataLoadException {
        notifyCurrentManagerToReloadData();
    }

    @Override
    public void loadData(String categoryName, DataLoadingListener listener) throws QuestionManagerDataLoadException {
        addListener(listener);
        if (hasThisManager(categoryName)) {
            setRequiredQuestionManager(categoryName);
            try {
                notifyCurrentManagerToReloadData();
            } catch (QuestionManagerDataLoadException e) {
                notifyOnFailure();
                return;
            }
            notifyOnLoaded();
            return;
        }
        loadNewQuestionManager(categoryName);
    }

    @Override
    public QuestionManager getCurrentQuestionManager() {
        return mQuestionManager;
    }

    private void notifyCurrentManagerToReloadData() throws QuestionManagerDataLoadException {
        mQuestionManager.reloadQuestions();
    }

    private void setRequiredQuestionManager(String categoryName) {
        for (QuestionManagerImpl manager : mManagers) {
            if (manager.getCategoryName().equals(categoryName))
                mQuestionManager = manager;
        }
    }

    private boolean hasThisManager(String categoryName) {
        for (QuestionManager manager : mManagers) {
            if (manager.getCategoryName().equals(categoryName)) {
                return true;
            }

        }
        return false;
    }

    private void addListener(DataLoadingListener listener) {
        mListener = listener;
    }

    private void loadNewQuestionManager(String categoryName) throws QuestionManagerDataLoadException {
        try {
            mQuestionManager = new QuestionManagerImpl(categoryName, new DataLoadingListener() {
                @Override
                public void onDataLoaded() {
                    mListener.onDataLoaded();
                }

                @Override
                public void onDataLoadingFailure() {
                    getRidOfEmptyManager();
                    mListener.onDataLoadingFailure();
                }
            });
            mManagers.add(mQuestionManager);
        } catch (Exception e) {
            throw new QuestionManagerDataLoadException();
        }

    }

    private void getRidOfEmptyManager() {
        mQuestionManager = null;
    }

    private void notifyOnLoaded() {
        mListener.onDataLoaded();
    }

    private void notifyOnFailure() {
        mListener.onDataLoadingFailure();
    }

    private void setCategoriesNumbers() {
        mCategories = App.getInstance().getAppConfig().getCategories();
    }
}
