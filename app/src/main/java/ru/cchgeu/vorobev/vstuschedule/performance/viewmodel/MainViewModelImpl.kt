package ru.cchgeu.vorobev.vstuschedule.performance.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.cchgeu.vorobev.vstuschedule.data.ApplicationPreferencesRepo
import ru.cchgeu.vorobev.vstuschedule.dto.AutoCompleteDTO
import ru.cchgeu.vorobev.vstuschedule.dto.ScheduleDTO
import ru.cchgeu.vorobev.vstuschedule.dto.UserDTO
import ru.cchgeu.vorobev.vstuschedule.network.NetworkRepository
import ru.cchgeu.vorobev.vstuschedule.network.ResultWrapper
import java.time.DayOfWeek

class MainViewModelImpl constructor(private val networkRepository: NetworkRepository, private val preferencesRepo: ApplicationPreferencesRepo): MainViewModel, ViewModel() {

    object Builder{
        private var singleton: MainViewModel? = null
        fun create(networkRepository: NetworkRepository, preferencesRepo: ApplicationPreferencesRepo):MainViewModel{
            if(singleton==null) {
                singleton = MainViewModelImpl(networkRepository, preferencesRepo)
                return singleton!!
            }
            else return singleton!!
        }
    }

    private val dayOfWeeks = listOf<String>("Пнд","Втр","Срд","Чтв","Птн","Сбт","Вск")
    private var weekIndex = 0
    private var weekType = 0
    private val allSchedule = arrayListOf<ScheduleDTO>()
    override var weekTextView = "ПОНЕДЕЛЬНИК ЧИСЛИТЕЛЬ"
    private val _state = MutableLiveData<UiState>(UiState.Wait)
    override val state: LiveData<UiState>
        get() = _state

    private val _userData = MutableLiveData<UserDTO>()
    override val userData: LiveData<UserDTO>
        get() = _userData

    private val _autoCompleteData = MutableLiveData<MutableList<AutoCompleteDTO>>()
    override val autoCompleteData: LiveData<MutableList<AutoCompleteDTO>>
        get() = _autoCompleteData

    private val _schedule = MutableLiveData<List<ScheduleDTO>>()
    override val schedule: LiveData<List<ScheduleDTO>>
        get() = _schedule

    override fun login(login: String, password: String) {
        _state.value = UiState.Loading
        viewModelScope.launch {
            _state.value = when(val result = networkRepository.login(login,password)){
                is ResultWrapper.Success -> {
                    _userData.value = result.value!!
                    preferencesRepo.pushUserData(result.value)
                    UiState.Succes
                }
                is ResultWrapper.NetworkError -> UiState.NetworkError
                is ResultWrapper.UnAuthorized -> UiState.InvalidLoginOrPass
                ResultWrapper.BadRequest -> UiState.DataError
                ResultWrapper.Conflict -> UiState.DataError
            }
        }
    }

    override fun signup(login: String, password: String, status: Int, inviteCode: String) {
        _state.value = UiState.Loading
        viewModelScope.launch {
            _state.value = when(val result = networkRepository.signup(login,password,status, inviteCode)){
                ResultWrapper.BadRequest -> UiState.DataError
                ResultWrapper.Conflict -> UiState.InvalidLoginOrPass
                ResultWrapper.NetworkError -> UiState.NetworkError
                ResultWrapper.UnAuthorized -> UiState.DataError
                is ResultWrapper.Success -> {
                    _userData.value = result.value!!
                    preferencesRepo.pushUserData(result.value)
                    UiState.PreSucces
                }
            }
        }
    }

    override fun getAllGroups() {
        _state.value = UiState.Loading
        viewModelScope.launch {
            when(val result = networkRepository.getAllGroups()){
                ResultWrapper.BadRequest -> UiState.DataError
                ResultWrapper.Conflict -> UiState.InvalidLoginOrPass
                ResultWrapper.NetworkError -> UiState.NetworkError
                ResultWrapper.UnAuthorized -> UiState.DataError
                is ResultWrapper.Success -> {
                    if(_autoCompleteData.value.isNullOrEmpty())
                        _autoCompleteData.value=result.value!!.toMutableList()
                    else _autoCompleteData.value!!.addAll(result.value)
                    _state.value = UiState.Wait
                }
            }
        }
    }

    override fun getAllMentors() {
        _state.value = UiState.Loading
        viewModelScope.launch {
            when(val result = networkRepository.getAllMentors()){
                ResultWrapper.BadRequest -> UiState.DataError
                ResultWrapper.Conflict -> UiState.InvalidLoginOrPass
                ResultWrapper.NetworkError -> UiState.NetworkError
                ResultWrapper.UnAuthorized -> UiState.DataError
                is ResultWrapper.Success -> {
                    if(_autoCompleteData.value.isNullOrEmpty())
                        _autoCompleteData.value=result.value!!.toMutableList()
                    else _autoCompleteData.value!!.addAll(result.value)
                    _state.value = UiState.Wait
                }
            }
        }
    }

    override fun waitState () {
        _state.value = UiState.Wait
    }

    override fun userDataCheck() {
        val userDTO = preferencesRepo.getUserData()
        if (userDTO.userID == -1) {
            _state.value = UiState.Unathorized
        }
        else {
            _userData.value = userDTO
            _state.value = UiState.Succes
        }
    }

    override fun associateGroup(groupName: String) {
        _state.value = UiState.Loading
        viewModelScope.launch {
            when(val result = networkRepository.associateGroup(userData.value!!.accessToken, userData.value!!.userID.toString(), groupName)){
                ResultWrapper.BadRequest -> UiState.DataError
                ResultWrapper.Conflict -> UiState.InvalidLoginOrPass
                ResultWrapper.NetworkError -> UiState.NetworkError
                ResultWrapper.UnAuthorized -> UiState.DataError
                is ResultWrapper.Success -> {
                    _state.value = UiState.Succes
                }
            }
        }
    }

    override fun associateMentor(mentorName: String) {
        _state.value = UiState.Loading
        viewModelScope.launch {
            when(val result = networkRepository.associateMentor(userData.value!!.accessToken, userData.value!!.userID.toString(), mentorName)){
                ResultWrapper.BadRequest -> UiState.DataError
                ResultWrapper.Conflict -> UiState.InvalidLoginOrPass
                ResultWrapper.NetworkError -> UiState.NetworkError
                ResultWrapper.UnAuthorized -> UiState.DataError
                is ResultWrapper.Success -> {
                    _state.value = UiState.Succes
                }
            }
        }
    }

    override fun getMainSchedule() {
        _state.value = UiState.Loading
        viewModelScope.launch {
            when(val result = networkRepository.getMainSchedule(userData.value!!.accessToken)){
                ResultWrapper.BadRequest -> UiState.DataError
                ResultWrapper.Conflict -> UiState.InvalidLoginOrPass
                ResultWrapper.NetworkError -> UiState.NetworkError
                ResultWrapper.UnAuthorized -> UiState.DataError
                is ResultWrapper.Success -> {
                    allSchedule.clear()
                    allSchedule.addAll(result.value)
                    _schedule.value = allSchedule.filter { it.weekType == weekType && it.dayWeek.equals(dayOfWeeks.get(weekIndex)) }
                    _state.value = UiState.Succes
                }
            }
        }
    }

    override fun getSchedule(searchName: String) {
        _state.value = UiState.Loading
        viewModelScope.launch {
            when(val result = if (searchName.length <= 9 ) networkRepository.getGroupSchedule(userData.value!!.accessToken, searchName)
            else networkRepository.getMentorSchedule(userData.value!!.accessToken, searchName)){
                ResultWrapper.BadRequest -> UiState.DataError
                ResultWrapper.Conflict -> UiState.InvalidLoginOrPass
                ResultWrapper.NetworkError -> UiState.NetworkError
                ResultWrapper.UnAuthorized -> UiState.DataError
                is ResultWrapper.Success -> {
                    allSchedule.clear()
                    allSchedule.addAll(result.value)
                    _schedule.value = allSchedule.filter { it.weekType == weekType && it.dayWeek.equals(dayOfWeeks.get(weekIndex)) }
                    _state.value = UiState.Succes
                }
            }
        }
    }

    override fun nextSchedule() {
        _state.value = UiState.Loading
        weekIndex++
        if (weekIndex > 6){
            weekIndex = 0
            weekType++
        }
        if (weekType > 1){
            weekIndex = 0
            weekType = 0
        }
        _schedule.value = allSchedule.filter { it.weekType == weekType && it.dayWeek.equals(dayOfWeeks.get(weekIndex)) }
        weekTextView = transformWeek(weekIndex, weekType)
        _state.value = UiState.Succes
    }

    override fun previousSchedule() {
        _state.value = UiState.Loading
        weekIndex--
        if (weekIndex < 0){
            weekIndex = 6
            weekType--
        }
        if (weekType < 0){
            weekIndex = 6
            weekType = 1
        }
        _schedule.value = allSchedule.filter { it.weekType == weekType && it.dayWeek.equals(dayOfWeeks.get(weekIndex)) }
        weekTextView = transformWeek(weekIndex, weekType)
        _state.value = UiState.Succes
    }

    private fun transformWeek(dayOfWeek: Int, weekType: Int) = "${
        when(dayOfWeek){
            0 -> "ПОНЕДЕЛЬНИК"
            1 -> "ВТОРНИК"
            2 -> "СРЕДА"
            3 -> "ЧЕТВЕРГ"
            4 -> "ПЯТНИЦА"
            5 -> "СУББОТА"
            6 -> "ВОСКРЕСЕНЬЕ"
            else -> ""
        }
    } ${
        if (weekType == 0) "ЧИСЛИТЕЛЬ"
        else "ЗНАМЕНАТЕЛЬ"
    }"
}

