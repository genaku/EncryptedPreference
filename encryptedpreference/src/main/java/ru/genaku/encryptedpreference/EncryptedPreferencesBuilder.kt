package ru.genaku.encryptedpreference

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

/**
 * Encrypted preferences builder
 *
 * @author Gena Kuchergin
 */
class EncryptedPreferencesBuilder {

    companion object {

        /**
         * Build EncryptedSharedPreferences
         *
         * @param applicationContext - application context
         * @param filename - encrypted shared preferences file name
         */
        @JvmStatic
        fun build(applicationContext: Context, filename: String = "preferences"): SharedPreferences {
            val masterKeyAlias = MasterKey
                .Builder(applicationContext)
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                .build()
            return EncryptedSharedPreferences.create(
                applicationContext,
                filename,
                masterKeyAlias,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            )
        }
    }
}