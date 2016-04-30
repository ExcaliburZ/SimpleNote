package com.wings.simplenote.presenter.impl;

import android.content.Context;
import android.os.AsyncTask;
import android.os.SystemClock;

import com.wings.simplenote.model.domain.Note;
import com.wings.simplenote.model.INoteModel;
import com.wings.simplenote.model.NoteModel;
import com.wings.simplenote.presenter.INotesShowPresenter;
import com.wings.simplenote.view.INotesShowView;

import java.util.List;

/**
 * Created by wing on 2016/4/20.
 */
public class NotesListPresenter implements INotesShowPresenter {
    private INotesShowView notesShowView;
    private INoteModel noteModel;

    public NotesListPresenter(Context context,INotesShowView notesShowView) {
        this.notesShowView = notesShowView;
        noteModel = new NoteModel(context);
    }

    @Override
    public void showNotesList() {
        new NotesShowAsyncTask().execute();
    }

    @Override
    public void refreshNotes() {
        new NotesRefreshAsyncTask().execute();
    }

    class NotesShowAsyncTask extends AsyncTask<Void, Void, List<Note>> {

        @Override
        protected List<Note> doInBackground(Void... params) {
            return getNotes();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            notesShowView.showLoading();
        }

        @Override
        protected void onPostExecute(List<Note> notes) {
            super.onPostExecute(notes);
            notesShowView.showNotes(notes);
            notesShowView.cancelLoading();
        }
    }

    class NotesRefreshAsyncTask extends AsyncTask<Void, Void, List<Note>> {

        @Override
        protected List<Note> doInBackground(Void... params) {
            return getNotes();
        }

        @Override
        protected void onPostExecute(List<Note> notes) {
            super.onPostExecute(notes);
            notesShowView.refreshNotes(notes);
            notesShowView.cancelLoading();
        }
    }

    private List<Note> getNotes() {
        List<Note> notes = noteModel.selectAll();
        SystemClock.sleep(1000);
        return notes;
    }
}
