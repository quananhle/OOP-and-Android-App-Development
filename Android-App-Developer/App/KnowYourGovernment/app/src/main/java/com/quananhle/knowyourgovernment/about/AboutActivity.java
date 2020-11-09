package com.quananhle.knowyourgovernment.about;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.quananhle.knowyourgovernment.R;

import androidx.appcompat.app.AppCompatActivity;

public class AboutActivity extends AppCompatActivity {
    private static final String TAG = "AboutActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
    }
    protected void apiClicked(View view){
        final String API_URL = "https://developers.google.com/civic-information/";
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(API_URL));
        startActivity(intent);
    }
}
