package com.wings.simplenote.view;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.wings.simplenote.R;
import com.wings.simplenote.adapter.NotesAdapter;
import com.wings.simplenote.model.domain.Note;
import com.wings.simplenote.presenter.INotesShowPresenter;
import com.wings.simplenote.presenter.NotesListPresenter;
import com.wings.simplenote.receiver.AlarmReceiver;

import java.util.Calendar;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements INotesShowView,
        SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = "MainActivity";
    private static final int ADD_NOTE_EVENT = 0;
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.content)
    RecyclerView mNotesViews;
    @Bind(R.id.refresh)
    SwipeRefreshLayout mRefreshLayout;
    @Bind(R.id.fab)
    FloatingActionButton mAddNoteButton;

    //    private List<Note> mNoteList;
    private INotesShowPresenter mShowPresenter;
    private NotesAdapter mNotesAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        setSupportActionBar(mToolbar);
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager
                (MainActivity.this, LinearLayoutManager.VERTICAL, false);
        mShowPresenter = new NotesListPresenter(this, this);
        mShowPresenter.showNotesList();
        mNotesViews.setLayoutManager(mLinearLayoutManager);
        setListener();
    }


    private void setListener() {
        mRefreshLayout.setOnRefreshListener(this);
    }

    private void addAlarm() {
        Log.i(TAG, "addAlarm");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.add(Calendar.SECOND, 5);
        Intent intent = new Intent(this, AlarmReceiver.class);

        Intent alarm = new Intent(AlarmClock.ACTION_SET_ALARM);

        alarm.putExtra(AlarmClock.EXTRA_HOUR, 23);
        alarm.putExtra(AlarmClock.EXTRA_MINUTES, calendar.get(Calendar.MINUTE) + 1);
        alarm.putExtra(AlarmClock.EXTRA_SKIP_UI, true);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent clockIntent = PendingIntent.getActivity(this, 0, alarm, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        manager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), clockIntent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void showNotes(List<Note> noteList) {
        mNotesAdapter = new NotesAdapter(MainActivity.this, noteList);
        mNotesViews.setAdapter(mNotesAdapter);
    }

    @Override
    public void refreshNotes(List<Note> noteList) {
        mNotesAdapter.setNotesList(noteList);
        mNotesAdapter.notifyDataSetChanged();
    }


    @Override
    public void showLoading() {
        mRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mRefreshLayout.setRefreshing(true);
            }
        });
    }

    @Override
    public void cancelLoading() {
        mRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onRefresh() {
        mShowPresenter.refreshNotes();
    }

    @OnClick(R.id.fab)
    public void onClick() {
        Intent enterNewActivity = new Intent(this, AddNoteActivity.class);
        startActivityForResult(enterNewActivity, ADD_NOTE_EVENT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case ADD_NOTE_EVENT:
                if (resultCode == AddNoteActivity.ADD_SUCCESS) {
                    mShowPresenter.showNotesList();
                }
                break;

        }

    }
}
