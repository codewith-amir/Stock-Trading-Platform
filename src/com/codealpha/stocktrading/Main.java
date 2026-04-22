package com.codealpha.stocktrading;

import com.codealpha.stocktrading.model.*;
import com.codealpha.stocktrading.service.*;
import com.codealpha.stocktrading.util.DisplayHelper;

import java.util.Scanner;

/**
 * Console entry point for the stock trading platform.
 */
public class Main {

    private static final Scanner scanner = new Scanner(System.in);
    private static final MarketService market = new MarketService();
    private static User user;

    public static void main(String[] args) {
        DisplayHelper.printBanner();
        loginUser();

        market.refreshMarket();
        DisplayHelper.info("Market is open. Starting balance: $" +
            String.format("%.2f", User.STARTING_CASH));
        System.out.println();

        boolean running = true;
        while (running) {
            DisplayHelper.printMenu(user, market);
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1" -> DisplayHelper.printMarketOverview(market);
                case "2" -> stockDetail();
                case "3" -> buyShares();
                case "4" -> sellShares();
                case "5" -> DisplayHelper.printPortfolio(user, market);
                case "6" -> DisplayHelper.printTransactionHistory(user);
                case "7" -> portfolioAnalytics();
                case "8" -> stockIntelligence();
                case "9" -> manageWatchlist();
                case "10" -> {
                    market.refreshMarket();
                    DisplayHelper.ok("Market prices refreshed.");
                }
                case "11" -> {
                    FileService.saveTransactionHistory(user);
                    FileService.savePortfolioSnapshot(user, market);
                }
                case "0" -> {
                    System.out.println();
                    DisplayHelper.info("Saving data before exit...");
                    FileService.saveTransactionHistory(user);
                    FileService.savePortfolioSnapshot(user, market);
                    System.out.println();
                    DisplayHelper.info("Goodbye, " + user.getUsername() + ".");
                    System.out.println();
                    running = false;
                }
                default -> DisplayHelper.err("Invalid choice. Enter a number 0-11.");
            }
        }
        scanner.close();
    }

    private static void loginUser() {
        System.out.println();
        System.out.print("  Enter your trader name: ");
        String name = scanner.nextLine().trim();
        if (name.isEmpty()) {
            name = "Trader";
        }
        user = new User(name);
        System.out.println();
        DisplayHelper.ok("Welcome, " + user.getUsername() + ". Account created.");
    }

    private static void stockDetail() {
        System.out.println();
        System.out.print("  Enter stock symbol (e.g. AAPL, TSLA): ");
        String symbol = scanner.nextLine().trim().toUpperCase();

        Stock stock = market.getStock(symbol);
        if (stock == null) {
            DisplayHelper.err("Symbol '" + symbol + "' not found. Use Market Overview to see all symbols.");
        } else {
            DisplayHelper.printStockDetail(stock, user);
        }
    }

    private static void buyShares() {
        System.out.println();
        System.out.print("  Enter symbol to BUY: ");
        String symbol = scanner.nextLine().trim().toUpperCase();

        if (symbol.isEmpty()) {
            DisplayHelper.err("Symbol cannot be empty.");
            return;
        }

        Stock stock = market.getStock(symbol);
        if (stock == null) {
            DisplayHelper.err("Symbol '" + symbol + "' not found.");
            return;
        }

        System.out.printf("  %s  current price: " +
            DisplayHelper.CYAN + DisplayHelper.BOLD + "$%.2f" + DisplayHelper.RESET + "%n",
            stock.getSymbol(), stock.getCurrentPrice());
        System.out.printf("  Your cash balance: " +
            DisplayHelper.CYAN + DisplayHelper.BOLD + "$%.2f" + DisplayHelper.RESET + "%n",
            user.getCashBalance());
        int maxShares = (int)(user.getCashBalance() / stock.getCurrentPrice());
        System.out.printf("  Max shares you can buy: " +
            DisplayHelper.BOLD + "%d" + DisplayHelper.RESET + "%n", maxShares);
        System.out.print("  Quantity to buy: ");

        int qty;
        try {
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) {
                DisplayHelper.err("Quantity cannot be empty.");
                return;
            }
            qty = Integer.parseInt(input);
            if (qty <= 0) {
                DisplayHelper.err("Quantity must be positive.");
                return;
            }
            if (qty > 1_000_000) {
                DisplayHelper.err("Quantity too large (max 1,000,000).");
                return;
            }
            if (qty > maxShares) {
                DisplayHelper.err("Insufficient funds. Maximum: " + maxShares + " shares.");
                return;
            }
        } catch (NumberFormatException e) {
            DisplayHelper.err("Invalid quantity. Please enter a valid integer.");
            return;
        }

        double total = qty * stock.getCurrentPrice();
        System.out.printf("  Confirm: BUY %d x %s @ $%.2f  =  " +
            DisplayHelper.BOLD + "$%.2f" + DisplayHelper.RESET + "  (y/n): ",
            qty, symbol, stock.getCurrentPrice(), total);
        String confirm = scanner.nextLine().trim().toLowerCase();

        if (!confirm.equals("y")) {
            DisplayHelper.info("Order cancelled.");
            return;
        }

        try {
            user.buyStock(stock, qty);
            DisplayHelper.ok(String.format("Bought %d share(s) of %s @ $%.2f  |  Total: $%.2f",
                qty, symbol, stock.getCurrentPrice(), total));
            DisplayHelper.info("Remaining cash: $" + String.format("%.2f", user.getCashBalance()));
        } catch (IllegalStateException e) {
            DisplayHelper.err(e.getMessage());
        }
    }

    private static void sellShares() {
        System.out.println();

        if (user.getPortfolio().isEmpty()) {
            DisplayHelper.err("You have no holdings to sell.");
            return;
        }

        System.out.print("  Enter symbol to SELL: ");
        String symbol = scanner.nextLine().trim().toUpperCase();

        if (symbol.isEmpty()) {
            DisplayHelper.err("Symbol cannot be empty.");
            return;
        }

        Stock stock = market.getStock(symbol);
        if (stock == null) {
            DisplayHelper.err("Symbol '" + symbol + "' not found.");
            return;
        }

        PortfolioHolding holding = user.getHolding(symbol);
        if (holding == null) {
            DisplayHelper.err("You don't own any shares of " + symbol + ".");
            return;
        }

        int ownedShares = holding.getQuantity();
        System.out.printf("  You own %d share(s) of %s  |  Current price: $%.2f%n",
            ownedShares, symbol, stock.getCurrentPrice());
        System.out.print("  Quantity to sell: ");

        int qty;
        try {
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) {
                DisplayHelper.err("Quantity cannot be empty.");
                return;
            }
            qty = Integer.parseInt(input);
            if (qty <= 0) {
                DisplayHelper.err("Quantity must be positive.");
                return;
            }
            if (qty > ownedShares) {
                DisplayHelper.err("Cannot sell " + qty + " shares. You only own " + ownedShares + ".");
                return;
            }
        } catch (NumberFormatException e) {
            DisplayHelper.err("Invalid quantity. Please enter a valid integer.");
            return;
        }

        double total = qty * stock.getCurrentPrice();
        double pnl = (stock.getCurrentPrice() - holding.getAverageCost()) * qty;
        String pColor = pnl >= 0 ? DisplayHelper.GREEN : DisplayHelper.RED;

        System.out.printf("  Confirm: SELL %d x %s @ $%.2f  =  " +
            DisplayHelper.BOLD + "$%.2f" + DisplayHelper.RESET +
            "  |  P&L: %s%s$%.2f%s  (y/n): ",
            qty, symbol, stock.getCurrentPrice(), total,
            DisplayHelper.BOLD, pColor, pnl, DisplayHelper.RESET);

        String confirm = scanner.nextLine().trim().toLowerCase();
        if (!confirm.equals("y")) {
            DisplayHelper.info("Order cancelled.");
            return;
        }

        try {
            user.sellStock(stock, qty);
            String pnlStr = pnl >= 0
                ? DisplayHelper.GREEN + "+$" + String.format("%.2f", pnl) + DisplayHelper.RESET
                : DisplayHelper.RED + "-$" + String.format("%.2f", Math.abs(pnl)) + DisplayHelper.RESET;
            DisplayHelper.ok(String.format("Sold %d share(s) of %s @ $%.2f  |  Received: $%.2f  |  P&L: %s",
                qty, symbol, stock.getCurrentPrice(), total, pnlStr));
            DisplayHelper.info("New cash balance: $" + String.format("%.2f", user.getCashBalance()));
        } catch (IllegalStateException e) {
            DisplayHelper.err(e.getMessage());
        }
    }

    private static void portfolioAnalytics() {
        if (user.getPortfolio().isEmpty()) {
            DisplayHelper.err("You have no portfolio to analyze. Buy some stocks first.");
            return;
        }
        DisplayHelper.printPortfolioAnalytics(user, market);
    }

    private static void stockIntelligence() {
        System.out.println();
        System.out.print("  Enter stock symbol to analyze: ");
        String symbol = scanner.nextLine().trim().toUpperCase();

        if (symbol.isEmpty()) {
            DisplayHelper.err("Symbol cannot be empty.");
            return;
        }

        Stock stock = market.getStock(symbol);
        if (stock == null) {
            DisplayHelper.err("Symbol '" + symbol + "' not found.");
            return;
        }

        DisplayHelper.printStockIntelligence(stock);
    }

    private static void manageWatchlist() {
        Watchlist watchlist = user.getWatchlist();

        while (true) {
            System.out.println();
            DisplayHelper.line('-');
            System.out.println(DisplayHelper.BOLD + "  WATCHLIST MANAGER" + DisplayHelper.RESET +
                "  (" + watchlist.getWatchlistSize() + " stocks watched)");
            DisplayHelper.line('-');
            System.out.println("  " + DisplayHelper.CYAN + "1." + DisplayHelper.RESET + "  View Watchlist");
            System.out.println("  " + DisplayHelper.CYAN + "2." + DisplayHelper.RESET + "  Add Stock");
            System.out.println("  " + DisplayHelper.CYAN + "3." + DisplayHelper.RESET + "  Remove Stock");
            System.out.println("  " + DisplayHelper.RED + "0." + DisplayHelper.RESET + "  Back to Menu");
            DisplayHelper.line('-');
            System.out.print(DisplayHelper.BOLD + "  Enter choice: " + DisplayHelper.RESET);

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1" -> viewWatchlist();
                case "2" -> addToWatchlist();
                case "3" -> removeFromWatchlist();
                case "0" -> {
                    return;
                }
                default -> DisplayHelper.err("Invalid choice.");
            }
        }
    }

    private static void viewWatchlist() {
        Watchlist watchlist = user.getWatchlist();
        java.util.List<String> watched = watchlist.getWatchedSymbols();

        System.out.println();
        DisplayHelper.line('=');
        System.out.println(DisplayHelper.BOLD + "  MY WATCHLIST" + DisplayHelper.RESET);
        DisplayHelper.line('-');

        if (watched.isEmpty()) {
            System.out.println(DisplayHelper.YELLOW + "  No stocks in watchlist." + DisplayHelper.RESET);
        } else {
            System.out.printf(DisplayHelper.BOLD + "  %-8s  %-22s  %-12s  %-10s  %s%n" + DisplayHelper.RESET,
                "SYMBOL", "COMPANY", "PRICE", "CHANGE%", "TREND");
            DisplayHelper.line('-');

            for (String symbol : watched) {
                Stock s = market.getStock(symbol);
                if (s != null) {
                    String color = s.isPositive() ? DisplayHelper.GREEN : DisplayHelper.RED;
                    String arrow = s.isPositive() ? "▲" : "▼";
                    String trend = com.codealpha.stocktrading.service.MarketIntelligence.analyzePriceTrend(s);
                    System.out.printf("  " + DisplayHelper.BOLD + "%-8s" + DisplayHelper.RESET +
                        "  %-22s  $%-11.2f  " + color + "%s%-9.2f%%" + DisplayHelper.RESET +
                        "  %s%n",
                        s.getSymbol(),
                        s.getCompanyName(),
                        s.getCurrentPrice(),
                        arrow,
                        s.getPriceChangePercent(),
                        trend);
                }
            }
        }
        DisplayHelper.line('=');
    }

    private static void addToWatchlist() {
        System.out.println();
        System.out.print("  Enter stock symbol to watch: ");
        String symbol = scanner.nextLine().trim().toUpperCase();

        if (symbol.isEmpty()) {
            DisplayHelper.err("Symbol cannot be empty.");
            return;
        }

        if (!market.exists(symbol)) {
            DisplayHelper.err("Symbol '" + symbol + "' not found.");
            return;
        }

        user.getWatchlist().addToWatchlist(symbol);
        DisplayHelper.ok("Added " + symbol + " to watchlist.");
    }

    private static void removeFromWatchlist() {
        System.out.println();
        System.out.print("  Enter stock symbol to remove: ");
        String symbol = scanner.nextLine().trim().toUpperCase();

        if (symbol.isEmpty()) {
            DisplayHelper.err("Symbol cannot be empty.");
            return;
        }

        if (!user.getWatchlist().isWatched(symbol)) {
            DisplayHelper.err(symbol + " is not in your watchlist.");
            return;
        }

        user.getWatchlist().removeFromWatchlist(symbol);
        DisplayHelper.ok("Removed " + symbol + " from watchlist.");
    }
}
