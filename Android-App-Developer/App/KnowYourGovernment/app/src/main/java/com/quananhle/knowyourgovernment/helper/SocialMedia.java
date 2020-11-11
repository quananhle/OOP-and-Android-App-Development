package com.quananhle.knowyourgovernment.helper;

import java.io.Serializable;

public class SocialMedia implements Serializable {
    private String facebookAccount;
    private String twitterAccount;
    private String youtubeChannel;
    private static final String DEFAULT_DISPLAY = "DATA NOT FOUND";
    //default constructor
    public SocialMedia() {
        this.facebookAccount = DEFAULT_DISPLAY;
        this.twitterAccount = DEFAULT_DISPLAY;
        this.youtubeChannel = DEFAULT_DISPLAY;
    }
    //non-default constructor
    public SocialMedia(String facebookAccount, String twitterAccount, String youtubeChannel) {
        this.facebookAccount = facebookAccount;
        this.twitterAccount = twitterAccount;
        this.youtubeChannel = youtubeChannel;
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
    public String getYouTubeChannel() {
        return youtubeChannel;
    }
    public void setYouTubeChannel(String youtubeChannel) {
        this.youtubeChannel = youtubeChannel;
    }
}
