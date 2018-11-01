package com.example.mf.quizzy;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mf.quizzy.Exceptions.QuestionManagerDataLoadException;
import com.example.mf.quizzy.Model.Model;
import com.example.mf.quizzy.Model.ModelFactory;
import com.example.mf.quizzy.Model.QuestionBank;

public class ResultsActivity extends AppCompatActivity {
    private static final String EXTRA_TOTAL_QUESTIONS = "com.example.mf.quizzy.resultactivity.total_questions";
    private static final String EXTRA_ANSWERS_CORRECT = "com.example.mf.quizzy.resultactivity.answers_correct";
    private Model mModel = ModelFactory.getFactory().getModel();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        View view = findViewById(R.id.id_result_activity);
        setResult(view);
        setButtons(view);
    }

    public static Intent newIntent(Context context, int questionsCorrect, int totalQuestions) {
        Intent intent = new Intent(context, ResultsActivity.class);
        intent.putExtra(EXTRA_TOTAL_QUESTIONS, totalQuestions);
        intent.putExtra(EXTRA_ANSWERS_CORRECT, questionsCorrect);
        return intent;
    }

    private void setResult(View view) {
        TextView correctAnswersCount = (TextView) view.findViewById(R.id.id_correct_answers_count);
        correctAnswersCount.setText(getStringResult());
    }

    private String getStringResult() {
        return getIntent().getIntExtra(EXTRA_ANSWERS_CORRECT, 0) + "/" + getIntent().getIntExtra(EXTRA_TOTAL_QUESTIONS, 0);
    }

    private void setButtons(View view) {
        final Button playAgainButton = view.findViewById(R.id.id_play_again_button);
        playAgainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    mModel.reloadCurrentData();
                    playAgain();
                } catch (QuestionManagerDataLoadException e) {
                    Toast.makeText(ResultsActivity.this, "Sorry, no more data available, please choose other category", Toast.LENGTH_SHORT).show();
                    backToMain();
                }
            }
        });

        Button backToHomeButton = view.findViewById(R.id.id_back_button);
        backToHomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToMain();

            }
        });
    }

    private void backToMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void playAgain() {
        startActivity(QuestionActivity.newIntent(this));
    }

}
