package com.onecab.domain.repository

interface LocalSourceRepository {

    fun setServerAddress(address: String): Boolean
    fun getServerAddress(): String?
    fun getAppVersion(): String
    fun saveLogin(login: String)
    fun getLogin(): String
    fun savePassword(pass: String)
    fun getPassword(): String
    fun removeUserData(): Boolean
}