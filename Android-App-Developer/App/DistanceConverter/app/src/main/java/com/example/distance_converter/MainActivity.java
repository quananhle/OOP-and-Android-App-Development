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
    //signal for different option
    private boolean isMileToKilometer = true;
    //set format of the decimal number
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
    //radio buttons
    public void radioClicked(View v) {
        String milesValue = "Miles Value: ";
        String kilosValue = "Kilometers Value: ";
        switch (v.getId()) {
            //when milesToKilo radio button is checked
            case R.id.milesToKilo:
                //change the text displays
                inputTypeDisplay.setText(milesValue);
                outputTypeDisplay.setText(kilosValue);
                //change the signal
                isMileToKilometer = true;
                break;
            //when kiloToMiles radio button is check
            case R.id.kiloToMiles:
                //change the text displays
                inputTypeDisplay.setText(kilosValue);
                outputTypeDisplay.setText(milesValue);
                isMileToKilometer = false;
                break;
        }
    }
    //CONVERSION button
    public void conversionClicked(View v) {
        double convertedVal = 0;
        String s = inputValue.getText().toString();
        inputValue.getText().clear();

        //Exception: check if user has not entered a valid input value
        if(s.matches("")) {
            Toast.makeText(this, "Value has not been entered",
                    Toast.LENGTH_SHORT).show();
            Log.e(TAG, "Value has not been entered");
            return;
        }
        //Calculations
        double inputVal = Double.parseDouble(s);
        //if kiloToMiles button is checked
        if(!isMileToKilometer) {
            convertedVal = inputVal * 0.621371;
            if(inputVal > 1) {
                Toast.makeText(this, "You entered value: " + inputVal + " kilometers", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(this, "You entered value: " + inputVal + " kilometer", Toast.LENGTH_SHORT).show();
            }
        }
        //if milesToKilo button is checked
        else if(isMileToKilometer) {
            convertedVal = inputVal * 1.60934;
            if(inputVal > 1) {
                Toast.makeText(this, "You entered value: " + inputVal + " miles", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(this, "You entered value: " + inputVal + " miles", Toast.LENGTH_SHORT).show();
            }
        }
        String str = new StringBuilder().append(DF.format(convertedVal)).toString();
        //if input value is converted
        if(!str.trim().isEmpty()) {
            //print converted value
            convertedValue.setText(str);
        }
        StringBuilder sb = new StringBuilder();
        //if kilometer to mile radio button is checked
        if(!isMileToKilometer) {
            sb.append(String.format(Locale.getDefault(),
                    "%.1f Km ===> %.1f Mi %n", inputVal, convertedVal));
        }
        //if mile to kilometer radio button is checked
        else if (isMileToKilometer) {
            sb.append(String.format(Locale.getDefault(),
                    "%.1f Mi ===> %.1f Km %n", inputVal, convertedVal));
        }
        //prepend the conversion to conversion history
        conversionHistory.setText(sb.toString() + conversionHistory.getText());

        Log.d(TAG, "doButton: " + inputVal);
    }
    //CLEAR button
    public void clearClicked(View v) {
        inputValue.getText().clear();
        conversionHistory.setText("");
        convertedValue.setText("");
    }

    /**
     * Landscape layout
     */
//    @Override1.1
//    protected void onSaveInstanceState(Bundle outState) {
//        outState.putString(inputValue.getText());
//    }

}