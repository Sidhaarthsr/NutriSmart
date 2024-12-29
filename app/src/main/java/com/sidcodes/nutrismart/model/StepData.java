package com.sidcodes.nutrismart.model;

import androidx.annotation.NonNull;

import java.util.List;

public class StepData {
    private final String title;
    private final List<String> inputTitles;

    public StepData(String title, List<String> inputTitles) {
        this.title = title;
        this.inputTitles = inputTitles;
    }

    public String getTitle() {
        return title;
    }

    public List<String> getInputTitles() {
        return inputTitles;
    }

    @NonNull
    @Override
    public String toString() {
        return "StepData{" +
                "title='" + title + '\'' +
                ", inputTitles=" + inputTitles +
                '}';
    }
}