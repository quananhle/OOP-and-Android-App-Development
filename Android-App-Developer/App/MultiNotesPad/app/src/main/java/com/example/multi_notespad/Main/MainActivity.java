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
        implements View.OnClickListener, View.OnLongClickListener {
//    private TextView textView;
    private RecyclerView recyclerView;
    private final List<NoteList> noteList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        for (int i=0; i<10;i++){
            noteList.add(new NoteList());
        }
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
        Toast.makeText(v.getContext(), "onClick" + note.toString(), Toast.LENGTH_SHORT).show();
    }
    // From OnLongClickListener
    @Override
    public boolean onLongClick(View v) {
        int pos = recyclerView.getChildLayoutPosition(v);
        NoteList note = noteList.get(pos);
        Toast.makeText(v.getContext(), "onLongClick " + note.toString(), Toast.LENGTH_SHORT).show();
        return false;
    }
    @Override
    public void onBackPressed() {
        Toast.makeText(this, "The back button was pressed - Bye!", Toast.LENGTH_SHORT).show();
        super.onBackPressed();
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
                Toast.makeText(this, "NEW", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.aboutButton:
                Toast.makeText(this, "INFO", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}




