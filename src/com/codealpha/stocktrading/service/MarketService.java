package com.codealpha.stocktrading.service;

import com.codealpha.stocktrading.model.Stock;

import java.util.*;

/**
 * Manages the stock market — holds all listed stocks and refreshes prices.
 * Author: Muhammad Amir | GitHub: codewith-amir
 */
public class MarketService {

    // symbol → Stock
    private final Map<String, Stock> stocks = new LinkedHashMap<>();

    // ─── Constructor — seed with 12 realistic stocks ───────────────────────────
    public MarketService() {
        // Tech
        addStock("AAPL", "Apple Inc.",          "Technology",    189.50);
        addStock("MSFT", "Microsoft Corp.",      "Technology",    415.30);
        addStock("GOOGL","Alphabet Inc.",        "Technology",    175.20);
        addStock("NVDA", "NVIDIA Corporation",   "Technology",    875.00);
        addStock("META", "Meta Platforms Inc.",  "Technology",    520.40);

        // Finance
        addStock("JPM",  "JPMorgan Chase",       "Finance",       198.70);
        addStock("BAC",  "Bank of America",      "Finance",        38.90);

        // EV / Automotive
        addStock("TSLA", "Tesla Inc.",           "EV/Auto",       245.60);

        // Healthcare
        addStock("JNJ",  "Johnson & Johnson",    "Healthcare",    155.80);
        addStock("PFE",  "Pfizer Inc.",          "Healthcare",     27.40);

        // E-Commerce
        addStock("AMZN", "Amazon.com Inc.",      "E-Commerce",    185.30);

        // Telecom
        addStock("T",    "AT&T Inc.",            "Telecom",        17.20);
    }

    private void addStock(String symbol, String name, String sector, double price) {
        stocks.put(symbol, new Stock(symbol, name, sector, price));
    }

    // ─── Market Refresh ────────────────────────────────────────────────────────
    /** Simulates one market tick — all prices fluctuate. */
    public void refreshMarket() {
        for (Stock s : stocks.values()) s.simulatePriceChange();
    }

    // ─── Lookup ────────────────────────────────────────────────────────────────
    public Stock getStock(String symbol) {
        return stocks.get(symbol.toUpperCase());
    }

    public boolean exists(String symbol) {
        return stocks.containsKey(symbol.toUpperCase());
    }

    public Map<String, Stock> getAllStocks() { return stocks; }

    public Collection<Stock> getStocksBySector(String sector) {
        List<Stock> result = new ArrayList<>();
        for (Stock s : stocks.values()) {
            if (s.getSector().equalsIgnoreCase(sector)) result.add(s);
        }
        return result;
    }

    public List<Stock> getTopGainers(int n) {
        List<Stock> sorted = new ArrayList<>(stocks.values());
        sorted.sort((a, b) -> Double.compare(b.getPriceChangePercent(), a.getPriceChangePercent()));
        return sorted.subList(0, Math.min(n, sorted.size()));
    }

    public List<Stock> getTopLosers(int n) {
        List<Stock> sorted = new ArrayList<>(stocks.values());
        sorted.sort(Comparator.comparingDouble(Stock::getPriceChangePercent));
        return sorted.subList(0, Math.min(n, sorted.size()));
    }
}
