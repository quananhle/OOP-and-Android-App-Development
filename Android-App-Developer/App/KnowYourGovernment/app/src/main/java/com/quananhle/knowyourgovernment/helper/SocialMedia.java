package com.quananhle.knowyourgovernment.helper;

import java.io.Serializable;

public class SocialMedia implements Serializable {
    private String channel;
    private String account;

    public SocialMedia (String type, String account) {
        this.channel = type;
        this.account = account;
    }
    public String getType() {
        return channel;
    }
    public String getAccount() {
        return account;
    }
}
