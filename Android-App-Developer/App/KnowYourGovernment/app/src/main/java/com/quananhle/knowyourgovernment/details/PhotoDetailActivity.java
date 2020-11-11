package com.quananhle.knowyourgovernment.details;

import android.widget.ImageView;
import android.widget.TextView;

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
}
