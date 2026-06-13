#!/usr/bin/env python3
"""DevUp :: Registry — Agent checkpoint registry (NDK v27 / Gradle 8.x compliant)."""
import json, datetime


def checkpoint(agent_id: str = "devup", status: str = "OK") -> dict:
    return {
        "agent_id": agent_id,
        "status": status,
        "ts": datetime.datetime.utcnow().isoformat() + "Z",
        "ndk_compliance": "v27",
        "gradle_compliance": "8.x",
    }


if __name__ == "__main__":
    print(json.dumps(checkpoint(), indent=2))
