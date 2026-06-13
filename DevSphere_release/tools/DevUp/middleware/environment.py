#!/usr/bin/env python3
"""DevUp :: Middleware — Environment resolver for NDK, SDK, Gradle paths."""
import os


def get_ndk_path() -> str:
    candidates = [
        os.environ.get("ANDROID_NDK_HOME", ""),
        os.environ.get("NDK_HOME", ""),
        os.path.join(os.environ.get("ANDROID_HOME", ""), "ndk-bundle"),
    ]
    for p in candidates:
        if p and os.path.isdir(p):
            return p
    return "/path/to/ndk"


def get_sdk_path() -> str:
    return os.environ.get("ANDROID_HOME", os.environ.get("ANDROID_SDK_ROOT", "/path/to/sdk"))


def get_build_tools_path(version: str = "") -> str:
    sdk = get_sdk_path()
    bt_root = os.path.join(sdk, "build-tools")
    if not os.path.isdir(bt_root):
        return os.path.join(sdk, "build-tools", version or "latest")
    versions = sorted(os.listdir(bt_root), reverse=True)
    if versions:
        return os.path.join(bt_root, version if version in versions else versions[0])
    return os.path.join(bt_root, version or "latest")


def get_apksigner_path() -> str:
    import shutil
    if shutil.which("apksigner"):
        return "apksigner"
    bt = get_build_tools_path()
    candidate = os.path.join(bt, "apksigner")
    return candidate if os.path.isfile(candidate) else "apksigner"
