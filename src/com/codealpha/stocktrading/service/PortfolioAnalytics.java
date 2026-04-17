package com.codealpha.stocktrading.service;

import com.codealpha.stocktrading.model.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Advanced portfolio analytics and performance metrics.
 * Provides insights into portfolio composition, sector allocation, risk metrics, etc.
 * Author: Muhammad Amir | GitHub: codewith-amir
 */
public class PortfolioAnalytics {

    /**
     * Calculate Sharpe Ratio approximation based on unrealized P&L volatility
     */
    public static double calculatePortfolioSharpeRatio(User user, MarketService market) {
        Map<String, PortfolioHolding> portfolio = user.getPortfolio();
        if (portfolio.isEmpty()) return 0;

        double totalValue = user.getPortfolioValue(market.getAllStocks());
        if (totalValue == 0) return 0;

        double variance = 0;
        for (PortfolioHolding h : portfolio.values()) {
            Stock s = market.getStock(h.getSymbol());
            if (s != null) {
                double pnlPercent = h.getPnLPercent(s.getCurrentPrice());
                variance += pnlPercent * pnlPercent;
            }
        }

        double stdDev = Math.sqrt(variance / portfolio.size());
        double riskFreeRate = 2.0; // Assumed 2% risk-free rate
        double avgReturn = user.getOverallPnLPercent(market.getAllStocks());

        return stdDev > 0 ? (avgReturn - riskFreeRate) / stdDev : 0;
    }

    /**
     * Get sector allocation breakdown
     */
    public static Map<String, Double> getSectorAllocation(User user, MarketService market) {
        Map<String, Double> sectorAllocation = new HashMap<>();
        double totalPortfolioValue = user.getPortfolioValue(market.getAllStocks());

        for (PortfolioHolding h : user.getPortfolio().values()) {
            Stock s = market.getStock(h.getSymbol());
            if (s != null) {
                double holdingValue = h.getCurrentValue(s.getCurrentPrice());
                String sector = s.getSector();
                sectorAllocation.put(sector,
                    sectorAllocation.getOrDefault(sector, 0.0) + holdingValue);
            }
        }

        // Convert to percentages
        if (totalPortfolioValue > 0) {
            sectorAllocation.replaceAll((k, v) -> (v / totalPortfolioValue) * 100);
        }

        return sectorAllocation;
    }

    /**
     * Get top holdings by value
     */
    public static List<PortfolioHolding> getTopHoldingsByValue(User user, MarketService market, int topN) {
        return user.getPortfolio().values().stream()
            .sorted((h1, h2) -> {
                Stock s1 = market.getStock(h1.getSymbol());
                Stock s2 = market.getStock(h2.getSymbol());
                double v1 = s1 != null ? h1.getCurrentValue(s1.getCurrentPrice()) : 0;
                double v2 = s2 != null ? h2.getCurrentValue(s2.getCurrentPrice()) : 0;
                return Double.compare(v2, v1);
            })
            .limit(topN)
            .collect(Collectors.toList());
    }

    /**
     * Identify underperforming holdings (negative P&L)
     */
    public static List<PortfolioHolding> getUnderperformingHoldings(User user, MarketService market) {
        return user.getPortfolio().values().stream()
            .filter(h -> {
                Stock s = market.getStock(h.getSymbol());
                return s != null && h.getUnrealizedPnL(s.getCurrentPrice()) < 0;
            })
            .sorted((h1, h2) -> {
                Stock s1 = market.getStock(h1.getSymbol());
                Stock s2 = market.getStock(h2.getSymbol());
                double pnl1 = s1 != null ? h1.getUnrealizedPnL(s1.getCurrentPrice()) : 0;
                double pnl2 = s2 != null ? h2.getUnrealizedPnL(s2.getCurrentPrice()) : 0;
                return Double.compare(pnl1, pnl2);
            })
            .collect(Collectors.toList());
    }

    /**
     * Get diversification score (0-100). Higher = more diversified
     */
    public static double getDiversificationScore(User user, MarketService market) {
        Map<String, Double> sectorAlloc = getSectorAllocation(user, market);
        if (sectorAlloc.isEmpty()) return 0;

        // Calculate Herfindahl-Hirschman Index (HHI)
        double hhi = sectorAlloc.values().stream()
            .mapToDouble(v -> (v / 100) * (v / 100))
            .sum();

        // Convert HHI (0-1) to diversification score (0-100)
        // HHI closer to 1 = less diversified; closer to 0 = more diversified
        return (1 - hhi) * 100;
    }

    /**
     * Calculate portfolio concentration - percentage held by top 3 positions
     */
    public static double getPortfolioConcentration(User user, MarketService market) {
        List<PortfolioHolding> topHoldings = getTopHoldingsByValue(user, market, 3);
        double totalValue = user.getPortfolioValue(market.getAllStocks());

        if (totalValue == 0) return 0;

        double topValue = topHoldings.stream()
            .mapToDouble(h -> {
                Stock s = market.getStock(h.getSymbol());
                return s != null ? h.getCurrentValue(s.getCurrentPrice()) : 0;
            })
            .sum();

        return (topValue / totalValue) * 100;
    }

    /**
     * Get recommendation: BUY, HOLD, or SELL based on portfolio metrics
     */
    public static String getPortfolioRecommendation(User user, MarketService market) {
        double pnl = user.getOverallPnL(market.getAllStocks());
        double netWorth = user.getTotalNetWorth(market.getAllStocks());
        double concentration = getPortfolioConcentration(user, market);
        List<PortfolioHolding> underperforming = getUnderperformingHoldings(user, market);

        int signals = 0;

        // Signal analysis
        if (concentration > 70) signals--; // Concentrated = risky
        if (concentration < 40) signals++; // Well-diversified = good
        if (pnl > 0) signals++; // Positive returns
        if (underperforming.size() > user.getPortfolio().size() * 0.5) signals--; // Many losers

        if (signals >= 2) return "BUY - Strong upside potential";
        if (signals <= -2) return "SELL - High risk, rebalance";
        return "HOLD - Maintain current strategy";
    }
}
