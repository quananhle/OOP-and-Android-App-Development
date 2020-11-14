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

}
