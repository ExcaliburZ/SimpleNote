package com.wings.simplenote.presenter.impl;

import android.content.Context;
import android.os.AsyncTask;

import com.wings.simplenote.model.INoteModel;
import com.wings.simplenote.model.NoteModel;
import com.wings.simplenote.model.domain.Note;
import com.wings.simplenote.presenter.INoteDetailPresenter;
import com.wings.simplenote.view.INoteUpdateView;

/**
 * Created by wing on 2016/4/29.
 */
public class NoteDetailPresenter implements INoteDetailPresenter {
    private INoteModel mNoteModel;
    private INoteUpdateView mNoteUpdateView;

    public NoteDetailPresenter(Context context, INoteUpdateView iNoteView) {
        this.mNoteModel = new NoteModel(context);
        this.mNoteUpdateView = iNoteView;
    }


    @Override
    public void getNote(long id) {
        new SelectNoteAsyncTask().execute(id);
    }

    class SelectNoteAsyncTask extends AsyncTask<Long, Void, Note> {

        @Override
        protected void onPreExecute() {
            mNoteUpdateView.showProcess();
        }

        @Override
        protected Note doInBackground(Long... params) {
            return mNoteModel.selectNote(params[0]);
        }

        @Override
        protected void onPostExecute(Note note) {
            mNoteUpdateView.updateViews(note);
            mNoteUpdateView.dismissProcess();
        }
    }

}
