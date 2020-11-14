package com.quananhle.newsgateway.service;

import java.io.Serializable;

public class Article implements Serializable {
    private String headline;
    private String author;
    private String publishingDate;
    private String url;
    private String imageUrl;
    private String content;
    //default constructor
    public Article() {
        //News Article Headline (can be null/blank)
        this.headline       = "";
        this.author         = "";
        this.publishingDate = "";
        this.url            = "";
        this.imageUrl       = "";
        this.content        = "";
    }
    //non-default constructor
    public Article(String headline, String author, String publishingDate, String url, String imageUrl, String content) {
        this.headline = headline;
        this.author = author;
        this.publishingDate = publishingDate;
        this.url = url;
        this.imageUrl = imageUrl;
        this.content = content;
    }

    public String getHeadline() {
        return headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
