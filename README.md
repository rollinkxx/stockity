# Stockity Demo Trader (Android)

A native Kotlin + Jetpack Compose foundation for a **demo-only** trading/research terminal. This remains an early, safety-first milestone—not a complete trading product. The app fails closed: it does not log in to Stockity, show invented prices as live, or submit orders.

## Current status

| Area | Status |
|---|---|
| Native Android Compose shell | Implemented |
| Demo-only safety policy | Implemented and unit-tested; default account is `UNKNOWN` |
| Signal behavior | Always `NO_TRADE` while the feed is unconfigured |
| OHLCV indicators | Pure Kotlin EMA, Wilder RSI, MACD, ATR, rolling support/resistance; unit-tested |
| Offline backtesting | Pure local hypothetical expiry model; explicit source/cost/payout inputs and next-bar-open entry; unit-tested; not yet wired to an import UI |
| Stockity realtime feed/authentication | Unverified / unavailable; no endpoint has been invented and no credentials are collected |
| Demo execution / auto trading | Disabled; no order provider or supported order API is implemented |
| Chart UI, import screen, journal, analytics dashboard | Not implemented in this milestone |
| Local Android lint/tests/build | Passed; 15 unit tests passed |
| GitHub Actions APK build | Passed for the current source commit in [run 36533751617](https://github.com/rollinkxx/stockity/actions/runs/36533751617); debug and unsigned release APKs uploaded |
| Signed release APK | Not available; release output is unsigned |
| Repository license | Not selected; `LICENSE` intentionally grants no license |

## Latest build artifacts

The successful [Android CI run 36533751617](https://github.com/rollinkxx/stockity/actions/runs/36533751617) was built from commit `29b8893017781719d264233e3a00fff837556075`. It includes the debug APK and an unsigned release APK; the debug package is `com.rollinkxx.stockitydemo.debug` (`0.1.0-debug`, min SDK 26). SHA-256 values are recorded in [the test report](docs/TEST-REPORT.md). The debug APK is development-signed; the release APK is not signed.

## Safety contract

`TradingSafety` permits a future demo order only when all of the following are true together: account mode is positively classified as `DEMO`, market data status is `VALID`, risk checks pass, and the emergency stop is inactive. `REAL`, `UNKNOWN`, stale, disconnected, or unconfigured conditions fail closed. `requireDemoTradeAllowed` throws before any future execution provider can be called. There is **no order provider** in this build.

The UI displays `ACCOUNT MODE: UNKNOWN` and `MARKET DATA: UNCONFIGURED`. Its demo-trading controls are disabled. The external website button opens `https://stockity.com/trading` in the user's browser; credentials are not entered into this app, and changing this informational link does not connect the app to Stockity.

## Stockity integration discovery

On 2026-09-29, the public [Stockity help center](https://stockity.tr/help-center/) and [platform homepage](https://stockity.tr/) were reviewed. The help page describes consumer registration and a practice account, but the public pages reviewed did not provide developer API documentation, a documented WebSocket feed, or a supported third-party demo-order interface. That does **not** prove no private or partner interface exists; none was verified here. No authenticated traffic inspection, reverse engineering, scraping, CAPTCHA/2FA bypass, or undocumented endpoint calls were attempted.

Before connecting a provider, obtain Stockity's written confirmation of an officially supported API/feed and its permitted demo-only order flow. Until then, product status is **Stockity integration: NOT VERIFIED** and **real-time data: UNAVAILABLE**. See [integration discovery](docs/STOCKITY-INTEGRATION.md), [safety invariants](docs/SAFETY.md), and [test report](docs/TEST-REPORT.md).

## Offline calculations

`IndicatorCalculator` operates only on caller-supplied price/candle values. `OfflineBacktester` has no network, account, or order interface; it labels its outputs hypothetical, requires a data-source label, an explicit payout ratio and modeled fee, restricts each signal callback to history available at that decision close, enters at the next bar's open, and avoids overlapping positions. No historical dataset or recommendation is bundled, no out-of-sample/robustness review has been run, and test fixtures are not market data.

## Build locally

Requirements: JDK 17+, Android SDK Platform 35, Android Build Tools, and network access to fetch Gradle/Maven dependencies.

```bash
./gradlew lint testDebugUnitTest assembleDebug assembleRelease
```

The debug APK is signed with Android's development debug key and is the installable development artifact. The release build is unsigned; do not treat it as a signed release. No release signing keys are configured. The repository owner must choose and add an explicit software license before licensing terms are granted.

## Project structure

- `app/src/main/.../domain/TradingSafety.kt`: fail-closed execution eligibility.
- `app/src/main/.../domain/SignalEngine.kt`: conservative `NO_TRADE` baseline.
- `app/src/main/.../domain/MarketAnalysis.kt`: validated candle and deterministic indicators.
- `app/src/main/.../domain/OfflineBacktester.kt`: isolated hypothetical simulator.
- `app/src/main/.../MainActivity.kt`: visible connection/account state and disabled controls.
- `app/src/test/...`: safety, indicator, and backtest tests.
- `docs/`: integration discovery, safety invariants, and test report.

## Known limitations

There is no verified Stockity data connection, account authentication/session, market chart, CSV import, in-app backtest screen, signal scoring strategy, demo execution, persistent journal, or analytics dashboard. The signal displayed in the app remains `NO_TRADE`. Offline backtesting is a computational engine only; it does not establish strategy efficacy and has no bundled historical data. No emulator/device installation test was available in the sandbox. No investment or profit claim is made.
