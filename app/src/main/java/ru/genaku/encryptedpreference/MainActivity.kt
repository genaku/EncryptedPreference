package ru.genaku.encryptedpreference

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import java.util.*

class MainActivity : AppCompatActivity() {

    private val preferences = EncryptedPreferencesBuilder.build(applicationContext)

    var login by EncryptedPreference(preferences, "LOGIN", "")
    var isAuthorized by EncryptedPreference(preferences, "AUTHORIZED", false)
    var lastLoginTime by EncryptedPreference(preferences, "LAST_LOGIN_TIME", Date())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    private fun saveLoginDate(login: String) {
        if (isAuthorized) {
            this.login = login
            lastLoginTime = Date()
        }
    }
}