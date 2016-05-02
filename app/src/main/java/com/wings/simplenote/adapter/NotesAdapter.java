package com.wings.simplenote.adapter;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.wings.simplenote.R;
import com.wings.simplenote.model.domain.MultiSelector;
import com.wings.simplenote.model.domain.Note;
import com.wings.simplenote.presenter.impl.DeleteNotePresenter;
import com.wings.simplenote.presenter.IDeleteNotePresenter;
import com.wings.simplenote.utils.TimeUtils;
import com.wings.simplenote.view.MainActivity;
import com.wings.simplenote.view.NoteDetailActivity;

import java.util.Calendar;
import java.util.List;

/**
 * Created by wing on 2016/4/19.
 */
public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NoteViewHolder> {
    private static final String TAG = "NotesAdapter";
    private AppCompatActivity mContext;
    private List<Note> mNotesList;
    private MultiSelector mSelector;
    private boolean isMultiMode;
    private IDeleteNotePresenter mDeleteNotePresenter;
    private ActionMode.Callback mActionSelectMode = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(android.view.ActionMode mode, Menu menu) {
            mContext.getMenuInflater().inflate(R.menu.menu_multi_mode, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(android.view.ActionMode mode, Menu menu) {
            setSelectable(true);
            isMultiMode = true;
            return false;
        }

        @Override
        public boolean onActionItemClicked(android.view.ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_delete:
                    deleteSelectedNotes();
                    break;
            }
            return true;
        }

        @Override
        public void onDestroyActionMode(android.view.ActionMode mode) {
            setSelectable(false);
            isMultiMode = false;
            mSelector.clear();
            notifyDataSetChanged();
        }
    };
    private ActionMode mActionMode;

    private void deleteSelectedNotes() {
        mDeleteNotePresenter = new DeleteNotePresenter(mContext);
        new AlertDialog.Builder(mContext)
                .setTitle(R.string.title_delete)
                .setMessage(R.string.content_delete)
                .setNegativeButton(R.string.btn_cancel, null)
                .setPositiveButton(R.string.btn_confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        for (Note item : mNotesList) {
                            if (mSelector.contains(item.id)) {
                                mNotesList.remove(item);
                                mSelector.remove(item.id);
                                mDeleteNotePresenter.deleteNote(item.id);
                            }
                        }
                        notifyDataSetChanged();
                    }
                }).create().show();
    }

    public NotesAdapter(AppCompatActivity context, List<Note> notesList, MultiSelector mSelectedList) {
        this.mContext = context;
        this.mNotesList = notesList;
        this.mSelector = mSelectedList;
    }

    public void setSelectable(Boolean isSelectable) {
        mSelector.setSelectable(isSelectable);
        this.notifyDataSetChanged();
    }

    public boolean isSelectable() {
        return mSelector.isSelectable();
    }

    public void setNotesList(List<Note> NotesList) {
        this.mNotesList = NotesList;
    }

    @Override
    public int getItemCount() {
        return mNotesList.size();
    }

    @Override
    public NoteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_note, parent, false);
        return new NoteViewHolder(itemView);
    }

    @Override
    public long getItemId(int position) {
        return mNotesList.get(position).id;
    }

    @Override
    public void onBindViewHolder(final NoteViewHolder holder, final int position) {
        final Note noteItem = mNotesList.get(position);
        holder.titleView.setText(noteItem.title);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(noteItem.createDate);
        String dateTime;
        //if today ,set time .else set date
        if (TimeUtils.isSameDay(calendar, Calendar.getInstance())) {
            dateTime = TimeUtils.formatTime(noteItem.createDate);
        } else {
            dateTime = TimeUtils.formatDate(noteItem.createDate);
        }
        holder.dateView.setText(dateTime);
        boolean itemSelected = isItemSelected(getItemId(position));

        if (itemSelected) {
            holder.itemView.setBackgroundColor(
                    mContext.getResources().getColor(R.color.cardview_select_background));
        } else {
            holder.itemView.setBackgroundColor(
                    mContext.getResources().getColor(R.color.cardview_light_background));
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isSelectable()) {
                    selectItem(position);
                } else {
                    enterNoteDetail(noteItem);
                }
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (!isMultiMode) {
                    mActionMode = mContext.startActionMode(mActionSelectMode);
                    setStatusBarColor();
                }
                selectItem(position);
                return true;
            }

        });
    }

    private void setStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = mContext.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(mContext.getResources().getColor(R.color.colorPrimaryDark));
        }
    }

    private void enterNoteDetail(Note noteItem) {
        Intent intent = new Intent(mContext, NoteDetailActivity.class);
        intent.putExtra("note", noteItem);
        mContext.startActivityForResult(intent, MainActivity.EDIT_NOTE_EVENT);
    }

    private void selectItem(int position) {
        boolean itemSelected = isItemSelected(getItemId(position));
        setItemSelected(position, !itemSelected);
        notifyItemChanged(position);
    }

    private void setItemSelected(int position, boolean beSelect) {
        if (beSelect) {
            mSelector.add(getItemId(position));
        } else {
            mSelector.remove(getItemId(position));
            if (mSelector.isEmpty()) {
                mActionMode.finish();
            }
        }
    }

    private boolean isItemSelected(long itemId) {
        return mSelector.contains(itemId);
    }


    class NoteViewHolder extends RecyclerView.ViewHolder {
        private TextView titleView;
        private TextView dateView;

        public NoteViewHolder(View itemView) {
            super(itemView);
            titleView = (TextView) itemView.findViewById(R.id.title);
            dateView = (TextView) itemView.findViewById(R.id.date);
        }
    }
}
