package com.glypheral.devsphere.models

data class ApiKey(
    val id: String = java.util.UUID.randomUUID().toString(),
    val provider: String = "",      // OpenAI, Anthropic, AWS, GCP, GitHub, etc.
    val label: String = "",
    val maskedKey: String = "",
    val isActive: Boolean = true,
    val addedAt: Long = System.currentTimeMillis()
)
