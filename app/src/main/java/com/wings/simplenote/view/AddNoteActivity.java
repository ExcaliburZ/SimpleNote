package com.wings.simplenote.view;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.wings.simplenote.R;
import com.wings.simplenote.listener.OnDatePickListener;
import com.wings.simplenote.listener.OnTimePickListener;
import com.wings.simplenote.view.fragment.DatePickerFragment;
import com.wings.simplenote.view.fragment.TimePickerFragment;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class AddNoteActivity extends AppCompatActivity {

    private static final String TIME_PICK_DIALOG = "TimePickerFragment";
    private static final String DATE_PICK_DIALOG = "DatePickerFragment";
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.ib_save)
    ImageButton mIbSave;
    @Bind(R.id.ib_trash)
    ImageButton mIbTrash;
    @Bind(R.id.et_title)
    EditText mEtTitle;
    @Bind(R.id.tv_date)
    TextView mTvDate;
    @Bind(R.id.tv_time)
    TextView mTvTime;
    @Bind(R.id.cb_alarm)
    CheckBox mCbAlarm;
    @Bind(R.id.et_content)
    EditText mEtContent;
    private boolean isPicked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        ButterKnife.bind(this);
        initView();
    }


    private void initView() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toggleSoftKeyboard();
        mCbAlarm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //first time, so pick date and time.
                    if (!isPicked) {
                        pickDate();
                    }
                    //has date and time ,just show it.
                    showDateTimeViews();
                } else {
                    hideDateTimeViews();
                }
            }


        });
    }

    private void hideDateTimeViews() {
        mTvDate.setVisibility(View.INVISIBLE);
        mTvTime.setVisibility(View.INVISIBLE);
    }

    private void showDateTimeViews() {
        mTvDate.setVisibility(View.VISIBLE);
        mTvTime.setVisibility(View.VISIBLE);
    }

    private void toggleSoftKeyboard() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                InputMethodManager inputManager =
                        (InputMethodManager) mEtTitle.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.showSoftInput(mEtTitle, 0);
            }
        }, 998);
    }

    @OnClick({R.id.ib_save, R.id.ib_trash, R.id.tv_date, R.id.tv_time})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ib_save:
                break;
            case R.id.ib_trash:
                break;
            case R.id.tv_date:
                pickDate();
                break;
            case R.id.tv_time:
                pickTime();
                break;
        }
    }

    private void pickDate() {
        DatePickerFragment datePickerDialog = new DatePickerFragment();
        datePickerDialog.setOnDatePickListener(new OnDatePickListener() {
            @Override
            public void onDatePick(int year, int monthOfYear, int dayOfMonth) {
                String date = year + getString(R.string.date_separator)
                        + monthOfYear + getString(R.string.date_separator) + dayOfMonth;
                mTvDate.setText(date);
                PickTimeIfCreateAlarm();
            }

            @Override
            public void onDatePickCancel() {
                if (!isPicked) {
                    mCbAlarm.setChecked(false);
                }
            }
        });
        datePickerDialog.show(getFragmentManager(), DATE_PICK_DIALOG);
    }

    private void PickTimeIfCreateAlarm() {
        if (!isPicked) {
            pickTime();
        }
    }

    private void pickTime() {
        TimePickerFragment timePickDialog = new TimePickerFragment();
        timePickDialog.setOnTimePickListener(new OnTimePickListener() {
            @Override
            public void onTimePick(int hourOfDay, int minute) {
                String time = hourOfDay + getString(R.string.time_separator) + minute;
                mTvTime.setText(time);
                if (!isPicked) {
                    isPicked = true;
                }
            }

            @Override
            public void onTimePickCancel() {
                if (!isPicked) {
                    mCbAlarm.setChecked(false);
                }
            }
        });
        timePickDialog.show(getFragmentManager(), TIME_PICK_DIALOG);
    }
}
