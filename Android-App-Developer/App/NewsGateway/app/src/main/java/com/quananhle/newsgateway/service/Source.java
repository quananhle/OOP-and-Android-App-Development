package com.quananhle.newsgateway.service;

public class Source {
    private String id;
    private String company;
    private String category;
    private String ranking;
    private Boolean isGovernmentFunded = true;
    //default constructor
    public Source() {
        id                 = null;
        company            = null;
        category           = null;
        ranking            = null;
        isGovernmentFunded = true;
    }
    //non-default constructor
    public Source(String id, String company, String category, String ranking, Boolean isGovernmentFunded) {
        this.id = id;
        this.company = company;
        this.category = category;
        this.ranking = ranking;
        this.isGovernmentFunded = isGovernmentFunded;
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

    public String getRanking() {
        return ranking;
    }

    public void setRanking(String ranking) {
        this.ranking = ranking;
    }

    public Boolean getGovernmentFunded() {
        return isGovernmentFunded;
    }

    public void setGovernmentFunded(Boolean governmentFunded) {
        isGovernmentFunded = governmentFunded;
    }

}
