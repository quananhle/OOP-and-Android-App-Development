package com.example.multi_notepad.Main;

import android.view.View;
import android.widget.TextView;

import com.example.multi_notepad.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class NoteViewHolder extends RecyclerView.ViewHolder {
    TextView title;
    TextView lastModified;
    TextView description;
    public NoteViewHolder(@NonNull View itemView) {
        super(itemView);
        this.title = (TextView) itemView.findViewById(R.id.noteTitle);
        this.lastModified = (TextView) itemView.findViewById(R.id.noteDateTime);
        this.description = (TextView) itemView.findViewById(R.id.noteDescription);
    }
}