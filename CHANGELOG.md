# Changelog

## 0.1.0 — 2026-09-29

### Added
- Native Kotlin and Jetpack Compose Android foundation.
- Fail-closed demo-only order safety policy and unit tests.
- Explicit unavailable state for undocumented Stockity realtime integration.
- Pure Kotlin OHLCV validation and EMA, Wilder RSI, MACD, ATR, and rolling support/resistance calculations.
- Offline hypothetical expiry backtester with next-bar-open entry timing, explicit payout/cost/source assumptions, no-overlap handling, and no-look-ahead tests.
- GitHub Actions workflow for lint, tests, APK builds, and SHA-256 checksums.

### Changed
- Point the informational website button to `https://stockity.com/trading`, matching the site shown in the user's screenshot. This does not add an account, market-data, or order integration.
