package com.onecab.data

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.onecab.domain.repository.LocalSourceRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

private const val PREF_NAME = "appPreference"
private const val SERVER_ADDRESS = "address"
private const val LOGIN = "login"
private const val PASSWORD = "pass"
private const val DEFAULT = "empty"

private const val TAG = "LocalSourceRepositoryImpl"

@Singleton
class LocalSourceRepositoryImpl @Inject constructor(
    @ApplicationContext context: Context
) : LocalSourceRepository {

    private val preferences: SharedPreferences = context.getSharedPreferences(
        PREF_NAME,
        Context.MODE_PRIVATE
    )

    private val editor = preferences.edit()

    private val pInfo = context.packageManager.getPackageInfo(context.packageName, 0)


    override fun getAppVersion(): String {
        try {
            return pInfo.versionName
        } catch (e: Exception) {
            e.printStackTrace()
            return ""
        }
    }

    override fun saveLogin(login: String) {
        if (login.isNotEmpty()) {
            try {
                editor.putString(LOGIN, login)
                editor.commit()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun getLogin(): String {
        return preferences.getString(LOGIN, "") ?: ""
    }

    override fun savePassword(pass: String) {
        if (pass.isNotEmpty()) {
            try {
                editor.putString(PASSWORD, pass)
                editor.commit()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun getPassword(): String {
        return preferences.getString(PASSWORD, "") ?: ""
    }

    override fun removeUserData(): Boolean {
        try {
            editor.remove(SERVER_ADDRESS)
            editor.remove(LOGIN)
            editor.commit()
            return true
        } catch (e: Exception) {
            return false
        }
    }

    override fun setServerAddress(address: String): Boolean {
        if (address.isNotEmpty()) {
            try {
                editor.putString(SERVER_ADDRESS, address)
                editor.commit()
                Log.d(TAG, "метод setServerAddress - Адрес сервера сохранен в Pref: $address")
                return true
            } catch (e: Exception) {
                e.printStackTrace()
                return false
            }
        } else {
            return false
        }
    }

    override fun getServerAddress(): String? {
        return preferences.getString(SERVER_ADDRESS, DEFAULT)
    }
}