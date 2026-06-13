package com.glypheral.devsphere.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.glypheral.devsphere.models.User
import com.glypheral.devsphere.models.UserRole

object SessionManager {
    private const val PREF_FILE = "ds_session"
    private const val KEY_USER_ID = "user_id"
    private const val KEY_USER_NAME = "user_name"
    private const val KEY_USER_EMAIL = "user_email"
    private const val KEY_USER_ROLE = "user_role"
    private const val KEY_TOKEN = "auth_token"
    private const val KEY_GITHUB_LINKED = "github_linked"

    private lateinit var prefs: SharedPreferences

    fun init(context: Context) {
        prefs = try {
            val masterKey = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
            EncryptedSharedPreferences.create(
                PREF_FILE, masterKey, context,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            )
        } catch (e: Exception) {
            context.getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE)
        }
    }

    fun saveSession(user: User, token: String) {
        prefs.edit().apply {
            putString(KEY_USER_ID, user.id)
            putString(KEY_USER_NAME, user.name)
            putString(KEY_USER_EMAIL, user.email)
            putString(KEY_USER_ROLE, user.role.name)
            putString(KEY_TOKEN, token)
            putBoolean(KEY_GITHUB_LINKED, user.githubLinked)
            apply()
        }
    }

    fun getUser(): User? {
        val id = prefs.getString(KEY_USER_ID, null) ?: return null
        return User(
            id = id,
            name = prefs.getString(KEY_USER_NAME, "") ?: "",
            email = prefs.getString(KEY_USER_EMAIL, "") ?: "",
            role = try { UserRole.valueOf(prefs.getString(KEY_USER_ROLE, "GUEST") ?: "GUEST") }
                   catch (e: Exception) { UserRole.GUEST },
            githubLinked = prefs.getBoolean(KEY_GITHUB_LINKED, false)
        )
    }

    fun getToken(): String? = prefs.getString(KEY_TOKEN, null)
    fun isLoggedIn(): Boolean = getToken() != null

    fun clearSession() {
        prefs.edit().clear().apply()
    }

    fun updateRole(role: UserRole) {
        prefs.edit().putString(KEY_USER_ROLE, role.name).apply()
    }
}
