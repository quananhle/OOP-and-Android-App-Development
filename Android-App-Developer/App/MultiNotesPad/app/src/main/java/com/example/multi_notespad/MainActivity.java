package com.example.multi_notespad;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private EditText title;
    private EditText description;
    private Notes notes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        title = findViewById(R.id.noteTitle);
        description = findViewById(R.id.noteDescription);
        description.setMovementMethod(new ScrollingMovementMethod());
        description.setTextIsSelectable(true);
    }
    @Override
    protected void onResume() {
        notes = loadFile();
        if (notes != null) {
            title.setText(notes.getTitle());
            description.setText(notes.getDescription());
        }
        super.onResume();
    }
    private Notes loadFile() {
        Log.d(TAG, "loadFile: Loading Multi-Note File");
        notes = new Notes();
        try {
            InputStream is = getApplicationContext().openFileInput("Multi-Note.json");
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = readerReadLine()) != null) {
                sb.append(line);
            }

            JSONObject jsonObject = new JSONObject(sb.toString());
            String titleStr = jsonObject.getString("title");
            String descrStr = jsonObject.getString("description");
            notes.setTitle(titleStr);
            notes.setDescription(descrStr);
        } catch (FileNotFoundException fnfe) {
            Toast.makeText(this, "No Multi-Note File Present", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return notes;
    }
}