package com.example.multi_notespad.Main;

import android.view.View;
import android.widget.TextView;

import com.example.multi_notespad.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class NoteViewHolder extends RecyclerView.ViewHolder {
    TextView title;
    TextView dateTime;
    TextView description;
    public NoteViewHolder(@NonNull View itemView) {
        super(itemView);
        this.title = itemView.findViewById(R.id.noteTitle);
        this.dateTime = itemView.findViewById(R.id.dateTime);
        this.description = itemView.findViewById(R.id.noteDescription);
    }
}
