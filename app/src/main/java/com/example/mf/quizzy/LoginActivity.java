package com.example.mf.quizzy;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mf.quizzy.Listeners.AuthenticationListener;
import com.example.mf.quizzy.Sessions.SessionManager;
import com.example.mf.quizzy.UsersManagement.UsersManager;

public class LoginActivity extends AppCompatActivity implements AuthenticationListener {
    private Button mLoginButton, mGoToRegisterButton;
    private TextView mEmailText, mPasswordText;
    private SessionManager mSessionManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mSessionManager = new SessionManager(getApplicationContext());
        // check if user is already logged in
        if (isUserLogged()) {
            goToMainActivity();
            return;
        }
        // if not set login page
        mLoginButton = findViewById(R.id.id_login_button);
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isInputFilledIn()) {
                    fillInInputToast();
                }

                // todo: dagger here would help obviously...
                UsersManager usersManager = UsersManager.getInstance(LoginActivity.this);
                try {
                    usersManager.loginUser(mEmailText.getText().toString(), mPasswordText.getText().toString());
                } catch (Exception e) {
                    displayTechnicalIssuesToast();
                }
            }
        });
        mGoToRegisterButton = findViewById(R.id.id_back_register_button);
        mGoToRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToRegisterActivity();
            }
        });

        mEmailText = findViewById(R.id.login_email);
        mPasswordText = findViewById(R.id.login_password);
    }

    private boolean isInputFilledIn() {
        return mEmailText.getText().toString().isEmpty() || mPasswordText.getText().toString().isEmpty();
    }

    private void fillInInputToast() {
        Toast.makeText(this, "Please fill in email and password fields", Toast.LENGTH_SHORT).show();
    }

    private boolean isUserLogged() {
        return mSessionManager.isLoggedIn();
    }

    private void goToMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void goToRegisterActivity() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    @Override
    public void onSuccess(String response) {

    }

    @Override
    public void onError(String response) {

    }

    private void displayTechnicalIssuesToast() {
        Toast.makeText(this, R.string.technical_issues_toast_text, Toast.LENGTH_LONG).show();
    }
}
