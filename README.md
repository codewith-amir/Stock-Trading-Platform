# Stock Trading Platform

Console-based Java application for simulated stock trading.

## Overview

This project implements a trading workflow with market simulation, portfolio tracking, transaction history, and CSV persistence.

## Features

- Market simulation for 12 predefined stocks
- Buy and sell with validation checks
- Portfolio valuation and unrealized P&L
- Transaction history with timestamps
- Portfolio analytics (diversification, concentration, Sharpe ratio)
- Technical indicators (trend, RSI, volatility, support/resistance)
- Watchlist management
- CSV export for transaction and portfolio data

## Project Structure

```text
src/com/codealpha/stocktrading/
  Main.java
  model/
    Stock.java
    User.java
    PortfolioHolding.java
    Transaction.java
    Watchlist.java
  service/
    MarketService.java
    FileService.java
    PortfolioAnalytics.java
    MarketIntelligence.java
  util/
    DisplayHelper.java
```

## Requirements

- Java 11 or later

## Build and Run

Use the command set that matches your shell:

Windows PowerShell:

```powershell
$files = Get-ChildItem -Recurse src -Filter *.java | ForEach-Object { $_.FullName }
javac -d out $files
java -cp out com.codealpha.stocktrading.Main
```

macOS / Linux / Git Bash:

```bash
javac -d out $(find src -name "*.java")
java -cp out com.codealpha.stocktrading.Main
```

## Data Output

The application writes CSV files to `trading_data/`:

- `<username>_transactions.csv`
- `<username>_portfolio_<timestamp>.csv`

## Notes

- Monetary values are rounded to cents to reduce floating-point drift.
- The app saves CSV snapshots to `trading_data/`, but it starts a new in-memory account each time it launches.
- See `ANALYSIS_REPORT.md` for implementation notes and improvements.

## License

See [LICENSE](LICENSE).
