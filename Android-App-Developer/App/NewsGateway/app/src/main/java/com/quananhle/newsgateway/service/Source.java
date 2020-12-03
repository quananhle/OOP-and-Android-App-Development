package com.quananhle.newsgateway.service;
import androidx.annotation.NonNull;
import java.io.Serializable;

public class Source implements Serializable{
    private String id;
    private String company;
    private String category;
    //default constructor
    public Source() {
        id                 = "";
        company            = "";
        category           = "";
    }
    //non-default constructor
    public Source(String id, String company, String category) {
        this.id = id;
        this.company = company;
        this.category = category;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
    @NonNull
    public String toString() {
        return company;
    }
}

