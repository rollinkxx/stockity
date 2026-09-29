# Demo-Only Safety Invariants

The app's current default state is `AccountMode.UNKNOWN` and `MarketDataStatus.UNCONFIGURED`. It does not infer DEMO from a user-selected label, balance size, URL, or locally stored preference.

`TradingSafety.mayOpenDemoTrade` returns true only if account mode is explicitly `DEMO`, the feed is `VALID`, risk checks passed, and the emergency stop is not active. `REAL`, `UNKNOWN`, stale data, disconnected data, unconfigured data, failed risk checks, and emergency-stop state all block. `requireDemoTradeAllowed` throws before any future execution provider can be called.

There is no order provider in this build; UI controls are disabled and the app sends no order requests. The tests cover real/unknown accounts, stale/disconnected/unconfigured data, risk failure, emergency stop, and the throwing guard. Any future network adapter must preserve these constraints at its own boundary, not rely only on UI visibility.
