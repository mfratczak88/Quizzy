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
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import com.example.mf.quizzy.R;
import com.example.mf.quizzy.config.AppConfig;
import com.example.mf.quizzy.usersManagement.UsersManager;

public class SettingsFragment extends Fragment {
    private View mView;
    private AppConfig.UserSettings mUserSettings;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_settings, container, false);
        mUserSettings = UsersManager.getInstance(getContext()).getUserSettings();
        setUserSettingsOnScreen();
        return mView;
    }

    private void setUserSettingsOnScreen() {
        setLevel();
        setAswerTime();
        setQuestionsPerSession();
        setCheckBoxSaveProgress();
    }

    private void setCheckBoxSaveProgress() {
        CheckBox checkBoxSaveProgress = mView.findViewById(R.id.checkBox_save_progress);
        checkBoxSaveProgress.setChecked(mUserSettings.doSaveProgress());
    }

    private void setLevel() {
        setSpinnerSelection((Spinner) mView.findViewById(R.id.spinner_level), mUserSettings.getLevel());
    }

    private void setQuestionsPerSession() {
        setSpinnerSelection(
                (Spinner) mView.findViewById(R.id.spinner_questions_per_session),
                String.valueOf(mUserSettings.getQuestionsPerSession()));
    }

    private void setAswerTime() {
        setSpinnerSelection(
                (Spinner) mView.findViewById(R.id.spinner_answer_time),
                String.valueOf(mUserSettings.getAnswerTimeInSeconds()));
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
