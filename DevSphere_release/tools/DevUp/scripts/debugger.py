#!/usr/bin/env python3
"""DevUp :: Debugger — ADB/JDWP debug session manager."""
import json


def debug(target: str = "localhost:8600") -> dict:
    return {"session": "ready", "target": target, "protocol": "JDWP"}


if __name__ == "__main__":
    import sys
    t = sys.argv[1] if len(sys.argv) > 1 else "localhost:8600"
    print(json.dumps(debug(t), indent=2))
