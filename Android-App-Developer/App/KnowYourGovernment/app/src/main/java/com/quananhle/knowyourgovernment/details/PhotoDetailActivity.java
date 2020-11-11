package com.quananhle.knowyourgovernment.details;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.quananhle.knowyourgovernment.R;
import com.quananhle.knowyourgovernment.helper.Official;
import com.quananhle.knowyourgovernment.helper.SocialMedia;
import com.squareup.picasso.Picasso;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

public class PhotoDetailActivity extends AppCompatActivity {
    private static final String TAG = "PhotoDetailsActivity";
    private TextView location, office, name, party;
    private ImageView profilePhoto, partyLogo;
    private Official official;
    private ConstraintLayout constraintLayout;

    public static final String DEM = "democratic";
    public static final String REP = "republican";
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
        constraintLayout = findViewById(R.id.constraint_layout);
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
                office.setText(official.getOffice());
                name.setText(official.getName());
                party.setText(official.getParty());
                if(official.getParty().trim().toLowerCase().contains(DEM)){
                    aDonkey();
                }
                else if(official.getParty().trim().toLowerCase().contains(REP)){
                    anElephant();
                }
                else {
                    anIndependent();
                }
                loadProfilePhoto(official.getPhotoUrl().trim());
            }
        }
    }
    protected void aDonkey(){
        constraintLayout.setBackgroundResource(R.color.democraticBlue);
        partyLogo.setImageResource(R.drawable.dem_logo);
        getWindow().setNavigationBarColor(getColor(R.color.democraticBlue));
    }
    protected void anElephant(){
        constraintLayout.setBackgroundResource(R.color.republicanRed);
        partyLogo.setImageResource(R.drawable.rep_logo);
        getWindow().setNavigationBarColor(getColor(R.color.republicanRed));
    }
    protected void anIndependent(){
        constraintLayout.setBackgroundResource(R.color.midnight_black);
        partyLogo.setImageResource(R.drawable.no_party_logo);
        getWindow().setNavigationBarColor(getColor(R.color.midnight_black));
    }

    private void loadProfilePhoto(String URL) {
        if (isConnected()) {
            Log.d(TAG, "onCreate: Loading Profile Photo");
            final String photoUrl = this.getIntent().getStringExtra("photoUrl");
            Log.d(TAG, "onCreate: Phot Url is " + photoUrl);
            Picasso picasso = new Picasso.Builder(this).listener(new Picasso.Listener() {
                @Override
                public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                    // Here we try https if the http image attempt failed
                    final String changedUrl = photoUrl.replace("http:", "https:");
                    Log.d(TAG, "onImageLoadFailed: AAA");
                    picasso.get().load(changedUrl)
                            .error(R.drawable.brokenimage)
                            .placeholder(R.drawable.placeholder)
                            .into(profilePhoto);

                }
            }).build();
            Log.d(TAG, "onCreate: BBB");
            picasso.load(photoUrl)
                    .error(R.drawable.brokenimage)
                    .placeholder(R.drawable.placeholder)
                    .into(profilePhoto);
        }
        else {
            profilePhoto.setImageResource(R.drawable.placeholder);
            showMessage(ERROR_ICON, "NO NETWORK CONNECTION",
                    "Data cannot be accessed/loaded without an Internet connection");
        }





        Picasso.get()
                .load(URL)
                .error(R.drawable.brokenimage)
                .placeholder(R.drawable.placeholder)
                .into(profilePhoto);
    }
}
