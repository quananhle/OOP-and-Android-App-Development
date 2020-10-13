package com.example.stockwatch;

import java.io.Serializable;

public class Stock implements Serializable {
    private String name;
    private String symbol;
    private double currentPrice;
    private double todayPriceChange;
    private double todayPercentChange;

    public Stock(String name, String symbol, double currentPrice,
                 double todayPriceChange, double todayPercentChange) {
        this.name = name;
        this.symbol = symbol;
        this.currentPrice = currentPrice;
        this.todayPriceChange = todayPriceChange;
        this.todayPercentChange = todayPercentChange;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
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


}
