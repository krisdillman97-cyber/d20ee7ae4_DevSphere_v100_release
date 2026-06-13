#!/usr/bin/env python3
"""DevUp :: Middleware — Unified logger."""
import sys


def log(msg: str, level: str = "INFO") -> None:
    prefix = {"INFO": "→", "WARN": "⚠", "ERROR": "✘", "OK": "✔"}.get(level, "·")
    print(f"  {prefix} {msg}", file=sys.stdout if level != "ERROR" else sys.stderr)
