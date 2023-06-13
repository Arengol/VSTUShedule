package ru.cchgeu.vorobev.vstuschedule.network

import ru.cchgeu.vorobev.vstuschedule.dto.AutoCompleteDTO
import ru.cchgeu.vorobev.vstuschedule.dto.ScheduleDTO
import ru.cchgeu.vorobev.vstuschedule.dto.UserDTO

interface NetworkRepository {
    suspend fun login(login: String, password: String): ResultWrapper<UserDTO>
    suspend fun signup(login: String, password: String, status: Int, inviteCode: String): ResultWrapper<UserDTO>
    suspend fun getAllGroups(): ResultWrapper<List<AutoCompleteDTO>>
    suspend fun getAllMentors(): ResultWrapper<List<AutoCompleteDTO>>
    suspend fun associateGroup(token: String, userID: String, groupName: String): ResultWrapper<Unit>
    suspend fun associateMentor(token: String, userID: String, mentorName: String): ResultWrapper<Unit>
    suspend fun getMainSchedule(token: String): ResultWrapper<List<ScheduleDTO>>
    suspend fun getGroupSchedule(token: String, groupName: String): ResultWrapper<List<ScheduleDTO>>
    suspend fun getMentorSchedule(token: String, mentorName: String): ResultWrapper<List<ScheduleDTO>>
}