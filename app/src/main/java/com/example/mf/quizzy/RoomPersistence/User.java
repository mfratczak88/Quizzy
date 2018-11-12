package com.example.mf.quizzy.RoomPersistence;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

@Entity
public class User implements Serializable {

    @PrimaryKey
    private int id;
    private String name;
    private String email;
    private String password;

    @ColumnInfo(name = "created_at")
    @TypeConverters({Timestamp.class})
    private Date createdAt;

    @ColumnInfo(name = "modified_at")
    @TypeConverters({Timestamp.class})
    private Date modifiedAt;


    public User() {
    }

    public User(int id, String name, String email, String password, Date createdAt, Date modifiedAt) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getModifiedAt() {
        return modifiedAt;
    }

    public void setModifiedAt(Date modifiedAt) {
        this.modifiedAt = modifiedAt;
    }
}