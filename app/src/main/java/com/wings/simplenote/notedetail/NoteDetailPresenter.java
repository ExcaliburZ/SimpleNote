package com.wings.simplenote.notedetail;

import android.content.Context;
import android.os.AsyncTask;

import com.wings.simplenote.model.INoteModel;
import com.wings.simplenote.model.NoteModel;
import com.wings.simplenote.model.domain.Note;

/**
 * Created by wing on 2016/4/29.
 */
public class NoteDetailPresenter implements NoteDetailContract.Presenter {
    private INoteModel mNoteModel;
    private NoteDetailContract.View mNoteUpdateView;

    public NoteDetailPresenter(Context context, NoteDetailContract.View iNoteView) {
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
