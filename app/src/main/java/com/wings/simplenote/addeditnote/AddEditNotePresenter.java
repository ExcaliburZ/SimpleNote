package com.wings.simplenote.addeditnote;

import android.content.Context;

import com.wings.simplenote.model.INoteModel;
import com.wings.simplenote.model.NoteModel;
import com.wings.simplenote.model.domain.Note;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

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
    public void saveNote(final Note note) {
        Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                boolean result = mNoteModel.addNote(note);
                subscriber.onNext(result);
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onNext(Boolean aBoolean) {
                        if (aBoolean) {
                            mAddEditNoteView.showSuccessRemind();
                        } else {
                            mAddEditNoteView.showFailureRemind();
                        }
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
    public void updateNote(final Note note) {
        Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                boolean result = mNoteModel.updateNote(note);
                subscriber.onNext(result);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onNext(Boolean result) {
                        if (result) {
                            mAddEditNoteView.showSuccessRemind();
                        } else {
                            mAddEditNoteView.showFailureRemind();
                        }
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
