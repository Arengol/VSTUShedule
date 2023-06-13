package ru.cchgeu.vorobev.vstuschedule.network.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import ru.cchgeu.vorobev.vstuschedule.dto.ScheduleDTO

@JsonClass(generateAdapter = true)
data class ScheduleResponse (
    @Json(name = "group") val group: String,
    @Json(name = "time") val time: String,
    @Json(name = "dayWeek") val dayWeek: String,
    @Json(name = "weekType") val weekType: Int,
    @Json(name = "name") val name: String,
    @Json(name = "classType") val classType: String,
    @Json(name = "auditory") val auditory: List<String>?,
    @Json(name = "mentor") val mentor: List<String>?
)

fun ScheduleResponse.toDTO(): ScheduleDTO = ScheduleDTO(
    group = this.group,
    time = this.time,
    dayWeek = this.dayWeek,
    weekType = this.weekType,
    name = this.name,
    classType = this.classType,
    auditory = this.auditory,
    mentor = this.mentor
)