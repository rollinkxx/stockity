# Stockity Demo Trader (Android)

A native Kotlin + Jetpack Compose foundation for a **demo-only** trading/research terminal. The current app intentionally fails closed: it does not log in to Stockity, show made-up or historical prices as live, or submit any orders.

## Current status

| Area | Status |
|---|---|
| Native Android Compose shell | Implemented |
| Demo-only safety policy | Implemented and unit-tested; default account is `UNKNOWN` |
| Signal behavior | Always `NO_TRADE` until valid reviewed inputs exist |
| Stockity realtime feed | Unverified / unavailable; no endpoint has been invented |
| Stockity authentication | Not implemented; the app does not collect credentials |
| Demo execution / auto trading | Disabled; no order API has been verified |
| Backtesting, chart, journal, analytics | Not implemented in this first milestone |
| GitHub Actions APK build | Configured |
| Signed release APK | Not configured; release output is unsigned unless signing is deliberately added later |

## Safety contract

`TradingSafety` permits an order only when all of the following are true at the same time: account mode is positively classified as `DEMO`, market data status is `VALID`, risk checks pass, and the emergency stop is inactive. `REAL`, `UNKNOWN`, stale, disconnected, or unconfigured conditions fail closed. The current app does not contain an execution provider, so even this policy does not cause a network order to be sent.

The UI displays `ACCOUNT MODE: UNKNOWN` and `MARKET DATA: UNCONFIGURED`. Its demo-trading controls are disabled. The external website button only opens Stockity's public website in the user's browser; credentials are not entered into this app.

## Stockity integration discovery

On 2026-09-29, the public [Stockity help center](https://stockity.tr/help-center/) and [platform homepage](https://stockity.tr/) were reviewed. The help page describes consumer registration and a practice account, but the public pages reviewed did not provide developer API documentation, a documented WebSocket feed, or a supported third-party demo-order interface. That does **not** prove no private or partner interface exists; it means none has been verified here. No account login, authenticated traffic inspection, reverse engineering, scraping, CAPTCHA/2FA bypass, or undocumented endpoint calls were attempted.

Before connecting a provider, obtain Stockity's written confirmation of an officially supported API/feed and its permitted demo-only order flow. Keep any adapter isolated from the strategy code, validate account mode server-side/provider-side for every request, and preserve this app's independent fail-closed order guard. Until then, the product status is **Stockity integration: NOT VERIFIED** and **real-time data: UNAVAILABLE**.

## Build locally

Requirements: JDK 17+, Android SDK Platform 35, Android Build Tools, and network access to fetch Gradle/Maven dependencies.

```bash
./gradlew lint testDebugUnitTest assembleDebug assembleRelease
```

The debug APK uses Android's debug signing configuration and is the installable development artifact. The release build is currently unsigned; do not treat it as a signed release. Release signing credentials must be provisioned through GitHub Actions secrets and a reviewed signing configuration before distribution.

## CI artifacts

`.github/workflows/android.yml` runs lint, local unit tests, and both debug/release assembly on pushes to `main`, pull requests, and manual dispatch. It renames and uploads APK artifacts and writes SHA-256 checksums. GitHub Actions does not receive Stockity credentials or signing keys in this first milestone.

## Project structure

- `app/src/main/.../domain/TradingSafety.kt`: fail-closed execution eligibility.
- `app/src/main/.../domain/SignalEngine.kt`: conservative `NO_TRADE` baseline.
- `app/src/main/.../MainActivity.kt`: visible connection/account state and disabled controls.
- `app/src/test/...`: account/data/risk/emergency-stop and signal baseline tests.

## Risks and limitations

This is an early milestone, not a feature-complete trading product. The app has no verified Stockity data connection, authentication/session handling, chart, indicators, strategy scoring, trade execution, backtest engine, persistent journal, or analytics dashboard. No performance or profit claim is made. A live feed must not be substituted with generated values while it is labeled as Stockity data.
