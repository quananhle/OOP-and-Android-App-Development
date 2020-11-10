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
    private static final String DEFAULT_DISPLAY = "DATA NOT FOUND";

    //default constructor
    public Officials() {
        this.office = DEFAULT_DISPLAY;
        this.name = DEFAULT_DISPLAY;
        this.party = DEFAULT_DISPLAY;
        this.address = DEFAULT_DISPLAY;
        this.phoneNumber = DEFAULT_DISPLAY;
        this.url = DEFAULT_DISPLAY;
        this.emailAddress = DEFAULT_DISPLAY;
        this.photoUrl = DEFAULT_DISPLAY;
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

    public String getOffice() {
        return office;
    }

    public void setOffice(String office) {
        this.office = office;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParty() {
        return party;
    }

    public void setParty(String party) {
        this.party = party;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public SocialMedia getSocialMedia() {
        return socialMedia;
    }

    public void setSocialMedia(SocialMedia socialMedia) {
        this.socialMedia = socialMedia;
    }

    @Override
    public String toString() {
        return "Officials{" +
                "office='" + office + '\'' +
                ", name='" + name + '\'' +
                ", party='" + party + '\'' +
                ", address='" + address + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", url='" + url + '\'' +
                ", emailAddress='" + emailAddress + '\'' +
                ", photoUrl='" + photoUrl + '\'' +
                ", socialMedia=" + socialMedia +
                '}';
    }
}