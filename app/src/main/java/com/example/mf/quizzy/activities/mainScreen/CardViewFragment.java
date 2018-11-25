package com.example.mf.quizzy.activities.mainScreen;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mf.quizzy.R;

public class CardViewFragment extends Fragment {
    interface CardViewClickListener {
        void onCardViewClick(String categoryName);
    }

    private GridLayout mGridLayout;
    private CardViewClickListener mListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (CardViewClickListener) context;
        } catch (ClassCastException e) {

        }

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_screen, container, false);
        setGridLayout(view);
        return view;
    }


    private void setGridLayout(View view) {
        mGridLayout = view.findViewById(R.id.gridLayoutID);
        setEventHandlersForGrid();

    }

    private void setEventHandlersForGrid() {
        for (int i = 0; i < mGridLayout.getChildCount(); i++) {
            final CardView cardView = (CardView) mGridLayout.getChildAt(i);
            final int counter = i;
            cardView.setOnClickListener(new View.OnClickListener() {
                final int i = counter;

                @Override
                public void onClick(View v) {
                    mListener.onCardViewClick(getCardViewText(i));
                }
            });
        }
    }

    private String getCardViewText(int number) {
        try {
            LinearLayout linearLayout = (LinearLayout) ((CardView) mGridLayout.getChildAt(number)).getChildAt(0);

            for (int i = 0; i < linearLayout.getChildCount(); i++) {
                if (linearLayout.getChildAt(i) instanceof TextView) {
                    return ((TextView) linearLayout.getChildAt(i)).getText().toString();
                }
            }
            return null;

        } catch (ClassCastException | NullPointerException e) {
            return e.getMessage();
        }
    }

}
