package com.onecab.crashlytics

import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.onecab.domain.crashlytics.CrashlyticsManager
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CrashlyticsManagerImpl @Inject constructor(
) : CrashlyticsManager {

    override fun log(message: String) {
        FirebaseCrashlytics.getInstance().log(message)
    }

    override fun log(throwable: Throwable) {
        FirebaseCrashlytics.getInstance().recordException(throwable)
    }

    override fun setUserIdentifier(userId: String) {
        FirebaseCrashlytics.getInstance().setUserId(userId)
    }

    override fun setCustomKey(key: String, value: String) {
        FirebaseCrashlytics.getInstance().setCustomKey(key, value)
    }
}