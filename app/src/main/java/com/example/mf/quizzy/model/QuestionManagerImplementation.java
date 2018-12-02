package com.example.mf.quizzy.model;

import android.text.Html;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.example.mf.quizzy.App;
import com.example.mf.quizzy.listeners.DataLoadingListener;
import com.example.mf.quizzy.exceptions.QuestionManagerDataLoadException;
import com.example.mf.quizzy.util.HttpUtil;

public class QuestionManagerImplementation implements QuestionManager {
    private static final int QUESTIONS_PER_SESSION = 5;
    private String mURL;
    private List<Question> mQuestions = new ArrayList<>();
    private List<Question> mOneSessionQuestions = new ArrayList<>();
    private List<DataLoadingListener> mListeners = new ArrayList<>();
    private Helper mHelper = new Helper();
    private String mCategoryName;
    private Question mCurrentQuestion;
    private Iterator mIterator = new Iterator();

    protected QuestionManagerImplementation(String categoryName, DataLoadingListener listener) {
        mCategoryName = categoryName;
        mURL = App.getInstance().getAppConfig().getUrlForCategory(categoryName);
        mListeners.add(listener);
        loadData();
    }

    @Override
    public String getQuestionText() {
        return mCurrentQuestion.getQuestionText();
    }

    @Override
    public void setNextQuestion() {
        mCurrentQuestion = mIterator.getNext();
    }

    @Override
    public boolean isItLastQuestion() {
        return !mIterator.hasNext();
    }

    @Override
    public boolean isQuestionBoolean() {
        return mCurrentQuestion.isQuestionBoolean();
    }

    @Override
    public ArrayList<String> getAllAnswers() {
        return mCurrentQuestion.getAllAnswers();
    }

    @Override
    public String getCorrectAnswer() {
        return mCurrentQuestion.getCorrectAnswerText();
    }

    @Override
    public String getCategoryName() {
        return mCategoryName;
    }

    protected void reloadQuestions() throws QuestionManagerDataLoadException {
        mOneSessionQuestions.clear();
        mIterator.reset();
        addOneSessionQuestions();
        setNextQuestion();
    }

    private void loadData() {
        HttpUtil.httpGetRequest(mURL, new HttpUtil.ResponseListener() {
            @Override
            public void onResponse(JSONObject response) {
                prepareQuestions(response);
            }

            @Override
            public void onError(String errorCause) {
                notifyOnFailure();
            }
        });
    }

    private void prepareQuestions(JSONObject jsonObject) {
        try {
            createAllQuestionObjects(jsonObject);
            shuffleQuestions();
            addOneSessionQuestions();
            setFirstQuestion();
            notifyOnSuccess();
        } catch (JSONException | QuestionManagerDataLoadException e) {
            notifyOnFailure();
        }
    }

    private void setFirstQuestion() {
        mCurrentQuestion = mOneSessionQuestions.get(0);
    }

    private void createAllQuestionObjects(JSONObject jsonObject) throws JSONException, QuestionManagerDataLoadException {
        JSONArray jsonArray = jsonObject.getJSONArray("results");
        if (jsonArray.length() == 0) throw new JSONException("No data received");

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject question = jsonArray.getJSONObject(i);
            createSingleQuestionObject(question);
        }
    }

    private void createSingleQuestionObject(JSONObject jsonObject) throws QuestionManagerDataLoadException {
        try {
            String questionText = mHelper.decodeString(jsonObject.getString("question"));
            JSONArray answersJArray = jsonObject.getJSONArray("incorrect_answers");

            answersJArray.put(jsonObject.getString("correct_answer"));
            ArrayList<String> answers = mHelper.getArrayListFromJsonArray(answersJArray);
            answers = mHelper.decodeArrayList(answers);

            Question question;
            if (answers.size() > 2) {
                question = new QuestionMultiple(questionText, answers);
            } else {
                question = new QuestionBoolean(questionText, answers);
            }

            mQuestions.add(question);

        } catch (JSONException | NullPointerException e) {
            throw new QuestionManagerDataLoadException();
        }
    }

    private void addOneSessionQuestions() throws QuestionManagerDataLoadException {
        // create questions for one session
        Question question;
        for (int i = 0; i < QuestionManagerImplementation.QUESTIONS_PER_SESSION; i++) {
            // get the last one
            try {
                question = mQuestions.get(mQuestions.size() - 1);
                mQuestions.remove(question);
                mOneSessionQuestions.add(question);
            } catch (ArrayIndexOutOfBoundsException e) {
                throw new QuestionManagerDataLoadException("Could not load data of " + mCategoryName);
            }
        }
    }

    private void notifyOnFailure() {
        for (DataLoadingListener listener : mListeners) {
            listener.onDataLoaded();
        }
    }

    private void notifyOnSuccess() {
        for (DataLoadingListener listener : mListeners) {
            listener.onDataLoaded();
        }
    }

    private void shuffleQuestions() {
        Collections.shuffle(mQuestions);
    }

    // Helper class
    private class Helper {
        private ArrayList<String> getArrayListFromJsonArray(JSONArray jsonArray) {
            ArrayList<String> arrayList = new ArrayList<>();
            try {
                for (int i = 0; i < jsonArray.length(); i++) {
                    arrayList.add(jsonArray.get(i).toString());
                }
                return arrayList;

            } catch (JSONException e) {
                return null;
            }
        }

        private ArrayList<String> decodeArrayList(ArrayList<String> arrayList) {
            for (String element : arrayList) {
                element = decodeString(element);
            }
            return arrayList;
        }

        private String decodeString(String text) {
            try {
                text = text.replaceAll("%(?![0-9a-fA-F]{2})", "%25").replaceAll("\\+", "%2B");
                text = Html.fromHtml(text).toString();
                return URLDecoder.decode(text, "UTF-8");
            } catch (UnsupportedEncodingException | IllegalArgumentException e) {
                return text;
            }
        }
    }

    private class Iterator {
        private int mCurrentIndex = 0;

        private boolean hasNext() {
            return mCurrentIndex < mOneSessionQuestions.size() - 1;
        }

        private Question getNext() {
            if (hasNext()) {
                mCurrentIndex++;
                return mOneSessionQuestions.get(mCurrentIndex);
            }
            return null;
        }

        private void reset() {
            mCurrentIndex = 0;
        }

        private int getCurrentIndex() {
            return mCurrentIndex;
        }
    }
}
