package com.example.mf.quizzy.Activities.GamePlay;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mf.quizzy.Model.Model;
import com.example.mf.quizzy.Model.ModelFactory;
import com.example.mf.quizzy.Model.QuestionBank;
import com.example.mf.quizzy.Model.QuestionManager;
import com.example.mf.quizzy.R;

public class QuestionContentFragment extends Fragment {
    private Model mModel = ModelFactory.getFactory().getModel();
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_question_content, container, false);
        TextView textView = view.findViewById(R.id.question_text);
        textView.setText(getQuestionText());
        return view;
    }

    private String getQuestionText() {
        return mModel.getCurrentQuestionManager().getQuestionText();
    }

}
