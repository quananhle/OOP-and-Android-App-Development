package com.example.multi_notespad.Main;

import android.view.View;
import android.widget.TextView;

import com.example.multi_notespad.R;

import androidx.recyclerview.widget.RecyclerView;

public class ViewHolder extends RecyclerView.ViewHolder {
    public TextView title;
    TextView dateTime;
    TextView description;
    ViewHolder(View view) {
        super(view);
        this.title = view.findViewById(R.id.noteTitle);
        this.dateTime = view.findViewById(R.id.dateTime);
        this.description = view.findViewById(R.id.noteDescription);
    }
}
