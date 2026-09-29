# Demo-Only Safety Invariants

The app's current default state is `AccountMode.UNKNOWN` and `MarketDataStatus.UNCONFIGURED`. It does not infer DEMO from a user-selected label, balance size, URL, or locally stored preference.

`TradingSafety.mayOpenDemoTrade` returns true only if account mode is explicitly `DEMO`, the feed is `VALID`, risk checks passed, and the emergency stop is not active. `REAL`, `UNKNOWN`, stale data, disconnected data, unconfigured data, failed risk checks, and emergency-stop state all block. `requireDemoTradeAllowed` throws before any future execution provider can be called.

There is no order provider in this build; UI controls are disabled and the app sends no order requests. Tests cover account/data/risk/emergency-stop gates and the throwing guard. Any future network adapter must preserve these constraints at its own boundary, not rely only on UI visibility.

`OfflineBacktester` is a separate hypothetical calculation with no network or account access. It requires an explicit source label, payout assumption, and modeled fee; decisions see only the candle prefix available at that close, hypothetical entries use the next candle's open, and simulated positions do not overlap. It does not validate source quality, adjust corporate actions, model venue liquidity, run robustness checks, or demonstrate profitability. Imported/user data, when supported, must be labeled and preserved as non-verified input. No backtest result is a live signal or an order instruction.
