package com.wings.simplenote;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;
import android.test.ApplicationTestCase;
import android.util.Log;

import com.wings.simplenote.model.domain.Note;
import com.wings.simplenote.model.NoteDbHelper;
import com.wings.simplenote.model.NoteModel;

import java.util.Date;
import java.util.List;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {


    private SQLiteDatabase mDatabase;
    private NoteModel model;
    private final static String TAG = "ApplicationTest";

    public ApplicationTest() {
        super(Application.class);
    }

    public void testModel() {
        NoteDbHelper mDbHelper = new NoteDbHelper(getContext());
        mDatabase = mDbHelper.getWritableDatabase();
    }

    public void testAdd() {
        model = new NoteModel(getContext());
//        model.deleteNote(8778);
        model.addNote(new Note("add", "test", false, new Date()));
        model.addNote(new Note("add", "test2", false, new Date()));
    }

    public void testSelectAll() {
        model = new NoteModel(getContext());
        List<Note> notes = model.selectAll();
        if (notes != null) {
            Log.i(TAG, "notes ::" + notes.size());
        }
    }

    public void testSelect() {
        model = new NoteModel(getContext());
        Note notes = model.selectNote(2);
        Log.i(TAG, "notes ::" + notes);
    }

    public void testDelete() {
        model = new NoteModel(getContext());
        model.deleteNote(8778);
        testAdd();
    }

    public void testUpdate() {
        model = new NoteModel(getContext());
        model.updateNote(new Note(2, "update", "zjq", true, new Date()));
    }
}