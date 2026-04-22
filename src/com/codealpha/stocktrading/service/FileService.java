package com.codealpha.stocktrading.service;

import com.codealpha.stocktrading.model.Transaction;
import com.codealpha.stocktrading.model.User;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Handles file I/O — saves transaction history and portfolio snapshots.
 * Files are written to the working directory under: trading_data/
 */
public class FileService {

    private static final String DIR = "trading_data";
    private static final DateTimeFormatter DT = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");

    static {
        new File(DIR).mkdirs();
    }
    public static void saveTransactionHistory(User user) {
        String filename = DIR + "/" + user.getUsername() + "_transactions.csv";
        try (PrintWriter pw = new PrintWriter(new FileWriter(filename))) {
            pw.println("Type,Symbol,Quantity,PricePerShare,TotalValue,Timestamp");
            for (Transaction t : user.getTransactionHistory()) {
                pw.printf("%s,%s,%d,%.2f,%.2f,%s%n",
                    t.getType(), t.getSymbol(), t.getQuantity(),
                    t.getPricePerShare(), t.getTotalValue(), t.getFormattedTime());
            }
            System.out.println("  ✔  Transaction history saved → " + filename);
        } catch (IOException e) {
            System.out.println("  ✘  Error saving transactions: " + e.getMessage());
        }
    }
    public static void savePortfolioSnapshot(User user, MarketService market) {
        String filename = DIR + "/" + user.getUsername() + "_portfolio_"
            + LocalDateTime.now().format(DT) + ".csv";
        try (PrintWriter pw = new PrintWriter(new FileWriter(filename))) {
            pw.println("Symbol,Quantity,AvgCost,CurrentPrice,CurrentValue,UnrealizedPnL,PnL%");
            for (var entry : user.getPortfolio().entrySet()) {
                var holding = entry.getValue();
                var stock   = market.getStock(entry.getKey());
                if (stock == null) continue;
                double price = stock.getCurrentPrice();
                pw.printf("%s,%d,%.2f,%.2f,%.2f,%.2f,%.2f%%%n",
                    holding.getSymbol(),
                    holding.getQuantity(),
                    holding.getAverageCost(),
                    price,
                    holding.getCurrentValue(price),
                    holding.getUnrealizedPnL(price),
                    holding.getPnLPercent(price));
            }
            pw.println();
            pw.printf("Cash Balance,,,,%.2f,,\n", user.getCashBalance());
            pw.printf("Total Net Worth,,,,%.2f,,\n", user.getTotalNetWorth(market.getAllStocks()));
            System.out.println("  ✔  Portfolio snapshot saved → " + filename);
        } catch (IOException e) {
            System.out.println("  ✘  Error saving portfolio: " + e.getMessage());
        }
    }
    public static void printSavedHistory(String username) {
        String filename = DIR + "/" + username + "_transactions.csv";
        File f = new File(filename);
        if (!f.exists()) {
            System.out.println("  No saved transaction file found for: " + username);
            return;
        }
        try {
            List<String> lines = Files.readAllLines(f.toPath());
            System.out.println("  Loaded " + (lines.size() - 1) + " transactions from file:");
            for (String line : lines) System.out.println("    " + line);
        } catch (IOException e) {
            System.out.println("  ✘  Error reading file: " + e.getMessage());
        }
    }
}
