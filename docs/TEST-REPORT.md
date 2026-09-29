# Test Report

## Local verification — 2026-09-29

Command: `./gradlew --no-daemon lint testDebugUnitTest assembleDebug assembleRelease`

- Build and both APK assembly tasks: **passed**.
- Unit tests: **5 passed, 0 failures, 0 errors, 0 skipped**.
- Lint: **passed with one non-fatal dependency advisory** — `androidx.activity:activity-compose:1.10.0` has a newer stable version available (1.13.0). No other lint findings remain.
- Debug APK: signature verified with Android's debug certificate; `zipalign` check passed; package metadata verified as `com.rollinkxx.stockitydemo.debug`, versionCode 1, versionName `0.1.0-debug`, min SDK 26, target SDK 35.
- Release APK: assembled as unsigned; not a signed/distribution-ready release.

The GitHub Actions run is pending the initial push to the selected empty repository. Its result and artifact status will be appended after CI completes.
