package com.example.mf.quizzy;

import android.content.Context;
import android.graphics.Color;
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

import com.example.mf.quizzy.Listeners.onAnsweredQuestionListener;
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

public abstract class AnswerFragment extends Fragment {
    private final static int SLEEP_TIME = 300;
    protected List<Button> mButtons = new ArrayList<>();
    private onAnsweredQuestionListener mOnAnsweredQuestionListener;
    private Util mUtil = new AnswerFragment.Util();
    private Model mModel = ModelFactory.getFactory().getModel();

    protected abstract void extractButtonsFromView(View view);

    protected abstract void setEventHandlersAndTextsForButtons();

    protected abstract int getLayoutNumber();

    protected abstract int getWrapperLayoutId();


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            setOnAnsweredQuestionListener((onAnsweredQuestionListener) context);
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement onAnsweredQuestionListener");
        }
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
        return view;
    }


    protected ArrayList<String> getAllAnswers() {
        return mModel.getCurrentQuestionManager().getAllAnswers();
    }

    protected void checkAnswerAndNotifyListener(int buttonNumber) {
        //@todo: create new class here obviously
        Button correctAnswerButton = getCorrectAnswerButton();
        Button chosenAnswerButton = mButtons.get(buttonNumber);

        boolean answerWasCorrect = chosenAnswerButton.getText().equals(correctAnswerButton.getText());
        Command listenerCallBackCommand = createListenerCallBackObject(chosenAnswerButton.getText().toString(), answerWasCorrect);
        HashMap<Button, Integer> greenButton = mUtil.getButtonColorHashMap(correctAnswerButton, Color.GREEN);

        if (answerWasCorrect) {
            getShowAnswerTask(listenerCallBackCommand)
                    .execute(greenButton);
        } else {
            getShowAnswerTask(listenerCallBackCommand)
                    .execute(greenButton, mUtil.getButtonColorHashMap(chosenAnswerButton, Color.RED));
        }
    }

    private Command createListenerCallBackObject(String chosenAnswerButton, boolean wasItCorrect) {
        Object[] args = {chosenAnswerButton, wasItCorrect};
        Method[] methods = getOnAnsweredQuestionListener().getClass().getMethods();
        Method methodName;
        for (Method method : methods) {
            if (method.getName().contains("onAnswerGiven")) { //todo : change this hideous hardcode
                methodName = method;
                return new ListenerNotifierCommand(getOnAnsweredQuestionListener(), methodName, args);
            }
        }
        return null;
    }

    private AsyncTask getShowAnswerTask(Command command) {
        return new ShowAnswerAsyncTask(command);
    }

    protected Button getCorrectAnswerButton() {
        Iterator iterator = getButtonsIterator();
        String correctAnswerText = mModel.getCurrentQuestionManager().getCorrectAnswer();
        Button button;
        while (iterator.hasNext()) {
            button = (Button) iterator.next();
            if (button.getText().toString().equals(correctAnswerText)) {
                return button;
            }
        }
        return null;
    }

    protected Iterator getButtonsIterator() {
        return mButtons.iterator();
    }

    private onAnsweredQuestionListener getOnAnsweredQuestionListener() {
        return mOnAnsweredQuestionListener;
    }

    private void setOnAnsweredQuestionListener(onAnsweredQuestionListener onAnsweredQuestionListener) {
        mOnAnsweredQuestionListener = onAnsweredQuestionListener;
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

        private void colorSingleButton(final Button button, final int color) {
            if (isAdded()) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        button.setBackgroundColor(color);
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
        private onAnsweredQuestionListener mReceiver;
        private Method mMethod;
        private Object mArgs[];

        private ListenerNotifierCommand(onAnsweredQuestionListener receiver, Method method, Object[] args) {
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
        private HashMap<Button, Integer> getButtonColorHashMap(Button button, int color) {
            HashMap<Button, Integer> hashMap = new HashMap<>();
            hashMap.put(button, color);
            return hashMap;
        }
    }
}
