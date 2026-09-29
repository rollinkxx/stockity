# Stockity Demo Trader (Android)

A native Kotlin + Jetpack Compose foundation for a **demo-only** trading/research terminal. This is an early safety-first milestone, not a complete trading product. The current app fails closed: it does not log in to Stockity, show made-up or historical prices as live, or submit orders.

## Current status

| Area | Status |
|---|---|
| Native Android Compose shell | Implemented |
| Demo-only safety policy | Implemented and unit-tested; default account is `UNKNOWN` |
| Signal behavior | Always `NO_TRADE` until valid reviewed inputs exist |
| Stockity realtime feed | Unverified / unavailable; no endpoint has been invented |
| Stockity authentication | Not implemented; the app does not collect credentials |
| Demo execution / auto trading | Disabled; no order API has been verified |
| Backtesting, chart, journal, analytics | Not implemented in this milestone |
| Local Android lint/tests/build | Passed; five unit tests passed |
| GitHub Actions APK build | Passed in [run 36532349372](https://github.com/rollinkxx/stockity/actions/runs/36532349372) |
| Signed release APK | Not available; release output is unsigned |
| Repository license | Not selected; `LICENSE` intentionally grants no license |

## Safety contract

`TradingSafety` permits a future order only when all of the following are true together: account mode is positively classified as `DEMO`, market data status is `VALID`, risk checks pass, and the emergency stop is inactive. `REAL`, `UNKNOWN`, stale, disconnected, or unconfigured conditions fail closed. `requireDemoTradeAllowed` throws before any future execution provider can be called.

The UI displays `ACCOUNT MODE: UNKNOWN` and `MARKET DATA: UNCONFIGURED`. Its demo-trading controls are disabled. The external website button only opens Stockity's public website in the user's browser; credentials are not entered into this app.

## Stockity integration discovery

On 2026-09-29, the public [Stockity help center](https://stockity.tr/help-center/) and [platform homepage](https://stockity.tr/) were reviewed. The help page describes consumer registration and a practice account, but the public pages reviewed did not provide developer API documentation, a documented WebSocket feed, or a supported third-party demo-order interface. That does **not** prove no private or partner interface exists; none was verified here. No authenticated traffic inspection, reverse engineering, scraping, CAPTCHA/2FA bypass, or undocumented endpoint calls were attempted.

Before connecting a provider, obtain Stockity's written confirmation of an officially supported API/feed and its permitted demo-only order flow. Until then, product status is **Stockity integration: NOT VERIFIED** and **real-time data: UNAVAILABLE**. See [integration discovery](docs/STOCKITY-INTEGRATION.md) and [safety invariants](docs/SAFETY.md).

## Build locally

Requirements: JDK 17+, Android SDK Platform 35, Android Build Tools, and network access to fetch Gradle/Maven dependencies.

```bash
./gradlew lint testDebugUnitTest assembleDebug assembleRelease
```

The debug APK is signed with Android's development debug key and is the installable development artifact. The release build is unsigned; do not treat it as a signed release. No release signing keys are configured. The repository owner must choose and add an explicit software license before licensing terms are granted.

## GitHub Actions artifacts

The successful [Android CI run](https://github.com/rollinkxx/stockity/actions/runs/36532349372) uploaded debug and unsigned release APKs plus `SHA256SUMS.txt`; artifacts are retained by GitHub Actions for 30 days. The exact local copies and their checksums are in the task artifacts directory. The CI workflow runs lint, unit tests, debug/release assembly, checksum generation, and artifact upload on main pushes, pull requests, and manual dispatch.

## Project structure

- `app/src/main/.../domain/TradingSafety.kt`: fail-closed execution eligibility.
- `app/src/main/.../domain/SignalEngine.kt`: conservative `NO_TRADE` baseline.
- `app/src/main/.../MainActivity.kt`: visible connection/account state and disabled controls.
- `app/src/test/...`: account/data/risk/emergency-stop and signal baseline tests.
- `docs/`: integration discovery, safety invariants, and test report.

## Known limitations

This build has no verified Stockity data connection, authentication/session handling, chart, indicators, strategy scoring, trade execution, backtest engine, persistent journal, or analytics dashboard. The tested signal engine remains `NO_TRADE`; no performance or profit claim is made. A live feed must not be substituted with generated values while labeled as Stockity data. No emulator/device installation test was available in the sandbox.
