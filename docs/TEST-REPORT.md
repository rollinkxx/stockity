# Test Report

## Local verification — 2026-09-29

Command: `./gradlew --no-daemon lint testDebugUnitTest assembleDebug assembleRelease`

The local build and both APK assembly tasks passed. Unit tests: **15 passed, 0 failures, 0 errors, 0 skipped**. Coverage includes demo safety gates, the no-trade baseline, EMA initialization, Wilder RSI edge cases, MACD warmup, ATR, rolling extrema, OHLC validation, hypothetical P&L, next-bar entry timing, history-prefix isolation, non-overlapping expiry positions, and insufficient future bars. Lint passed with one non-fatal advisory: `androidx.activity:activity-compose:1.10.0` has a newer stable version available (1.13.0); no other lint findings remain.

## GitHub Actions verification — 2026-09-29

The complete Android CI workflow passed for commit `29b8893017781719d264233e3a00fff837556075` in [run 36533751617](https://github.com/rollinkxx/stockity/actions/runs/36533751617). SDK setup, lint and unit tests, debug/release assembly, checksum generation, and artifact upload all completed successfully. The APKs were downloaded and matched the CI checksum file. The debug APK signature and 4-byte zip alignment were verified; its package is `com.rollinkxx.stockitydemo.debug`, versionCode 1, versionName `0.1.0-debug`, min SDK 26, target SDK 35. It is signed with the Android debug key. The release APK is intentionally unsigned.

| Artifact | SHA-256 |
|---|---|
| `StockityDemoTrader-debug.apk` | `b5db03c84e94ad640ff683520cfe7ffa1cd73f1d059f5137e8dec1b8c2ea07ea` |
| `StockityDemoTrader-release-unsigned.apk` | `6f345a63a92a5c68fe831f8a9bc12199b4d62b89de2720400f8955b5df30c6b5` |

## Scope and data boundary

Indicator and backtest tests use synthetic fixtures strictly as software tests, not market evidence. No historical market dataset is bundled. No Stockity feed, authenticated session, signal execution, demo order, data-source quality check, out-of-sample analysis, fees beyond the explicit simulator input, or strategy robustness validation is claimed. No emulator/device installation test was available.
