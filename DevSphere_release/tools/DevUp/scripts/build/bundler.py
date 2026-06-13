#!/usr/bin/env python3
"""DevUp :: Build :: Bundler — APK / AAB bundle assembly."""
import json, sys


def bundle(artifact: str = "app-release.apk", format: str = "apk") -> dict:
    return {"artifact": artifact, "format": format, "status": "SIMULATED"}


if __name__ == "__main__":
    art = sys.argv[1] if len(sys.argv) > 1 else "app-release.apk"
    fmt = sys.argv[2] if len(sys.argv) > 2 else "apk"
    print(json.dumps(bundle(art, fmt), indent=2))
