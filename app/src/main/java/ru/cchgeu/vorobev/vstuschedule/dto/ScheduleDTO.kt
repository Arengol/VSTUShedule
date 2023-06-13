package ru.cchgeu.vorobev.vstuschedule.dto

data class ScheduleDTO(
    val group: String,
    val time: String,
    val dayWeek: String,
    val weekType: Int,
    val name: String,
    val classType: String,
    val auditory: List<String>?,
    val mentor: List<String>?
)
