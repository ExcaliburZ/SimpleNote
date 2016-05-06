package com.wings.simplenote.notes;

import com.wings.simplenote.model.domain.Note;

import java.util.List;

/**
 * Created by wing on 2016/4/20.
 * the view interface for IListNotesPresenter
 */
public interface INotesShowView {

    void showNotes(List<Note> noteList);

    void refreshNotes(List<Note> noteList);

    void showLoading();

    void cancelLoading();
}
