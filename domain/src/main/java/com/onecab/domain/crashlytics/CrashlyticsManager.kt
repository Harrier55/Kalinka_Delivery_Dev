package com.onecab.domain.crashlytics

interface CrashlyticsManager {
    fun log(message: String)
    fun log(throwable: Throwable)
    fun setUserIdentifier(userId: String)
    fun setCustomKey(key: String, value: String)
}