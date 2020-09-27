package com.example.multi_notespad;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.EditText;

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
}