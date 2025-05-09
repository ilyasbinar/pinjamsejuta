package com.example.pinjamsejuta.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import androidx.core.content.edit

object SharedPrefsUtils {

    private const val PREFS_NAME = "app_preferences"
    private const val KEY_IS_LOGGED_IN = "is_logged_in"
    private const val KEY_TOKEN = "token"

    private fun getEncryptedSharedPreferences(context: Context) =
        EncryptedSharedPreferences.create(
            context,
            PREFS_NAME,
            MasterKey.Builder(context)
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                .build(),
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )

    fun setToken(context: Context, token: String) {
        val prefs = getEncryptedSharedPreferences(context)
        prefs.edit() { putString(KEY_TOKEN, token) }
    }

    fun setLoginStatus(context: Context, isLoggedIn: Boolean) {
        val prefs = getEncryptedSharedPreferences(context)
        prefs.edit() { putBoolean(KEY_IS_LOGGED_IN, isLoggedIn) }
    }

    fun isLoggedIn(context: Context): Boolean {
        val prefs = getEncryptedSharedPreferences(context)
        return prefs.getBoolean(KEY_IS_LOGGED_IN, false)
    }

    fun getToken(context: Context): String? {
        val prefs = getEncryptedSharedPreferences(context)
        return prefs.getString(KEY_TOKEN, null)
    }

    fun clearLoginData(context: Context) {
        val prefs = getEncryptedSharedPreferences(context)
        prefs.edit() { clear() }
    }
}
