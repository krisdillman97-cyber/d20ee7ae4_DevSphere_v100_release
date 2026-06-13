#!/usr/bin/env bash
# DevUp :: Main Router — root command: DevUp
# Usage: DevUp <sub-command> [args...]
set -euo pipefail

DEVUP_ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
DEVUP_VERSION="1.0.0"

usage() {
cat << HELPEOF
DevUp v${DEVUP_VERSION} — Android SDLC Skill (Glypheral Studios)

Usage: DevUp <command> [options]

Commands:
  build           NDK native → Gradle JVM → Bundle pipeline
  lint            Java/Kotlin + XML + security lint
  analyze [file]  ADR Matrix 8,400-agent sweep
  sign <apk>      Sign with apksigner (auto v1/v2/v3/v4)
  publish         Cloud/Drive sync of signed artifact
  debug [target]  ADB/JDWP debug session manager
  dry-run         Full pipeline simulation (no side effects)
  ruut            PCRUUT Termux full-bridge engine
  status          Registry + telemetry health check
  spawn [n]       Spawn N parallel sub-agents
  repair          Self-heal broken pipeline stages
  help            Show this message

Signing:
  DevUp sign app-release.apk
  DevUp sign app.apk --ks release.keystore --ks-pass mypass --min-sdk 33

HELPEOF
}

CMD="${1:-help}"
shift || true

case "$CMD" in
  build)
    echo "[DevUp] 🔨 Build pipeline starting..."
    python3 "$DEVUP_ROOT/build/native_build.py" "$@"
    python3 "$DEVUP_ROOT/build/compiler.py" "$@"
    python3 "$DEVUP_ROOT/build/bundler.py" "$@"
    echo "[DevUp] ✔ Build complete"
    ;;

  lint)
    echo "[DevUp] 🔍 Lint sweep..."
    python3 "$DEVUP_ROOT/lint/lint_java.py" "$@"
    python3 "$DEVUP_ROOT/lint/lint_xml.py" "$@"
    python3 "$DEVUP_ROOT/lint/security_check.py" "$@"
    echo "[DevUp] ✔ All lint checks passed"
    ;;

  analyze)
    echo "[DevUp] 🧠 ADR Matrix 8,400 sweep..."
    python3 "$DEVUP_ROOT/analyzer.py" ${1:+-f "$1"}
    ;;

  sign)
    APK="${1:-}"
    if [ -z "$APK" ]; then echo "[DevUp] ❌ Usage: DevUp sign <apk.apk> [options]"; exit 1; fi
    shift
    echo "[DevUp] ✍️  Signing with apksigner..."
    python3 "$DEVUP_ROOT/sign/signer.py" "$APK" "$@"
    ;;

  publish)
    echo "[DevUp] ☁️  Publishing artifact..."
    python3 "$DEVUP_ROOT/sign/publish/drive_sync.py" "$@"
    ;;

  debug)
    echo "[DevUp] 🐛 Debug session..."
    python3 "$DEVUP_ROOT/debugger.py" "${1:-localhost:8600}"
    ;;

  dry-run|dry_run)
    echo "[DevUp] 🔄 Dry-run simulation..."
    python3 "$DEVUP_ROOT/dry_run.py"
    ;;

  ruut)
    echo "[DevUp] ⚡ PCRUUT Termux engine activating..."
    python3 "$DEVUP_ROOT/reasoner.py" --mode ruut "$@"
    ;;

  status)
    echo "[DevUp] 📊 Status check..."
    python3 "$DEVUP_ROOT/registry.py"
    ;;

  spawn)
    N="${1:-4}"
    echo "[DevUp] 🌐 Spawning $N agents..."
    python3 "$DEVUP_ROOT/spawn.py" "$N"
    ;;

  repair)
    echo "[DevUp] 🛠️  Self-healing pipeline..."
    python3 "$DEVUP_ROOT/repairman.py" "$@"
    ;;

  help|--help|-h)
    usage
    ;;

  *)
    echo "[DevUp] Unknown command: $CMD"
    usage
    exit 1
    ;;
esac
