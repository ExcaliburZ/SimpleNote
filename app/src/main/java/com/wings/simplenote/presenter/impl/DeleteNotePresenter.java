package com.wings.simplenote.presenter.impl;

import android.content.Context;
import android.os.AsyncTask;

import com.wings.simplenote.model.INoteModel;
import com.wings.simplenote.model.NoteModel;
import com.wings.simplenote.presenter.IDeleteNotePresenter;

/**
 * Created by wing on 2016/4/29.
 */
public class DeleteNotePresenter implements IDeleteNotePresenter {
    private INoteModel mNoteModel;

    public DeleteNotePresenter(Context context) {
        this.mNoteModel = new NoteModel(context);
    }

    @Override
    public void deleteNote(Long id) {
        new AddNoteAsyncTask().execute(id);
    }

    class AddNoteAsyncTask extends AsyncTask<Long, Void, Void> {

        @Override
        protected Void doInBackground(Long... params) {
            mNoteModel.deleteNote(params[0]);
            return null;
        }
    }

}
