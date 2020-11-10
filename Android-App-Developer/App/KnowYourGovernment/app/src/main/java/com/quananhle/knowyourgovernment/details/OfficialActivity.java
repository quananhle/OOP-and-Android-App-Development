package com.quananhle.knowyourgovernment.details;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.quananhle.knowyourgovernment.R;
import com.quananhle.knowyourgovernment.helper.OfficialAdapter;
import com.quananhle.knowyourgovernment.helper.Officials;
import com.quananhle.knowyourgovernment.helper.SocialMedia;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;

public class OfficialActivity extends AppCompatActivity {
    private static final String TAG = "OfficialActivity";
    private TextView office, name, party, address, email, url, phone, website, location;
    private ImageView profilePhoto, partyLogo, facebook, twitter, youtube;
    private SocialMedia facebookHandle, twitterHandle, youtubeHandle;
    private Officials official;
    private ConstraintLayout constraintLayout, information;

    private static final String DEFAULT_DISPLAY = "DATA NOT FOUND";
    private static final String UNKNOWN_PARTY = "Unknown";
    public static final String DEM = "Democrat";
    public static final String REP = "Republican";

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_officials);

        setupComponents();
    }


    public void photoClicked(View view){

    }
    public void logoClicked(View view){

    }
    public void facebookClicked(View view){

    }
    public void twitterClicked(View view){

    }
    public void youtubeClicked(View view){

    }

    //====================== *** HELPER•METHODS *** ======================//

    //=====* onCreate *====//
    private void setupComponents(){
//        private TextView location, office, name, party, addressLine1, addressLine2, addressLine3, email, url, phone, website;
        

        recyclerView = findViewById(R.id.recyclerView);
        officialAdapter = new OfficialAdapter(officialsList, this);
        recyclerView.setAdapter(officialAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        locationView = findViewById(R.id.location);
    }

}
