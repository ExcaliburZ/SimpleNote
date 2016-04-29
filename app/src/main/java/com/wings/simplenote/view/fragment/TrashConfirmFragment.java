package com.wings.simplenote.view.fragment;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import com.wings.simplenote.listener.OnConfirmListener;


public class TrashConfirmFragment extends DialogFragment
        implements DialogInterface.OnClickListener {
    private static final String TAG = "TrashConfirmFragment";
    private OnConfirmListener mConfirmListener;

    public void setOnConfirmListener(OnConfirmListener onConfirmListener) {
        this.mConfirmListener = onConfirmListener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog
                .Builder(getActivity())
                .setTitle("Give up?")
                .setPositiveButton("yes", this)
                .setNegativeButton("no", null);
        return builder.create();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        if (mConfirmListener != null) {
            mConfirmListener.onConfirm();
        }
        this.dismiss();
    }
}
