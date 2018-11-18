package com.example.mf.quizzy;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mf.quizzy.Listeners.AuthenticationListener;
import com.example.mf.quizzy.MainController.AppController;
import com.example.mf.quizzy.UsersManagement.UsersManager;
import com.example.mf.quizzy.Util.Validator;

import java.util.Map;

public class RegisterActivity extends AppCompatActivity implements AuthenticationListener {
    private Button mRegisterButton, mBackButton;
    private TextView mNameText, mEmailText, mPasswordText;
    private InputValidator mInputValidator = new InputValidator();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setButtonsAndTexts();
        setListenerForRegisterButton();
        setListenerForBackButton();

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
        UsersManager userManager = UsersManager.getInstance(RegisterActivity.this);
        userManager.registerUser(mNameText.getText().toString(), mEmailText.getText().toString(), mPasswordText.getText().toString());
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
        startActivity(AppController.getInstance().getLoginIntent(this));
    }

    private void passwordRulesToast() {
        Toast.makeText(this, mInputValidator.getPasswordRules(), Toast.LENGTH_LONG).show();
    }

    private void validationToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSuccess(Map<String, String> response) {
        showSuccessfulRegistrationDialog();
    }

    @Override
    public void onError(String response) {

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
