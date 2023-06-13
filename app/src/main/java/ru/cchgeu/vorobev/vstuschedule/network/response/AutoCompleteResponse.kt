package ru.cchgeu.vorobev.vstuschedule.network.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import ru.cchgeu.vorobev.vstuschedule.dto.AutoCompleteDTO

@JsonClass(generateAdapter = true)
data class AutoCompleteResponse(
    @Json(name = "name") val name: String
)

fun AutoCompleteResponse.toDTO(): AutoCompleteDTO {
    return AutoCompleteDTO(
        name = this.name
    )
}