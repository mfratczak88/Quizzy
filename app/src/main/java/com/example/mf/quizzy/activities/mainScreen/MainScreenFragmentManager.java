package com.example.mf.quizzy.activities.mainScreen;

import android.support.v4.app.Fragment;

class MainScreenFragmentManager {

    MainScreenFragmentManager() {
    }

    static MainScreenFragmentManager geMainScreenFragmentManager(){
        return new MainScreenFragmentManager();
    }

    Fragment getScoresFragment() {
        return new ScoresFragment();
    }

    Fragment getSettingsFragment() {
        return new SettingsFragment();
    }

    Fragment getCardViewFragment() {
        return new CardViewFragment();
    }

    Fragment getLoadingScreenFragment(int loadingTimeInSeconds) {
        LoadingScreenFragment loadingScreenFragment = new LoadingScreenFragment();
        loadingScreenFragment.setLoadingTimeInSeconds(loadingTimeInSeconds);
        return loadingScreenFragment;
    }


}
