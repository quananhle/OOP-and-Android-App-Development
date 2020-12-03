package com.quananhle.newsgateway.service;

import java.io.Serializable;

public class Article implements Serializable {
    private String title;
    private String author;
    private String publishingDate;
    private String url;
    private String imageUrl;
    private String description;
    //default constructor
    public Article() {
        //News Article Headline (can be null/blank)
        this.title          = "";
        this.author         = "";
        this.publishingDate = "";
        this.url            = "";
        this.imageUrl       = "";
        this.description    = "";
    }
    //non-default constructor
    public Article(String title, String author, String publishingDate,
                   String url, String imageUrl, String description) {
        this.title = title;
        this.author = author;
        this.publishingDate = publishingDate;
        this.url = url;
        this.imageUrl = imageUrl;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPublishingDate() {
        return publishingDate;
    }

    public void setPublishingDate(String publishingDate) {
        this.publishingDate = publishingDate;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}


