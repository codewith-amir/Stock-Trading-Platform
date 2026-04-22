package com.codealpha.stocktrading.service;

import com.codealpha.stocktrading.model.Stock;
import java.util.*;

/**
 * Market Intelligence and Stock Recommendation Engine.
 * Provides technical analysis, price trends, and smart recommendations.
 */
public class MarketIntelligence {

    /**
     * Analyze price trend: UPTREND, DOWNTREND, SIDEWAYS
     */
    public static String analyzePriceTrend(Stock stock) {
        List<Double> history = stock.getPriceHistory();
        if (history.size() < 3) return "INSUFFICIENT_DATA";

        double oldAvg = history.subList(0, Math.min(3, history.size() / 2)).stream()
            .mapToDouble(Double::doubleValue).average().orElse(0);
        double newAvg = history.subList(Math.max(0, history.size() - 3), history.size()).stream()
            .mapToDouble(Double::doubleValue).average().orElse(0);

        double change = ((newAvg - oldAvg) / oldAvg) * 100;

        if (change > 1) return "UPTREND";
        if (change < -1) return "DOWNTREND";
        return "SIDEWAYS";
    }

    /**
     * Calculate Relative Strength Index (RSI) approximation (0-100)
     */
    public static double calculateRSI(Stock stock) {
        List<Double> prices = stock.getPriceHistory();
        if (prices.size() < 2) return 50;

        double gains = 0, losses = 0;
        for (int i = 1; i < prices.size(); i++) {
            double change = prices.get(i) - prices.get(i - 1);
            if (change > 0) gains += change;
            else losses += Math.abs(change);
        }

        double avgGain = gains / (prices.size() - 1);
        double avgLoss = losses / (prices.size() - 1);

        if (avgLoss == 0) return 100;
        double rs = avgGain / avgLoss;
        return 100 - (100 / (1 + rs));
    }

    /**
     * Get stock recommendation: BUY, HOLD, SELL
     */
    public static String getStockRecommendation(Stock stock) {
        double rsi = calculateRSI(stock);
        String trend = analyzePriceTrend(stock);
        double changePercent = stock.getPriceChangePercent();

        int signals = 0;

        // RSI signals
        if (rsi < 30) signals += 2; // Oversold = BUY
        else if (rsi < 40) signals += 1;
        else if (rsi > 70) signals -= 2; // Overbought = SELL
        else if (rsi > 60) signals -= 1;

        // Trend signals
        if ("UPTREND".equals(trend)) signals += 1;
        if ("DOWNTREND".equals(trend)) signals -= 1;

        // Price change signals
        if (changePercent > 2) signals += 1;
        if (changePercent < -2) signals -= 1;

        if (signals >= 2) return "BUY ▲";
        if (signals <= -2) return "SELL ▼";
        return "HOLD →";
    }

    /**
     * Calculate price volatility (standard deviation of daily changes)
     */
    public static double calculateVolatility(Stock stock) {
        List<Double> prices = stock.getPriceHistory();
        if (prices.size() < 2) return 0;

        double mean = prices.stream().mapToDouble(Double::doubleValue).average().orElse(0);
        double variance = prices.stream()
            .mapToDouble(p -> Math.pow(p - mean, 2))
            .average().orElse(0);

        return Math.sqrt(variance);
    }

    /**
     * Find stocks within a sector that match recommendation criteria
     */
    public static List<Stock> findBuyOpportunities(MarketService market, String sector) {
        List<Stock> opportunities = new ArrayList<>();
        for (Stock s : market.getStocksBySector(sector)) {
            String rec = getStockRecommendation(s);
            if (rec.contains("BUY")) {
                opportunities.add(s);
            }
        }
        opportunities.sort((a, b) -> Double.compare(calculateRSI(b), calculateRSI(a)));
        return opportunities;
    }

    /**
     * Calculate support and resistance levels (simplified)
     */
    public static Map<String, Double> calculateSupportResistance(Stock stock) {
        List<Double> prices = stock.getPriceHistory();
        Map<String, Double> levels = new HashMap<>();

        if (prices.isEmpty()) return levels;

        double high = prices.stream().mapToDouble(Double::doubleValue).max().orElse(0);
        double low = prices.stream().mapToDouble(Double::doubleValue).min().orElse(0);
        double mid = (high + low) / 2;

        levels.put("RESISTANCE", high);
        levels.put("PIVOT", mid);
        levels.put("SUPPORT", low);

        return levels;
    }
}
