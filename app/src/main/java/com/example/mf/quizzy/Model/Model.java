package com.example.mf.quizzy.model;

import com.example.mf.quizzy.exceptions.QuestionManagerDataLoadException;
import com.example.mf.quizzy.listeners.DataLoadingListener;

public interface Model {
    void reloadCurrentData() throws QuestionManagerDataLoadException;
    void loadData(String categoryName, DataLoadingListener listener) throws QuestionManagerDataLoadException;
    QuestionManager getCurrentQuestionManager();
}
