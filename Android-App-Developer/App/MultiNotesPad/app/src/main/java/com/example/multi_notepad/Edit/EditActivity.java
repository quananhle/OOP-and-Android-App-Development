package com.example.multi_notepad.Edit;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.JsonWriter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.multi_notepad.R;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EditActivity extends AppCompatActivity {
    private static final String TAG = "EditActivity";
    private EditText editName;
    private EditText editBody;
    private Notes note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        editName = findViewById(R.id.editTitle);
        editBody = findViewById(R.id.editDescription);
        editBody.setMovementMethod(new ScrollingMovementMethod());
        editBody.setTextIsSelectable(true);

        Intent i = getIntent();
        if(i.hasExtra("NOTE")){
            note = (Notes) i.getSerializableExtra("NOTE");
            editName.setText(note.getName());
            editBody.setText(note.getBody());
            Log.d(TAG, "onCreate: " + i.getSerializableExtra("NOTE"));
        }
    }
    @Override
    protected void onResume() {
        //Load the file containing the file data - if exists
        note = loadFile();
        //check if file is loaded
        if (note != null) {
            editName.setText(note.getName());
            editBody.setText(note.getBody());
        }
        super.onResume();
    }
    @Override
    public void onBackPressed() {
        doReturn(null);
        super.onBackPressed();
    }
    @Override
    protected void onPause() {
        note.setName(editName.getText().toString());
        note.setBody(editBody.getText().toString());
        saveNote();
        super.onPause();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.saveButton:
                Toast.makeText(this, "You want to save", Toast.LENGTH_SHORT).show();
                doSaveButton(null);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    //====================== *** Helper methods *** ======================//
    private void saveNote() {
        Log.d(TAG, "saveNote: Saving Note");
        try {
            FileOutputStream fos = getApplicationContext().openFileOutput(
                    getString(R.string.file_name), Context.MODE_PRIVATE);
            JsonWriter writer = new JsonWriter(new OutputStreamWriter(fos,
                    getString(R.string.encoding)));
            writer.setIndent(" ");
            writer.beginObject();
            writer.name("title").value(note.getName());
            writer.name("descrition").value(note.getBody());
            writer.endObject();
            writer.close();

            /**
             * Print out JSON
             */
            StringWriter sw = new StringWriter();
            writer = new JsonWriter(sw);
            writer.setIndent(" ");
            writer.beginObject();
            writer.name("title").value(note.getName());
            writer.name("descrition").value(note.getBody());
            writer.endObject();
            writer.close();
            Log.d(TAG, "saveNote: Note:\n" + sw.toString());
            //==== End of print ===//

            Toast.makeText(this, getString(R.string.saved), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.getStackTrace();
        }
    }
    private Notes loadFile() {
        Log.d(TAG, "loadFile: Loading Multi-Note File");
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
            note.setName(titleStr);
            note.setBody(descrStr);
        } catch (FileNotFoundException fnfe) {
            Toast.makeText(this, getString(R.string.no_file), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return note;
    }
    private void doReturn(View v) {
        EditText editTitle = findViewById(R.id.editTitle);
        final String titleStr = editTitle.getText().toString();
        EditText editDescription = findViewById(R.id.editDescription);
        final String descrStr = editDescription.getText().toString();
        //check if title field is empty or no changes has been made in an existing note
        if(titleStr.trim().isEmpty() ||
                (titleStr.trim().equals(note.getName()) && descrStr.trim().equals(note.getBody()))){
            Intent dataToReturn = new Intent();
            //if no changes have been made to the current note, the Edit Activity simply exits
            setResult(RESULT_CANCELED, dataToReturn);
            finish();
        }
        else{
            /*
            display a confirmation dialog where the user can opt to save the note
            (if changes have been made) before exiting the activity.
            */
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("YOUR NOTE IS NOT SAVED!");
            builder.setMessage("SAVE NOTE \'" + titleStr + "\'?");
            //if user selected 'Yes'
            builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Notes savedNote = new Notes(titleStr, descrStr, getCurrentTime());
                    Intent dataToReturn = new Intent();
                    //if note title is new, return NEW_NOTE, otherwise return UPDATED_NOTE
                    dataToReturn.putExtra((note.getName()==null ? "NEW_NOTE" : "UPDATED_NOTE"),
                            savedNote);
                    setResult(RESULT_OK, dataToReturn);
                    finish();
                }
            });
            //if user selected 'No'
            builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent dataToReturn = new Intent();
                    setResult(RESULT_CANCELED, dataToReturn);
                    finish();
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
    }
    public void doSaveButton(View v){
        EditText editTitle = findViewById(R.id.editTitle);
        String titleStr = editTitle.getText().toString();
        EditText editDescription = findViewById(R.id.editDescription);
        String descrStr = editDescription.getText().toString();
        //check if title field is empty
        if (titleStr.trim().isEmpty()){
            Toast.makeText(this, "A note without a title is not allowed to be saved",
                    Toast.LENGTH_SHORT).show();
            Intent dataToReturn = new Intent();
            setResult(RESULT_CANCELED, dataToReturn);
            finish();
        }
        //check if no changes has been made in an existing note
        else if (titleStr.equals(note.getName()) && descrStr.equals(note.getBody())){
            Intent dataToReturn = new Intent();
            setResult(RESULT_CANCELED, dataToReturn);
            finish();
        }
        //otherwise, creating a new note or updating an existing note
        else{
            Notes savedNote = new Notes(titleStr, descrStr, getCurrentTime());
            Intent dataToReturn = new Intent();
            //if note title is new, return NEW_NOTE, otherwise return UPDATED_NOTE
            dataToReturn.putExtra(note.getName()==null ? "NEW_NOTE" : "UPDATED_NOTE", savedNote);
            setResult(RESULT_OK, dataToReturn);
            finish();
        }
    }
    public String getCurrentTime(){
        DateFormat df = new SimpleDateFormat("E MM d, h:m s");
        return df.format(new Date().toString());
    }
}