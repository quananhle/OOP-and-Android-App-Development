package com.example.multi_notespad.Edit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.JsonWriter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.multi_notespad.R;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;

public class EditActivity extends AppCompatActivity {
    private static final String TAG = "EditActivity";
    private EditText title;
    private EditText description;
    private Notes notes;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        title = findViewById(R.id.noteTitle);
        description = findViewById(R.id.noteDescription);
        description.setMovementMethod(new ScrollingMovementMethod());
        description.setTextIsSelectable(true);
    }
    @Override
    protected void onResume() {
        //Load the file containing the file data - if exists
        notes = loadFile();
        //check if file is loaded
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
            while ((line = reader.readLine()) != null) {
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

            /**
             * Print out JSON
              */
            StringWriter sw = new StringWriter();
            writer = new JsonWriter(sw);
            writer.setIndent(" ");
            writer.beginObject();
            writer.name("title").value(notes.getTitle());
            writer.name("descrition").value(notes.getDescription());
            writer.endObject();
            writer.close();
            Log.d(TAG, "saveNote: Note:\n" + sw.toString());
            //==== End of print ===//

            Toast.makeText(this, getString(R.string.saved), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.getStackTrace();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        return super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.saveButton:
                Toast.makeText(this, "You want to save", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}