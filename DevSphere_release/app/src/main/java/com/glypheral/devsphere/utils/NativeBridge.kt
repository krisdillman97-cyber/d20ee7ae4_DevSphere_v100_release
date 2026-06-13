package com.glypheral.devsphere

class NativeBridge {
    companion object {
        init {
            try { System.loadLibrary("devsphere_native") }
            catch (e: UnsatisfiedLinkError) { /* NDK not built yet */ }
        }
    }
    external fun getVersion(): String
}
