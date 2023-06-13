package ru.cchgeu.vorobev.vstuschedule.network.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import ru.cchgeu.vorobev.vstuschedule.dto.UserDTO

@JsonClass(generateAdapter = true)
data class AuthInfoResponse (
    @Json(name = "userId") val userID: Int,
    @Json(name = "accessToken") val accessToken: String,
    @Json(name = "refreshToken") val refreshToken: String
)

fun AuthInfoResponse.toDTO(): UserDTO {
    return UserDTO(
        userID = this.userID,
        accessToken = this.accessToken,
        refreshToken = this.refreshToken
    )
}