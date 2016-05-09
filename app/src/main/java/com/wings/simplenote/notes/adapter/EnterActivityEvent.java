package com.wings.simplenote.notes.adapter;

import android.content.Intent;

/**
 * Created by wing on 2016/5/9.
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
