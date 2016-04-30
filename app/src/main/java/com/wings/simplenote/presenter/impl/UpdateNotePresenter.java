package com.wings.simplenote.presenter.impl;

import android.content.Context;
import android.os.AsyncTask;

import com.wings.simplenote.model.INoteModel;
import com.wings.simplenote.model.NoteModel;
import com.wings.simplenote.model.domain.Note;
import com.wings.simplenote.presenter.IUpdateNotePresenter;
import com.wings.simplenote.view.INoteView;

/**
 * Created by wing on 2016/4/29.
 */
public class UpdateNotePresenter implements IUpdateNotePresenter {
    private INoteModel mNoteModel;
    private INoteView mAddNoteView;

    public UpdateNotePresenter(Context context, INoteView iNoteView) {
        this.mNoteModel = new NoteModel(context);
        this.mAddNoteView = iNoteView;
    }


    @Override
    public void updateNote(Note note) {
        new UpdateNoteAsyncTask().execute(note);
    }

    class UpdateNoteAsyncTask extends AsyncTask<Note, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            mAddNoteView.showProcess();
        }

        @Override
        protected Boolean doInBackground(Note... params) {
            return mNoteModel.updateNote(params[0]);
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
