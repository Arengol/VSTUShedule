package ru.cchgeu.vorobev.vstuschedule.network

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import ru.cchgeu.vorobev.vstuschedule.network.request.AssociateGroupRequestBody
import ru.cchgeu.vorobev.vstuschedule.network.request.AssociateMentorRequestBody
import ru.cchgeu.vorobev.vstuschedule.network.request.GroupScheduleRequestBody
import ru.cchgeu.vorobev.vstuschedule.network.request.LoginRequestBody
import ru.cchgeu.vorobev.vstuschedule.network.request.MentorScheduleRequestBody
import ru.cchgeu.vorobev.vstuschedule.network.request.SignupRequestBody
import ru.cchgeu.vorobev.vstuschedule.network.response.AutoCompleteResponse
import ru.cchgeu.vorobev.vstuschedule.network.response.AuthInfoResponse
import ru.cchgeu.vorobev.vstuschedule.network.response.ScheduleResponse

interface ServerAPI {
    @POST("signin")
    suspend fun authLogin (@Body body: LoginRequestBody): AuthInfoResponse
    @POST("signup")
    suspend fun authSignup (@Body body: SignupRequestBody): AuthInfoResponse
    @GET("allGroups")
    suspend fun getAllGroups (): List<AutoCompleteResponse>
    @GET("allMentors")
    suspend fun getAllMentors (): List<AutoCompleteResponse>
    @POST("associategroup")
    suspend fun associateGroup (@Header ("Authorization") token: String, @Body body: AssociateGroupRequestBody)
    @POST("associatementor")
    suspend fun associateMentor (@Header ("Authorization") token: String, @Body body: AssociateMentorRequestBody)
    @GET("schedulebyaccountid")
    suspend fun getMainSchedule (@Header ("Authorization") token: String): List<ScheduleResponse>
    @POST("schedulebygroup")
    suspend fun getScheduleByGroup (@Header ("Authorization") token: String, @Body body: GroupScheduleRequestBody): List<ScheduleResponse>
    @POST("mentorschedulebyname")
    suspend fun getScheduleByMentor (@Header ("Authorization") token: String, @Body body: MentorScheduleRequestBody): List<ScheduleResponse>
}