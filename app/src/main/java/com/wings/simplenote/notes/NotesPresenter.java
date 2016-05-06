package com.wings.simplenote.notes;

import android.content.Context;
import android.os.AsyncTask;
import android.os.SystemClock;

import com.wings.simplenote.model.INoteModel;
import com.wings.simplenote.model.NoteModel;
import com.wings.simplenote.model.domain.Note;

import java.util.List;

/**
 * Created by wing on 2016/4/20.
 */
public class NotesPresenter implements NotesContract.Presenter {
    private NotesContract.View notesShowView;
    private INoteModel mNoteModel;

    public NotesPresenter(Context context, NotesContract.View notesShowView) {
        this.notesShowView = notesShowView;
        mNoteModel = new NoteModel(context);
    }

    @Override
    public void showNotesList() {
        new NotesShowAsyncTask().execute();
    }

    @Override
    public void refreshNotes() {
        new NotesRefreshAsyncTask().execute();
    }

    @Override
    public void deleteNote(Long id) {
        new DeleteNoteAsyncTask().execute(id);
    }

    class DeleteNoteAsyncTask extends AsyncTask<Long, Void, Void> {

        @Override
        protected Void doInBackground(Long... params) {
            mNoteModel.deleteNote(params[0]);
            return null;
        }
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
        List<Note> notes = mNoteModel.selectAll();
        SystemClock.sleep(1000);
        return notes;
    }
}
