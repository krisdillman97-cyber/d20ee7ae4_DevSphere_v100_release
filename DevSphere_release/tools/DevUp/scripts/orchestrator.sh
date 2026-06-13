#!/usr/bin/env bash
# DevUp :: Crew Orchestrator — Full SDLC pipeline shell driver
set -euo pipefail
echo '[DevUp Crew] Starting build pipeline...'
echo '[DevUp Crew] Phase 1: Native NDK build'
python3 "$(dirname "$0")/build/native_build.py"
echo '[DevUp Crew] Phase 2: JVM/Gradle assembly'
python3 "$(dirname "$0")/build/compiler.py"
echo '[DevUp Crew] Phase 3: Bundle'
python3 "$(dirname "$0")/build/bundler.py"
echo '[DevUp Crew] Phase 4: Sign (apksigner)'
echo '[DevUp Crew] Phase 5: Publish'
python3 "$(dirname "$0")/sign/publish/drive_sync.py"
echo '[DevUp Crew] Pipeline complete.'
