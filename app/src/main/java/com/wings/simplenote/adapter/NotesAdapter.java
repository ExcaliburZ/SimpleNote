package com.wings.simplenote.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wings.simplenote.R;
import com.wings.simplenote.domain.Note;
import com.wings.simplenote.utils.DateFormatUtils;

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
        Note noteItem = mNotesList.get(position);
        holder.titleView.setText(noteItem.title);
        String date = DateFormatUtils.formatDate(noteItem.date);
        holder.dateView.setText(date);
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
