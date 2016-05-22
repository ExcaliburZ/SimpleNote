package com.wings.simplenote.addeditnote;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.wings.simplenote.R;
import com.wings.simplenote.model.domain.Note;
import com.wings.simplenote.utils.RxBus;
import com.wings.simplenote.utils.SingletonToastUtils;
import com.wings.simplenote.view.dialogfragment.TrashConfirmFragment;
import com.wings.simplenote.view.dialogfragment.event.TrashEvent;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscription;
import rx.functions.Action1;


/**
 * add note Activity,use EditNoteFragment to edit data
 */
public class AddNoteActivity extends AppCompatActivity implements AddEditNoteContract.View {

    private static final String TAG = "AddNoteActivity";
    private static final String TRASH_CONFIRM_FRAGMENT = "TrashConfirmFragment";
    public static final int ADD_SUCCESS = 1;
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.fab_save)
    FloatingActionButton mSaveFab;
    private EditNoteFragment mNoteFragment;
    private Subscription mTrashConfirmed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        ButterKnife.bind(this);
        initData();
        initView();
    }

    private void initData() {
        mNoteFragment = (EditNoteFragment)
                getFragmentManager().findFragmentByTag(getString(R.string.add_note_fragment));

        mTrashConfirmed = RxBus.getDefault()
                .toObserverable(TrashEvent.class)
                .subscribe(new Action1<TrashEvent>() {
                    @Override
                    public void call(TrashEvent trashEvent) {
                        exit();
                    }
                });
    }


    private void initView() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    @OnClick({R.id.fab_save})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab_save:
                saveNote();
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                confirmTrash();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        confirmTrash();
    }

    private void saveNote() {
        if (mNoteFragment.confirmNoteComplete()) {
            Note note = mNoteFragment.getNote(true, 0);
            new AddEditNotePresenter(this, this).saveNote(note);
        }
    }

    private void confirmTrash() {
        if (noteIsEmpty()) {
            //note is not edit
            finish();
        } else {
            //note has been edit
            TrashConfirmFragment fragment = new TrashConfirmFragment();
            fragment.show(getFragmentManager(), TRASH_CONFIRM_FRAGMENT);
        }
    }

    private boolean noteIsEmpty() {
        return mNoteFragment.isNoteEmpty();
    }

    private void exit() {
        this.finish();
    }


    @Override
    public void showProcess() {

    }

    @Override
    public void dismissProcess() {

    }

    @Override
    public void showSuccessRemind() {
        setResult(ADD_SUCCESS);
        exit();
        SingletonToastUtils.showToast(this, getString(R.string.add_success));
    }

    @Override
    public void showFailureRemind() {
        SingletonToastUtils.showToast(this, getString(R.string.add_error));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!mTrashConfirmed.isUnsubscribed()) {
            mTrashConfirmed.unsubscribe();
        }
    }
}
