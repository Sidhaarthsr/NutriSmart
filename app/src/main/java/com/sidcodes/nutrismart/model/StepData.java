package com.sidcodes.nutrismart.model;

import java.util.List;

public class StepData {

    private String title;
    private List<String> inputTitles;
    private int backgroundResource;

    // Constructor, getters, and setters
    public StepData(String title, List<String> inputTitles, int backgroundResource) {
        this.title = title;
        this.inputTitles = inputTitles;
        this.backgroundResource = backgroundResource;
    }

    public String getTitle() {
        return title;
    }

    public List<String> getInputTitles() {
        return inputTitles;
    }

    public int getBackgroundResource() {
        return backgroundResource;
    }
}
