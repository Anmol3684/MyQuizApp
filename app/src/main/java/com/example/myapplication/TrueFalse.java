package com.example.myapplication;

public class TrueFalse   {
    private int mQuestionID;
    private boolean mAnswer;
    TrueFalse(int QuestionResourceID, boolean TrueOrFalse){
        mQuestionID = QuestionResourceID;
        mAnswer = TrueOrFalse;

    }

    public int getmQuestionID() {
        return mQuestionID;
    }

    public void setmQuestionID(int mQuestionID) {
        this.mQuestionID = mQuestionID;
    }

    public boolean ismAnswer() {
        return mAnswer;
    }

    public void setmAnswer(boolean mAnswer) {
        this.mAnswer = mAnswer;
    }
}
