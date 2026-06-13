#!/usr/bin/env python3
"""DevUp :: Core Telemetry — metric logging across all agent domains."""
import json, datetime


def log_metric(agent_id: str, metric: str, value) -> None:
    """Emit a telemetry record for a given agent and metric."""
    record = {
        "ts": datetime.datetime.utcnow().isoformat() + "Z",
        "agent_id": agent_id,
        "metric": metric,
        "value": value,
    }
    print(json.dumps(record))
