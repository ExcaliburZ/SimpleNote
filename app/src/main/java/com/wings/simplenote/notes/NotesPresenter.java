package com.wings.simplenote.notes;

import android.content.Context;
import android.os.SystemClock;

import com.wings.simplenote.model.INoteModel;
import com.wings.simplenote.model.NoteModel;
import com.wings.simplenote.model.domain.Note;

import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by wing on 2016/4/20.
 */
public class NotesPresenter implements NotesContract.Presenter {
    private NotesContract.View mNotesShowView;
    private INoteModel mNoteModel;
    public NotesPresenter(Context context, NotesContract.View notesShowView) {
        this.mNotesShowView = notesShowView;
        mNoteModel = new NoteModel(context);
    }

    private Observable.OnSubscribe<List> mGetNoteCall = new Observable.OnSubscribe<List>() {
        @Override
        public void call(Subscriber<? super List> subscriber) {
            List<Note> notes = getNotes();
            subscriber.onNext(notes);
            subscriber.onCompleted();
        }
    };

    @Override
    public void showNotesList() {
        Observable.create(mGetNoteCall)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List>() {

                    @Override
                    public void onNext(List notes) {
                        mNotesShowView.showNotes(notes);
                        mNotesShowView.cancelLoading();
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }

    @Override
    public void refreshNotes() {
        Observable.create(mGetNoteCall)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List>() {
                    @Override
                    public void onNext(List list) {
                        mNotesShowView.refreshNotes(list);
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }

    @Override
    public void deleteNote(final Long id) {
        Observable.create(new Observable.OnSubscribe<Void>() {
            @Override
            public void call(Subscriber<? super Void> subscriber) {
                mNoteModel.deleteNote(id);
            }
        }).subscribeOn(Schedulers.io());
    }

    private List<Note> getNotes() {
        List<Note> notes = mNoteModel.selectAll();
        SystemClock.sleep(1000);
        return notes;
    }
}
