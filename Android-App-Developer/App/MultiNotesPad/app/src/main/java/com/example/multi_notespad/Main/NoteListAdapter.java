package com.example.multi_notespad.Main;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.multi_notespad.R;

import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class NoteListAdapter extends RecyclerView.Adapter<noteViewHolder> {
    private static final String TAG = "NoteAdapter";
    private List<NoteList>noteList;
    private MainActivity mainActivity;

    public NoteListAdapter(List<NoteList> noteList, MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        this.noteList = noteList;
    }

    @NonNull
    @Override
    public noteViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: MAKING NEW noteViewHolder");
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_entry, parent, false);
        itemView.setOnClickListener(mainActivity);
        itemView.setOnLongClickListener(mainActivity);
        return new noteViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull noteViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: FILLING VIEW HOLDER Note" + position);
        NoteList nl = noteList.get(position);
        holder.title.setText(nl.getName());
        holder.dateTime.setText(new Date().toString());
        holder.description.setText(nl.getBody());
    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }
}
