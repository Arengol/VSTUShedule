package ru.cchgeu.vorobev.vstuschedule.network.request

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GroupScheduleRequestBody(
    @Json(name = "groupName") val groupName: String
)
