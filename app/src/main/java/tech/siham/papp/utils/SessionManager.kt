package tech.siham.papp.utils

import android.content.Context
import android.content.SharedPreferences
import tech.siham.papp.application


class SessionManager () {

    companion object {
        const val USER_TOKEN = "user_token"
        const val USER_ID = "user_id"
    }

    var prefs: SharedPreferences = application.instance.applicationContext.getSharedPreferences("papp", Context.MODE_PRIVATE)

    fun saveAuthToken(token: String) {
        val editor = prefs.edit()
        editor.putString(USER_TOKEN, token)
        editor.apply()
    }

    fun logoutAuthToken() {
        val editor = prefs.edit()
        editor.putString(USER_TOKEN, null)
        editor.apply()
    }

    fun fetchAuthToken(): String? {
        return prefs.getString(USER_TOKEN, null)
    }

    fun saveUserId(userId: Int) {
        val editor = prefs.edit()
        editor.putInt(USER_ID, userId)
        editor.apply()
    }

    fun clearUserId() {
        val editor = prefs.edit()
        editor.putInt(USER_ID, 0)
        editor.apply()
    }

    fun getUserId(): Int { return prefs.getInt(USER_ID, 0) }

}