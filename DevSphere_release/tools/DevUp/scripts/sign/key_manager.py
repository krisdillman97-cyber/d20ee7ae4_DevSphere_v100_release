#!/usr/bin/env python3
"""DevUp :: Sign :: Key Manager — Keystore and key alias manager for apksigner."""
import json, os


def get_key(alias: str = "androiddebugkey") -> dict:
    debug_ks = os.path.expanduser("~/.android/debug.keystore")
    return {
        "alias": alias,
        "keystore": debug_ks if os.path.isfile(debug_ks) else None,
        "signer": "apksigner",
    }


if __name__ == "__main__":
    import sys
    alias = sys.argv[1] if len(sys.argv) > 1 else "androiddebugkey"
    print(json.dumps(get_key(alias), indent=2))
