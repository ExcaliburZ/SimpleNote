package com.wings.simplenote.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wings.simplenote.R;
import com.wings.simplenote.model.domain.Note;
import com.wings.simplenote.utils.TimeUtils;
import com.wings.simplenote.view.NoteDetailActivity;

import java.util.Calendar;
import java.util.List;

/**
 * Created by wing on 2016/4/19.
 */
public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NoteViewHolder> {
    private Context mContext;
    private List<Note> mNotesList;

    public NotesAdapter(Context context, List<Note> notesList) {
        this.mContext = context;
        this.mNotesList = notesList;
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
    public void onBindViewHolder(NoteViewHolder holder, int position) {
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
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, NoteDetailActivity.class);
                intent.putExtra("note", noteItem);
                mContext.startActivity(intent);
            }
        });
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
