package com.quananhle.knowyourgovernment.details;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.quananhle.knowyourgovernment.R;
import com.quananhle.knowyourgovernment.helper.OfficialAdapter;
import com.quananhle.knowyourgovernment.helper.Officials;
import com.quananhle.knowyourgovernment.helper.SocialMedia;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;

public class OfficialActivity extends AppCompatActivity {
    private static final String TAG = "OfficialActivity";
    private TextView office, name, party, addressLine1, addressLine2, addressLine3, email, url, phone, website, location;
    private TextView addressLabel, phoneLabel, emailLabel, websiteLabel;
    private ImageView profilePhoto, partyLogo, facebookButton, twitterButton, youtubeButton;
    private SocialMedia facebook, twitter, youtube;
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
        setupLocations();
        populateData();




    }




    //====================== *** HELPER•METHODS *** ======================//

    //=====* onCreate *====//
    protected void setupComponents(){
        this.location       = findViewById(R.id.location);
        this.office         = findViewById(R.id.title);
        this.name           = findViewById(R.id.name);
        this.party          = findViewById(R.id.party);
        this.addressLine1   = findViewById(R.id.address_line1);
        this.addressLine2   = findViewById(R.id.address_line2);
        this.addressLine3   = findViewById(R.id.address_line3);
        this.email          = findViewById(R.id.email);
        this.url            = findViewById(R.id.url);
        this.phone          = findViewById(R.id.phone);
        this.website        = findViewById(R.id.website);

        this.addressLabel   = findViewById(R.id.address_label);
        this.phoneLabel     = findViewById(R.id.phone_label);
        this.emailLabel     = findViewById(R.id.email_label);
        this.websiteLabel   = findViewById(R.id.website_label);

        this.profilePhoto   = findViewById(R.id.photo);
        this.partyLogo      = findViewById(R.id.party_logo);
        this.facebookButton = findViewById(R.id.facebook);
        this.twitterButton  = findViewById(R.id.twitter);
        this.youtubeButton  = findViewById(R.id.youtube);
    }
    protected void setupLocations(){
        if (this.getIntent().hasExtra("location")){
            location.setText(getIntent().getStringExtra("location"));
        }
        else {
            location.setText("");
        }
    }
    protected void populateData(){
        Bundle bundle = this.getIntent().getExtras();
        if (this.getIntent().hasExtra("official")){
            official = (Officials) bundle.getSerializable("official");
            ArrayList<SocialMedia> socialMedia = new ArrayList<>();
            if (official.getOffice().equals(DEFAULT_DISPLAY))
        }
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


}
