package com.wings.simplenote.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;

import com.wings.simplenote.R;
import com.wings.simplenote.model.domain.Note;
import com.wings.simplenote.presenter.IUpdateNotePresenter;
import com.wings.simplenote.presenter.impl.UpdateNotePresenter;
import com.wings.simplenote.utils.SingletonToastUtils;
import com.wings.simplenote.utils.TimeUtils;
import com.wings.simplenote.view.fragment.EditNoteFragment;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * EditNoteActivity ,contain EditNoteFragment and Toolbar.
 */
public class EditNoteActivity extends AppCompatActivity implements INoteView {

    public static final int UPDATE_SUCCESS = 1;
    public static final int UPDATE_FAILED = 0;
    @Bind(R.id.ib_update)
    ImageButton mIbUpdate;
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    private EditNoteFragment mNoteFragment;
    private Note mNoteItem;
    private IUpdateNotePresenter mUpdateNotePresenter;
    private Note note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);
        ButterKnife.bind(this);
        initData();
        initView();
    }

    private void initData() {
        mNoteItem = (Note) getIntent().getSerializableExtra("note");
        mNoteFragment = (EditNoteFragment)
                getFragmentManager().findFragmentByTag(getString(R.string.edit_notes_fragment));
        mUpdateNotePresenter = new UpdateNotePresenter(this, this);
    }

    private void initView() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mNoteFragment.setTitleText(mNoteItem.title);
        mNoteFragment.setContentText(mNoteItem.content);
        mNoteFragment.setItemID(mNoteItem.id);
        boolean hasAlarm = mNoteItem.hasAlarm;
        if (hasAlarm) {
            mNoteFragment.setPicked(true);
            mNoteFragment.setAlarmChecked(true);
            mNoteFragment.setDateText(TimeUtils.formatDate(mNoteItem.date));
            mNoteFragment.setTimeText(TimeUtils.formatTime(mNoteItem.date));
        }
    }

    @OnClick({R.id.ib_update})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ib_update:
                updateNote();
                break;
        }
    }

    private void updateNote() {
        if (mNoteFragment.confirmNoteComplete()) {
            note = mNoteFragment.getNote(false, mNoteItem.id);
            mUpdateNotePresenter.updateNote(note);
        }
    }

    @Override
    public void showProcess() {

    }

    @Override
    public void dismissProcess() {

    }

    @Override
    public void showSuccessRemind() {
        SingletonToastUtils.showToast(this, "update success");
        Intent data = new Intent();
        data.putExtra("edit", note);
        setResult(UPDATE_SUCCESS, data);
        finish();
    }

    @Override
    public void showFailureRemind() {
        SingletonToastUtils.showToast(this, "update failed");
        setResult(UPDATE_FAILED);
        finish();
    }
}
