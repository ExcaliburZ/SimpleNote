package com.wings.simplenote.model;

import com.wings.simplenote.domain.Note;

import java.util.List;

/**
 * Created by wing on 2016/4/12.
 */
public interface INoteModel {
    void addNote(Note note);

    void updateNote(Note note);

    void deleteNote(int id);

    List<Note> selectAll();

    Note selectNote(int id);
}
