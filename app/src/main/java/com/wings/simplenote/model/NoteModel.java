package com.wings.simplenote.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;
import android.util.Log;

import com.wings.simplenote.BuildConfig;
import com.wings.simplenote.model.domain.Note;
import com.wings.simplenote.utils.DateFormatUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.wings.simplenote.model.NoteContract.FeedReaderContract.NoteEntry;

/**
 * Created by wing on 2016/4/13.
 */
public class NoteModel implements INoteModel {

    private static final String TAG = "NoteModel";
    private SQLiteOpenHelper mDbHelper;
    public static final String[] PROJECTION = new String[]{
            NoteEntry._ID,
            NoteEntry.COLUMN_NAME_TITLE,
            NoteEntry.COLUMN_NAME_CONTENT,
            NoteEntry.COLUMN_NAME_ALARM,
            NoteEntry.COLUMN_NAME_ALARM_TIME,
    };

    public NoteModel(Context context) {
        this.mDbHelper = new NoteDbHelper(context);
    }

    @Override
    public boolean addNote(Note note) {
        // Gets the data repository in write mode
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        if (note.id != 0) {
            values.put(NoteEntry._ID, note.id);
        }
        values.put(NoteEntry.COLUMN_NAME_TITLE, note.title);
        values.put(NoteEntry.COLUMN_NAME_CONTENT, note.content);
        values.put(NoteEntry.COLUMN_NAME_ALARM, note.hasAlarm);
        String dateTime = DateFormatUtils.formatDateTime(note.date);

        values.put(NoteEntry.COLUMN_NAME_ALARM_TIME, dateTime);

        // Insert the new row, returning the primary key value of the new row
        long newRowId;
        newRowId = db.insert(
                NoteEntry.TABLE_NAME,
                NoteEntry.COLUMN_NAME_NULLABLE,
                values);
        mDbHelper.close();
        return newRowId != -1;
    }

    @Override
    public void updateNote(Note note) {
        // Gets the data repository in write mode
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(NoteEntry.COLUMN_NAME_TITLE, note.title);
        values.put(NoteEntry.COLUMN_NAME_CONTENT, note.content);
        values.put(NoteEntry.COLUMN_NAME_ALARM, note.hasAlarm);
        String dateTime = DateFormatUtils.formatDateTime(note.date);

        values.put(NoteEntry.COLUMN_NAME_ALARM_TIME, dateTime);

        db.update(
                NoteEntry.TABLE_NAME,
                values, NoteEntry._ID + " = ?",
                new String[]{String.valueOf(note.id)});
    }

    @Override
    public void deleteNote(int id) {
        // Gets the data repository in write mode
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        // Define 'where' part of query.
        String selection = NoteEntry._ID + " = ?";
        // Specify arguments in placeholder order.
        String[] selectionArgs = {String.valueOf(id)};
        // Issue SQL statement.
        int result = db.delete(NoteEntry.TABLE_NAME, selection, selectionArgs);
        Log.i(TAG, "result ::" + result);
        //代替断言
        // assert result!=0:"delete 0";
//        if (BuildConfig.DEBUG) {
//            Assert.assertTrue("delete 0", result == 0);
//        }
    }

    @Override
    public List<Note> selectAll() {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        Cursor cursor = db.query(
                NoteEntry.TABLE_NAME,                   // The table to query
                PROJECTION,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );
        List<Note> notes = new ArrayList<>();
        if (cursor.getCount() == 0) {
            return notes;
        }
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            Note item = getNoteAndMoveNext(cursor);
            notes.add(item);
        }
        cursor.close();
        return notes;
    }

    @Override
    public Note selectNote(int id) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        Cursor cursor = db.query(
                NoteEntry.TABLE_NAME,                   // The table to query
                PROJECTION,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );
        boolean hasItem = cursor.moveToFirst();
        if (BuildConfig.DEBUG && !hasItem) {
            throw new AssertionError("id not exist");
        }
        Note note = getNoteAndMoveNext(cursor);

        cursor.close();
        return note;
    }

    @NonNull
    private Note getNoteAndMoveNext(Cursor cursor) {
        long itemId = cursor.getLong(
                cursor.getColumnIndexOrThrow(NoteEntry._ID));
        String title = cursor.getString(
                cursor.getColumnIndexOrThrow(NoteEntry.COLUMN_NAME_TITLE));
        String content = cursor.getString(
                cursor.getColumnIndexOrThrow(NoteEntry.COLUMN_NAME_CONTENT));
        long alarmL = cursor.getLong(
                cursor.getColumnIndexOrThrow(NoteEntry.COLUMN_NAME_ALARM));
        boolean alarm = alarmL != 0;
        String alarmTimeStr = cursor.getString(
                cursor.getColumnIndexOrThrow(NoteEntry.COLUMN_NAME_ALARM_TIME));
        Date date = DateFormatUtils.parse(alarmTimeStr);
        Note item = new Note(itemId, title, content, alarm, date);
        cursor.moveToNext();
        return item;
    }
}
