package com.example.mf.quizzy.activities.register;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Patterns;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mf.quizzy.listeners.AuthenticationListener;
import com.example.mf.quizzy.App;
import com.example.mf.quizzy.R;
import com.example.mf.quizzy.usersManagement.RegistrationCredentials;
import com.example.mf.quizzy.usersManagement.UsersManagementFactory;
import com.example.mf.quizzy.usersManagement.UsersManager;
import com.example.mf.quizzy.util.Validator;

import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    private Button mRegisterButton, mBackButton;
    private TextView mNameText, mEmailText, mPasswordText;
    private UsersManager mUsersManager;
    private InputValidator mInputValidator = new InputValidator();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setButtonsAndTexts();
        setListenerForRegisterButton();
        setListenerForBackButton();

    }

    private void loadUsersManager() {
        mUsersManager = UsersManagementFactory.getUsersManager(this);
    }

    private void setButtonsAndTexts() {
        mNameText = findViewById(R.id.register_name);
        mEmailText = findViewById(R.id.register_email);
        mPasswordText = findViewById(R.id.register_password);
        mRegisterButton = findViewById(R.id.btnRegister);
        mBackButton = findViewById(R.id.btnLinkToLoginScreen);
    }

    private void setListenerForRegisterButton() {
        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    mInputValidator.validate();
                    registerUser();

                } catch (Exception e) {
                    validationToast(e.getMessage());
                    if (e.getMessage().equalsIgnoreCase(InputValidator.InputException.INVALID_PASSWORD)) {
                        passwordRulesToast();
                    }
                }
            }
        });
    }

    private void registerUser() {
        try {
            RegistrationCredentials registrationCredentials = new RegistrationCredentials(mNameText.getText().toString(), mEmailText.getText().toString(), mPasswordText.getText().toString());
            mUsersManager.registerUser(registrationCredentials, new AuthenticationListener() {
                @Override
                public void onSuccess(Map<String, String> response) {
                    showSuccessfulRegistrationDialog();
                }

                @Override
                public void onError(String response) {
                    showTechnicalProblemsToast();
                }
            });
        } catch (Exception e) {
            Log.d(getClass().toString(), e.toString());
        }
    }


    private void setListenerForBackButton() {
        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToLogin();
            }
        });
    }

    private void goToLogin() {
        startActivity(App.getInstance().getLoginIntent(this));
    }

    private void passwordRulesToast() {
        Toast.makeText(this, mInputValidator.getPasswordRules(), Toast.LENGTH_LONG).show();
    }

    private void validationToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void showTechnicalProblemsToast() {
        Toast.makeText(this, R.string.technical_issues_toast_text, Toast.LENGTH_SHORT).show();
    }

    private void showSuccessfulRegistrationDialog() {
        AlertDialog alertDialog = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.Dialog))
                .setMessage(R.string.successful_registration)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        goToLogin();
                    }
                })
                .create();
        alertDialog.show();
    }


    private class InputValidator {
        private class InputException extends Exception {

            private static final String EMPTY_PASSWORD = "Empty password";
            private static final String EMPTY_EMAIL = "Empty email";
            private static final String EMPTY_NAME = "Empty name";
            private static final String INVALID_PASSWORD = "Invalid password";
            private static final String INVALID_EMAIL = "Invalid email";

            private InputException(String exceptionText) {
                super(exceptionText);
            }
        }

        private static final String PASSWORD_RULES = "Password must contain: between 7 and 25 characters, have capital letters, numbers and at least one special character";
        private Validator mValidator;

        private InputValidator() {
            mValidator = Validator.
                    getBuilderForPasswordLengthBetween(7, 25)
                    .requireCapitalLetter()
                    .requireNumber()
                    .requireSpecialChar()
                    .build();
        }

        private String getPasswordRules() {
            return PASSWORD_RULES;
        }

        private void validate() throws Exception {
            validateName();
            validateEmail();
            validatePassword();
        }

        private void validateEmail() throws Exception {
            String email = RegisterActivity.this.mEmailText.getText().toString();
            if (email.isEmpty()) {
                throw new InputException(InputException.EMPTY_EMAIL);
            }
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                throw new InputException(InputException.INVALID_EMAIL);
            }
        }

        private void validatePassword() throws Exception {
            String password = RegisterActivity.this.mPasswordText.getText().toString();
            if (password.isEmpty()) {
                throw new InputException(InputException.EMPTY_PASSWORD);
            }

            if (!mValidator.isPasswordValid(password)) {
                throw new InputException(InputException.INVALID_PASSWORD);
            }
        }

        private void validateName() throws Exception {
            if (RegisterActivity.this.mNameText.getText().toString().isEmpty()) {
                throw new InputException(InputException.EMPTY_NAME);
            }
        }
    }
}
