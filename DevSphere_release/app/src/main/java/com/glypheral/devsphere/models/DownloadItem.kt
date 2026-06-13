package com.glypheral.devsphere.models

data class DownloadItem(
    val id: String = java.util.UUID.randomUUID().toString(),
    val url: String = "",
    val fileName: String = "",
    val status: DownloadStatus = DownloadStatus.PENDING,
    val progress: Int = 0,
    val filePath: String = "",
    val createdAt: Long = System.currentTimeMillis()
)

enum class DownloadStatus { PENDING, DOWNLOADING, COMPLETE, FAILED }
