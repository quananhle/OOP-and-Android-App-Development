package com.example.distance_converter;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private long ms = System.currentTimeMillis();
    private EditText inputValue;
    private TextView convertedValue, conversionHistory, inputTypeDisplay, outputTypeDisplay;
    private boolean isMileToKilometer = false;
    private final DecimalFormat DF = new DecimalFormat("#,###,##0.0");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inputValue = findViewById(R.id.myValue);

        convertedValue = findViewById(R.id.convertedValue);

        conversionHistory = findViewById(R.id.historyConversions);
        conversionHistory.setMovementMethod(new ScrollingMovementMethod());

        inputTypeDisplay = findViewById(R.id.inputType);
        outputTypeDisplay = findViewById(R.id.outputType);

        Log.d(TAG, "onCreate: " + ms);
    }

    public void radioClicked(View v) {
        String milesValue = "Miles Value: ";
        String kilosValue = "Kilometers Value: ";
        switch (v.getId()) {
            case R.id.kiloToMiles:
                inputTypeDisplay.setText(kilosValue);
                outputTypeDisplay.setText(milesValue);
                break;
            case R.id.milesToKilo:
                inputTypeDisplay.setText(milesValue);
                outputTypeDisplay.setText(kilosValue);
                isMileToKilometer = true;
                break;
        }
    }

    public void conversionClicked(View v) {
        String s = inputValue.getText().toString();
        //check if user has not entered a valid input value
        if(s.matches("")) {
            Toast.makeText(this, "Value has not been entered",
                    Toast.LENGTH_SHORT).show();
            Log.e(TAG, "Value has not been entered");
            return;
        }
        double d = Double.parseDouble(s);
        if(!isMileToKilometer) {
            d *= 0.621371;
        }
        else if(isMileToKilometer) {
            d *= 1.60934;
        }
        String str = new StringBuilder().append(DF.format(d)).toString();
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

    public void clearClicked(View v) {
        Log.d(TAG, "onClear: " + ms);
    }

}