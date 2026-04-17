package com.codealpha.stocktrading.model;

import java.util.*;

/**
 * Watchlist for tracking stocks of interest without owning them.
 * Allows users to monitor stocks and set price alerts.
 * Author: Muhammad Amir | GitHub: codewith-amir
 */
public class Watchlist {

    private final List<String> watchedSymbols;
    private final Map<String, Double> priceAlerts; // symbol -> alert price

    public Watchlist() {
        this.watchedSymbols = new ArrayList<>();
        this.priceAlerts = new HashMap<>();
    }

    // ─── Watchlist Management ──────────────────────────────────────────────────
    public void addToWatchlist(String symbol) {
        if (!watchedSymbols.contains(symbol)) {
            watchedSymbols.add(symbol);
        }
    }

    public void removeFromWatchlist(String symbol) {
        watchedSymbols.remove(symbol);
        priceAlerts.remove(symbol);
    }

    public boolean isWatched(String symbol) {
        return watchedSymbols.contains(symbol);
    }

    // ─── Price Alerts ─────────────────────────────────────────────────────────
    public void setPriceAlert(String symbol, double alertPrice) {
        if (isWatched(symbol)) {
            priceAlerts.put(symbol, alertPrice);
        }
    }

    public void removePriceAlert(String symbol) {
        priceAlerts.remove(symbol);
    }

    public boolean checkAlerts(String symbol, double currentPrice) {
        if (priceAlerts.containsKey(symbol)) {
            return Math.abs(currentPrice - priceAlerts.get(symbol)) < 0.01;
        }
        return false;
    }

    // ─── Getters ──────────────────────────────────────────────────────────────
    public List<String> getWatchedSymbols()           { return new ArrayList<>(watchedSymbols); }
    public Map<String, Double> getPriceAlerts()      { return new HashMap<>(priceAlerts); }
    public int getWatchlistSize()                     { return watchedSymbols.size(); }
    public Double getPriceAlert(String symbol)       { return priceAlerts.get(symbol); }
}
