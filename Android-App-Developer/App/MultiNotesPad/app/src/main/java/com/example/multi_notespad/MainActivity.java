package com.example.multi_notespad;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.JsonWriter;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
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
            InputStream is = getApplicationContext().openFileInput(getString(R.string.file_name));
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
            Toast.makeText(this, getString(R.string.no_file), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return notes;
    }
    @Override
    protected void onPause() {
        notes.setTitle(title.getText().toString());
        notes.setDescription(description.getText().toString());
        saveNote();
        super.onPause();
    }
    private void saveNote() {
        Log.d(TAG, "saveNote: Saving Note");
        try {
            FileOutputStream fos = getApplicationContext().openFileOutput(getString(R.string.file_name), Context.MODE_PRIVATE);
            JsonWriter writer = new JsonWriter(new OutputStreamWriter(fos, getString(R.string.encoding)));
            writer.setIndent(" ");
            writer.beginObject();
            writer.name("title").value(notes.getTitle());
            writer.name("descrition").value(notes.getDescription());
            writer.endObject();
            writer.close();

            // Print out JSON
            StringWriter sw = new StringWriter();
            writer = new JsonWriter(sw);
            writer.setIndent(" ");
            writer.beginObject();
            writer.name("title").value(notes.getTitle());
            writer.name("descrition").value(notes.getDescription());
            writer.endObject();
            writer.close();
            Log.d(TAG, "saveNote: Note:\n" + sw.toString());

            Toast.makeText(this, getString(R.string.saved), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.getStackTrace();
        }
    }
}