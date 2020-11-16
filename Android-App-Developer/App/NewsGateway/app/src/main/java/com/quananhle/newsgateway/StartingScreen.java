package com.quananhle.newsgateway;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;

public class StartingScreen extends AppCompatActivity{
    private static final String TAG = "AboutActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.starting_screen);
    }
    public void apiClicked(View view){
        final String API_URL = "https://developers.google.com/civic-information/";
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(API_URL));
        startActivity(intent);
    }
    public void appNameClicked(View view){
        final String APP_SOURCE_URL = "https://github.com/Quananhle/OOP-and-Android-App-Development/tree/master/Android-App-Developer/App/KnowYourGovernment";
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(APP_SOURCE_URL));
        startActivity(intent);
    }
    public void iconClicked(View view){
        final String APP_SOURCE_URL = "https://github.com/Quananhle/OOP-and-Android-App-Development/tree/master/Android-App-Developer/App/KnowYourGovernment";
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(APP_SOURCE_URL));
        startActivity(intent);
    }
    public void developerNameClicked(View view){
        final String DEVELOPER_GITHUB_URL = "https://github.com/Quananhle";
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(DEVELOPER_GITHUB_URL));
        startActivity(intent);
    }
}
