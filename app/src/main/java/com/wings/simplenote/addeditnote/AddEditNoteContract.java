package com.wings.simplenote.addeditnote;

import com.wings.simplenote.model.domain.Note;

/**
 * Created by wing on 2016/5/6.
 */
public interface AddEditNoteContract {
    interface Presenter {
        void saveNote(Note note);

        void updateNote(Note note);
    }

    interface View {
        void showProcess();

        void dismissProcess();

        void showSuccessRemind();

        void showFailureRemind();
    }

}
