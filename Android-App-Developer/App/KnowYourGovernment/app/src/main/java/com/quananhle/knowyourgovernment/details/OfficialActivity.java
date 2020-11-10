package com.quananhle.knowyourgovernment.details;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.quananhle.knowyourgovernment.R;
import com.quananhle.knowyourgovernment.helper.Official;
import com.quananhle.knowyourgovernment.helper.SocialMedia;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.squareup.picasso.Picasso;

public class OfficialActivity extends AppCompatActivity {
    private static final String TAG = "OfficialActivity";
    private TextView location, office, name, party, addressLine1, addressLine2, addressLine3, phone, email, website;
    private TextView addressLabel, phoneLabel, emailLabel, websiteLabel;
    private ImageView profilePhoto, partyLogo, facebookButton, twitterButton, youtubeButton;
    private SocialMedia facebook, twitter, youtube;
    private Official official;
    private ConstraintLayout constraintLayout, information;

    private static final String DEFAULT_DISPLAY = "DATA NOT FOUND";
    private static final String UNKNOWN_PARTY = "Unknown";
    public static final String DEM = "Democratic";
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
            official = (Official) bundle.getSerializable("official");
            ArrayList<SocialMedia> socialMedia = new ArrayList<>();

            if (!official.getOffice().equals(DEFAULT_DISPLAY)){office.setText(official.getOffice());}
            if (!official.getName().equals(DEFAULT_DISPLAY)){name.setText(official.getName());}
            if (!official.getParty().equals(DEFAULT_DISPLAY)){party.setText(official.getParty());}

            if(official.getParty().trim().toLowerCase().contains(DEM)){
                aDonkey();
            }
            else if(official.getParty().trim().toLowerCase().contains(REP)){
                anElephant();
            }
            else{
                anIndependent();
            }

            if (!official.getAddress().equals(DEFAULT_DISPLAY)){
                String[] address = official.getAddress().split("\r\n");
                if (address.length == 3){
                    addressLine1.setText(address[0]);
                    addressLine1.setLinkTextColor(getColor(R.color.americanWhite));
                    addressLine2.setText(address[1]);
                    addressLine2.setLinkTextColor(getColor(R.color.americanWhite));
                    addressLine3.setText(address[2]);
                    addressLine3.setLinkTextColor(getColor(R.color.americanWhite));
                }
            }
            else {
                hideView(addressLabel);
                hideView(addressLine1);
                hideView(addressLine2);
                hideView(addressLine3);
                ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams
                        (ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(0, 0, 0, 0);
                phoneLabel.setLayoutParams(layoutParams);
            }

            if (!official.getPhoneNumber().equals(DEFAULT_DISPLAY)){
                phone.setText(official.getPhoneNumber());
                phone.setLinkTextColor(getColor(R.color.americanWhite));
            }
            else {
                hideView(phoneLabel);
                hideView(phone);
            }

            if (!official.getEmailAddress().equals(DEFAULT_DISPLAY)){
                email.setText(official.getEmailAddress());
                email.setLinkTextColor(getColor(R.color.americanWhite));
            }
            else {
                hideView(emailLabel);
                hideView(email);
            }

            if (!official.getUrl().equals(DEFAULT_DISPLAY)){
                website.setText(official.getUrl());
                website.setLinkTextColor(getColor(R.color.americanWhite));
            }
            else {
                hideView(websiteLabel);
                hideView(website);
            }

            if (isConnected()){
                profilePhoto.setImageResource(R.drawable.placeholder);
                if (official.getPhotoUrl().equals(DEFAULT_DISPLAY)){
                    profilePhoto.setImageResource(R.drawable.missing);
                }
                else {
                    final String photoUrl = official.getPhotoUrl();
                    Picasso picasso = new Picasso.Builder(this).listener(new Picasso.Listener() {
                        @Override
                        public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                            final String secureUrl = photoUrl.replace("http:", "https:");
                            picasso.load(secureUrl).error(R.drawable.brokenimage)
                                    .placeholder(R.drawable.placeholder).into(profilePhoto);
                        }
                    }).build();
                    picasso.load(photoUrl).error(R.drawable.brokenimage).placeholder(R.drawable.placeholder)
                            .into(profilePhoto);
                }
            }
            else {
                profilePhoto.setImageResource(R.drawable.placeholder);
            }
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

    protected void aDonkey(){
        constraintLayout.setBackgroundResource(R.color.democraticBlue);
        partyLogo.setImageResource(R.drawable.dem_logo);
        information.setBackgroundResource(R.color.democraticBlue);
        getWindow().setNavigationBarColor(getColor(R.color.democraticBlue));
    }
    protected void anElephant(){
        constraintLayout.setBackgroundResource(R.color.republicanRed);
        partyLogo.setImageResource(R.drawable.rep_logo);
        information.setBackgroundResource(R.color.republicanRed);
        getWindow().setNavigationBarColor(getColor(R.color.republicanRed));
    }
    protected void anIndependent(){
        constraintLayout.setBackgroundResource(R.color.colorPrimaryDark);
        information.setBackgroundResource(R.color.colorPrimaryDark);
        getWindow().setNavigationBarColor(getColor(R.color.colorPrimaryDark));
    }
    private static void hideView(View view){
        view.setVisibility(View.GONE);
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
