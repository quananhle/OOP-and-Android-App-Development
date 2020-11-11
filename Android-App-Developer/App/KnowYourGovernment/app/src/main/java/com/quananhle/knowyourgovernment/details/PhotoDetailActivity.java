package com.quananhle.knowyourgovernment.details;

import android.widget.ImageView;
import android.widget.TextView;

import com.quananhle.knowyourgovernment.helper.Official;
import com.quananhle.knowyourgovernment.helper.SocialMedia;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

public class PhotoDetailActivity extends AppCompatActivity {
    private static final String TAG = "PhotoDetailsActivity";
    private TextView location, office, name, party, addressLine1, addressLine2, addressLine3, phone, email, website;
    private TextView addressLabel, phoneLabel, emailLabel, websiteLabel;
    private ImageView profilePhoto, partyLogo, facebookButton, twitterButton, youtubeButton;
    private Official official;
    private ConstraintLayout constraintLayout, information;
    private SocialMedia fbChannel, twChannel, ytChannel;
}
