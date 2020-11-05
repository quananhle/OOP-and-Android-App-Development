package com.quananhle.knowyourgovernment.helper;

import java.io.Serializable;

public class SocialMedia implements Serializable {
    private String googleAccount;
    private String facebookAccount;
    private String twitterAccount;
    private String youtubeChannel;
    private static final String DEFAULT_DISPLAY = "DATA NOT FOUND";
    //default constructor
    public SocialMedia() {
        this.googleAccount = DEFAULT_DISPLAY;
        this.facebookAccount = DEFAULT_DISPLAY;
        this.twitterAccount = DEFAULT_DISPLAY;
        this.youtubeChannel = DEFAULT_DISPLAY;
    }
    //non-default constructor
    public SocialMedia(String googleAccount, String facebookAccount, String twitterAccount, String youtubeChannel) {
        this.googleAccount = googleAccount;
        this.facebookAccount = facebookAccount;
        this.twitterAccount = twitterAccount;
        this.youtubeChannel = youtubeChannel;
    }
    public String getGoogleAccount() {
        return googleAccount;
    }
    public void setGoogleAccount(String googleAccount) {
        this.googleAccount = googleAccount;
    }
    public String getFacebookAccount() {
        return facebookAccount;
    }
    public void setFacebookAccount(String facebookAccount) {
        this.facebookAccount = facebookAccount;
    }
    public String getTwitterAccount() {
        return twitterAccount;
    }
    public void setTwitterAccount(String twitterAccount) {
        this.twitterAccount = twitterAccount;
    }
    public String getYoutubeChannel() {
        return youtubeChannel;
    }
    public void setYoutubeChannel(String youtubeChannel) {
        this.youtubeChannel = youtubeChannel;
    }
}
