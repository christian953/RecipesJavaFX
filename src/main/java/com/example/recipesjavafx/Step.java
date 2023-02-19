package com.example.recipesjavafx;

import java.util.Timer;

public class Step {
    private final int stepNumber;
    private String text;
    private String time;

    public Step(int stepNumber, String text, String time) {
        this.stepNumber = stepNumber;
        this.text = text;
        this.time = time;
    }

    public int stepNumber() {
        return stepNumber;
    }

    public String text() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String time() {
        return time;
    }
    public void setTime(String time){
        this.time = time;
    }
}
