package com.codealpha.stocktrading.util;

import com.codealpha.stocktrading.model.*;
import com.codealpha.stocktrading.service.MarketService;

import java.util.List;
import java.util.Map;

/**
 * Console UI / display helper for the Stock Trading Platform.
 * Author: Muhammad Amir | GitHub: codewith-amir
 */
public class DisplayHelper {

    // ─── ANSI Colors ───────────────────────────────────────────────────────────
    public static final String RESET   = "\u001B[0m";
    public static final String BOLD    = "\u001B[1m";
    public static final String GREEN   = "\u001B[32m";
    public static final String RED     = "\u001B[31m";
    public static final String YELLOW  = "\u001B[33m";
    public static final String CYAN    = "\u001B[36m";
    public static final String BLUE    = "\u001B[34m";
    public static final String MAGENTA = "\u001B[35m";
    public static final String WHITE   = "\u001B[97m";
    public static final String DIM     = "\u001B[2m";

    private static final int W = 72;

    // ─── Banner ────────────────────────────────────────────────────────────────
    public static void printBanner() {
        line('=');
        System.out.println(BOLD + CYAN +
            "         STOCK TRADING PLATFORM  ─  CodeAlpha Internship" + RESET);
        System.out.println(CYAN +
            "               Java Developer Internship  |  Task 2" + RESET);
        System.out.println(WHITE +
            "          Author: Muhammad Amir  |  GitHub: codewith-amir" + RESET);
        line('=');
    }

    // ─── Main Menu ─────────────────────────────────────────────────────────────
    public static void printMenu(User user, MarketService market) {
        System.out.println();
        line('-');
        System.out.printf(BOLD + "  MAIN MENU" + RESET +
            "  ·  Cash: " + CYAN + BOLD + "$%-12.2f" + RESET +
            "  Net Worth: " + netWorthColor(user, market) + BOLD + "$%.2f" + RESET + "%n",
            user.getCashBalance(),
            user.getTotalNetWorth(market.getAllStocks()));
        line('-');
        System.out.println("  " + CYAN + "1." + RESET + "  Market Overview  (all stocks)");
        System.out.println("  " + CYAN + "2." + RESET + "  Stock Detail     (search by symbol)");
        System.out.println("  " + CYAN + "3." + RESET + "  Buy Shares");
        System.out.println("  " + CYAN + "4." + RESET + "  Sell Shares");
        System.out.println("  " + CYAN + "5." + RESET + "  My Portfolio");
        System.out.println("  " + CYAN + "6." + RESET + "  Transaction History");
        System.out.println("  " + CYAN + "7." + RESET + "  Portfolio Analytics");
        System.out.println("  " + CYAN + "8." + RESET + "  Stock Intelligence");
        System.out.println("  " + CYAN + "9." + RESET + "  Watchlist Manager");
        System.out.println("  " + CYAN + "10." + RESET + " Refresh Market Prices");
        System.out.println("  " + CYAN + "11." + RESET + " Save Data to File");
        System.out.println("  " + RED  + "0." + RESET + "  Exit");
        line('-');
        System.out.print(BOLD + "  Enter choice: " + RESET);
    }

    // ─── Market Overview ───────────────────────────────────────────────────────
    public static void printMarketOverview(MarketService market) {
        System.out.println();
        line('=');
        System.out.println(BOLD + "  LIVE MARKET  ─  " + market.getAllStocks().size() + " Stocks" + RESET);
        line('-');
        System.out.printf(BOLD + "  %-6s  %-22s  %-12s  %-10s  %-10s  %s%n" + RESET,
            "SYMBOL", "COMPANY", "PRICE", "CHANGE", "CHANGE%", "SECTOR");
        line('-');

        for (Stock s : market.getAllStocks().values()) {
            String color = s.isPositive() ? GREEN : RED;
            String arrow = s.isPositive() ? "▲" : "▼";
            System.out.printf("  " + BOLD + "%-6s" + RESET + "  %-22s  $%-11.2f  " +
                color + "%s%-9.2f  %-9.2f%%" + RESET + "  " + DIM + "%s" + RESET + "%n",
                s.getSymbol(),
                truncate(s.getCompanyName(), 22),
                s.getCurrentPrice(),
                arrow,
                Math.abs(s.getPriceChange()),
                s.getPriceChangePercent(),
                s.getSector());
        }
        line('=');
        printGainersLosers(market);
    }

    private static void printGainersLosers(MarketService market) {
        List<Stock> gainers = market.getTopGainers(3);
        List<Stock> losers  = market.getTopLosers(3);

        System.out.println();
        System.out.print(GREEN + BOLD + "  TOP GAINERS: " + RESET);
        for (Stock s : gainers)
            System.out.printf(GREEN + "%s (+%.2f%%)  " + RESET, s.getSymbol(), s.getPriceChangePercent());

        System.out.println();
        System.out.print(RED + BOLD + "  TOP LOSERS:  " + RESET);
        for (Stock s : losers)
            System.out.printf(RED + "%s (%.2f%%)  " + RESET, s.getSymbol(), s.getPriceChangePercent());
        System.out.println();
        line('=');
    }

    // ─── Stock Detail ──────────────────────────────────────────────────────────
    public static void printStockDetail(Stock s, User user) {
        System.out.println();
        line('=');
        String color = s.isPositive() ? GREEN : RED;
        String arrow = s.isPositive() ? "▲" : "▼";

        System.out.printf(BOLD + "  %s  ─  %s%n" + RESET, s.getSymbol(), s.getCompanyName());
        System.out.printf(DIM + "  Sector: %s%n" + RESET, s.getSector());
        line('-');
        System.out.printf("  %-18s : " + BOLD + color + "$%.2f  %s %.2f  (%.2f%%)%n" + RESET,
            "Current Price", s.getCurrentPrice(), arrow, Math.abs(s.getPriceChange()), s.getPriceChangePercent());
        System.out.printf("  %-18s : $%.2f%n", "Previous Close", s.getPreviousClose());
        System.out.printf("  %-18s : $%.2f%n", "Open",           s.getOpenPrice());
        System.out.printf("  %-18s : $%.2f%n", "Day High",       s.getDayHigh());
        System.out.printf("  %-18s : $%.2f%n", "Day Low",        s.getDayLow());
        System.out.printf("  %-18s : %,d%n",   "Volume",         s.getVolume());

        // Mini price chart
        line('-');
        System.out.println(BOLD + "  Price History (recent ticks):" + RESET);
        System.out.print("  ");
        for (double p : s.getPriceHistory()) System.out.printf("$%.2f  ", p);
        System.out.println();

        // User's holding
        PortfolioHolding h = user.getHolding(s.getSymbol());
        if (h != null) {
            line('-');
            System.out.printf(BOLD + "  YOUR POSITION:%n" + RESET);
            System.out.printf("  %-18s : %d shares%n",  "Owned",       h.getQuantity());
            System.out.printf("  %-18s : $%.2f%n",       "Avg Cost",    h.getAverageCost());
            System.out.printf("  %-18s : $%.2f%n",       "Market Value",h.getCurrentValue(s.getCurrentPrice()));
            double pnl = h.getUnrealizedPnL(s.getCurrentPrice());
            String pnlColor = pnl >= 0 ? GREEN : RED;
            System.out.printf("  %-18s : %s%s$%.2f  (%.2f%%)%s%n",
                "Unrealized P&L", BOLD, pnlColor, pnl, h.getPnLPercent(s.getCurrentPrice()), RESET);
        }
        line('=');
    }

    // ─── Portfolio ─────────────────────────────────────────────────────────────
    public static void printPortfolio(User user, MarketService market) {
        System.out.println();
        line('=');
        System.out.println(BOLD + "  MY PORTFOLIO  ─  " + user.getUsername() + RESET);
        line('-');

        if (user.getPortfolio().isEmpty()) {
            System.out.println(YELLOW + "  No holdings yet. Buy some stocks!" + RESET);
        } else {
            System.out.printf(BOLD + "  %-6s  %-6s  %-10s  %-12s  %-12s  %-12s  %s%n" + RESET,
                "SYMBOL", "QTY", "AVG COST", "CURR PRICE", "MARKET VAL", "UNREAL P&L", "P&L%");
            line('-');

            double totalValue = 0;
            double totalCost  = 0;
            for (PortfolioHolding h : user.getPortfolio().values()) {
                Stock s = market.getStock(h.getSymbol());
                if (s == null) continue;
                double price = s.getCurrentPrice();
                double pnl   = h.getUnrealizedPnL(price);
                double pnlPct= h.getPnLPercent(price);
                String pColor= pnl >= 0 ? GREEN : RED;
                String arrow = pnl >= 0 ? "▲" : "▼";
                totalValue += h.getCurrentValue(price);
                totalCost  += h.getTotalCost();

                System.out.printf("  " + BOLD + "%-6s" + RESET + "  %-6d  $%-9.2f  $%-11.2f  $%-11.2f  %s%s%-6.2f%s  %s%.2f%%%s%n",
                    h.getSymbol(), h.getQuantity(), h.getAverageCost(), price,
                    h.getCurrentValue(price), pColor + BOLD, arrow, Math.abs(pnl), RESET,
                    pColor, pnlPct, RESET);
            }
            line('-');
            double totalPnL    = totalValue - totalCost;
            String totalColor  = totalPnL >= 0 ? GREEN : RED;
            System.out.printf("  " + BOLD + "%-6s  %-6s  %-10s  %-12s  $%-11.2f  %s$%-11.2f%s%n" + RESET,
                "TOTAL", "", "", "", totalValue, totalColor + BOLD, totalPnL, RESET);
        }

        line('-');
        double netWorth = user.getTotalNetWorth(market.getAllStocks());
        double overallPnL = user.getOverallPnL(market.getAllStocks());
        String nwColor = overallPnL >= 0 ? GREEN : RED;

        System.out.printf("  %-22s : " + CYAN + BOLD + "$%.2f%n" + RESET, "Cash Balance",    user.getCashBalance());
        System.out.printf("  %-22s : " + CYAN + BOLD + "$%.2f%n" + RESET, "Total Net Worth",  netWorth);
        System.out.printf("  %-22s : %s%s$%.2f  (%.2f%%)%s%n",            "Overall P&L",
            BOLD, nwColor, overallPnL, user.getOverallPnLPercent(market.getAllStocks()), RESET);
        System.out.printf("  %-22s : $%.2f%n", "Starting Balance", user.getStartingBalance());
        line('=');
    }

    // ─── Transaction History ───────────────────────────────────────────────────
    public static void printTransactionHistory(User user) {
        System.out.println();
        line('=');
        System.out.println(BOLD + "  TRANSACTION HISTORY  ─  " + user.getUsername() + RESET);
        line('-');
        var history = user.getTransactionHistory();

        if (history.isEmpty()) {
            System.out.println(YELLOW + "  No transactions yet." + RESET);
        } else {
            System.out.printf(BOLD + "  %-5s  %-4s  %-7s  %-4s  %-10s  %-12s  %s%n" + RESET,
                "No.", "TYPE", "SYMBOL", "QTY", "PRICE", "TOTAL", "TIMESTAMP");
            line('-');
            int i = 1;
            for (Transaction t : history) {
                String c = t.getType() == Transaction.Type.BUY ? GREEN : RED;
                System.out.printf("  %-5d  %s%-4s%s  %-7s  %-4d  $%-9.2f  $%-11.2f  %s%n",
                    i++, BOLD + c, t.getType(), RESET,
                    t.getSymbol(), t.getQuantity(),
                    t.getPricePerShare(), t.getTotalValue(),
                    t.getFormattedTime());
            }
        }
        line('=');
    }

    // ─── Utilities ─────────────────────────────────────────────────────────────
    public static void line(char ch) {
        System.out.println("  " + String.valueOf(ch).repeat(W));
    }

    public static void ok(String msg)   { System.out.println(GREEN  + "  ✔  " + msg + RESET); }
    public static void err(String msg)  { System.out.println(RED    + "  ✘  " + msg + RESET); }
    public static void info(String msg) { System.out.println(YELLOW + "  ➜  " + msg + RESET); }

    private static String truncate(String s, int max) {
        return s.length() <= max ? s : s.substring(0, max - 1) + "…";
    }

    private static String netWorthColor(User u, MarketService m) {
        return u.getOverallPnL(m.getAllStocks()) >= 0 ? GREEN : RED;
    }

    // ─── Portfolio Analytics ───────────────────────────────────────────────────
    public static void printPortfolioAnalytics(User user, MarketService market) {
        java.util.Map<String, Double> sectorAlloc = com.codealpha.stocktrading.service.PortfolioAnalytics.getSectorAllocation(user, market);
        double diversification = com.codealpha.stocktrading.service.PortfolioAnalytics.getDiversificationScore(user, market);
        double concentration = com.codealpha.stocktrading.service.PortfolioAnalytics.getPortfolioConcentration(user, market);
        double sharpeRatio = com.codealpha.stocktrading.service.PortfolioAnalytics.calculatePortfolioSharpeRatio(user, market);
        String recommendation = com.codealpha.stocktrading.service.PortfolioAnalytics.getPortfolioRecommendation(user, market);

        System.out.println();
        line('=');
        System.out.println(BOLD + "  PORTFOLIO ANALYTICS" + RESET);
        line('-');

        System.out.printf("  %-20s : %.2f%%%n", "Diversification Score", diversification);
        System.out.printf("  %-20s : %.2f%%%n", "Concentration (Top 3)", concentration);
        System.out.printf("  %-20s : %.4f%n", "Sharpe Ratio", sharpeRatio);
        System.out.printf("  %-20s : %s%s%s%n", "Recommendation",
            recommendation.contains("BUY") ? GREEN : recommendation.contains("SELL") ? RED : YELLOW,
            recommendation, RESET);

        if (!sectorAlloc.isEmpty()) {
            line('-');
            System.out.println(BOLD + "  SECTOR ALLOCATION" + RESET);
            sectorAlloc.entrySet().stream()
                .sorted((a, b) -> Double.compare(b.getValue(), a.getValue()))
                .forEach(e -> System.out.printf("  %-20s : %6.2f%%%n", e.getKey(), e.getValue()));
        }

        line('=');
    }

    // ─── Stock Intelligence ────────────────────────────────────────────────────
    public static void printStockIntelligence(Stock stock) {
        String trend = com.codealpha.stocktrading.service.MarketIntelligence.analyzePriceTrend(stock);
        double rsi = com.codealpha.stocktrading.service.MarketIntelligence.calculateRSI(stock);
        double volatility = com.codealpha.stocktrading.service.MarketIntelligence.calculateVolatility(stock);
        String recommendation = com.codealpha.stocktrading.service.MarketIntelligence.getStockRecommendation(stock);
        java.util.Map<String, Double> levels = com.codealpha.stocktrading.service.MarketIntelligence.calculateSupportResistance(stock);

        System.out.println();
        line('-');
        System.out.println(BOLD + "  TECHNICAL ANALYSIS" + RESET);
        line('-');

        String trendColor = "UPTREND".equals(trend) ? GREEN : "DOWNTREND".equals(trend) ? RED : YELLOW;
        System.out.printf("  %-18s : %s%s%s%n", "Trend", trendColor, trend, RESET);
        System.out.printf("  %-18s : %.2f%n", "RSI (Momentum)", rsi);
        System.out.printf("  %-18s : %.4f%n", "Volatility", volatility);

        String recColor = recommendation.contains("BUY") ? GREEN : recommendation.contains("SELL") ? RED : YELLOW;
        System.out.printf("  %-18s : %s%s%s%n", "Recommendation", BOLD + recColor, recommendation, RESET);

        if (!levels.isEmpty()) {
            line('-');
            System.out.println(BOLD + "  SUPPORT/RESISTANCE" + RESET);
            System.out.printf("  %-18s : $%.2f%n", "Resistance", levels.getOrDefault("RESISTANCE", 0.0));
            System.out.printf("  %-18s : $%.2f%n", "Pivot", levels.getOrDefault("PIVOT", 0.0));
            System.out.printf("  %-18s : $%.2f%n", "Support", levels.getOrDefault("SUPPORT", 0.0));
        }

        line('-');
    }
}
