package com.wings.simplenote.view;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.wings.simplenote.R;
import com.wings.simplenote.listener.OnConfirmListener;
import com.wings.simplenote.listener.OnDatePickListener;
import com.wings.simplenote.listener.OnTimePickListener;
import com.wings.simplenote.model.domain.Note;
import com.wings.simplenote.presenter.AddNotePresenter;
import com.wings.simplenote.presenter.IAddNotePresenter;
import com.wings.simplenote.utils.DateFormatUtils;
import com.wings.simplenote.utils.SingletonToastUtils;
import com.wings.simplenote.view.fragment.DatePickerFragment;
import com.wings.simplenote.view.fragment.TimePickerFragment;
import com.wings.simplenote.view.fragment.TrashConfirmFragment;

import junit.framework.Assert;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class AddNoteActivity extends AppCompatActivity implements IAddNoteView {

    private static final String TAG = "AddNoteActivity";
    private static final String TIME_PICK_DIALOG = "TimePickerFragment";
    private static final String DATE_PICK_DIALOG = "DatePickerFragment";
    private static final String TRASH_CONFIRM_FRAGMENT = "TrashConfirmFragment";
    public static final int ADD_SUCCESS = 1;
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
                saveNote();
                break;
            case R.id.ib_trash:
                confirmTrash();
                break;
            case R.id.tv_date:
                pickDate();
                break;
            case R.id.tv_time:
                pickTime();
                break;
        }
    }

    private void saveNote() {
        if (confirmNoteComplete()) {
            Note note = getNote();
            IAddNotePresenter presenter = new AddNotePresenter(this, this);
            presenter.saveNote(note);
        }
    }

    private boolean confirmNoteComplete() {
        if (TextUtils.isEmpty(mEtTitle.getText())) {
            SingletonToastUtils.showToast(this, "title can't be empty");
            return false;
        }
        return true;
    }

    @NonNull
    private Note getNote() {
        Note note = new Note();

        note.title = String.valueOf(mEtTitle.getText());
        note.content = String.valueOf(mEtContent.getText());
        note.hasAlarm = mCbAlarm.isChecked();
        note.date = new Date();
        String date = String.valueOf(mTvDate.getText()) + " " +
                String.valueOf(mTvTime.getText());
        if (!TextUtils.isEmpty(date.trim())) {
            note.date = DateFormatUtils.parseText(date);
        }
        return note;
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

    private void pickDate() {
        DatePickerFragment datePickerDialog = new DatePickerFragment();
        datePickerDialog.setOnDatePickListener(new OnDatePickListener() {
            @Override
            public void onDatePick(int year, int monthOfYear, int dayOfMonth) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, monthOfYear, dayOfMonth);
                Date date = new Date(calendar.getTimeInMillis());
                String text = DateFormatUtils.formatDate(date);
                mTvDate.setText(text);
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
                CharSequence text = mTvDate.getText();
                Assert.assertNotNull(text);
                String today = DateFormatUtils.formatDate(new Date());
                //if the date is today,decide time has not passed
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);

                if (TextUtils.equals(String.valueOf(text), today)) {
                    if (calendar.getTimeInMillis() < System.currentTimeMillis() + 60000) {
                        //Alarm time over now less than one minute,choose time again
                        SingletonToastUtils.showToast(AddNoteActivity.this, "The time has passed");
                        pickTime();
                    }
                }
                String time = DateFormatUtils.formatTime(calendar.getTime());
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
