package ru.cchgeu.vorobev.vstuschedule.dto

import com.squareup.moshi.Json

data class UserDTO(
    val userID: Int,
    val accessToken: String,
    val refreshToken: String
)

