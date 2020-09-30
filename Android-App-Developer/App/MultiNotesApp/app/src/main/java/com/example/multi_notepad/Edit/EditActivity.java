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
import java.util.Calendar;
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
        if(i.getExtras() != null){
            editName.setText(i.getStringExtra("UPDATED_TITLE"));
            editBody.setText(i.getStringExtra("UPDATED_DESCRIPTION"));
            Log.d(TAG, "onCreate: " + i.getSerializableExtra("NOTE"));
        }
    }
    @Override
    protected void onResume() {
        //load the file containing the file data - if exists
        note = loadFile();
        //check if file is loaded
        if (note != null) {
            editName.setText(note.getName());
            editBody.setText(note.getBody());
        }
        super.onResume();
    }
    @Override
    public void onBackPressed(){
        final String titleStr = editName.getText().toString();
        final String descrStr = editBody.getText().toString();
        //check if title is missing
        if(titleStr.trim().isEmpty()){
            Intent dataToReturn = new Intent();
            setResult(RESULT_OK, dataToReturn);
            finish();
        }
        //otherwise, if a title has been given
        else{
            Intent dataToReturn = new Intent();
            if(getIntent().getExtras() != null) {
                //if the note with the same title and description is existing
                if (descrStr.equals(getIntent().getStringExtra("UPDATED_DESCRIPTION"))
                        && titleStr.equals(getIntent().getStringExtra("UPDATED_TITLE"))) {
                    setResult(RESULT_OK, dataToReturn);
                    finish();
                }
                //show dialog box if changes have been made
                else {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                    //if user selected 'YES'
                    alertDialogBuilder.setPositiveButton("YES!", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent dataToReturn = new Intent();
                            //edit an existing note
                            if(getIntent().getExtras() != null){
                                //if no changes have been made to an existing note
                                if(descrStr.equals(getIntent().getStringExtra("UPDATED_DESCRIPTION"))
                                        && titleStr.equals(getIntent().getStringExtra("UPDATED_TITLE"))){
                                    setResult(RESULT_OK, dataToReturn);
                                    finish();
                                }
                                //otherwise, if changes have been made
                                else{
                                    dataToReturn.putExtra("UPDATED_TITLE", titleStr);
                                    dataToReturn.putExtra("UPDATED_DESCRIPTION", descrStr);
                                    setResult(RESULT_CANCELED, dataToReturn);
                                    finish();
                                }
                            }
                            //creat a new note
                            else{
                                dataToReturn.putExtra("NEW_TITLE", titleStr);
                                dataToReturn.putExtra("NEW_DESCRIPTION", descrStr);
                                setResult(0, dataToReturn);
                                finish();
                            }
                        }
                    });
                    //if user selected 'No'
                    alertDialogBuilder.setNegativeButton("NO!", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent dataToReturn = new Intent();
                            setResult(RESULT_OK, dataToReturn);
                            finish();
                        }
                    });
                    //dialog Box
                    alertDialogBuilder.setTitle("YOUR NOTE IS NOT SAVED!");
                    alertDialogBuilder.setMessage("SAVE NOTE \'" + titleStr + "\'?");
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }
            }
            //otherwise, create a new note
            else{
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

                //if user selected 'YES'
                alertDialogBuilder.setPositiveButton("YES!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent dataToReturn = new Intent();
                        dataToReturn.putExtra("NEW_TITLE", titleStr);
                        dataToReturn.putExtra("NEW_DESCRIPTION", descrStr);
                        setResult(RESULT_CANCELED, dataToReturn);
                        finish();
                    }
                });
                //if user selected 'NO'
                alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent dataToReturn = new Intent();
                        setResult(RESULT_OK, dataToReturn);
                        finish();
                    }
                });
                //dialog Box
                alertDialogBuilder.setTitle("YOUR NOTE IS NOT SAVED!");
                alertDialogBuilder.setMessage("SAVE NOTE \'" + titleStr + "\'?");
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        }

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.getMenuInflater().inflate(R.menu.menu_edit, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.saveButton:
                Toast.makeText(this, "SAVING", Toast.LENGTH_SHORT).show();
                EditText editTitle = findViewById(R.id.editTitle);
                String titleStr = editTitle.getText().toString();
                EditText editDescription = findViewById(R.id.editDescription);
                String descrStr = editDescription.getText().toString();
                //check if title field is empty, a note without a title is not allowed to be saved
                if (titleStr.trim().isEmpty()){
                    Toast.makeText(this, "MISING TITLE",
                            Toast.LENGTH_SHORT).show();
                    Intent dataToReturn = new Intent();
                    setResult(RESULT_CANCELED, dataToReturn);
                    finish();
                }
                //otherwise, creating a new note or updating an existing note
                else{
                    Intent dataToReturn = new Intent();
                    Intent intent = getIntent();
                    //editing an existing note
                    if(intent.getExtras() != null){
                        //if no changes are made
                        if(descrStr.equals(intent.getStringExtra("UPDATED_DESCRIPTION")) &&
                                titleStr.equals(intent.getStringExtra("UPDATED_TITLE"))){
                            setResult(-1, dataToReturn);
                            finish();
                        }
                        //make changes to an existing note
                        else{
                            dataToReturn.putExtra("UPDATED_TITLE", titleStr);
                            dataToReturn.putExtra("UPDATED_DESCRIPTION", descrStr);
                            setResult(0, dataToReturn);
                            finish();
                        }
                    }
                    //otherwise, create a new note
                    else{
                        dataToReturn.putExtra("NEW_TITLE", titleStr);
                        dataToReturn.putExtra("NEW_DESCRIPTION", descrStr);
                        setResult(0, dataToReturn);
                        finish();
                    }
                }
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
    public String getCurrentTime() {
        Date D = Calendar.getInstance().getTime();
        SimpleDateFormat SDF = new SimpleDateFormat("E MMM d, h:mm a");
        String lastSaveDate = SDF.format(D).toString();
        return lastSaveDate;
    }
}