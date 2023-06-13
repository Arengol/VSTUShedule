package ru.cchgeu.vorobev.vstuschedule.network.request

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SignupRequestBody(
    @Json(name = "username") val username: String,
    @Json(name = "password") val password: String,
    @Json(name = "status") val status: Int,
    @Json(name = "inviteCode") val inviteCode: String
)
