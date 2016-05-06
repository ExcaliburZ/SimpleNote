package com.wings.simplenote.notes;

import com.wings.simplenote.model.domain.Note;

import java.util.List;

/**
 * Created by wing on 2016/5/6.
 */
public interface NotesContract {
    interface Presenter {
        void showNotesList();

        void refreshNotes();

        void deleteNote(Long id);

    }

    interface View {

        void showNotes(List<Note> noteList);

        void refreshNotes(List<Note> noteList);

        void showLoading();

        void cancelLoading();
    }
}
