package com.quananhle.knowyourgovernment.details;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.quananhle.knowyourgovernment.R;
import com.quananhle.knowyourgovernment.helper.Official;
import com.quananhle.knowyourgovernment.helper.SocialMedia;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

public class PhotoDetailActivity extends AppCompatActivity {
    private static final String TAG = "PhotoDetailsActivity";
    private TextView location, office, name, party;
    private ImageView profilePhoto, partyLogo;
    private Official official;
    private ConstraintLayout constraintLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        setupComponents();
        setupLocations();
        populateData();
    }
    protected void setupComponents(){
        location         = findViewById(R.id.location);
        office           = findViewById(R.id.title);
        name             = findViewById(R.id.name);
        party            = findViewById(R.id.party);
        profilePhoto     = findViewById(R.id.photo);
        partyLogo        = findViewById(R.id.logo);
        constraintLayout = findViewById(R.id.information);
    }
    protected void setupLocations(){
        Intent intent = this.getIntent();
        if(intent.hasExtra("location"))
            location.setText(intent.getStringExtra("location"));
        else
            location.setText("");
    }
    protected void populateData(){
        Intent intent = this.getIntent();
        if (intent.hasExtra("official")){
            official = (Official) getIntent().getSerializableExtra("official");
            if (official != null){
                
            }
        }
    }
}
