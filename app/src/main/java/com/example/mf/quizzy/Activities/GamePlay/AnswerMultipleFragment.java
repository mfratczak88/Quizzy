package com.example.mf.quizzy.Activities.GamePlay;

import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.Button;

import com.example.mf.quizzy.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

public class AnswerMultipleFragment extends AnswerFragment {
    @Override
    protected void extractButtonsFromView(View view) {
        ConstraintLayout layout = (ConstraintLayout) view.findViewById(R.id.fragment_question_choices_layout);
        for (int i = 0; i < 4; i++) {
            try {
                Button button = (Button) layout.getChildAt(i);
                mButtons.add(button);
            } catch (ClassCastException | NullPointerException exception) {
                // not possible as long as the layout is fine
            }
        }
    }

    protected void setEventHandlersAndTextsForButtons() {
        Button button;
        int counter = 0;
        ArrayList<String> allAnswers = getAllAnswers();
        Collections.shuffle(allAnswers);
        Iterator iterator = mButtons.iterator();

        while (iterator.hasNext()) {
            final int clickedButtonNumber = counter;
            button = (Button) iterator.next();
            button.setText(allAnswers.get(counter));
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
        return R.layout.fragment_question_choices;
    }

    @Override
    protected int getWrapperLayoutId() {
        return R.id.fragment_question_choices_layout;
    }
}
