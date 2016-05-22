package com.wings.simplenote.listener;

/**
 * Time Pick call-back callback.
 */
public interface OnTimePickListener {
    void onTimePick(int hourOfDay, int minute);

    void onTimePickCancel();
}
