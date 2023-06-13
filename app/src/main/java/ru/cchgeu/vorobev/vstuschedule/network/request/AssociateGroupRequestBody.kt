package ru.cchgeu.vorobev.vstuschedule.network.request

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AssociateGroupRequestBody (
    @Json(name = "userId") val userID: String,
    @Json(name = "groupName") val groupName: String
    )