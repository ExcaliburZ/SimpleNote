package com.wings.simplenote.notes.adapter;

/**
 * Created by wing on 2016/5/17.
 */
public class ChoiceCountEvent {
    private String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String count) {
        this.title = count;
    }

    public ChoiceCountEvent(String count) {
        this.title = count;
    }
}
