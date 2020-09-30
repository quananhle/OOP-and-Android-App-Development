package com.example.multi_notepad.Main;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.multi_notepad.About.AboutActivity;
import com.example.multi_notepad.Edit.EditActivity;
import com.example.multi_notepad.Edit.Notes;
import com.example.multi_notepad.R;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity
        implements View.OnClickListener, View.OnLongClickListener {
    private static final String TAG = "MainActivity";
    private static final int ADD_REQUEST_CODE = 1;
    private static final int EDIT_REQUEST_CODE = 1;
    private RecyclerView recyclerView;
    private final List<Notes> notesList = new ArrayList<>();
    private NoteAdapter noteAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recycler);
        //Data to recyclerview adapter
        noteAdapter = new NoteAdapter(notesList, this);
        recyclerView.setAdapter(noteAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        this.loadFile();
        if(notesList.size()>0){
            getSupportActionBar().setTitle(getString(R.string.file_name)
                    + "(" + notesList.size() + ")");
        }
        noteAdapter.notifyDataSetChanged();
    }
    @Override
    public void onClick(View v) {
        int pos = recyclerView.getChildLayoutPosition(v);
        Notes note = notesList.get(pos);
    }
    // From OnLongClickListener
    @Override
    public boolean onLongClick(View v) {
        int pos = recyclerView.getChildLayoutPosition(v);
        notesList.remove(pos);
        noteAdapter.notifyDataSetChanged();
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
                Intent intentEdit = new Intent(MainActivity.this, EditActivity.class);
                startActivityForResult(intentEdit, ADD_REQUEST_CODE);
                return true;
            case R.id.aboutButton:
                Toast.makeText(this, "INFO", Toast.LENGTH_SHORT).show();
                Intent intentAbout = new Intent(MainActivity.this, AboutActivity.class);
                startActivity(intentAbout);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_REQUEST_CODE){
            if (resultCode == RESULT_OK){
                String s = data.getStringExtra("USER_STRING");
                if(s == null){
                    Toast.makeText(this, "Null text value returned", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(s.isEmpty()){
                    Toast.makeText(this, "Empty text returned", Toast.LENGTH_SHORT).show();
                }
                ((TextView) findViewById(R.id.editTitle)).setText(s);
                Log.d(TAG, "onActivityResult: User Text: " + s);
            }
            else {
                Log.d(TAG, "onActivityResult: Result NOT OK: " + resultCode);
            }
        }
        else {
            Log.d(TAG, "onActivityResult: Unexpected request code: " + requestCode);
        }
    }
    //
    public void openNewActivity(View v){
        Notes newNoteList = new Notes();
        Intent intent = new Intent(this, EditActivity.class);
        intent.putExtra("NOTE_OBJECT", newNoteList);
        startActivityForResult(intent, ADD_REQUEST_CODE);


    }
    private void loadFile() {
        try {
            InputStream inputStream = getApplicationContext().openFileInput(getString(R.string.notes_file));
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(inputStream, getString(R.string.encoding)));
            String line;
            StringBuilder stringBuilder = new StringBuilder();
            while ((line = bufferedReader.readLine()) != null){
                stringBuilder.append(line);
            }
            JSONArray jsonArray = new JSONArray(stringBuilder.toString());
            for (int i = 0; i < jsonArray.length(); i++) {
                
            }
        }
    }
}
