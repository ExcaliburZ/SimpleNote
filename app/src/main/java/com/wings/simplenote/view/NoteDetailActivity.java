package com.wings.simplenote.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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
    private INoteDetailPresenter mNoteDetaulPresenter;

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
        getSupportActionBar().setDisplayShowHomeEnabled(true);

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
        }
    }

    private void initDate() {
        mNoteItem = (Note) getIntent().getSerializableExtra("note");
        mNoteDetaulPresenter = new NoteDetailPresenter(this, this);
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
                mNoteDetaulPresenter.getNote(mNoteItem.id);
                break;
            case EditNoteActivity.UPDATE_FAILED:
                Log.i(TAG, "onActivityResult: failed");
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

}
