package ru.cchgeu.vorobev.vstuschedule.network

sealed class ResultWrapper <out T> {
    data class Success<out T>(val value: T): ResultWrapper<T>()
   // object LoginFailed:ResultWrapper<Nothing>()
    object UnAuthorized:ResultWrapper<Nothing>()
    object NetworkError:ResultWrapper<Nothing>()
    object Conflict:ResultWrapper<Nothing>()
    object BadRequest:ResultWrapper<Nothing>()
}
