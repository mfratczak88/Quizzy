package com.example.mf.quizzy;

import android.content.Context;

import com.example.mf.quizzy.Listeners.AnswerShownListener;
import com.example.mf.quizzy.Listeners.TimeOutListener;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.mf.quizzy.Model.Model;
import com.example.mf.quizzy.Model.ModelFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static java.lang.Thread.sleep;

public abstract class AnswerFragment extends Fragment implements TimeOutListener {
    private final static int SLEEP_TIME = 3000;
    protected List<Button> mButtons = new ArrayList<>();
    private AnswerShownListener mAnswerShownListener;
    private Util mUtil = new AnswerFragment.Util();
    private Model mModel = ModelFactory.getFactory().getModel();
    private Button mCorrectAnswerButton;

    protected abstract void extractButtonsFromView(View view);

    protected abstract void setEventHandlersAndTextsForButtons();

    protected abstract int getLayoutNumber();

    protected abstract int getWrapperLayoutId();


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            setAnswerShownListener((AnswerShownListener) context);
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement AnswerShownListener");
        }
    }

    public void setAnswerShownListener(AnswerShownListener answerShownListener) {
        mAnswerShownListener = answerShownListener;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutNumber(), container, false);
        extractButtonsFromView(view);
        setEventHandlersAndTextsForButtons();
        setCorrectAnswerButton();
        return view;
    }

    private void setCorrectAnswerButton() {
        Iterator iterator = getButtonsIterator();
        String correctAnswerText = mModel.getCurrentQuestionManager().getCorrectAnswer();
        Button button;
        while (iterator.hasNext()) {
            button = (Button) iterator.next();
            if (button.getText().toString().equals(correctAnswerText)) {
                mCorrectAnswerButton = button;
            }
        }
    }

    protected ArrayList<String> getAllAnswers() {
        return mModel.getCurrentQuestionManager().getAllAnswers();
    }

    protected void checkAnswerAndNotifyListener(int buttonNumber) {
        //@todo: create new class here obviously
        mAnswerShownListener.stopClock();
        Button chosenAnswerButton = mButtons.get(buttonNumber);

        boolean answerWasCorrect = chosenAnswerButton.getText().equals(mCorrectAnswerButton.getText());
        Command listenerCallBackCommand = createOnAnswerGivenCallBackCommand(chosenAnswerButton.getText().toString(), answerWasCorrect);

        if (answerWasCorrect) {
            getShowAnswerTaskForCommand(listenerCallBackCommand)
                    .execute(getGreenButtonHashMap());
        } else {
            getShowAnswerTaskForCommand(listenerCallBackCommand)
                    .execute(getGreenButtonHashMap(), getRedButtonHashMap(chosenAnswerButton));
        }
    }

    private HashMap<Button, Integer> getGreenButtonHashMap() {
        return mUtil.getButtonDrawableHashMap(mCorrectAnswerButton, R.drawable.rounded_button_true);
    }

    private HashMap<Button, Integer> getRedButtonHashMap(Button incorrectAnswerButton) {
        return mUtil.getButtonDrawableHashMap(incorrectAnswerButton, R.drawable.rounded_button_false);
    }

    private Command createOnAnswerGivenCallBackCommand(String chosenAnswerButton, boolean wasItCorrect) {
        Object[] args = {chosenAnswerButton, wasItCorrect};
        Method[] methods = mAnswerShownListener.getClass().getMethods();
        Method methodName;
        for (Method method : methods) {
            if (method.getName().contains("onAnswerGiven")) { //todo : change this hideous hardcode
                methodName = method;
                return new ListenerNotifierCommand(mAnswerShownListener, methodName, args);
            }
        }
        return null;
    }

    private AsyncTask getShowAnswerTaskForCommand(Command command) {
        return new ShowAnswerAsyncTask(command);
    }

    protected Iterator getButtonsIterator() {
        return mButtons.iterator();
    }

    @Override
    public void onTimeOut() {
        new ShowAnswerAsyncTask(createOnAnswerShownCallBackCommand()).execute(getGreenButtonHashMap());
    }

    private Command createOnAnswerShownCallBackCommand() {
        Method[] methods = mAnswerShownListener.getClass().getMethods();
        Method methodName;
        for (Method method : methods) {
            if (method.getName().contains("onAnswerShown")) { //todo : change this hideous hardcode
                methodName = method;
                return new ListenerNotifierCommand(mAnswerShownListener, methodName, null);
            }
        }
        return null;
    }

    private class ShowAnswerAsyncTask extends AsyncTask<Object, Void, Void> {
        Command mCommand;

        private ShowAnswerAsyncTask(Command command) {
            mCommand = command;
        }

        @Override
        protected Void doInBackground(Object... buttonColorHashMaps) {
            if (buttonColorHashMaps.length == 0) {
                return null;
            }
            colorAllButtonsInArray(buttonColorHashMaps);
            _sleep();
            return null;
        }

        private void colorAllButtonsInArray(Object[] buttonColorHashMaps) {
            Map<Button, Integer> buttonColorMap;
            try {
                for (Object element : buttonColorHashMaps) {
                    buttonColorMap = (Map) element;
                    for (Map.Entry<Button, Integer> entry : buttonColorMap.entrySet()) {
                        colorSingleButton(entry.getKey(), entry.getValue());
                    }
                }
            } catch (Exception e) {
                Log.d(getClass().toString(), e.toString());
            }
        }

        private void colorSingleButton(final Button button, final int background) {
            if (isAdded()) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        button.setBackground(getResources().getDrawable(background));
                    }
                });
            }
        }

        private void _sleep() {
            try {
                sleep(SLEEP_TIME);
            } catch (Exception e) {
                Log.d(getClass().toString(), e.toString());
            }
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if (mCommand != null) {
                mCommand.executeCommand();
            }
        }

    }

    private interface Command {
        void executeCommand();
    }

    private class ListenerNotifierCommand implements Command {
        private AnswerShownListener mReceiver;
        private Method mMethod;
        private Object mArgs[];

        private ListenerNotifierCommand(AnswerShownListener receiver, Method method, Object[] args) {
            this.mReceiver = receiver;
            mMethod = method;
            this.mArgs = args;
        }

        @Override
        public void executeCommand() {
            try {
                mMethod.invoke(mReceiver, mArgs);
            } catch (IllegalAccessException | InvocationTargetException e) {
                Log.d(this.getClass().toString(), e.toString());
            }
        }
    }

    private class Util {
        private HashMap<Button, Integer> getButtonDrawableHashMap(Button button, int background) {
            HashMap<Button, Integer> hashMap = new HashMap<>();
            hashMap.put(button, background);
            return hashMap;
        }
    }
}
