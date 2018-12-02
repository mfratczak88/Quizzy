package com.example.mf.quizzy.usersManagement;

public interface UserSettings {

    boolean doSaveProgress();

    void setSaveProgress(boolean saveProgress);

    int getAnswerTimeInSeconds();

    void setAnswerTimeInSeconds(int answerTimeInSeconds);

    int getQuestionsPerSession();

    void setQuestionsPerSession(int questionsPerSession);

    String getLevel();

    void setLevel(String level);
}
