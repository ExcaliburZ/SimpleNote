package com.wings.simplenote.listener;

/**
 * Date Pick call-back callback.
 */
public interface OnDatePickListener {
    void onDatePick(int year, int monthOfYear, int dayOfMonth);

    void onDatePickCancel();
}
