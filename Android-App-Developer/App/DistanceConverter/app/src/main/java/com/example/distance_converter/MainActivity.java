package com.example.distance_converter;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private long ms = System.currentTimeMillis();
    private EditText inputValue;
    private TextView convertedValue;
    private TextView conversionHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inputValue = findViewById(R.id.myValue);

        convertedValue = findViewById(R.id.convertedValue);

        conversionHistory = findViewById(R.id.historyConversion);
        conversionHistory.setMovementMethod(new ScrollingMovementMethod());

        Log.d(TAG, "onCreate: " + ms);
    }
    public void doClear(View v) {
        Log.d(TAG, "onCreate: " + ms);
    }
    public void conversionClicked(View v) {
        String s = inputValue.getText().toString();
        if (s.isEmpty()) {}
        double d = Double.parseDouble(s);
        d *= 2.0;
        String str = new StringBuilder().append(d).toString();
        if(!str.trim().isEmpty()) {
            convertedValue.setText(str);
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 20; i++) {
            sb.append(String.format(Locale.getDefault(), "Line #%d%n", i));
        }
        conversionHistory.setText(sb.toString());

        Log.d(TAG, "doButton: " + d);
    }

}