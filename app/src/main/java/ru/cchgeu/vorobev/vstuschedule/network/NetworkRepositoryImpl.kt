package ru.cchgeu.vorobev.vstuschedule.network

import kotlinx.coroutines.Dispatchers
import ru.cchgeu.vorobev.vstuschedule.dto.AutoCompleteDTO
import ru.cchgeu.vorobev.vstuschedule.dto.ScheduleDTO
import ru.cchgeu.vorobev.vstuschedule.dto.UserDTO
import ru.cchgeu.vorobev.vstuschedule.network.request.AssociateGroupRequestBody
import ru.cchgeu.vorobev.vstuschedule.network.request.AssociateMentorRequestBody
import ru.cchgeu.vorobev.vstuschedule.network.request.GroupScheduleRequestBody
import ru.cchgeu.vorobev.vstuschedule.network.request.LoginRequestBody
import ru.cchgeu.vorobev.vstuschedule.network.request.MentorScheduleRequestBody
import ru.cchgeu.vorobev.vstuschedule.network.request.SignupRequestBody
import ru.cchgeu.vorobev.vstuschedule.network.response.toDTO

class NetworkRepositoryImpl (private val serverApi: ServerAPI): NetworkRepository {
    override suspend fun login(login: String, password: String): ResultWrapper<UserDTO> {
        val response = safeApiCall(Dispatchers.IO){
            serverApi.authLogin(LoginRequestBody(login, password))
        }
        return when (response) {
            is ResultWrapper.Success -> ResultWrapper.Success(response.value.toDTO())
          //  is ResultWrapper.LoginFailed -> ResultWrapper.LoginFailed
            ResultWrapper.UnAuthorized -> ResultWrapper.UnAuthorized
            ResultWrapper.NetworkError -> ResultWrapper.NetworkError
            ResultWrapper.BadRequest -> ResultWrapper.BadRequest
            ResultWrapper.Conflict -> ResultWrapper.Conflict
        }
    }

    override suspend fun signup(
        login: String,
        password: String,
        status: Int,
        inviteCode: String
    ): ResultWrapper<UserDTO> {
        val response = safeApiCall(Dispatchers.IO){
            serverApi.authSignup(SignupRequestBody(login, password, status, inviteCode))
        }
        return when (response) {
            is ResultWrapper.Success -> ResultWrapper.Success(response.value.toDTO())
            ResultWrapper.BadRequest -> ResultWrapper.BadRequest
            ResultWrapper.Conflict -> ResultWrapper.Conflict
            ResultWrapper.NetworkError -> ResultWrapper.NetworkError
            ResultWrapper.UnAuthorized -> ResultWrapper.UnAuthorized
        }
    }

    override suspend fun getAllGroups(): ResultWrapper<List<AutoCompleteDTO>> {
        val response = safeApiCall(Dispatchers.IO){
            serverApi.getAllGroups()
        }
        return when (response) {
            is ResultWrapper.Success -> {
                val result = arrayListOf<AutoCompleteDTO>()
                response.value.forEach { result.add(it.toDTO()) }
                ResultWrapper.Success(result)
            }
            ResultWrapper.BadRequest -> ResultWrapper.BadRequest
            ResultWrapper.Conflict -> ResultWrapper.Conflict
            ResultWrapper.NetworkError -> ResultWrapper.NetworkError
            ResultWrapper.UnAuthorized -> ResultWrapper.UnAuthorized
        }
    }

    override suspend fun getAllMentors(): ResultWrapper<List<AutoCompleteDTO>> {
        val response = safeApiCall(Dispatchers.IO){
            serverApi.getAllMentors()
        }
        return when (response) {
            is ResultWrapper.Success -> {
                val result = arrayListOf<AutoCompleteDTO>()
                response.value.forEach { result.add(it.toDTO()) }
                ResultWrapper.Success(result)
            }
            ResultWrapper.BadRequest -> ResultWrapper.BadRequest
            ResultWrapper.Conflict -> ResultWrapper.Conflict
            ResultWrapper.NetworkError -> ResultWrapper.NetworkError
            ResultWrapper.UnAuthorized -> ResultWrapper.UnAuthorized
        }
    }

    override suspend fun associateGroup(token: String, userID: String, groupName: String): ResultWrapper<Unit> {
        val response = safeApiCall(Dispatchers.IO){
            serverApi.associateGroup("Bearer $token", AssociateGroupRequestBody(
                userID = userID,
                groupName = groupName
            ))
        }
        return when (response) {
            is ResultWrapper.Success -> ResultWrapper.Success(Unit)
            ResultWrapper.BadRequest -> ResultWrapper.BadRequest
            ResultWrapper.Conflict -> ResultWrapper.Conflict
            ResultWrapper.NetworkError -> ResultWrapper.NetworkError
            ResultWrapper.UnAuthorized -> ResultWrapper.UnAuthorized
        }
    }

    override suspend fun associateMentor(
        token: String,
        userID: String,
        mentorName: String
    ): ResultWrapper<Unit> {
        val response = safeApiCall(Dispatchers.IO){
            serverApi.associateMentor("Bearer $token", AssociateMentorRequestBody(
                userID = userID,
                mentorName = mentorName
            )
            )
        }
        return when (response) {
            is ResultWrapper.Success -> ResultWrapper.Success(Unit)
            ResultWrapper.BadRequest -> ResultWrapper.BadRequest
            ResultWrapper.Conflict -> ResultWrapper.Conflict
            ResultWrapper.NetworkError -> ResultWrapper.NetworkError
            ResultWrapper.UnAuthorized -> ResultWrapper.UnAuthorized
        }
    }

    override suspend fun getMainSchedule(token: String): ResultWrapper<List<ScheduleDTO>> {
        val response = safeApiCall(Dispatchers.IO){
            serverApi.getMainSchedule("Bearer $token")
        }
        return when (response) {
            is ResultWrapper.Success -> {
                val result = arrayListOf<ScheduleDTO>()
                response.value.forEach { result.add(it.toDTO()) }
                ResultWrapper.Success(result)
            }
            ResultWrapper.BadRequest -> ResultWrapper.BadRequest
            ResultWrapper.Conflict -> ResultWrapper.Conflict
            ResultWrapper.NetworkError -> ResultWrapper.NetworkError
            ResultWrapper.UnAuthorized -> ResultWrapper.UnAuthorized
        }
    }

    override suspend fun getGroupSchedule(token: String, groupName: String): ResultWrapper<List<ScheduleDTO>> {
        val response = safeApiCall(Dispatchers.IO){
            serverApi.getScheduleByGroup("Bearer $token", GroupScheduleRequestBody(groupName))
        }
        return when (response) {
            is ResultWrapper.Success -> {
                val result = arrayListOf<ScheduleDTO>()
                response.value.forEach { result.add(it.toDTO()) }
                ResultWrapper.Success(result)
            }
            ResultWrapper.BadRequest -> ResultWrapper.BadRequest
            ResultWrapper.Conflict -> ResultWrapper.Conflict
            ResultWrapper.NetworkError -> ResultWrapper.NetworkError
            ResultWrapper.UnAuthorized -> ResultWrapper.UnAuthorized
        }
    }

    override suspend fun getMentorSchedule(token: String, mentorName: String): ResultWrapper<List<ScheduleDTO>> {
        val response = safeApiCall(Dispatchers.IO){
            serverApi.getScheduleByMentor("Bearer $token", MentorScheduleRequestBody(mentorName))
        }
        return when (response) {
            is ResultWrapper.Success -> {
                val result = arrayListOf<ScheduleDTO>()
                response.value.forEach { result.add(it.toDTO()) }
                ResultWrapper.Success(result)
            }
            ResultWrapper.BadRequest -> ResultWrapper.BadRequest
            ResultWrapper.Conflict -> ResultWrapper.Conflict
            ResultWrapper.NetworkError -> ResultWrapper.NetworkError
            ResultWrapper.UnAuthorized -> ResultWrapper.UnAuthorized
        }
    }
}