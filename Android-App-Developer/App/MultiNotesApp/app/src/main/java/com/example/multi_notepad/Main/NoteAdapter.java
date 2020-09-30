package com.example.multi_notepad.Main;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.multi_notepad.Edit.Notes;
import com.example.multi_notepad.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class NoteAdapter extends RecyclerView.Adapter<NoteViewHolder> {
    private static final String TAG = "NoteAdapter";
    private List<Notes>noteList;
    private MainActivity mainActivity;

    public NoteAdapter(List<Notes> noteList, MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        this.noteList = noteList;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: MAKING NEW NoteViewHolder");
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.note_entry, parent, false);
        itemView.setOnClickListener(mainActivity);
        itemView.setOnLongClickListener(mainActivity);
        return new NoteViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: FILLING VIEW HOLDER Note" + position);
        Notes note = noteList.get(position);
        holder.title.setText(note.getName());
        holder.description.setText(note.getMainBody());
        holder.lastModified.setText(note.getTime());
    }
    @Override
    public int getItemCount() {
        return this.noteList.size();
    }
}