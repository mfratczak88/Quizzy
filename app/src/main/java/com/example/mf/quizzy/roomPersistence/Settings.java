package com.example.mf.quizzy.roomPersistence;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(tableName = "settings",
        foreignKeys = {@ForeignKey(
                entity = User.class,
                parentColumns = "id",
                childColumns = "userId",
                onDelete = CASCADE)})

public class Settings implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int userId;
    private boolean saveProgress;
    private int answerTimeInSeconds;
    private int questionsPerSession;
    private String level;

    public Settings() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public boolean isSaveProgress() {
        return saveProgress;
    }

    public void setSaveProgress(boolean saveProgress) {
        this.saveProgress = saveProgress;
    }

    public int getAnswerTimeInSeconds() {
        return answerTimeInSeconds;
    }

    public void setAnswerTimeInSeconds(int answerTimeInSeconds) {
        this.answerTimeInSeconds = answerTimeInSeconds;
    }

    public int getQuestionsPerSession() {
        return questionsPerSession;
    }

    public void setQuestionsPerSession(int questionsPerSession) {
        this.questionsPerSession = questionsPerSession;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }
}
