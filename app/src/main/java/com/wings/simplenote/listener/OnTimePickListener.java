package com.wings.simplenote.listener;

/**
 * Created by wing on 2016/4/28.
 * Time Pick call-back hook.
 */
public interface OnTimePickListener {
    void onTimePick(int hourOfDay, int minute);

    void onTimePickCancel();
}
