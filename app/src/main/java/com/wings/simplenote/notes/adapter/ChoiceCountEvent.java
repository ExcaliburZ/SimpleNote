package com.wings.simplenote.notes.adapter;

/**
 * Event with the count the checked item.
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
