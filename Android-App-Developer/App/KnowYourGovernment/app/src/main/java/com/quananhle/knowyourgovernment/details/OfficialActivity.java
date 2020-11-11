package com.quananhle.knowyourgovernment.details;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.text.util.Linkify;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.quananhle.knowyourgovernment.R;
import com.quananhle.knowyourgovernment.helper.Official;
import com.quananhle.knowyourgovernment.helper.SocialMedia;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.squareup.picasso.Picasso;

public class OfficialActivity extends AppCompatActivity {
    private static final String TAG = "OfficialActivity";
    private TextView location, office, name, party, addressLine1, addressLine2, addressLine3, phone, email, website;
    private TextView addressLabel, phoneLabel, emailLabel, websiteLabel;
    private ImageView profilePhoto, partyLogo, facebookButton, twitterButton, youtubeButton;
    private SocialMedia channels;
    private Official official;
    private ConstraintLayout constraintLayout, information;

    private static final String DEFAULT_DISPLAY = "DATA NOT FOUND";
    private static final String UNKNOWN_PARTY = "Unknown";
    public static final String DEM = "democratic";
    public static final String REP = "republican";

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

        constraintLayout = findViewById(R.id.constraint_layout);
        information = findViewById(R.id.information);
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

            if (!official.getOffice().equals(DEFAULT_DISPLAY)){office.setText(official.getOffice());}
            if (!official.getName().equals(DEFAULT_DISPLAY)){name.setText(official.getName());}
            if (!official.getParty().equals(DEFAULT_DISPLAY)){party.setText(official.getParty());}

            if(official.getParty().trim().toLowerCase().contains(DEM)){
                aDonkey();
            }
            else if(official.getParty().trim().toLowerCase().contains(REP)){
                anElephant();
            }
            else {
                anIndependent();
            }

            if (!official.getAddress().equals(DEFAULT_DISPLAY)){
                String[] lines = official.getAddress().split("\\r?\\n");
                if (lines.length == 3){
                    addressLine1.setText(lines[0]);
                    addressLine2.setText(lines[1]);
                    addressLine3.setText(lines[2]);
                }
                else if (lines.length == 2){
                    addressLine1.setText(lines[0]);
                    addressLine2.setText(lines[1]);
                }
                else {
                    addressLine1.setText(lines[0]);
                }
                addressLine1.setLinkTextColor(getColor(R.color.americanWhite));
                addressLine2.setLinkTextColor(getColor(R.color.americanWhite));
                addressLine3.setLinkTextColor(getColor(R.color.americanWhite));
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
        }
        Linkify.addLinks(addressLine1,Linkify.MAP_ADDRESSES);
        Linkify.addLinks(phone,Linkify.PHONE_NUMBERS);
        Linkify.addLinks(email,Linkify.EMAIL_ADDRESSES);
        Linkify.addLinks(website,Linkify.WEB_URLS);
        loadProfilePhoto(official.getPhotoUrl().trim());
        loadSocialMediaIcons();
    }

    //=====* onClicked Methods *====//
    public void loadProfilePhoto(String photoUrl){
        if (isConnected()){
            profilePhoto.setImageResource(R.drawable.placeholder);
            if (photoUrl.equals(DEFAULT_DISPLAY)){
                profilePhoto.setImageResource(R.drawable.missing);
            }
            else {
                final String url = photoUrl;
                Picasso picasso = new Picasso.Builder(this).listener(new Picasso.Listener() {
                    @Override
                    public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                        final String secureUrl = url.replace("http:", "https:");
                        picasso.load(secureUrl).error(R.drawable.brokenimage)
                                .placeholder(R.drawable.placeholder).into(profilePhoto);
                    }
                }).build();
                picasso.load(photoUrl).error(R.drawable.brokenimage).placeholder(R.drawable.placeholder)
                        .into(profilePhoto);
            }
        }
    }

    protected void loadSocialMediaIcons(){
        if (!official.getSocialMedia().getFacebookAccount().equals(DEFAULT_DISPLAY)){
            facebookButton.setVisibility(View.VISIBLE);
        }
        if (!official.getSocialMedia().getTwitterAccount().equals(DEFAULT_DISPLAY)){
            twitterButton.setVisibility(View.VISIBLE);
        }
        if (!official.getSocialMedia().getYouTubeChannel().equals(DEFAULT_DISPLAY)){
            youtubeButton.setVisibility(View.VISIBLE);
        }
//        if(official.getSocialMedia().getYouTubeChannel().equals(DEFAULT_DISPLAY)){
//            hideView(youtubeButton);
//        }
//        else {
//            youtubeButton.setVisibility(View.VISIBLE);
//        }
//        if(official.getSocialMedia().getTwitterAccount().equals(DEFAULT_DISPLAY)){
//            hideView(twitterButton);
//        }
//        else {
//            twitterButton.setVisibility(View.VISIBLE);
//        }
//        if(official.getSocialMedia().getFacebookAccount().equals(DEFAULT_DISPLAY)){
//            hideView(facebookButton);
//        }
//        else {
//            facebookButton.setVisibility(View.VISIBLE);
//        }
    }

    public void photoClicked(View view){
        if (!official.getPhotoUrl().equals(DEFAULT_DISPLAY)){
            Intent intent = new Intent(this, PhotoDetailActivity.class);
            intent.putExtra("location", location.getText());
            intent.putExtra("official", official);
            startActivity(intent);
        }
        else {
            Toast.makeText(this, "No Profile Photo", Toast.LENGTH_SHORT).show();
        }
    }

    public void logoClicked(View view){
        Log.d(TAG, "logoClicked: ");
        final String GOP_URL = "https://www.gop.com/";
        final String DEM_URL = "https://democrats.org/";
        if (official.getParty().toLowerCase().trim().contains(DEM)){
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(DEM_URL));
            startActivity(intent);
        }
        else if (official.getParty().toLowerCase().trim().contains(REP)){
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(GOP_URL));
            startActivity(intent);
        }
    }

    public void facebookClicked(View view){
        Log.d(TAG, "facebookClicked: ");
        int currFacebookAppVersion = 3002850;
        String facebookID = official.getSocialMedia().getFacebookAccount();
        String FACEBOOK_URL = "https://www.facebook.com/" + facebookID;
        String urlToUse;
        PackageManager packageManager = getPackageManager();
        try {
            int versionCode = packageManager.getPackageInfo("com.facebook.katana", 0).versionCode;
            if (versionCode >= currFacebookAppVersion){
                urlToUse = "fb://facewebmodal/f?href=" + FACEBOOK_URL;
            }
            else {
                urlToUse = "fb://page/" + official.getSocialMedia().getFacebookAccount();
            }
        }
        catch (PackageManager.NameNotFoundException e) {
            urlToUse = FACEBOOK_URL;
        }
        Intent facebookIntent = new Intent(Intent.ACTION_VIEW);
        facebookIntent.setData(Uri.parse(urlToUse));
        startActivity(facebookIntent);
    }

    public void twitterClicked(View view){
        Log.d(TAG, "twitterClicked: ");
        Intent intent = null;
        String twitterID = official.getSocialMedia().getTwitterAccount();
        try {
            getPackageManager().getPackageInfo("com.twitter.android",0);
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?screen_name=" + twitterID));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }catch (Exception e){
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://twitter.com/" + twitterID));
        }
        startActivity(intent);
    }

    public void youtubeClicked(View view){
        Log.d(TAG, "youtubeClicked: ");
        String youTubeChannel = official.getSocialMedia().getYouTubeChannel();
        Intent intent = null;
        try {
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setPackage("com.google.android.youtube");
            intent.setData(Uri.parse("https://www.youtube.com/" + youTubeChannel));
            startActivity(intent);
        }
        catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/" + youTubeChannel)));
        }
    }

    //=====* Logistic Methods *====//
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
        constraintLayout.setBackgroundResource(R.color.midnight_black);
        partyLogo.setImageResource(R.drawable.no_party_logo);
        information.setBackgroundResource(R.color.midnight_black);
        getWindow().setNavigationBarColor(getColor(R.color.midnight_black));
    }
}
