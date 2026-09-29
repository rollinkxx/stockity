# Test Report

## Local verification — 2026-09-29

Command: `./gradlew --no-daemon lint testDebugUnitTest assembleDebug assembleRelease`

The build and both APK assembly tasks passed. Unit tests: **15 passed, 0 failures, 0 errors, 0 skipped**. Coverage includes demo safety gates, the no-trade baseline, EMA initialization, Wilder RSI edge cases, MACD warmup, ATR, rolling extrema, OHLC validation, hypothetical P&L, next-bar entry timing, history-prefix isolation, non-overlapping expiry positions, and insufficient future bars. Lint passed with one non-fatal dependency advisory: `androidx.activity:activity-compose:1.10.0` has a newer stable version available (1.13.0); no other lint findings remain.

The current offline-engine sources compiled and assembled debug and unsigned release APKs locally. The last hosted CI run before these offline-engine changes passed at [run 36532951902](https://github.com/rollinkxx/stockity/actions/runs/36532951902). Hosted verification and artifacts for the new source commit are pending. No device/emulator installation test was available.

## Scope and data boundary

Indicator and backtest tests use synthetic fixtures strictly as software tests, not market evidence. No historical market dataset is bundled. No Stockity feed, authenticated session, signal execution, demo order, data-source quality check, out-of-sample analysis, fees beyond the explicit simulator input, or strategy robustness validation is claimed.
