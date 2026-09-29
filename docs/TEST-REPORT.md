# Test Report

## Local verification — 2026-09-29

Command: `./gradlew --no-daemon lint testDebugUnitTest assembleDebug assembleRelease`

The build and both APK assembly tasks passed. Unit tests: 5 passed, 0 failures, 0 errors, 0 skipped. Lint passed with one non-fatal dependency advisory: `androidx.activity:activity-compose:1.10.0` has a newer stable version available (1.13.0); no other lint findings remain. The local debug APK signature and alignment were verified; package metadata is `com.rollinkxx.stockitydemo.debug`, versionCode 1, versionName `0.1.0-debug`, min SDK 26, target SDK 35. The release APK is unsigned.

## GitHub Actions verification

The complete Android CI workflow passed on commit `783b8942800acba12df707c22fb4dbbbeea3ab1c`: [run 36532349372](https://github.com/rollinkxx/stockity/actions/runs/36532349372). Android SDK setup, lint and unit tests, debug/release assembly, checksum generation, and artifact upload all completed successfully. The uploaded APK checksums were verified after download. The debug APK signature and `zipalign` check passed; the release APK is intentionally unsigned.

| Artifact | SHA-256 |
|---|---|
| `StockityDemoTrader-debug.apk` | `15398cfd68d1896e35a63ba2a0787ad379266b9654d728b31ec49233aa1d9af6` |
| `StockityDemoTrader-release-unsigned.apk` | `aee34cbdd3406116bd465d5ab026a029633d951f2d9aade1345e1e0c67d7bbef` |

No Stockity real-time feed, authenticated session, demo order execution, or install-on-device test was part of these checks.
