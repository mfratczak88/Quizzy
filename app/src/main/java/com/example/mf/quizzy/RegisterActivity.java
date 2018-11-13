package com.example.mf.quizzy;

import android.os.Bundle;
import android.os.UserManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mf.quizzy.Listeners.AuthenticationListener;
import com.example.mf.quizzy.UsersManagement.UsersManager;

import java.util.Map;

public class RegisterActivity extends AppCompatActivity implements AuthenticationListener{
    Button mRegisterButton;
    TextView mNameText, mEmailText, mPasswordText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mRegisterButton = findViewById(R.id.btnRegister);
        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isInputFilledIn()) {
                    fillInMandatoryFieldsToast();
                    return;
                }

                // todo: again - dagger
                UsersManager userManager = UsersManager.getInstance(RegisterActivity.this);
                userManager.registerUser(mNameText.getText().toString(), mEmailText.getText().toString(), mPasswordText.getText().toString());
            }
        });

        mNameText = findViewById(R.id.register_name);
        mEmailText = findViewById(R.id.register_email);
        mPasswordText = findViewById(R.id.register_password);
    }

    private boolean isInputFilledIn() {
        return mEmailText.getText().toString().isEmpty() || mPasswordText.getText().toString().isEmpty() || mNameText.getText().toString().isEmpty();
    }

    private void fillInMandatoryFieldsToast() {
        Toast.makeText(this, R.string.fill_in_manadatory_fields_toast, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSuccess(Map<String, String> response) {

    }

    @Override
    public void onError(String response) {

    }
}
