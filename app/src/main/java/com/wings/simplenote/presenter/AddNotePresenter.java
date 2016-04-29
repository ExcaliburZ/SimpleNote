package com.wings.simplenote.presenter;

import android.content.Context;
import android.os.AsyncTask;

import com.wings.simplenote.model.INoteModel;
import com.wings.simplenote.model.NoteModel;
import com.wings.simplenote.model.domain.Note;
import com.wings.simplenote.view.IAddNoteView;

/**
 * Created by wing on 2016/4/29.
 */
public class AddNotePresenter implements IAddNotePresenter {
    private INoteModel mNoteModel;
    private IAddNoteView mAddNoteView;

    public AddNotePresenter(Context context, IAddNoteView iAddNoteView) {
        this.mNoteModel = new NoteModel(context);
        this.mAddNoteView = iAddNoteView;
    }

    @Override
    public void saveNote(Note note) {
        new AddNoteAsyncTask().execute(note);
    }

    class AddNoteAsyncTask extends AsyncTask<Note, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            mAddNoteView.showProcess();
        }

        @Override
        protected Boolean doInBackground(Note... params) {
            return mNoteModel.addNote(params[0]);
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if (aBoolean) {
                mAddNoteView.showSuccessRemind();
            } else {
                mAddNoteView.showFailureRemind();
            }
        }
    }

}
