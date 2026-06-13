#!/usr/bin/env python3
"""DevUp :: Build :: Native — NDK build via ndk-build / CMake / Ninja."""
import json, os, shutil


def run_ndk_build(project_dir: str = ".") -> dict:
    ndk_home = os.environ.get("ANDROID_NDK_HOME", os.environ.get("NDK_HOME", ""))
    tool = "ndk-build"
    if ndk_home:
        candidate = os.path.join(ndk_home, "ndk-build")
        if os.path.isfile(candidate):
            tool = candidate
    result = {
        "tool": tool,
        "abi": "arm64-v8a",
        "ndk_home": ndk_home or "not set",
        "status": "SIMULATED",
    }
    # Uncomment to execute: subprocess.run([tool, "-C", project_dir], check=True)
    return result


if __name__ == "__main__":
    import sys
    d = sys.argv[1] if len(sys.argv) > 1 else "."
    print(json.dumps(run_ndk_build(d), indent=2))
