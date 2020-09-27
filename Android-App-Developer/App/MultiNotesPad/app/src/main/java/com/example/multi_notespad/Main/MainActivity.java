package com.example.multi_notespad.Main;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.multi_notespad.R;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity
        implements View.OnClickListener, View.onLongClickListener {
//    private TextView textView;
    private RecyclerView recyclerView;
    private final List<NoteList> noteList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        textView = findViewById(R.id.textView);
        recyclerView = findViewById(R.id.recycler);
        //Data to recyclerview adapter
        NoteListAdapter noteListAdapter = new NoteListAdapter(noteList, this);
        recyclerView.setAdapter(noteListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
    @Override
    public void onClick(View v) {
        int pos = recyclerView.getChildLayoutPosition(v);
        NoteList note = noteList.get(pos);
        Toast.makeText(v.getContext(), "SHORT" + note.toString(), Toast.LENGTH_SHORT).show();
    }
    @Override
    public boolean onLongClick(View v) {
        int pos = recyclerView.getChildLayoutPosition(v);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        return super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        textView.setText(String.format("You selected: %s", item.getTitle()));
        switch (item.getItemId()) {
            case R.id.createButton:
                Toast.makeText(this, "You want to create new note", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.aboutButton:
                Toast.makeText(this, "You want to select about", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
