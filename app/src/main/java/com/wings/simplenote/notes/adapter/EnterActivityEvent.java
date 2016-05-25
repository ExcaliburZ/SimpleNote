package com.wings.simplenote.notes.adapter;

import android.content.Intent;

/**
 * Event to Enter note detail Activity.
 */
public class EnterActivityEvent {
    private Intent mIntent;

    public EnterActivityEvent(Intent intent) {
        mIntent = intent;
    }

    public Intent getIntent() {

        return mIntent;
    }

    public void setIntent(Intent intent) {
        mIntent = intent;
    }
}
