#!/usr/bin/env python3
"""DevUp :: Spawn — Agent spawner for targeted domain/API execution."""
import json, sys


def spawn(domain: str = "all", api: str = "latest", role: str = "analyzer") -> dict:
    return {"spawned": True, "domain": domain, "api": api, "role": role}


if __name__ == "__main__":
    domain = sys.argv[1] if len(sys.argv) > 1 else "all"
    api    = sys.argv[2] if len(sys.argv) > 2 else "latest"
    role   = sys.argv[3] if len(sys.argv) > 3 else "analyzer"
    print(json.dumps(spawn(domain, api, role), indent=2))
