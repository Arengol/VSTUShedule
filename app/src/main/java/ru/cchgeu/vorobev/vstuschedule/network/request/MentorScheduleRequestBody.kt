package ru.cchgeu.vorobev.vstuschedule.network.request

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MentorScheduleRequestBody(
    @Json(name = "mentor") val mentor: String
)
