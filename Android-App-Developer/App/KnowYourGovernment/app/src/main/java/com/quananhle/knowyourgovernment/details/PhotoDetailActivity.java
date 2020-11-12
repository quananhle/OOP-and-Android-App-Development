package com.quananhle.knowyourgovernment.details;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.quananhle.knowyourgovernment.R;
import com.quananhle.knowyourgovernment.helper.Official;
import com.squareup.picasso.Picasso;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

public class PhotoDetailActivity extends AppCompatActivity {
    private static final String TAG = "PhotoDetailsActivity";
    private TextView location, office, name, party;
    private ImageView profilePhoto, partyLogo;
    private Official official;
    private ConstraintLayout constraintLayout;

    final int WARNING_ICON = 1;
    final int ERROR_ICON = 2;

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
        partyLogo        = findViewById(R.id.party_logo);
        constraintLayout = findViewById(R.id.constraint_layout);
    }
    protected void setupLocations(){
        location.setBackgroundResource(R.color.americanRed);
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
        getWindow().setNavigationBarColor(getColor(R.color.americanBlue));
    }
    protected void anElephant(){
        constraintLayout.setBackgroundResource(R.color.republicanRed);
        partyLogo.setImageResource(R.drawable.rep_logo);
        getWindow().setNavigationBarColor(getColor(R.color.americanBlue));
    }
    protected void anIndependent(){
        constraintLayout.setBackgroundResource(R.color.midnight_black);
        partyLogo.setImageResource(R.drawable.no_party_logo);
        getWindow().setNavigationBarColor(getColor(R.color.americanBlue));
    }

    private void loadProfilePhoto(String photoUrl) {
        if (isConnected()) {
            Log.d(TAG, "onCreate: Loading Photo");
            Log.d(TAG, "onCreate: Photo Url " + photoUrl);
            Picasso.get().load(photoUrl).error(R.drawable.brokenimage)
                    .placeholder(R.drawable.placeholder)
                    .into(profilePhoto);
        }
        else {
            profilePhoto.setImageResource(R.drawable.placeholder);
            showMessage(ERROR_ICON, "NO NETWORK CONNECTION",
                    "Data cannot be accessed/loaded without an Internet connection");
        }
    }
    protected void logoClicked(View view) {
        Log.d(TAG, "logoClicked: ");
        String GOP_URL = "https://www.gop.com";
        String DEM_URL = "https://democrats.org";
        if(official.getParty().toLowerCase().trim().contains(DEM)) {
            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(DEM_URL));
            startActivity(i);
        }
        else if(official.getParty().toLowerCase().trim().contains(REP)) {
            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(GOP_URL));
            startActivity(i);
        }
    }

    public void showMessage(int icon, String title, String message) {
        AlertDialog dialog = new AlertDialog.Builder(this).create();
        dialog.setTitle(title);
        dialog.setMessage(message);
        if (icon == WARNING_ICON) {
            dialog.setIcon(R.drawable.warning);
        } else if (icon == ERROR_ICON) {
            dialog.setIcon(R.drawable.error);
        } else {
            dialog.setIcon(null);
        }
        dialog.show();
    }
    public boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (connectivityManager == null) {
            return false;
        } else if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        } else {
            return false;
        }
    }
}
