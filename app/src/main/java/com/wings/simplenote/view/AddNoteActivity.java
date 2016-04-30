package com.wings.simplenote.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;

import com.wings.simplenote.R;
import com.wings.simplenote.listener.OnConfirmListener;
import com.wings.simplenote.model.domain.Note;
import com.wings.simplenote.presenter.IAddNotePresenter;
import com.wings.simplenote.presenter.impl.AddNotePresenter;
import com.wings.simplenote.utils.SingletonToastUtils;
import com.wings.simplenote.view.fragment.EditNoteActivityFragment;
import com.wings.simplenote.view.fragment.TrashConfirmFragment;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class AddNoteActivity extends AppCompatActivity implements INoteView {

    private static final String TAG = "AddNoteActivity";
    private static final String TRASH_CONFIRM_FRAGMENT = "TrashConfirmFragment";
    public static final int ADD_SUCCESS = 1;
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.ib_save)
    ImageButton mIbSave;
    @Bind(R.id.ib_trash)
    ImageButton mIbTrash;
    private EditNoteActivityFragment mNoteFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        ButterKnife.bind(this);
        initData();
        initView();
    }

    private void initData() {
        mNoteFragment = (EditNoteActivityFragment)
                getFragmentManager().findFragmentByTag(getString(R.string.add_note_fragment));
    }


    private void initView() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }


    @OnClick({R.id.ib_save, R.id.ib_trash})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ib_save:
                saveNote();
                break;
            case R.id.ib_trash:
                confirmTrash();
                break;
        }
    }

    private void saveNote() {
        if (mNoteFragment.confirmNoteComplete()) {
            Note note = mNoteFragment.getNote(true, 0);
            IAddNotePresenter presenter = new AddNotePresenter(this, this);
            presenter.saveNote(note);
        }
    }

    private void confirmTrash() {
        TrashConfirmFragment fragment = new TrashConfirmFragment();
        fragment.setOnConfirmListener(new OnConfirmListener() {
            @Override
            public void onConfirm() {
                exit();
            }

        });
        fragment.show(getFragmentManager(), TRASH_CONFIRM_FRAGMENT);
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
        SingletonToastUtils.showToast(this, "add success");
    }

    @Override
    public void showFailureRemind() {
        SingletonToastUtils.showToast(this, "Error,please try again");
    }
}
