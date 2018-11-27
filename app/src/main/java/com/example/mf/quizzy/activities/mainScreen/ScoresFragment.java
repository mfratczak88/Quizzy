package com.example.mf.quizzy.activities.mainScreen;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mf.quizzy.R;
import com.example.mf.quizzy.usersManagement.UsersManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ScoresFragment extends Fragment {
    private View mView;
    private RecyclerView mRecyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_scores, container, false);
        setRecyclerView();
        return mView;
    }

    void setRecyclerView() {
        mRecyclerView = mView.findViewById(R.id.recycler_view_scores);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(new Adapter(getUserResultsArray(), mRecyclerView));

    }

    private List<UserResult> getUserResultsArray() {
        UsersManager usersManager = UsersManager.getInstance(getContext());
        List<UserResult> userResults = new ArrayList<>();
        Map<String, String> userCategoryAndPointsMap = usersManager.getUserCategoryAndPointsMap();

        for (String category : userCategoryAndPointsMap.keySet()) {
            userResults.add(new UserResult(category, userCategoryAndPointsMap.get(category)));
        }
        return userResults;
    }

    private class Adapter extends RecyclerView.Adapter {
        private List<UserResult> mItems;

        private class ViewHolder extends RecyclerView.ViewHolder {
            private TextView mCategoryName, mCategoryPoints;

            ViewHolder(@NonNull View itemView) {
                super(itemView);
                mCategoryName = itemView.findViewById(R.id.score_item_category_name);
                mCategoryPoints = itemView.findViewById(R.id.score_item_category_points);
            }
        }

        Adapter(List<UserResult> items, RecyclerView recyclerView) {
            mItems = items;
            mRecyclerView = recyclerView;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.score_item, viewGroup, false);

            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
            ((ViewHolder) viewHolder).mCategoryName.setText(mItems.get(i).getCategoryName());
            ((ViewHolder) viewHolder).mCategoryPoints.setText(mItems.get(i).getCategoryPoints());
        }

        @Override
        public int getItemCount() {
            return mItems.size();
        }
    }

    // on purpose not trying to retrieve Objects
    // from roomPersistence to not couple it
    private class UserResult {
        String categoryName, CategoryPoints;

        UserResult(String categoryName, String categoryPoints) {
            this.categoryName = categoryName;
            CategoryPoints = categoryPoints;
        }

        String getCategoryPoints() {
            return CategoryPoints;
        }


        String getCategoryName() {
            return categoryName;
        }

    }
}
