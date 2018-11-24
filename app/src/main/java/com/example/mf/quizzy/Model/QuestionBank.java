package com.example.mf.quizzy.model;

// Questions taken from: https://opentdb.com/
// Specific API numbers collections corresponding to certain sets
// are in strings.xml file

import com.example.mf.quizzy.exceptions.QuestionManagerDataLoadException;
import com.example.mf.quizzy.listeners.DataLoadingListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuestionBank implements Model {
    private static Model sInstance;
    private Map<String, String> mCategories;
    private QuestionManagerImplementation mQuestionManager;
    private DataLoadingListener mListener;
    private List<QuestionManagerImplementation> mManagers = new ArrayList<>();

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
        for (QuestionManagerImplementation manager : mManagers) {
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
            String categoryNumber = mCategories.get(categoryName);
            mQuestionManager = new QuestionManagerImplementation(categoryNumber, categoryName, new DataLoadingListener() {
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
        } catch (ClassCastException | NullPointerException e) {
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
        // @TODO make it more elegantly
        mCategories = new HashMap<>();
        mCategories.put("Books", "10");
        mCategories.put("Film", "11");
        mCategories.put("Music", "12");
        mCategories.put("TV", "14");
        mCategories.put("Science", "17");
        mCategories.put("History", "23");
        mCategories.put("Politics", "24");
        mCategories.put("Animals", "27");
        mCategories.put("Geo", "22");

    }

}
