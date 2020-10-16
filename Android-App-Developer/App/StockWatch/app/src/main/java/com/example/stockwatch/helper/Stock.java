package com.example.stockwatch.helper;

import java.io.Serializable;

public class Stock implements Serializable, Comparable<Stock> {
    private String company;
    private String symbol;
    private double currentPrice;
    private double todayPriceChange;
    private double todayPercentChange;

    public Stock(String company, String symbol, double currentPrice,
                 double todayPriceChange, double todayPercentChange) {
        this.company = company;
        this.symbol = symbol;
        this.currentPrice = currentPrice;
        this.todayPriceChange = todayPriceChange;
        this.todayPercentChange = todayPercentChange;
    }
    public String getCompany() {
        return company;
    }
    public void setCompany(String name) {
        this.company = name;
    }

    public String getSymbol() {
        return symbol;
    }
    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }
    public double getCurrentPrice() {
        return currentPrice;
    }
    public void setCurrentPrice(double currentPrice) {
        this.currentPrice = currentPrice;
    }
    public double getTodayPriceChange() {
        return todayPriceChange;
    }
    public void setTodayPriceChange(double todayPriceChange) {
        this.todayPriceChange = todayPriceChange;
    }
    public double getTodayPercentChange() {
        return todayPercentChange;
    }
    public void setTodayPercentChange(double todayPercentChange) {
        this.todayPercentChange = todayPercentChange;
    }

    @Override
    public int compareTo(Stock o) {
        return 0;
    }

    @Override
    public boolean


}
