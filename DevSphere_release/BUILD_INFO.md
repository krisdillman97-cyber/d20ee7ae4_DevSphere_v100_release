# DevSphere Release Build
**Date:** 2026-06-13  
**Studio:** Glypheral Studios  
**Package:** com.glypheral.devsphere  
**Version:** 1.0.0 (build 1)  
**Min SDK:** 24 (Android 7.0)  
**Target SDK:** 34 (Android 14)  
**ABIs:** arm64-v8a, armeabi-v7a, x86_64

## Build Tool
**DevUp v1.0.0** — root command `DevUp`

## Signing
- **Tool:** apksigner (Android build-tools 35.0.0)
- **Scheme:** v2 + v3 (minSdk 24)
- **Key:** RSA-2048, SHA256withRSA
- **Cert:** Glypheral Studios / DevSphere Release Key (10,000 days)
- **Key files:** `signing/devsphere_key.pem`, `signing/devsphere_cert.pem`
- **Sign script:** `signing/sign_release.sh`

## To build and sign on your machine:
```bash
# 1. Open in Android Studio or run Gradle
./gradlew assembleRelease

# 2. Sign with DevUp
DevUp sign app/build/outputs/apk/release/app-release-unsigned.apk \
    --ks-pass DevSphere2026

# 3. Or use the sign script directly
bash signing/sign_release.sh app/build/outputs/apk/release/app-release-unsigned.apk
```

## User Roles
| Role | Price | Features |
|------|-------|---------|
| Guest | Free | Browse app only |
| Free | Free | Basic browser |
| Developer | $40/mo or $1,200 lifetime | Full devtools, server manager, code editor, downloader |
| Admin | $150/mo or $2,400 lifetime | All Dev features + user mgmt, billing, API keys, hidden dev menu |

## Modules (ingested via DevUp)
- ADR Matrix 8,400 (adr_matrix_8400)
- Deep Matrix Studio (android_studio_deep_matrix_opt1)  
- Crew Orchestrator (crew-orchestrator)
- PCRUUT Termux Bridge (pcruut.sh)
