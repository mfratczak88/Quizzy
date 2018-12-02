package com.example.mf.quizzy.model;

public class ModelFactory implements Factory{
    private Model mModel;
    public static Factory getFactory(){
        return new ModelFactory();
    }

    private ModelFactory(){
        mModel = QuestionBank.getInstance();
    }

    @Override
    public Model getModel() {
        return mModel;
    }
}
