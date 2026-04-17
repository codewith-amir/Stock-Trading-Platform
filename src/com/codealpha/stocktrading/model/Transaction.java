package com.codealpha.stocktrading.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents a single BUY or SELL transaction.
 * Author: Muhammad Amir | GitHub: codewith-amir
 */
public class Transaction {

    public enum Type { BUY, SELL }

    private final Type   type;
    private final String symbol;
    private final int    quantity;
    private final double pricePerShare;
    private final double totalValue;
    private final LocalDateTime timestamp;

    private static final DateTimeFormatter FORMATTER =
        DateTimeFormatter.ofPattern("dd-MMM-yyyy  HH:mm:ss");

    public Transaction(Type type, String symbol, int quantity, double pricePerShare) {
        this.type           = type;
        this.symbol         = symbol;
        this.quantity       = quantity;
        this.pricePerShare  = pricePerShare;
        this.totalValue     = quantity * pricePerShare;
        this.timestamp      = LocalDateTime.now();
    }

    // ─── Getters ───────────────────────────────────────────────────────────────
    public Type   getType()          { return type; }
    public String getSymbol()        { return symbol; }
    public int    getQuantity()      { return quantity; }
    public double getPricePerShare() { return pricePerShare; }
    public double getTotalValue()    { return totalValue; }
    public String getFormattedTime() { return timestamp.format(FORMATTER); }
}
