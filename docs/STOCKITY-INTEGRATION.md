# Stockity Integration Discovery

**Discovery date:** 2026-09-29

**Current status:** Not verified; no Stockity integration is implemented.

## Public materials reviewed

- [Stockity help center](https://stockity.tr/help-center/): consumer registration, practice account, account/security topics, and product FAQs.
- [Stockity platform homepage](https://stockity.tr/): consumer-facing platform information.

The public pages reviewed do not publish a developer API reference, a market-data WebSocket specification, or documented third-party demo-order endpoints. This is a statement about the sources reviewed, not proof that Stockity has no private, partner, or support-provided interface.

## Discovery results

| Capability | Finding |
|---|---|
| Official login / registration | Consumer website flow is described; third-party app auth is not documented. |
| Verified demo-mode detection | No public integration contract found. App therefore uses `UNKNOWN`. |
| Asset listing and availability | No public API specification found. |
| Realtime quotes / candles / OHLC | No public feed contract or supported WebSocket details found. |
| Expiry and market status | No developer interface documented on the pages reviewed. |
| Demo balance / order submission / outcome events | No supported third-party demo trading API documented. |

## Constraints and next steps

Do not derive or guess endpoints from site behavior, inspect authenticated traffic, scrape private interfaces, evade rate limits, or bypass CAPTCHA/2FA. Contact Stockity through its official support channel and request written confirmation of a developer-supported **demo-only** interface, including authentication, mode verification, rate limits, licensing/redistribution, quote/candle semantics, order idempotency, and account-scope enforcement.

If Stockity confirms an official interface, implement it behind `MarketDataProvider`, `AuthenticationProvider`, and `TradingProvider` abstractions. Confirm the account mode on the provider boundary for every attempted operation, treat all uncertainty as blocked, enforce stale/out-of-order data checks, and add contract tests before enabling any UI control. Until those conditions are met, live data and trade execution remain unavailable and the app reports `NO_TRADE`.
