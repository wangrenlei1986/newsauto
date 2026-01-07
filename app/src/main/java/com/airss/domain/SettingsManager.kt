package com.airss.domain

import android.content.Context
import android.content.SharedPreferences

object SettingsManager {
    private const val PREF = "ai_news_settings"
    private const val KEY_API = "api_key"
    private const val KEY_BASE = "base_url"
    private const val KEY_MODEL = "model"

    fun getPrefs(ctx: Context): SharedPreferences =
        ctx.getSharedPreferences(PREF, Context.MODE_PRIVATE)

    fun getApiKey(ctx: Context): String? = getPrefs(ctx).getString(KEY_API, null)
    fun setApiKey(ctx: Context, v: String) { getPrefs(ctx).edit().putString(KEY_API, v).apply() }

    fun getBaseUrl(ctx: Context): String? = getPrefs(ctx).getString(KEY_BASE, null)
    fun setBaseUrl(ctx: Context, v: String) { getPrefs(ctx).edit().putString(KEY_BASE, v).apply() }

    fun getModel(ctx: Context): String? = getPrefs(ctx).getString(KEY_MODEL, null)
    fun setModel(ctx: Context, v: String) { getPrefs(ctx).edit().putString(KEY_MODEL, v).apply() }
}
