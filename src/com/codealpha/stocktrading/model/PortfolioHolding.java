package com.codealpha.stocktrading.model;

/**
 * Tracks how many shares of a stock the user owns + their average buy price.
 * Author: Muhammad Amir | GitHub: codewith-amir
 */
public class PortfolioHolding {

    private final String symbol;
    private int    quantity;
    private double totalCost;   // cumulative cost for average calculation

    public PortfolioHolding(String symbol, int quantity, double pricePerShare) {
        this.symbol    = symbol;
        this.quantity  = quantity;
        this.totalCost = quantity * pricePerShare;
    }

    // ─── Mutators ──────────────────────────────────────────────────────────────
    public void addShares(int qty, double price) {
        totalCost += qty * price;
        // Round to preserve floating-point precision
        totalCost = Math.round(totalCost * 100.0) / 100.0;
        quantity  += qty;
    }

    /**
     * Remove shares on SELL. Returns true if fully liquidated.
     * Maintains floating-point precision by adjusting cost proportionally.
     */
    public boolean removeShares(int qty) {
        if (qty > quantity) throw new IllegalArgumentException("Cannot sell more than you own.");
        
        // Preserve floating-point precision: subtract proportional cost
        double costPerShare = totalCost / quantity;
        totalCost -= qty * costPerShare;
        // Round to avoid accumulating floating-point errors
        totalCost = Math.round(totalCost * 100.0) / 100.0;
        
        quantity -= qty;
        return quantity == 0;
    }

    // ─── Computed ─────────────────────────────────────────────────────────────
    public double getAverageCost()              { return quantity > 0 ? totalCost / quantity : 0; }
    public double getCurrentValue(double price) { return quantity * price; }
    public double getUnrealizedPnL(double price){ return getCurrentValue(price) - totalCost; }
    public double getPnLPercent(double price)   { return totalCost > 0 ? (getUnrealizedPnL(price) / totalCost) * 100 : 0; }

    // ─── Getters ──────────────────────────────────────────────────────────────
    public String getSymbol()   { return symbol; }
    public int    getQuantity() { return quantity; }
    public double getTotalCost(){ return totalCost; }
}
