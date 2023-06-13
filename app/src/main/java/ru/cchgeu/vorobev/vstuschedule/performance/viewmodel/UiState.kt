package ru.cchgeu.vorobev.vstuschedule.performance.viewmodel

sealed interface UiState {
    object Succes: UiState
    object PreSucces: UiState
    object Unathorized: UiState
    object InvalidLoginOrPass: UiState
    object Loading: UiState
    object DataError: UiState
    object Wait: UiState
    object NetworkError: UiState
}