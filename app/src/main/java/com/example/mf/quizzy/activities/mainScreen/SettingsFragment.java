package com.example.mf.quizzy.activities.mainScreen;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import com.example.mf.quizzy.R;
import com.example.mf.quizzy.config.AppConfig;
import com.example.mf.quizzy.roomPersistence.Settings;
import com.example.mf.quizzy.usersManagement.UsersManager;

public class SettingsFragment extends Fragment {
    private View mView;
    private Settings mUserSettings;
    private Spinner mLevelSpinner, mQuestionsPerSessionSpinner, mAnswerTimeSpinner;
    private CheckBox mSaveProgressCheckBox;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_settings, container, false);
        setUserSettings();
        setUiElements();
        setUserSettingsOnScreen();
        return mView;
    }

    private void setUserSettings() {
        try {
            mUserSettings = UsersManager.getInstance(getContext()).getUserSettings();

        } catch (Exception e) {
            Log.d("SettingsFragment", "could not retrieve user settings");
        }
    }

    private void setUiElements() {
        mSaveProgressCheckBox = mView.findViewById(R.id.checkBox_save_progress);
        mAnswerTimeSpinner = mView.findViewById(R.id.spinner_answer_time);
        mQuestionsPerSessionSpinner = mView.findViewById(R.id.spinner_questions_per_session);
        mLevelSpinner = mView.findViewById(R.id.spinner_level);
    }

    private void setUserSettingsOnScreen() {
        setLevel();
        setAnswerTime();
        setQuestionsPerSession();
        setCheckBoxSaveProgress();
    }

    private void setCheckBoxSaveProgress() {
        mSaveProgressCheckBox.setChecked(mUserSettings.isSaveProgress());
    }

    private void setLevel() {
        setSpinnerSelection(mLevelSpinner, mUserSettings.getLevel());
    }

    private void setQuestionsPerSession() {
        setSpinnerSelection(mQuestionsPerSessionSpinner, String.valueOf(mUserSettings.getQuestionsPerSession()));
    }

    private void setAnswerTime() {
        setSpinnerSelection(mAnswerTimeSpinner, String.valueOf(mUserSettings.getAnswerTimeInSeconds()));
    }

    private void setSpinnerSelection(Spinner spinner, String userSetting) {
        try {
            SpinnerAdapter spinnerAdapter = spinner.getAdapter();
            for (int i = 0; i < spinnerAdapter.getCount(); i++) {
                if (((String) spinnerAdapter.getItem(i)).equalsIgnoreCase(userSetting)) {
                    spinner.setSelection(i);
                    return;
                }
            }
        } catch (Exception e) {
            Log.d("SettingsFragment", "Exception on setting spinner selection for " + userSetting);
        }
    }

}
