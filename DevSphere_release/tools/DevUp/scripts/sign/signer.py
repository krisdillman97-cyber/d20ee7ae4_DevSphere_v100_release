#!/usr/bin/env python3
"""DevUp :: Sign :: Signer — apksigner wrapper.

Signs APKs using apksigner (Android SDK build-tools).
Selects v1/v2/v3/v4 schemes automatically based on min SDK.
"""
import argparse, json, os, shutil, subprocess, sys


def find_apksigner() -> str:
    if shutil.which("apksigner"):
        return "apksigner"
    android_home = os.environ.get("ANDROID_HOME", os.environ.get("ANDROID_SDK_ROOT", ""))
    bt_root = os.path.join(android_home, "build-tools")
    if os.path.isdir(bt_root):
        for ver in sorted(os.listdir(bt_root), reverse=True):
            candidate = os.path.join(bt_root, ver, "apksigner")
            if os.path.isfile(candidate):
                return candidate
    raise FileNotFoundError("apksigner not found. Set ANDROID_HOME or add build-tools to PATH.")


def sign(
    apk: str,
    keystore: str = "",
    ks_pass: str = "android",
    ks_alias: str = "androiddebugkey",
    key_pass: str = "",
    min_sdk: int = 24,
    verify: bool = True,
) -> dict:
    signer = find_apksigner()
    cmd = [signer, "sign"]

    # Signature scheme selection
    if min_sdk >= 33:
        cmd += ["--v1-signing-enabled", "false", "--v2-signing-enabled", "true",
                "--v3-signing-enabled", "true",  "--v4-signing-enabled", "true"]
        scheme = "v2/v3/v4"
    elif min_sdk >= 28:
        cmd += ["--v1-signing-enabled", "false", "--v2-signing-enabled", "true",
                "--v3-signing-enabled", "true"]
        scheme = "v2/v3"
    elif min_sdk >= 24:
        cmd += ["--v1-signing-enabled", "false", "--v2-signing-enabled", "true",
                "--v3-signing-enabled", "false"]
        scheme = "v2"
    else:
        cmd += ["--v1-signing-enabled", "true", "--v2-signing-enabled", "true",
                "--v3-signing-enabled", "false"]
        scheme = "v1+v2"

    # Keystore
    if not keystore:
        keystore = os.path.expanduser("~/.android/debug.keystore")
    cmd += ["--ks", keystore, "--ks-pass", f"pass:{ks_pass}", "--ks-key-alias", ks_alias,
            "--key-pass", f"pass:{key_pass or ks_pass}"]
    cmd.append(apk)

    result = {"apk": apk, "signer": signer, "scheme": scheme, "min_sdk": min_sdk}
    try:
        subprocess.run(cmd, check=True, capture_output=True, text=True)
        result["signed"] = True
        if verify:
            v = subprocess.run([signer, "verify", "--verbose", apk],
                               capture_output=True, text=True)
            result["verified"] = v.returncode == 0
            result["verify_output"] = v.stdout.strip()
    except subprocess.CalledProcessError as e:
        result["signed"] = False
        result["error"] = e.stderr.strip()
    return result


if __name__ == "__main__":
    p = argparse.ArgumentParser(description="DevUp apksigner wrapper")
    p.add_argument("apk")
    p.add_argument("--ks", default="", dest="keystore")
    p.add_argument("--ks-pass", default="android")
    p.add_argument("--ks-key-alias", default="androiddebugkey")
    p.add_argument("--key-pass", default="")
    p.add_argument("--min-sdk", type=int, default=24)
    p.add_argument("--no-verify", action="store_true")
    args = p.parse_args()
    print(json.dumps(sign(
        args.apk, args.keystore, args.ks_pass,
        args.ks_key_alias, args.key_pass, args.min_sdk,
        not args.no_verify,
    ), indent=2))
