package com.example.mf.quizzy.activities.mainScreen;

import android.content.DialogInterface;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.example.mf.quizzy.exceptions.QuestionManagerDataLoadException;
import com.example.mf.quizzy.listeners.DataLoadingListener;
import com.example.mf.quizzy.App;
import com.example.mf.quizzy.model.ModelFactory;
import com.example.mf.quizzy.model.Model;
import com.example.mf.quizzy.R;
import com.example.mf.quizzy.sessions.SessionManager;
import java.util.Map;

public class MainActivity extends AppCompatActivity
        implements MainScreenFragment.CardViewClickListener, LoadingScreenFragment.PlayClickedListener {
    private Model mModel;
    private DrawerLayout mDrawerLayout;
    private SessionManager mSessionManager;
    private NavigationView mNavigationView;
    private Dialog mDialog = new Dialog();
    private NavigationDrawer mNavigationDrawer = new NavigationDrawer();
    private boolean mDataLoaded;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setMainFragment();
        setActionBar();
        setSessionManager();
        mNavigationDrawer.setNavigationDrawer();
        setModel();
    }

    private void setMainFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.frame_main_screen, new MainScreenFragment())
                .commit();
    }

    private void setActionBar() {
        try {
            ActionBar actionbar = getSupportActionBar();
            actionbar.setDisplayHomeAsUpEnabled(true);
            actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);
        } catch (Exception e) {
            Log.d(getClass().toString(), "Setting the action bar error");
            Toast.makeText(this, R.string.technical_issues_toast_text, Toast.LENGTH_SHORT).show();
        }
    }

    private void setSessionManager() {
        mSessionManager = new SessionManager(this);
    }

    private void setModel() {
        if (mModel == null)
            mModel = ModelFactory.getFactory().getModel();
    }

    private void logOut() {
        mSessionManager.logOutUser();
        startActivity(App.getInstance().getLoginIntent(this));
        finish();
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

    private void loadModelData(String categoryName) {
        if (mModel != null) {
            try {
                mModel.loadData(categoryName, new DataLoadingListener() {
                    @Override
                    public void onDataLoaded() {
                        mDataLoaded = true;
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

    private void showLoadingScreen() {
        replaceMainFragmentWithLoadingFragment();
    }

    private void replaceMainFragmentWithLoadingFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_main_screen, new LoadingScreenFragment())
                .commit();
    }

    private void launchQuestionActivity() {
        startActivity(App.getInstance().getGamePlayIntent(this));
    }


    private void couldNotLoadDataToast() {
        Toast.makeText(this, "Could not load data, please try again", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        mDialog.showAreYouSureYouWantToQuitDialog();
    }

    @Override
    public void onCardViewClick(String categoryName) {
        loadModelData(categoryName);
        showLoadingScreen();
    }

    @Override
    public void onPlayButtonClicked() {
        if (mDataLoaded) {
            launchQuestionActivity();
            return;
        }
        couldNotLoadDataToast();
    }

    private void onExit() {
        // todo : add local and backend update here
        finish();
    }

    private class Dialog {

        private void showAreYouSureYouWantToQuitDialog() {
            AlertDialog quitDialog = new AlertDialog.Builder(new ContextThemeWrapper(MainActivity.this, R.style.Dialog))
                    .setMessage(R.string.on_quit_message)
                    .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            onExit();
                        }
                    })
                    .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    })
                    .create();

            quitDialog.show();
        }

        private void showAreYouSureYouWantToLogOutDialog() {
            AlertDialog logOutDialog = new AlertDialog.Builder(new ContextThemeWrapper(MainActivity.this, R.style.Dialog))
                    .setMessage(R.string.on_log_out_message)
                    .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            logOut();
                        }
                    })
                    .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    })
                    .create();
            logOutDialog.show();
        }
    }


    private class NavigationDrawer {

        private void setNavigationDrawer() {
            mDrawerLayout = findViewById(R.id.drawer);
            mNavigationView = findViewById(R.id.nav_view);
            setUserDetails();
            setOnClickHandlers();
        }

        private void setOnClickHandlers() {
            setOnLogOutItemClickedHandler();
            setOnScoresItemClickedHandler();
        }

        private void setOnScoresItemClickedHandler(){

        }

        private void setOnLogOutItemClickedHandler() {
            final MenuItem logOut = mNavigationView.getMenu().findItem(R.id.log_out);
            logOut.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    mDrawerLayout.closeDrawers();
                    mDialog.showAreYouSureYouWantToLogOutDialog();
                    return true;
                }
            });
        }

        private void setUserDetails() {
            try {
                View headerLayout = mNavigationView.getHeaderView(0);
                if (headerLayout.getId() != R.id.header_layout) {
                    return;
                }
                Map<String, String> userDetails = mSessionManager.getUserDetails();

                ((TextView) headerLayout.findViewById(R.id.id_nav_name)).setText(userDetails.get("name"));
                ((TextView) headerLayout.findViewById(R.id.id_nav_email)).setText(userDetails.get("email"));

            } catch (Exception e) {
                Log.d(getClass().toString(), "Setting user details in the drawer error");
            }
        }
    }
}
