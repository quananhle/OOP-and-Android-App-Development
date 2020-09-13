package com.example.distance_converter;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
//    private EditText userText;
//    private TextView output;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        //Bind var's to the screen widgets
//        userText = findViewById(R.id.editText);
//        output = findViewById(R.id.textView);
//    }
//    public void buttonClicked(View v) {
//        String text = userText.getText().toString();
//        StringBuilder sb = new StringBuilder(text);
//        if(!text.trim().isEmpty()) {
//            output.setText(sb.reverse());
//        }
//    }
    private static final String TAG = "MainActivity";
    private long ms = System.currentTimeMillis();
    private EditText inputValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG, "onCreate: " + ms);

        inputValue = findViewById(myValue);
    }
    public void doPress(View v) {
        Log.d(TAG, "onCreate: " + ms);
    }
    public void doButton(View v) {
        Log.d(TAG, "onCreate: " + ms);
    }
}