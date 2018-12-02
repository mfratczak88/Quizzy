package com.example.mf.quizzy.activities.mainScreen;

import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
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
import com.example.mf.quizzy.R;
import com.example.mf.quizzy.model.ModelFactory;
import com.example.mf.quizzy.model.Model;
import com.example.mf.quizzy.usersManagement.UsersManagementFactory;
import com.example.mf.quizzy.usersManagement.UsersManager;


public class MainActivity extends AppCompatActivity implements
        CardViewFragment.CardViewClickListener,
        LoadingScreenFragment.PlayClickedListener {

    private Model mModel;
    private static final String MENU_BAR_TITLE = "Quizzy";
    private static final int LOADING_TIME_IN_SECONDS = 5;
    private DrawerLayout mDrawerLayout;
    private UsersManager mUsersManager;
    private NavigationView mNavigationView;
    private Dialog mDialog = new Dialog();
    private NavigationDrawer mNavigationDrawer = new NavigationDrawer();
    private MainScreenFragmentManager mMainScreenFragmentManager = new MainScreenFragmentManager();
    private boolean mDataLoaded;
    private Fragment mCurrentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setFragment(mMainScreenFragmentManager.getCardViewFragment());
        setActionBar();
        setUsersManager();
        mNavigationDrawer.setNavigationDrawer();
        setModel();
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

    private void setUsersManager() {
        mUsersManager = UsersManagementFactory.getUsersManager(getApplicationContext());
    }

    private void setModel() {
        if (mModel == null)
            mModel = ModelFactory.getFactory().getModel();
    }

    private void logOut() {
        mUsersManager.logOutUser();
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
        setFragment(MainScreenFragmentManager.geMainScreenFragmentManager().getLoadingScreenFragment(LOADING_TIME_IN_SECONDS));
    }


    private void setFragment(Fragment fragment) {
        mCurrentFragment = fragment;
        assert (fragment != null);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_main_screen, fragment)
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
        if (mCurrentFragment instanceof CardViewFragment) {
            mDialog.showAreYouSureYouWantToQuitDialog();
            return;
        }
        setFragment(mMainScreenFragmentManager.getCardViewFragment());
        setActionBarDefaultTitle();
    }

    private void setActionBarDefaultTitle() {
        try {
            getSupportActionBar().setTitle(MENU_BAR_TITLE);
        } catch (Exception e) {
            Log.d("MainActivity", "Could not set ActionBar main title");
        }
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
            mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    if (menuItem.getItemId() == R.id.log_out) {
                        onLogOutSelected();
                    } else {
                        onItemSelected(menuItem);
                    }
                    return true;
                }
            });
            setUserDetails();
        }

        private void onItemSelected(MenuItem menuItem) {
            Fragment fragment;
            switch (menuItem.getItemId()) {
                case R.id.nav_scores:
                    fragment = mMainScreenFragmentManager.getScoresFragment();
                    break;
                case R.id.nav_settings:
                    fragment = mMainScreenFragmentManager.getSettingsFragment();
                    break;
                default:
                    return;
            }
            menuItem.setChecked(false);
            setTitle(menuItem.getTitle());
            setFragment(fragment);
            mDrawerLayout.closeDrawers();
        }


        private void onLogOutSelected() {
            mDrawerLayout.closeDrawers();
            mDialog.showAreYouSureYouWantToLogOutDialog();
        }


        private void setUserDetails() {
            try {
                View headerLayout = mNavigationView.getHeaderView(0);
                if (headerLayout.getId() != R.id.header_layout) {
                    return;
                }

                ((TextView) headerLayout.findViewById(R.id.id_nav_name)).setText(mUsersManager.getUserName());
                ((TextView) headerLayout.findViewById(R.id.id_nav_email)).setText(mUsersManager.getUserEmail());

            } catch (Exception e) {
                Log.d(getClass().toString(), "Setting user details in the drawer error");
            }
        }
    }
}
