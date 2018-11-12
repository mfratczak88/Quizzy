package com.example.mf.quizzy.Model;

import com.example.mf.quizzy.Exceptions.QuestionManagerDataLoadException;
import com.example.mf.quizzy.Listeners.DataLoadingListener;

public interface Model {
    void reloadCurrentData() throws QuestionManagerDataLoadException;

    void loadData(String categoryName, DataLoadingListener listener) throws QuestionManagerDataLoadException;

    QuestionManager getCurrentQuestionManager();
}
