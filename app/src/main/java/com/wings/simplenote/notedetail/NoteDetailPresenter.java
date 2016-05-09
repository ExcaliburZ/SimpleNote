package com.wings.simplenote.notedetail;

import android.content.Context;

import com.wings.simplenote.model.INoteModel;
import com.wings.simplenote.model.NoteModel;
import com.wings.simplenote.model.domain.Note;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;

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
    public void getNote(final long id) {
        Observable.create(new Observable.OnSubscribe<Note>() {
            @Override
            public void call(Subscriber<? super Note> subscriber) {
                Note note = mNoteModel.selectNote(id);
                subscriber.onNext(note);
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        mNoteUpdateView.showProcess();
                    }
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Note>() {

                    @Override
                    public void onNext(Note note) {
                        mNoteUpdateView.updateViews(note);
                        mNoteUpdateView.dismissProcess();
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }
}
