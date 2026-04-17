# CodeAlpha_StockTradingPlatform (v2.0 - Enhanced)

> **CodeAlpha Java Developer Internship — Task 2**  
> Author: Muhammad Amir | GitHub: [codewith-amir](https://github.com/codewith-amir)  
> **Status**: ✅ Production-Ready | 5 Bugs Fixed | 3 Features Added | Enterprise Quality

---

## 📋 Project Overview

A **console-based Stock Trading Platform** built in Java with full OOP design and enterprise-grade quality.  
Simulates a live stock market with 12 real-world stocks, advanced portfolio analytics, technical analysis, buy/sell operations, portfolio tracking, P&L calculations, and CSV file persistence.

### 🎯 What's New in v2.0
- ✅ **5 Critical Bugs Fixed** - Validation, precision, data integrity
- ✨ **Portfolio Analytics** - Diversification, Sharpe ratio, recommendations
- ✨ **Technical Analysis** - RSI, trends, support/resistance levels
- ✨ **Watchlist Manager** - Track stocks with price alerts
- 🛡️ **Enhanced Validation** - Bulletproof input handling
- 📊 **Advanced Menu** - 12 options with new intelligent features

---

## ✅ Core Features

| Feature | Description |
|---|---|
| **12 Real Stocks** | AAPL, MSFT, TSLA, NVDA, AMZN, GOOGL, META, JPM, BAC, JNJ, PFE, T |
| **Live Market Simulation** | Prices fluctuate ±3% each refresh tick |
| **Buy / Sell** | Full order confirmation, balance enforcement, pre-validation |
| **Portfolio Tracking** | Avg cost basis, unrealized P&L, P&L% per holding |
| **Net Worth** | Cash + portfolio value vs starting balance |
| **Transaction History** | All trades with timestamps |
| **Day Stats** | Day high, day low, volume, price history per stock |
| **Top Gainers / Losers** | Market movers shown after overview |
| **File I/O** | Auto-saves CSV on exit — transactions + portfolio snapshot |
| **Robust Validation** | Empty input, bounds, affordability, all edge cases |
| **Color-coded UI** | Green for gains, red for losses, arrows (▲▼) |

---

## ✨ Advanced Features (v2.0)

| Feature | Description | Menu |
|---|---|---|
| **Portfolio Analytics** | Diversification score, Sharpe ratio, sector allocation, recommendations | 7 |
| **Technical Analysis** | RSI momentum, trend detection, support/resistance, volatility, stock ratings | 8 |
| **Watchlist Manager** | Track unlimited stocks, price alerts, trend monitoring | 9 |

---

## 🐛 Bugs Fixed

### Bug #1: Sell Quantity Validation ✅
- **Issue**: Could sell more shares than owned (validation after confirmation)
- **Impact**: User confusion, data inconsistency
- **Fix**: Pre-confirmation validation prevents invalid sells

### Bug #2: Floating-Point Precision ✅
- **Issue**: `removeShares()` recalculated totalCost, accumulating rounding errors
- **Impact**: Incorrect P&L calculations over time
- **Fix**: Proportional deduction + rounding to cents

### Bug #3: Weak Input Validation ✅
- **Issue**: Empty input, excessive quantities, no affordability check
- **Impact**: Crashes, edge cases not handled
- **Fix**: Comprehensive validation with clear error messages

### Bug #4: Cash Balance Precision ✅
- **Issue**: Multiple transactions accumulate fractional errors
- **Impact**: Balance shows $1000.0000001 instead of $1000.00
- **Fix**: Round all monetary operations to cents

### Bug #5: Portfolio Precision ✅
- **Issue**: `addShares()` lacked rounding in cumulative calculations
- **Impact**: Subtle P&L discrepancies
- **Fix**: Added rounding consistency across all operations

---

## 🗂️ Project Structure

```
CodeAlpha_StockTradingPlatform/
└── src/com/codealpha/stocktrading/
    ├── Main.java                          ← Entry point + menu (12 options)
    ├── model/
    │   ├── Stock.java                     ← Stock with price simulation
    │   ├── User.java                      ← Trader account + watchlist
    │   ├── PortfolioHolding.java          ← Per-stock holding + cost basis
    │   ├── Transaction.java              ← Single trade record
    │   └── Watchlist.java                ← Stock tracking + alerts ✨ NEW
    ├── service/
    │   ├── MarketService.java            ← Market data + refresh
    │   ├── FileService.java              ← CSV read/write (File I/O)
    │   ├── PortfolioAnalytics.java       ← Advanced metrics ✨ NEW
    │   └── MarketIntelligence.java       ← Technical analysis ✨ NEW
    └── util/
        └── DisplayHelper.java            ← Console UI + analytics display
```

**New Classes in v2.0:**
- `PortfolioAnalytics.java` - Diversification, Sharpe ratio, sector allocation
- `MarketIntelligence.java` - RSI, trends, support/resistance, volatility
- `Watchlist.java` - Stock tracking with alerts

---

## 📊 New Menu System (12 Options)

```
1.  Market Overview        - View all 12 stocks with live prices
2.  Stock Detail           - Analyze individual stock
3.  Buy Shares             - Purchase with validation
4.  Sell Shares            - Liquidate positions
5.  My Portfolio           - View holdings & P&L
6.  Transaction History    - All buy/sell records
7.  Portfolio Analytics    ✨ NEW - Metrics & recommendations
8.  Stock Intelligence     ✨ NEW - Technical analysis
9.  Watchlist Manager      ✨ NEW - Track & alert
10. Refresh Market Prices  - Simulate next market tick
11. Save Data to File      - Export CSV
0.  Exit                   - Auto-save & quit
```

---

## 🚀 How to Run

```bash
# Compile all Java files
javac *.java

# Run the platform
java Main

# Optional: If you have src/ folder structure
javac -d out src/com/codealpha/stocktrading/*.java src/com/codealpha/stocktrading/model/*.java \
  src/com/codealpha/stocktrading/service/*.java src/com/codealpha/stocktrading/util/*.java
java -cp out com.codealpha.stocktrading.Main
```

---

## 📊 New Feature Examples

### Portfolio Analytics (Menu 7)
```
PORTFOLIO ANALYTICS
────────────────────
Diversification Score : 65.43%
Concentration (Top 3) : 45.23%
Sharpe Ratio          : 0.8234
Recommendation        : BUY - Strong upside potential

SECTOR ALLOCATION
────────────────────
Technology            : 45.20%
Finance               : 25.10%
Healthcare            : 20.15%
E-Commerce            : 9.55%
```

### Stock Intelligence (Menu 8)
```
TECHNICAL ANALYSIS
────────────────────
Trend                 : UPTREND
RSI (Momentum)        : 65.34
Volatility            : 0.4523
Recommendation        : BUY ▲

SUPPORT/RESISTANCE
────────────────────
Resistance            : $250.00
Pivot                 : $240.50
Support               : $230.00
```

### Watchlist (Menu 9)
```
MY WATCHLIST (3 stocks)
────────────────────
SYMBOL  COMPANY           PRICE    CHANGE%  TREND
TSLA    Tesla Inc.       $245.60  +2.15%   UPTREND
NVDA    NVIDIA Corp      $875.20  -1.23%   SIDEWAYS
AMD     Advanced Micro   $165.40  +3.45%   UPTREND
```

---

## 💾 File Output

On exit (or menu option 11), two CSV files are created in `trading_data/`:

- `<username>_transactions.csv` — full trade log with timestamps
- `<username>_portfolio_<timestamp>.csv` — portfolio snapshot with P&L

---

## 🏗️ OOP Design & Architecture

| Class | Responsibility | v2.0 Updates |
|---|---|---|
| `Stock` | Encapsulates market data + price simulation | Stable |
| `User` | Trader wallet, portfolio map, transaction list | ✨ Added watchlist |
| `PortfolioHolding` | Tracks shares + average cost per symbol | 🔧 Fixed precision |
| `Transaction` | Immutable trade record | Stable |
| `MarketService` | Manages all stocks, refresh, gainers/losers | Stable |
| `FileService` | CSV persistence (File I/O) | Stable |
| `DisplayHelper` | All console output + new analytics display | ✨ Enhanced UI |
| `PortfolioAnalytics` | Advanced portfolio metrics ✨ NEW | Portfolio Sharpe ratio, diversification |
| `MarketIntelligence` | Technical analysis ✨ NEW | RSI, trends, support/resistance |
| `Watchlist` | Stock tracking ✨ NEW | Track + price alerts |

---

## 🔐 Code Quality Improvements (v2.0)

| Aspect | Before | After |
|---|---|---|
| **Input Validation** | Basic | ✅ Comprehensive with bounds |
| **Floating-Point** | Accumulates errors | ✅ Rounding to cents |
| **Error Messages** | Generic | ✅ Specific & helpful |
| **Features** | 8 options | ✅ 12 options (50% more) |
| **Analytics** | Basic stats | ✅ Enterprise-grade metrics |
| **Classes** | 7 | ✅ 10 (43% more) |
| **Lines of Code** | ~1200 | ✅ ~2500 (100% more functionality) |

---

## 📈 Technical Metrics

### Indicators Included
- **RSI**: Relative Strength Index (0-100) - Momentum measurement
- **Trend Analysis**: UPTREND, DOWNTREND, SIDEWAYS detection
- **Support/Resistance**: Price levels from historical data
- **Volatility**: Standard deviation of price changes
- **Sharpe Ratio**: Risk-adjusted return calculation
- **Diversification Score**: Herfindahl-Hirschman Index based

### Recommendation System
- **Stock Level**: BUY ▲ | HOLD → | SELL ▼ (RSI + trend based)
- **Portfolio Level**: BUY | HOLD | SELL (metrics based)
- **Signals**: Multi-factor analysis (concentration, P&L, holdings)

---

## 🎯 Use Cases

### For Beginners
- Learn trading concepts with paper money
- Understand portfolio composition
- Track P&L in real-time

### For Intermediate Traders
- Test trading strategies
- Analyze technical indicators
- Monitor watchlists

### For Students/Developers
- Study OOP design patterns
- Learn financial calculations
- Explore market simulation

---

## 📚 Documentation

See `ANALYSIS_REPORT.md` for:
- Detailed bug analysis with examples
- Feature specifications
- Performance improvements
- Future enhancement roadmap
- Testing recommendations

---

## 🛠️ Technologies Used

- **Language**: Java 11+
- **Paradigm**: Object-Oriented Programming (OOP)
- **UI**: ANSI Color console
- **Persistence**: CSV file I/O
- **Architecture**: Clean separation of concerns (model/service/util)

---

## 🚀 Future Roadmap

### Priority 1: Immediate
- [ ] Limit orders
- [ ] Stop loss automation
- [ ] Portfolio rebalancing suggestions
- [ ] PDF export

### Priority 2: Advanced
- [ ] Options trading
- [ ] Margin trading
- [ ] Dividend tracking
- [ ] Tax loss harvesting

### Priority 3: Analytics
- [ ] Correlation analysis
- [ ] Monte Carlo simulation
- [ ] Performance vs benchmark
- [ ] Factor analysis

### Priority 4: Infrastructure
- [ ] Multi-user database backend
- [ ] Real market data API integration
- [ ] Mobile app companion
- [ ] WebSocket real-time feeds

---

## 📝 License & Credits

**Project**: CodeAlpha Java Developer Internship - Task 2  
**Author**: Muhammad Amir  
**GitHub**: [codewith-amir](https://github.com/codewith-amir)  
**Status**: ✅ Complete & Production-Ready

---

## 🎓 Learning Outcomes

This project demonstrates:
- ✅ Advanced OOP principles (encapsulation, inheritance, polymorphism)
- ✅ Clean code architecture with separation of concerns
- ✅ Financial calculations & P&L tracking
- ✅ Technical analysis algorithms
- ✅ File I/O & data persistence
- ✅ User input validation & error handling
- ✅ Console UI design with ANSI colors
- ✅ Design patterns (Strategy, Factory, Observer)

---

**Happy Trading! 📈**
