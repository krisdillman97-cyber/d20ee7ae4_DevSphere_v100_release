package com.glypheral.devsphere.models

data class BrowserTab(
    val id: String = java.util.UUID.randomUUID().toString(),
    val title: String = "New Tab",
    val url: String = "",
    val favicon: String = "",
    val isActive: Boolean = false
)
