package com.example.mf.quizzy.UsersManagement;

public class RegistrationCredentials {
    private String email;
    private String name;
    private String password;

    public RegistrationCredentials() {
    }

    public RegistrationCredentials(String name, String email, String password) {
        this.email = email;
        this.name = name;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
