package com.example.mf.quizzy.exceptions;

public class QuestionManagerDataLoadException extends Exception {

    public QuestionManagerDataLoadException(){

    }

    public QuestionManagerDataLoadException(String message) {
        super(message);
    }

    public QuestionManagerDataLoadException(String message, Throwable cause) {
        super(message, cause);
    }

    public QuestionManagerDataLoadException(Throwable cause) {
        super(cause);
    }

}
