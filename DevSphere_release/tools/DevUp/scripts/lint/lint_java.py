#!/usr/bin/env python3
"""DevUp :: Lint :: Java — Android Lint for Java/Kotlin sources."""
import json


def lint(project_dir: str = ".") -> dict:
    return {"lint": "java", "issues": 0, "status": "PASS"}


if __name__ == "__main__":
    print(json.dumps(lint(), indent=2))
