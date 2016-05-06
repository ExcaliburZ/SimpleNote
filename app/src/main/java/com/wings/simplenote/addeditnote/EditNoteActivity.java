package com.wings.simplenote.addeditnote;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.wings.simplenote.R;
import com.wings.simplenote.model.domain.Note;
import com.wings.simplenote.utils.SingletonToastUtils;
import com.wings.simplenote.utils.TimeUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * EditNoteActivity ,contain EditNoteFragment and Toolbar.
 */
public class EditNoteActivity extends AppCompatActivity implements AddEditNoteContract.View {

    public static final int UPDATE_SUCCESS = 1;
    public static final int UPDATE_FAILED = 0;
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.fab_update)
    FloatingActionButton mUpdateFab;
    private EditNoteFragment mNoteFragment;
    private Note mNoteItem;
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

    }

    private void initView() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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

    private void updateNote() {
        if (mNoteFragment.confirmNoteComplete()) {
            note = mNoteFragment.getNote(false, mNoteItem.id);
            new AddEditNotePresenter(this, this).updateNote(note);
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

    @OnClick(R.id.fab_update)
    public void onClick() {
        updateNote();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
