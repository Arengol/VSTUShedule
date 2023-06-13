package ru.cchgeu.vorobev.vstuschedule.data

import android.content.Context
import ru.cchgeu.vorobev.vstuschedule.dto.UserDTO

class ApplicationPreferencesRepo(context: Context) {
    private val pref = context.getSharedPreferences("APPLICATION_PREFERENCES", Context.MODE_PRIVATE)
    private val editor = pref.edit()

    fun pushUserData (data: UserDTO) {
        editor.putInt("USER_ID", data.userID)
        editor.putString("ACCESS_TOKEN", data.accessToken)
        editor.putString("REFRESH_TOKEN", data.refreshToken)
        editor.apply()
    }

    fun getUserData (): UserDTO = UserDTO(
        userID = pref.getInt("USER_ID", -1),
        accessToken = pref.getString("ACCESS_TOKEN", "")!!,
        refreshToken = pref.getString("REFRESH_TOKEN","")!!
    )
}