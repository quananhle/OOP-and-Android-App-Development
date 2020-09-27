package com.example.multi_notespad.Main;

import android.view.ViewGroup;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class NoteListAdapter extends RecyclerView.Adapter {
    private List<NoteList>noteLists;
    private MainActivity mainActivity;
    public NoteListAdapter(List<NoteList> noteList, MainActivity mainActivity) {
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
