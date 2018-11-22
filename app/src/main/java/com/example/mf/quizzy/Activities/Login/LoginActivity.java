package com.example.mf.quizzy.Activities.Login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mf.quizzy.Listeners.AuthenticationListener;
import com.example.mf.quizzy.App;
import com.example.mf.quizzy.R;
import com.example.mf.quizzy.Activities.Register.RegisterActivity;
import com.example.mf.quizzy.Sessions.SessionManager;
import com.example.mf.quizzy.UsersManagement.LoginCredentials;
import com.example.mf.quizzy.UsersManagement.UsersManager;

import java.util.Map;

public class LoginActivity extends AppCompatActivity{
    private Button mLoginButton, mGoToRegisterButton;
    private TextView mEmailText, mPasswordText;
    private SessionManager mSessionManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loadSessionManager();
        if (isUserLogged()) {
            goToMainActivity();
            finish();
            return;
        }
        findTexts();
        findButtons();
        setLoginButtonListener();
        setRegisterButtonListener();
    }

    private void loadSessionManager(){
        mSessionManager = new SessionManager(this);
    }

    private void findTexts(){
        mEmailText = findViewById(R.id.login_email);
        mPasswordText = findViewById(R.id.login_password);
    }
    private void findButtons(){
        mLoginButton = findViewById(R.id.id_login_button);
        mGoToRegisterButton = findViewById(R.id.id_back_register_button);
    }

    private void setRegisterButtonListener(){
        mGoToRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToRegisterActivity();
            }
        });


    }
    private void setLoginButtonListener(){
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isInputFilledIn()) {
                    fillInInputToast();
                    return;
                }
                loginUser();
            }
        });
    }

    private void loginUser(){
        UsersManager usersManager = UsersManager.getInstance(LoginActivity.this);
        try {

            usersManager.loginUser(new LoginCredentials(mEmailText.getText().toString(), mPasswordText.getText().toString()), new AuthenticationListener() {
                @Override
                public void onSuccess(Map<String, String> response) {
                    goToMainActivity();
                }

                @Override
                public void onError(String response) {
                    displayTechnicalIssuesToast();
                }
            });
        } catch (Exception e) {
            displayTechnicalIssuesToast();
        }
    }

    private boolean isInputFilledIn() {
        return  !(mEmailText.getText().toString().isEmpty() || mPasswordText.getText().toString().isEmpty());
    }

    private void fillInInputToast() {
        Toast.makeText(this, "Please fill in email and password fields", Toast.LENGTH_SHORT).show();
    }

    private boolean isUserLogged() {
        return mSessionManager.isLoggedIn();
    }

    private void goToMainActivity() {
        startActivity(App.getInstance().getMainIntent(this));
        finish();
    }

    private void goToRegisterActivity() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    private void displayTechnicalIssuesToast() {
        Toast.makeText(this, R.string.technical_issues_toast_text, Toast.LENGTH_LONG).show();
    }

    private void displayInvalidCredentialsToast(){
        Toast.makeText(this, R.string.no_user_found_at_login, Toast.LENGTH_SHORT).show();
    }
}
