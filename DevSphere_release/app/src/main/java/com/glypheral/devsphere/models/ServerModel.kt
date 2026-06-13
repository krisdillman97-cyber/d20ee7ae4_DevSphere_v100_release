package com.glypheral.devsphere.models

data class ServerModel(
    val id: String = java.util.UUID.randomUUID().toString(),
    val name: String = "",
    val runtime: String = "Node.js", // Node.js, Python, PHP, Static
    val port: Int = 3000,
    val status: ServerStatus = ServerStatus.STOPPED,
    val createdAt: Long = System.currentTimeMillis(),
    val logs: List<String> = emptyList()
)

enum class ServerStatus { RUNNING, STOPPED, PENDING }
