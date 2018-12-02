package com.example.mf.quizzy.usersManagement;

import com.example.mf.quizzy.App;
import com.example.mf.quizzy.config.AppConfig;
import com.example.mf.quizzy.roomPersistence.Settings;

class UserSettingsImpl implements UserSettings {
    private Settings mSettings;

    UserSettingsImpl(Settings settings) {
        mSettings = settings;
    }

    @Override
    public boolean doSaveProgress() {
        return mSettings.isSaveProgress();
    }

    @Override
    public void setSaveProgress(boolean saveProgress) {
        mSettings.setSaveProgress(saveProgress);
    }

    @Override
    public int getAnswerTimeInSeconds() {
        return  mSettings.getAnswerTimeInSeconds();
    }

    @Override
    public void setAnswerTimeInSeconds(int answerTimeInSeconds) {
        mSettings.setAnswerTimeInSeconds(answerTimeInSeconds);
    }

    @Override
    public int getQuestionsPerSession() {
        return mSettings.getQuestionsPerSession();
    }

    @Override
    public void setQuestionsPerSession(int questionsPerSession) {
        mSettings.setQuestionsPerSession(questionsPerSession);
    }

    @Override
    public String getLevel() {
        return mSettings.getLevel();
    }

    @Override
    public void setLevel(String level) {
        mSettings.setLevel(level);
    }

    Settings getSettings() {
        return mSettings;
    }

}
