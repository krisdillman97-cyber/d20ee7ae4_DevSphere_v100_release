# DevUp — The Android Developer SDLC Skill
**Root command:** `DevUp`  
**Location:** `.agents/skills/DevUp/`  
**Version:** 1.0.0  
**Signing tool:** `apksigner` (Android build-tools)

---

## Overview
DevUp is a unified Android Software Development Lifecycle (SDLC) skill that ingests and orchestrates:
- **ADR Matrix 8,400** — 8,400-agent reasoning array (adr_matrix_8400)
- **Android Studio Deep Matrix** — Studio integration layer (android_studio_deep_matrix_opt1)
- **Crew Orchestrator** — Multi-agent pipeline driver (crew-orchestrator)
- **PCRUUT** — Termux-native full bridge engine (pcruut.sh)

---

## Sub-commands

| Command | Description |
|---------|-------------|
| `DevUp build` | NDK native → Gradle JVM → Bundle |
| `DevUp lint` | Java/Kotlin lint + XML + security scan |
| `DevUp analyze [file]` | ADR Matrix 8,400-agent analysis sweep |
| `DevUp sign <apk>` | Sign with **apksigner** (v1/v2/v3/v4 auto-select by minSdk) |
| `DevUp publish` | Drive/cloud sync of signed artifact |
| `DevUp debug [target]` | ADB/JDWP debug session |
| `DevUp dry-run` | Full pipeline simulation (no side effects) |
| `DevUp ruut` | Activate PCRUUT Termux full-bridge engine |
| `DevUp status` | Registry + telemetry health check |
| `DevUp spawn [n]` | Spawn N parallel sub-agents |
| `DevUp repair` | Self-healing: detect + fix broken pipeline stages |

---

## Signing (apksigner)

DevUp uses **apksigner** exclusively (not jarsigner). Signature scheme is auto-selected:

| minSdk | Schemes enabled |
|--------|----------------|
| < 24   | v1 + v2        |
| 24–27  | v2             |
| 28–32  | v2 + v3        |
| ≥ 33   | v2 + v3 + v4   |

PEM key/cert supported directly (`--key key.pem --cert cert.pem`).  
Release keystore: `signing/devsphere_key.pem` + `signing/devsphere_cert.pem`

---

## Architecture

```
DevUp/
├── SKILL.md              ← This file
├── scripts/
│   ├── run.sh            ← Main entry point / router
│   ├── orchestrator.sh   ← Full pipeline shell driver (Crew Orchestrator)
│   ├── analyzer.py       ← ADR Matrix 8,400-agent sweep
│   ├── reasoner.py       ← Deep Matrix reasoning layer
│   ├── repairman.py      ← Self-healing pipeline repair
│   ├── researcher.py     ← Context research engine
│   ├── registry.py       ← Agent registry + health
│   ├── spawn.py          ← Sub-agent spawner
│   ├── debugger.py       ← ADB/JDWP debug manager
│   ├── dry_run.py        ← Pipeline simulation
│   ├── build/
│   │   ├── native_build.py   ← NDK / ndk-build / CMake
│   │   ├── compiler.py       ← Gradle / D8 / R8
│   │   └── bundler.py        ← APK / AAB bundler
│   ├── lint/
│   │   ├── lint_java.py      ← Java/Kotlin lint
│   │   ├── lint_xml.py       ← Manifest/resource XML lint
│   │   └── security_check.py ← Static security scan
│   └── sign/
│       ├── key_manager.py    ← Keystore / PEM key manager
│       ├── signer.py         ← apksigner wrapper (PRIMARY)
│       └── publish/
│           └── drive_sync.py ← Cloud/Drive sync
├── core/
│   ├── security.py           ← Encryption / token safety
│   └── telemetry.py          ← Metrics / tracing
├── middleware/
│   ├── base44_codec.py       ← Base44 agent protocol codec
│   ├── environment.py        ← Env detection (Android/Termux/CI)
│   └── logger.py             ← Structured logging
└── native/
    ├── Android.mk            ← NDK Makefile
    ├── Application.mk        ← NDK ABI config (arm64, x86_64)
    └── jni/
        └── main.cpp          ← JNI bridge entry point
```

---

## PCRUUT (ruut command)

PCRUUT is a Termux-native build acceleration engine sourced from `pcruut.sh`.

What it does when activated:
1. **Environment bootstrap** — detects Termux vs CI vs Docker host
2. **Dependency resolver** — auto-installs missing NDK toolchain packages
3. **Parallel executor** — spawns multi-core build workers
4. **ADB bridge** — connects to attached/wireless Android devices
5. **Hot-reload pipeline** — watches source changes and re-triggers build
6. **Output router** — streams logs to console + stores in `~/.devup/ruut.log`

---

## Sources ingested
- `android_adr_matrix_8400.zip` — ADR Matrix agent configuration
- `android_studio_deep_matrix_opt1.zip` — Deep Matrix Studio integration
- `crew-orchestrator.zip` — Crew pipeline orchestration engine
- `pcruut.sh` — Termux PCRUUT bridge engine
