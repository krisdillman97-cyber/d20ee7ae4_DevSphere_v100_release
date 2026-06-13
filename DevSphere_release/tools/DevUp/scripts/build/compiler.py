#!/usr/bin/env python3
"""DevUp :: Build :: Compiler — JVM assembly via Gradle / D8 / R8."""
import json, subprocess, shutil


def compile(project_dir: str = ".") -> dict:
    gradlew = "./gradlew" if shutil.which("./gradlew") else "gradle"
    result = {"tool": gradlew, "task": "assembleRelease", "status": "SIMULATED"}
    # Uncomment to execute: subprocess.run([gradlew, "assembleRelease"], cwd=project_dir, check=True)
    return result


if __name__ == "__main__":
    import sys
    d = sys.argv[1] if len(sys.argv) > 1 else "."
    print(json.dumps(compile(d), indent=2))
