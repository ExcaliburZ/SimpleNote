package com.wings.simplenote.addeditnote;

import android.content.Context;
import android.os.AsyncTask;

import com.wings.simplenote.model.INoteModel;
import com.wings.simplenote.model.NoteModel;
import com.wings.simplenote.model.domain.Note;

/**
 * Created by wing on 2016/4/29.
 */
public class AddEditNotePresenter implements AddEditNoteContract.Presenter {
    private INoteModel mNoteModel;
    private AddEditNoteContract.View mAddEditNoteView;

    public AddEditNotePresenter(Context context, AddEditNoteContract.View iNoteView) {
        this.mNoteModel = new NoteModel(context);
        this.mAddEditNoteView = iNoteView;
    }


    @Override
    public void saveNote(Note note) {
        new AddNoteAsyncTask().execute(note);
    }

    class AddNoteAsyncTask extends AsyncTask<Note, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            mAddEditNoteView.showProcess();
        }

        @Override
        protected Boolean doInBackground(Note... params) {
            return mNoteModel.addNote(params[0]);
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if (aBoolean) {
                mAddEditNoteView.showSuccessRemind();
            } else {
                mAddEditNoteView.showFailureRemind();
            }
        }
    }

    @Override
    public void updateNote(Note note) {
        new UpdateNoteAsyncTask().execute(note);
    }

    class UpdateNoteAsyncTask extends AsyncTask<Note, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            mAddEditNoteView.showProcess();
        }

        @Override
        protected Boolean doInBackground(Note... params) {
            return mNoteModel.updateNote(params[0]);
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if (aBoolean) {
                mAddEditNoteView.showSuccessRemind();
            } else {
                mAddEditNoteView.showFailureRemind();
            }
        }
    }

}
