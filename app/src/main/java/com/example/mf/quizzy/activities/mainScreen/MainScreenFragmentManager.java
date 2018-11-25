package com.example.mf.quizzy.activities.mainScreen;

import android.support.v4.app.Fragment;

class MainScreenFragmentManager {

    public MainScreenFragmentManager() {
    }

    Fragment getScoresFragment() {
        return new ScoresFragment();
    }

    Fragment getSettingsFragment() {
        return new SettingsFragment();
    }

    Fragment getCardViewFragment(){
        return new CardViewFragment();
    }

    Fragment getLoadingScreenFragment(){
        return new LoadingScreenFragment();
    }



}
