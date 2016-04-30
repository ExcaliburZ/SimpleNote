package com.wings.simplenote.model;

import com.wings.simplenote.model.domain.Note;

import java.util.List;

/**
 * Created by wing on 2016/4/12.
 */
public interface INoteModel {
    boolean addNote(Note note);

    boolean updateNote(Note note);

    void deleteNote(int id);

    List<Note> selectAll();

    Note selectNote(long id);
}
