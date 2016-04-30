package com.wings.simplenote.view.fragment;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wings.simplenote.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class EditNoteActivityFragment extends Fragment {

    public EditNoteActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_edit_note, container, false);
    }


}
