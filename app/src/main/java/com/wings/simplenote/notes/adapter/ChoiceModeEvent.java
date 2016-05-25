package com.wings.simplenote.notes.adapter;

import android.view.ActionMode;

/**
 * Event to change Choice Mode.
 */
public class ChoiceModeEvent {
    private boolean isChoiceMode;
    private ActionMode.Callback mCallback;

    public ChoiceModeEvent(boolean isChoiceMode, ActionMode.Callback callback) {
        this.isChoiceMode = isChoiceMode;
        mCallback = callback;
    }

    public boolean isChoiceMode() {
        return isChoiceMode;
    }

    public void setChoiceMode(boolean choiceMode) {
        isChoiceMode = choiceMode;
    }

    public ActionMode.Callback getCallback() {
        return mCallback;
    }

    public void setCallback(ActionMode.Callback callback) {
        mCallback = callback;
    }
}
