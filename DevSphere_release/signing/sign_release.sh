#!/usr/bin/env bash
# DevUp DevSphere Release Signing Script
# Uses apksigner with PEM key/cert — no JKS required
# Compatible with Android build-tools 30.0.2+
set -euo pipefail

APK="${1:-app/build/outputs/apk/release/app-release-unsigned.apk}"
OUT="${2:-app-release-signed.apk}"
KEY="signing/devsphere_key.pem"
CERT="signing/devsphere_cert.pem"
MIN_SDK=24

echo "[DevUp Sign] APK: $APK"
echo "[DevUp Sign] Key: $KEY"
echo "[DevUp Sign] Min SDK: $MIN_SDK → using v2+v3 schemes"

# Locate apksigner
if command -v apksigner &>/dev/null; then
    APKSIGNER=apksigner
elif [ -n "${ANDROID_HOME:-}" ]; then
    APKSIGNER=$(find "$ANDROID_HOME/build-tools" -name apksigner | sort -rV | head -1)
else
    echo "[DevUp Sign] ❌ apksigner not found. Set ANDROID_HOME or add build-tools to PATH."
    exit 1
fi
echo "[DevUp Sign] apksigner: $APKSIGNER"

# Copy APK to output first (apksigner signs in-place if --out not specified)
cp "$APK" "$OUT"

# Sign — v2+v3 for minSdk 24+, v4 if targeting API 33+
"$APKSIGNER" sign \
    --key "$KEY" \
    --cert "$CERT" \
    --v1-signing-enabled false \
    --v2-signing-enabled true \
    --v3-signing-enabled true \
    --in "$OUT" \
    --out "$OUT"

echo "[DevUp Sign] ✔ Signed: $OUT"

# Verify
"$APKSIGNER" verify --verbose "$OUT"
echo "[DevUp Sign] ✔ Verification passed"
