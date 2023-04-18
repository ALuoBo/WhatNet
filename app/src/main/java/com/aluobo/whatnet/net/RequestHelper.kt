package com.aluobo.whatnet.net

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

object RequestHelper {
    suspend fun <T> request(
        call: suspend () -> Response<T>
    ): Flow<RequestResult<Response<T>>> {
        return flow {
            val response = call()
            if (response.isSuccess()) {
                emit(RequestResult.Success(response))
            } else {
                emit(RequestResult.Error(response.errorCode, response.errorMsg))
            }
        }.flowOn(Dispatchers.IO)
            .catch { throwable: Throwable ->
                emit(RequestResult.Error(-1, throwable.message))
            }
    }
}