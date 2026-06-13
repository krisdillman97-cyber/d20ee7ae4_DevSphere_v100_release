package com.glypheral.devsphere.models

import java.io.Serializable

enum class UserRole {
    GUEST, FREE, DEVELOPER, ADMIN;

    fun canUseBrowser() = this >= DEVELOPER || this == FREE
    fun canUseDevTools() = this >= DEVELOPER
    fun canUseServerManager() = this >= DEVELOPER
    fun canUseDownloader() = this >= DEVELOPER
    fun canUseCodeEditor() = this >= DEVELOPER
    fun canUseTeam() = this >= DEVELOPER
    fun canUseAdminPanel() = this == ADMIN
    fun canUseApiKeyManager() = this == ADMIN
    fun canUseHiddenDevMenu() = this == ADMIN
    fun isAtLeast(other: UserRole) = this.ordinal >= other.ordinal
}

data class User(
    val id: String = "",
    val name: String = "",
    val email: String = "",
    val role: UserRole = UserRole.GUEST,
    val avatarUrl: String = "",
    val githubLinked: Boolean = false,
    val googleLinked: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
    val subscriptionType: String = "none" // none, monthly, lifetime
) : Serializable
