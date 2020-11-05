package com.quananhle.knowyourgovernment.helper;

import java.io.Serializable;

public class Officials implements Serializable {
    private String office;
    private String name;
    private String party;
    private String address;
    private String phoneNumber;
    private String url;
    private String emailAddress;
    private String photoUrl;
    private SocialMedia socialMedia;
    //default constructor
    public Officials() {
        this.office = office;
        this.name = name;
        this.party = party;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.url = url;
        this.emailAddress = emailAddress;
        this.photoUrl = photoUrl;
        this.socialMedia = socialMedia;
    }
    //non-default constructor
    public Officials(String office, String name, String party, String address, String phoneNumber,
                     String url, String emailAddress, String photoUrl, SocialMedia socialMedia) {
        this.office = office;
        this.name = name;
        this.party = party;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.url = url;
        this.emailAddress = emailAddress;
        this.photoUrl = photoUrl;
        this.socialMedia = socialMedia;
    }
}
