package ru.cchgeu.vorobev.vstuschedule.performance.viewmodel

import androidx.lifecycle.LiveData
import ru.cchgeu.vorobev.vstuschedule.dto.AutoCompleteDTO
import ru.cchgeu.vorobev.vstuschedule.dto.ScheduleDTO
import ru.cchgeu.vorobev.vstuschedule.dto.UserDTO

interface MainViewModel {

    fun login(login: String, password: String)
    fun signup(login: String, password: String, status: Int, inviteCode: String)
    fun getAllGroups()
    fun getAllMentors()
    fun waitState()
    fun userDataCheck()
    fun associateGroup(groupName: String)
    fun associateMentor(mentorName: String)
    fun getMainSchedule()
    fun getSchedule(searchName: String)
    fun nextSchedule()
    fun previousSchedule()
    val schedule: LiveData<List<ScheduleDTO>>
    val state: LiveData<UiState>
    val userData: LiveData<UserDTO>
    val autoCompleteData: LiveData<MutableList<AutoCompleteDTO>>
    val weekTextView: String
}