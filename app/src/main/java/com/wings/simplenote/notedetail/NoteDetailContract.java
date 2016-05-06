package com.wings.simplenote.notedetail;

import com.wings.simplenote.model.domain.Note;

/**
 * Created by wing on 2016/5/6.
 */
public interface NoteDetailContract {
    interface Presenter {
        void getNote(long id);
    }

    interface View {
        void showProcess();

        void dismissProcess();

        void updateViews(Note noteItem);
    }
}
