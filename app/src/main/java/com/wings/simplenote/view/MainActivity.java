package com.wings.simplenote.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.ActionMode;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;

import com.wings.simplenote.R;
import com.wings.simplenote.adapter.NotesAdapter;
import com.wings.simplenote.config.DividerItemDecoration;
import com.wings.simplenote.model.domain.MultiSelector;
import com.wings.simplenote.model.domain.Note;
import com.wings.simplenote.presenter.INotesShowPresenter;
import com.wings.simplenote.presenter.impl.NotesListPresenter;
import com.wings.simplenote.utils.SingletonToastUtils;
import com.wings.simplenote.view.fragment.AboutFragment;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * The Main Interface to show the notes list.
 */
public class MainActivity extends AppCompatActivity implements INotesShowView,
        SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = "MainActivity";
    public static final int ADD_NOTE_EVENT = 0;
    public static final int EDIT_NOTE_EVENT = 1;
    private static final String ABOUT_FRAGMENT = "ABOUT_FRAGMENT";
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
    private long[] mHits = new long[2];
    private ActionMode.Callback mMultiMode;
    private MultiSelector mSelector;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(R.string.all_notes);
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager
                (MainActivity.this, LinearLayoutManager.VERTICAL, false);
        mShowPresenter = new NotesListPresenter(this, this);
        mShowPresenter.showNotesList();
        mNotesViews.setLayoutManager(mLinearLayoutManager);
        mNotesViews.addItemDecoration(
                new DividerItemDecoration(this, null));
        mRefreshLayout.setColorSchemeResources(
                R.color.colorPrimary,
                R.color.green_cycle,
                R.color.orange_cycle,
                R.color.red_cycle);
        setListener();
    }


    private void setListener() {
        mRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_about:
                AboutFragment aboutFragment = new AboutFragment();
                aboutFragment.show(getFragmentManager(), ABOUT_FRAGMENT);
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void showNotes(List<Note> noteList) {
        mNotesAdapter = new NotesAdapter(MainActivity.this, noteList, new MultiSelector());
//        mSelector = mNotesAdapter.getSelector();
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

            case EDIT_NOTE_EVENT:
                if (resultCode == EditNoteActivity.UPDATE_SUCCESS) {
                    mShowPresenter.showNotesList();
                }
                break;
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                //Google Multi Hit logic
                System.arraycopy(mHits, 1, mHits, 0, mHits.length - 1);
                mHits[mHits.length - 1] = SystemClock.uptimeMillis();

                if (mHits[0] >= (SystemClock.uptimeMillis() - 2000)) {
                    //between (SystemClock.uptimeMillis()- 2000) And SystemClock.uptimeMillis()
                    finish();
                } else {
                    SingletonToastUtils.showToast(this, getString(R.string.double_click_exit));
                }
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
