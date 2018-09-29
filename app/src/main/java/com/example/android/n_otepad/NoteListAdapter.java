package com.example.android.n_otepad;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

public class NoteListAdapter extends RecyclerView.Adapter<NoteListAdapter.ViewHolder> {
    private Context mContext;
    private List<Note> notesList;
    private DatabaseHelper mDatabase;

    public NoteListAdapter(Context context, List<Note> notes)
    {
        this.mContext = context;
        this.notesList = notes;
        mDatabase = new DatabaseHelper(context);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        public TextView edt_title;
        public  TextView edt_content;

        public ViewHolder(View view)
        {
            super(view);
            edt_title = view.findViewById(R.id.note_title);
            edt_content = view.findViewById(R.id.note_content);
        }
    }
    @NonNull
    @Override
    public NoteListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_list,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Note singleNote = notesList.get(position);
        holder.edt_title.setText(singleNote.getTitle());
        holder.edt_content.setText(singleNote.getContent());
    }

    @Override
    public int getItemCount() {
        return notesList.size();
    }
}
