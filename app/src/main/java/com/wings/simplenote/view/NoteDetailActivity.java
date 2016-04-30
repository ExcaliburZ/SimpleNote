package com.wings.simplenote.view;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.wings.simplenote.R;
import com.wings.simplenote.model.domain.Note;
import com.wings.simplenote.utils.TimeUtils;

import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NoteDetailActivity extends AppCompatActivity {

    private static final String TAG = "NoteDetailActivity";
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_detail);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        initDate();
        initView();
    }

    private void initView() {
        mEtTitle.setText(mNoteItem.title);
        mEtContent.setText(mNoteItem.content);
        if (mNoteItem.hasAlarm) {
            mCbAlarm.setChecked(true);
            Date date = mNoteItem.date;

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
    }

    @OnClick(R.id.edit)
    public void onClick() {
    }
}
