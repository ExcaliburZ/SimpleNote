package com.wings.simplenote.view.fragment;


import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;

import com.wings.simplenote.R;

/**
 * A Fragment is show me.
 */
public class AboutFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        return new AlertDialog.Builder(getActivity()).setView(R.layout.dialog_about).create();
    }
}
