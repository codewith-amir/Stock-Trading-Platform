package com.codealpha.stocktrading.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Represents a publicly traded stock with real-time simulated price fluctuation.
 * Author: Muhammad Amir | GitHub: codewith-amir
 * CodeAlpha Java Internship — Task 2: Stock Trading Platform
 */
public class Stock {

    private final String symbol;
    private final String companyName;
    private final String sector;
    private double currentPrice;
    private double openPrice;
    private double previousClose;
    private double dayHigh;
    private double dayLow;
    private long volume;
    private final List<Double> priceHistory;

    private static final Random random = new Random();

    // ─── Constructor ───────────────────────────────────────────────────────────
    public Stock(String symbol, String companyName, String sector, double initialPrice) {
        this.symbol        = symbol;
        this.companyName   = companyName;
        this.sector        = sector;
        this.currentPrice  = initialPrice;
        this.openPrice     = initialPrice;
        this.previousClose = initialPrice * (0.97 + random.nextDouble() * 0.06);
        this.dayHigh       = initialPrice;
        this.dayLow        = initialPrice;
        this.volume        = 0;
        this.priceHistory  = new ArrayList<>();
        this.priceHistory.add(initialPrice);
    }

    // ─── Price Simulation ──────────────────────────────────────────────────────
    /**
     * Simulates realistic market price fluctuation (±3% per tick).
     */
    public void simulatePriceChange() {
        double changePercent = (random.nextDouble() * 6.0) - 3.0;  // –3% to +3%
        double change = currentPrice * (changePercent / 100.0);
        currentPrice = Math.max(0.01, currentPrice + change);
        currentPrice = Math.round(currentPrice * 100.0) / 100.0;

        // Update day high/low
        if (currentPrice > dayHigh) dayHigh = currentPrice;
        if (currentPrice < dayLow)  dayLow  = currentPrice;

        // Add simulated volume
        volume += (long)(random.nextInt(10000) + 1000);

        // Keep last 10 prices for history
        priceHistory.add(currentPrice);
        if (priceHistory.size() > 10) priceHistory.remove(0);
    }

    // ─── Computed Properties ───────────────────────────────────────────────────
    public double getPriceChange()        { return currentPrice - previousClose; }
    public double getPriceChangePercent() { return (getPriceChange() / previousClose) * 100; }
    public boolean isPositive()           { return getPriceChange() >= 0; }

    // ─── Getters & Setters ─────────────────────────────────────────────────────
    public String getSymbol()        { return symbol; }
    public String getCompanyName()   { return companyName; }
    public String getSector()        { return sector; }
    public double getCurrentPrice()  { return currentPrice; }
    public double getOpenPrice()     { return openPrice; }
    public double getPreviousClose() { return previousClose; }
    public double getDayHigh()       { return dayHigh; }
    public double getDayLow()        { return dayLow; }
    public long getVolume()          { return volume; }
    public List<Double> getPriceHistory() { return new ArrayList<>(priceHistory); }

    public void setCurrentPrice(double price) {
        this.currentPrice = price;
        if (price > dayHigh) dayHigh = price;
        if (price < dayLow)  dayLow  = price;
    }
}
