package com.example.mf.quizzy.Activities.GamePlay;

import android.view.View;
import android.widget.Button;

import com.example.mf.quizzy.Activities.GamePlay.AnswerFragment;
import com.example.mf.quizzy.R;

public class AnswerBooleanFragment extends AnswerFragment {
    @Override
    protected void extractButtonsFromView(View view) {
        mButtons.add((Button) view.findViewById(R.id.button_true));
        mButtons.add((Button) view.findViewById(R.id.button_false));
    }

    @Override
    protected void setEventHandlersAndTextsForButtons() {
        int counter = 0;
        for (int i = 0; i < 2; i++) {
            final int clickedButtonNumber = counter;
            Button button = mButtons.get(i);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    checkAnswerAndNotifyListener(clickedButtonNumber);
                }
            });
            counter++;
        }
    }

    @Override
    protected int getLayoutNumber() {
        return (R.layout.fragment_question_boolean);
    }

    @Override
    protected int getWrapperLayoutId() {
        return (R.id.fragment_question_boolean_layout);
    }
}
