package ru.cchgeu.vorobev.vstuschedule.network

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

suspend fun <T> safeApiCall(dispatcher: CoroutineDispatcher, apiCall: suspend () -> T): ResultWrapper<T>{
    return withContext(dispatcher){
        try {
            ResultWrapper.Success(apiCall.invoke())
        } catch (error: Throwable){
            when (error){
                is IOException -> ResultWrapper.NetworkError
                is HttpException -> {
                    when (error.code()){
                        400 -> ResultWrapper.BadRequest
                        409 -> ResultWrapper.Conflict
                        401 -> ResultWrapper.UnAuthorized
                        else -> ResultWrapper.NetworkError
                    }
                }
                else -> {
                    println(error.message)
                    ResultWrapper.NetworkError
                }
            }
        }
    }
}