package com.wings.simplenote.view.fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.wings.simplenote.R;
import com.wings.simplenote.listener.OnDatePickListener;
import com.wings.simplenote.listener.OnTimePickListener;
import com.wings.simplenote.model.domain.Note;
import com.wings.simplenote.utils.SingletonToastUtils;
import com.wings.simplenote.utils.TimeUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A placeholder fragment containing a simple view.
 */
public class EditNoteActivityFragment extends Fragment {
    private static final String TAG = "EditNoteActivityFragment";
    private static final String TIME_PICK_DIALOG = "TimePickerFragment";
    private static final String DATE_PICK_DIALOG = "DatePickerFragment";
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

    public void setPicked(boolean picked) {
        isPicked = picked;
    }

    public EditNoteActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View FragmentView = inflater.inflate(R.layout.fragment_edit_note, container, false);
        ButterKnife.bind(this, FragmentView);
        initView();
        return FragmentView;
    }

    private void initView() {
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

    public Note getNote(Boolean isAddNoted, long id) {
        Note note = new Note();
        note.title = String.valueOf(mEtTitle.getText());
        note.content = String.valueOf(mEtContent.getText());
        note.hasAlarm = mCbAlarm.isChecked();
        if (isAddNoted) {
            note.createDate = new Date();
        } else {
            note.id = id;
        }
        note.date = new Date();
        String date = String.valueOf(mTvDate.getText()) + " " +
                String.valueOf(mTvTime.getText());
        if (!TextUtils.isEmpty(date.trim())) {
            note.date = TimeUtils.parseText(date);
        }

        return note;
    }

    private void PickTimeIfCreateAlarm() {
        if (!isPicked) {
            pickTime();
        }
    }

    private void pickDate() {
        DatePickerFragment datePickerDialog = new DatePickerFragment();
        datePickerDialog.setOnDatePickListener(new OnDatePickListener() {
            @Override
            public void onDatePick(int year, int monthOfYear, int dayOfMonth) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, monthOfYear, dayOfMonth);
                Date date = new Date(calendar.getTimeInMillis());
                String text = TimeUtils.formatDate(date);
                mTvDate.setText(text);
                confirmTimeValidity(year, monthOfYear, dayOfMonth);
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

    private void confirmTimeValidity(int year, int monthOfYear, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, monthOfYear, dayOfMonth);
        if (!TimeUtils.isSameDay(calendar, Calendar.getInstance())) {
            return;
        }
        String timeStr = mTvTime.getText().toString().trim();
        if (TextUtils.isEmpty(timeStr)) {
            return;
        }
        Date timeDate = TimeUtils.parseTime(timeStr);
        Calendar rightNow = Calendar.getInstance();
        rightNow.setTime(timeDate);
        rightNow.set(Calendar.YEAR, year);
        rightNow.set(Calendar.MONTH, monthOfYear);
        rightNow.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        if (rightNow.getTimeInMillis() < System.currentTimeMillis() + 60000) {
            //Alarm time over now less than one minute,choose time again
            SingletonToastUtils.showToast(getActivity(), "The time has passed");
            pickDate();
        }
    }

    private void pickTime() {
        TimePickerFragment timePickDialog = new TimePickerFragment();
        timePickDialog.setOnTimePickListener(new OnTimePickListener() {
            @Override
            public void onTimePick(int hourOfDay, int minute) {
                String dateStr = String.valueOf(mTvDate.getText());
                Date date = TimeUtils.parseDate(dateStr);
                Calendar tvCalendar = Calendar.getInstance();
                tvCalendar.setTime(date);

                boolean isToday = TimeUtils.isSameDay(tvCalendar, Calendar.getInstance());
                //if the date is today,decide time has not passed
                Calendar rightNow = Calendar.getInstance();
                rightNow.set(Calendar.HOUR_OF_DAY, hourOfDay);
                rightNow.set(Calendar.MINUTE, minute);
                if (isToday) {
                    if (rightNow.getTimeInMillis() < System.currentTimeMillis() + 60000) {
                        //Alarm time over now less than one minute,choose time again
                        SingletonToastUtils.showToast(getActivity(), "The time has passed");
                        pickTime();
                    }
                }
                String time = TimeUtils.formatTime(rightNow.getTime());
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.tv_date, R.id.tv_time})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_date:
                pickDate();
                break;
            case R.id.tv_time:
                pickTime();
                break;
        }
    }

    public void setTitleText(String title) {
        mEtTitle.setText(title);
    }

    public void setContentText(String content) {
        mEtContent.setText(content);
    }

    public void setAlarmChecked(boolean isChecked) {
        mCbAlarm.setChecked(isChecked);
    }

    public void setDateText(String date) {
        mTvDate.setText(date);
    }

    public void setTimeText(String time) {
        mTvTime.setText(time);
    }
}
