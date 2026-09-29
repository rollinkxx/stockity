# Test Report

## Local verification — 2026-09-29

Command: `./gradlew --no-daemon lint testDebugUnitTest assembleDebug assembleRelease`

The local build and both APK assembly tasks passed after the website-button change. Unit tests: **15 passed, 0 failures, 0 errors, 0 skipped**. Lint passed with one non-fatal advisory: `androidx.activity:activity-compose:1.10.0` has a newer stable version available (1.13.0); no other lint findings remain.

## GitHub Actions verification — 2026-09-29

The complete Android CI workflow passed for commit `1d58734f2a488909e096f55fd55268c4ba350f1b` in [run 36567388135](https://github.com/rollinkxx/stockity/actions/runs/36567388135). SDK setup, lint and unit tests, debug/release assembly, checksum generation, and artifact upload all completed successfully. The current debug APK includes `https://stockity.com/trading`; this was confirmed in the compiled DEX. Both downloaded APKs match the CI checksum file. The debug APK signature and 4-byte zip alignment were verified; its package is `com.rollinkxx.stockitydemo.debug`, versionCode 1, versionName `0.1.0-debug`, min SDK 26, target SDK 35. It is signed with the Android debug key. The release APK is intentionally unsigned.

| Artifact | SHA-256 |
|---|---|
| `StockityDemoTrader-debug.apk` | `83f588a6f1765952b54130321694393eaace766d76961a43e9a30cf9aba6d07c` |
| `StockityDemoTrader-release-unsigned.apk` | `4b7041afa75ea6154b867287fbd4ff415c21dd3de0d9ef7eeb466936f2834d75` |

## Scope and data boundary

The URL correction changes only the informational website button. It does not add Stockity login, market data, chart scraping, signal generation from live data, or order execution. No emulator/device installation test was available.
