package ru.genaku.encryptedpreference

import android.content.SharedPreferences
import java.math.BigDecimal
import java.math.BigInteger
import java.util.*
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * Generic encrypted preference read/write property
 *
 * @property encryptedPreferences
 * @property key - key of preference
 * @property default - default value
 *
 * @sample
 * var login: String by EncryptedPreference(preferences, "LOGIN", "")
 *
 * or
 *
 * var login by EncryptedPreference(preferences, "LOGIN", "")
 *
 *
 * if (login.isEmpty()) {
 *   throw (Exception("login is empty"))
 * }
 * login = "somelogin"
 *
 * value is stored in encrypted shared preferences dynamically
 *
 * @author Gena Kuchergin
 */
open class EncryptedPreference<T>(
    private val encryptedPreferences: SharedPreferences,
    private val key: String,
    private val default: T
) : ReadWriteProperty<Any?, T> {

    override fun getValue(thisRef: Any?, property: KProperty<*>): T {
        val result: Any? = when (default) {
            is String -> encryptedPreferences.getString(key, default)
            is Long -> encryptedPreferences.getLong(key, default)
            is Int -> encryptedPreferences.getInt(key, default)
            is Float -> encryptedPreferences.getFloat(key, default)
            is Double -> encryptedPreferences.getString(key, null)?.toDouble() ?: default
            is Boolean -> encryptedPreferences.getBoolean(key, default)
            is BigDecimal -> encryptedPreferences.getString(key, null)?.toBigDecimal() ?: default
            is BigInteger -> encryptedPreferences.getString(key, null)?.toBigInteger() ?: default
            is Date -> getDate(encryptedPreferences.getString(key, null))
            else -> throw IllegalArgumentException("This type can't be load from Preferences")
        }
        @Suppress("UNCHECKED_CAST")
        return result as T? ?: default
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        encryptedPreferences.set(value)
    }

    private inline fun SharedPreferences.edit(operation: (SharedPreferences.Editor) -> Unit) {
        val editor = this.edit()
        operation(editor)
        editor.apply()
    }

    private fun SharedPreferences.set(value: T) {
        when (value) {
            is String -> edit { it.putString(key, value) }
            is Date -> edit { it.putLong(key, value.time) }
            is Long -> edit { it.putLong(key, value) }
            is Int -> edit { it.putInt(key, value) }
            is Float -> edit { it.putFloat(key, value) }
            is Double -> edit { it.putString(key, value.toString()) }
            is Boolean -> edit { it.putBoolean(key, value) }
            is BigDecimal,
            is BigInteger -> edit { it.putString(key, value.toString()) }
            else -> throw IllegalArgumentException("This type can't be saved into Preferences")
        }
    }

    private fun getDate(value: String?): Date {
        value ?: return default as Date
        return Date(value.toLong())
    }
}