package com.example.mf.quizzy;

import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.mf.quizzy.Exceptions.QuestionManagerDataLoadException;
import com.example.mf.quizzy.Listeners.DataLoadingListener;
import com.example.mf.quizzy.Model.ModelFactory;
import com.example.mf.quizzy.Model.Model;


public class MainActivity extends AppCompatActivity {
    private GridLayout mGridLayout;
    private Model mModel;
    private DrawerLayout mDrawerLayout;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mGridLayout = findViewById(R.id.gridLayoutID);

        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);

        mDrawerLayout = findViewById(R.id.drawer);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                menuItem.setChecked(true);
                mDrawerLayout.closeDrawers();
                return true;
            }
        });
        setEventHandlersForGrid();
        fetchModel();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void fetchModel() {
        if (mModel == null)
            mModel = ModelFactory.getFactory().getModel();
    }

    private void loadModelData(int categoryNumber) {
        if (mModel != null) {
            String categoryName = getCardViewText(categoryNumber);
            try {
                mModel.loadData(categoryName, new DataLoadingListener() {
                    @Override
                    public void onDataLoaded() {
                        launchQuestionActivity();
                    }

                    @Override
                    public void onDataLoadingFailure() {
                        couldNotLoadDataToast();
                    }
                });
            } catch (QuestionManagerDataLoadException e) {
                couldNotLoadDataToast();
            }
        }
    }

    private void setEventHandlersForGrid() {
        for (int i = 0; i < mGridLayout.getChildCount(); i++) {
            final CardView cardView = (CardView) mGridLayout.getChildAt(i);
            final int counter = i;
            cardView.setOnClickListener(new View.OnClickListener() {
                final int i = counter;

                @Override
                public void onClick(View v) {
                    loadModelData(i);
                }
            });
        }
    }

    private void launchQuestionActivity() {
        startActivity(QuestionActivity.newIntent(this));

    }


    private void couldNotLoadDataToast() {
        Toast.makeText(this, "Could not load data, please try again", Toast.LENGTH_SHORT).show();
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
