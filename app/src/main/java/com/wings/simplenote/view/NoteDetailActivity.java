package com.wings.simplenote.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.wings.simplenote.R;
import com.wings.simplenote.model.domain.Note;
import com.wings.simplenote.presenter.INoteDetailPresenter;
import com.wings.simplenote.presenter.impl.NoteDetailPresenter;
import com.wings.simplenote.utils.TimeUtils;

import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * The Activity to show the note detail.
 */
public class NoteDetailActivity extends AppCompatActivity implements INoteUpdateView {

    private static final String TAG = "NoteDetailActivity";
    private static final int EDIT_NOTE_ITEM = 0;
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.et_title)
    TextView mEtTitle;
    @Bind(R.id.tv_date)
    TextView mTvDate;
    @Bind(R.id.tv_time)
    TextView mTvTime;
    @Bind(R.id.cb_alarm)
    CheckBox mCbAlarm;
    @Bind(R.id.et_content)
    TextView mEtContent;
    @Bind(R.id.edit)
    FloatingActionButton mEdit;
    private Note mNoteItem;
    private INoteDetailPresenter mNoteDetailPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_detail);
        ButterKnife.bind(this);
        initDate();
        initView();
    }

    private void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        updateViews(mNoteItem);
    }

    public void setViews(Note noteItem) {
        mEtTitle.setText(noteItem.title);
        mEtContent.setText(noteItem.content);
        if (noteItem.hasAlarm) {
            mCbAlarm.setChecked(true);
            Date date = noteItem.date;

            String dateStr = TimeUtils.formatDate(date);
            mTvDate.setText(dateStr);
            mTvDate.setVisibility(View.VISIBLE);

            String timeStr = TimeUtils.formatTime(date);
            mTvTime.setText(timeStr);
            mTvTime.setVisibility(View.VISIBLE);
        } else {
            mCbAlarm.setChecked(false);
            mTvDate.setVisibility(View.INVISIBLE);
            mTvTime.setVisibility(View.INVISIBLE);
        }
    }

    private void initDate() {
        mNoteItem = (Note) getIntent().getSerializableExtra("note");
        mNoteDetailPresenter = new NoteDetailPresenter(this, this);
    }

    @OnClick(R.id.edit)
    public void onClick() {
        Intent intent = new Intent(this, EditNoteActivity.class);
        intent.putExtra("note", mNoteItem);
        startActivityForResult(intent, EDIT_NOTE_ITEM);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case EditNoteActivity.UPDATE_SUCCESS:
                setResult(EditNoteActivity.UPDATE_SUCCESS);
                Note edit = (Note) data.getSerializableExtra("edit");
                setViews(edit);
                break;
            case EditNoteActivity.UPDATE_FAILED:
                break;
        }
    }

    @Override
    public void showProcess() {

    }

    @Override
    public void dismissProcess() {

    }

    @Override
    public void updateViews(Note noteItem) {
        setViews(noteItem);
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
