package com.codealpha.stocktrading.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents the trader — wallet balance, portfolio holdings, and trade history.
 */
public class User {

    private final String username;
    private double cashBalance;
    private final double startingBalance;
    private final Map<String, PortfolioHolding> portfolio;
    private final List<Transaction> transactionHistory;
    private final Watchlist watchlist;

    public static final double STARTING_CASH = 50_000.00;
    public User(String username) {
        this.username           = username;
        this.cashBalance        = STARTING_CASH;
        this.startingBalance    = STARTING_CASH;
        this.portfolio          = new HashMap<>();
        this.transactionHistory = new ArrayList<>();
        this.watchlist          = new Watchlist();
    }
    public void buyStock(Stock stock, int quantity) {
        double totalCost = stock.getCurrentPrice() * quantity;

        if (totalCost > cashBalance) {
            throw new IllegalStateException(
                String.format("Insufficient funds. Need $%.2f, have $%.2f", totalCost, cashBalance));
        }

        // Deduct from cash balance with precision handling
        cashBalance = Math.round((cashBalance - totalCost) * 100.0) / 100.0;

        String symbol = stock.getSymbol();
        if (portfolio.containsKey(symbol)) {
            portfolio.get(symbol).addShares(quantity, stock.getCurrentPrice());
        } else {
            portfolio.put(symbol,
                new PortfolioHolding(symbol, quantity, stock.getCurrentPrice()));
        }

        transactionHistory.add(new Transaction(
            Transaction.Type.BUY, symbol, quantity, stock.getCurrentPrice()));
    }
    public void sellStock(Stock stock, int quantity) {
        String symbol = stock.getSymbol();
        PortfolioHolding holding = portfolio.get(symbol);

        if (holding == null || holding.getQuantity() < quantity) {
            throw new IllegalStateException(
                String.format("You only own %d share(s) of %s.",
                    holding == null ? 0 : holding.getQuantity(), symbol));
        }

        // Add proceeds to cash balance
        double saleProceeds = stock.getCurrentPrice() * quantity;
        cashBalance = Math.round((cashBalance + saleProceeds) * 100.0) / 100.0;
        
        // Remove shares from portfolio
        boolean fullyLiquidated = holding.removeShares(quantity);
        if (fullyLiquidated) {
            portfolio.remove(symbol);
        }

        // Record transaction
        transactionHistory.add(new Transaction(
            Transaction.Type.SELL, symbol, quantity, stock.getCurrentPrice()));
    }
    public double getPortfolioValue(Map<String, Stock> marketData) {
        double total = 0;
        for (PortfolioHolding h : portfolio.values()) {
            Stock s = marketData.get(h.getSymbol());
            if (s != null) total += h.getCurrentValue(s.getCurrentPrice());
        }
        return total;
    }

    public double getTotalNetWorth(Map<String, Stock> marketData) {
        return cashBalance + getPortfolioValue(marketData);
    }

    public double getOverallPnL(Map<String, Stock> marketData) {
        return getTotalNetWorth(marketData) - startingBalance;
    }

    public double getOverallPnLPercent(Map<String, Stock> marketData) {
        return (getOverallPnL(marketData) / startingBalance) * 100;
    }

    public PortfolioHolding getHolding(String symbol) { return portfolio.get(symbol); }
    public boolean hasHolding(String symbol)           { return portfolio.containsKey(symbol); }
    public Watchlist getWatchlist()                    { return watchlist; }
    public String                          getUsername()           { return username; }
    public double                          getCashBalance()        { return cashBalance; }
    public double                          getStartingBalance()    { return startingBalance; }
    public Map<String, PortfolioHolding>   getPortfolio()         { return portfolio; }
    public List<Transaction>               getTransactionHistory() { return transactionHistory; }
}
