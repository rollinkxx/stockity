# Test Report

## Local verification — 2026-09-29

Command: `./gradlew --no-daemon lint testDebugUnitTest assembleDebug assembleRelease`

- Build and both APK assembly tasks: **passed**.
- Unit tests: **5 passed, 0 failures, 0 errors, 0 skipped**.
- Lint: **passed with one non-fatal dependency advisory** — `androidx.activity:activity-compose:1.10.0` has a newer stable version available (1.13.0). No other lint findings remain.
- Debug APK: signature verified with Android's debug certificate; `zipalign` check passed; package metadata verified as `com.rollinkxx.stockitydemo.debug`, versionCode 1, versionName `0.1.0-debug`, min SDK 26, target SDK 35.
- Release APK: assembled as unsigned; not a signed/distribution-ready release.

## GitHub Actions

The first run failed in the Android SDK setup action because it attempted to install the legacy SDK package named `tools`, which the current runner's SDK manager no longer publishes. The workflow was changed to use the runner's preinstalled SDK manager. The second run accepted SDK licenses but the `yes` process then returned a broken-pipe status, causing the shell's `pipefail` to fail the step. The workflow now tolerates that expected license-input pipe exit while keeping the actual platform/build-tools installation fail-fast. A new remote CI run is pending; no hosted artifact is claimed yet.
