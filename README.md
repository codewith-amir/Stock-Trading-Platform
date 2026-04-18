# Stock Trading Platform

> **CodeAlpha Java Developer Internship — Task 2**  
> Author: Muhammad Amir | GitHub: [@codewith-amir](https://github.com/codewith-amir)

---

## 📋 Project Overview

A **console-based Stock Trading Platform** built in Java with advanced portfolio analytics, technical analysis, buy/sell operations, and real-time market simulation. Features 12 real-world stocks, P&L tracking, watchlist management, and CSV file persistence.

---

## ✨ Core Features

- ✅ **12 Real Stocks** — AAPL, MSFT, TSLA, NVDA, AMZN, GOOGL, META, JPM, BAC, JNJ, PFE, T
- ✅ **Live Market Simulation** — Prices fluctuate ±3% each refresh
- ✅ **Buy/Sell Operations** — Full order validation & confirmation
- ✅ **Portfolio Tracking** — Average cost basis, unrealized P&L, P&L%
- ✅ **Net Worth Calculation** — Cash + portfolio value vs starting balance
- ✅ **Transaction History** — All trades with timestamps
- ✅ **Portfolio Analytics** — Diversification score, Sharpe ratio, sector allocation
- ✅ **Technical Analysis** — RSI, trend detection, support/resistance levels
- ✅ **Watchlist Manager** — Track stocks with price alerts
- ✅ **File I/O** — Auto-saves CSV snapshots (transactions + portfolio)
- ✅ **Color-coded UI** — Green for gains, red for losses

---

## 🎯 Advanced Features

| Feature | Description |
|---------|-------------|
| **Portfolio Analytics** | Diversification score, Sharpe ratio, sector allocation, recommendations |
| **Stock Intelligence** | RSI momentum, trend analysis, support/resistance, volatility |
| **Watchlist Manager** | Track unlimited stocks, price alerts, trend monitoring |

---

## 📂 Project Structure
CodeAlpha_StockTradingPlatform/
└── src/com/codealpha/stocktrading/
├── Main.java                       ← Entry point + menu
├── model/
│   ├── Stock.java                  ← Stock with price simulation
│   ├── User.java                   ← Trader account + watchlist
│   ├── PortfolioHolding.java       ← Per-stock holding + cost basis
│   ├── Transaction.java            ← Single trade record
│   └── Watchlist.java              ← Stock tracking + alerts
├── service/
│   ├── MarketService.java          ← Market data + refresh
│   ├── FileService.java            ← CSV read/write
│   ├── PortfolioAnalytics.java     ← Advanced metrics
│   └── MarketIntelligence.java     ← Technical analysis
└── util/
└── DisplayHelper.java          ← Console UI + formatting

---

## 🚀 How to Run

### Prerequisites

- Java JDK 11 or higher
- Terminal/Command Prompt

### Compile & Run

```bash
# 1. Clone the repository
git clone https://github.com/codewith-amir/CodeAlpha_StockTradingPlatform.git
cd CodeAlpha_StockTradingPlatform

# 2. Compile all source files
javac -d out src/com/codealpha/stocktrading/*.java \
              src/com/codealpha/stocktrading/model/*.java \
              src/com/codealpha/stocktrading/service/*.java \
              src/com/codealpha/stocktrading/util/*.java

# 3. Run the application
java -cp out com.codealpha.stocktrading.Main
```

### Alternative: Quick Compile (Single Command)

```bash
# Compile
javac src/com/codealpha/stocktrading/**/*.java -d out

# Run
java -cp out com.codealpha.stocktrading.Main
```

---

## 📊 Menu Options

Market Overview         - View all 12 stocks with live prices
Stock Detail            - Analyze individual stock
Buy Shares              - Purchase with validation
Sell Shares             - Liquidate positions
My Portfolio            - View holdings & P&L
Transaction History     - All buy/sell records
Portfolio Analytics     - Metrics & recommendations ✨
Stock Intelligence      - Technical analysis ✨
Watchlist Manager       - Track & alert ✨
Refresh Market Prices   - Simulate next market tick
Save Data to File       - Export CSV
Exit                    - Auto-save & quit


---

## 💾 File Output

On exit, two CSV files are created in `trading_data/`:

- `<username>_transactions.csv` — Full trade log with timestamps
- `<username>_portfolio_<timestamp>.csv` — Portfolio snapshot with P&L

---

## 💻 Technologies Used

- **Java** (JDK 11+)
- **OOP** — Clean separation of concerns (model/service/util)
- **Collections** — HashMap, ArrayList for portfolio management
- **File I/O** — CSV persistence
- **ANSI Colors** — Terminal styling

---

## 📈 Technical Indicators

- **RSI** (Relative Strength Index) — Momentum measurement (0-100)
- **Trend Analysis** — UPTREND, DOWNTREND, SIDEWAYS detection
- **Support/Resistance** — Price levels from historical data
- **Volatility** — Standard deviation of price changes
- **Sharpe Ratio** — Risk-adjusted return calculation

---

## 📸 Screenshot
══════════════════════════════════════════════════════════════════════
STOCK TRADING PLATFORM  ─  CodeAlpha Internship
Java Developer Internship  |  Task 2
Author: Muhammad Amir  |  GitHub: codewith-amir
══════════════════════════════════════════════════════════════════════
────────────────────────────────────────────────────────────────────
MAIN MENU  ·  Cash: $45,230.50   Net Worth: $52,145.30
────────────────────────────────────────────────────────────────────

Market Overview  (all stocks)
Stock Detail     (search by symbol)
Buy Shares
Sell Shares
My Portfolio
...
────────────────────────────────────────────────────────────────────
Enter choice:


---

## 🎓 Learning Outcomes

This project demonstrates:
- ✅ Advanced OOP (encapsulation, inheritance, polymorphism)
- ✅ Clean code architecture
- ✅ Financial calculations & P&L tracking
- ✅ Technical analysis algorithms
- ✅ File I/O & data persistence
- ✅ User input validation & error handling
- ✅ Console UI design

---

## 🐛 Bug Fixes (v2.0)

- ✅ **Sell Quantity Validation** — Pre-confirmation checks
- ✅ **Floating-Point Precision** — All monetary operations rounded to cents
- ✅ **Input Validation** — Comprehensive bounds checking
- ✅ **Cash Balance Precision** — Prevents rounding errors
- ✅ **Portfolio Precision** — Consistent rounding across operations

See `ANALYSIS_REPORT.md` for detailed bug analysis and improvements.

---

## 📝 License

Copyright © 2026 Muhammad Amir. All rights reserved.

---

## 🔗 Connect

- **GitHub:** [@codewith-amir](https://github.com/codewith-amir)
- **LinkedIn:** [muhammad-amir-pk](https://linkedin.com/in/muhammad-amir-pk)
- **Email:** muhammadamir191491@gmail.com
- **X (Twitter):** [@MuhammadAamirPK](https://x.com/MuhammadAamirPK)

---

**Built with ❤️ by Muhammad Amir | BSCS @ NUML University, Lahore**