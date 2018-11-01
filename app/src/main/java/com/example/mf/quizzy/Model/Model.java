package com.example.mf.quizzy.Model;

import com.example.mf.quizzy.Exceptions.QuestionManagerDataLoadException;
import com.example.mf.quizzy.Listeners.onDataLoadingListener;

public interface Model {
    void reloadCurrentData() throws QuestionManagerDataLoadException;

    void loadData(String categoryName, onDataLoadingListener listener) throws QuestionManagerDataLoadException;

    QuestionManager getCurrentQuestionManager();
}
