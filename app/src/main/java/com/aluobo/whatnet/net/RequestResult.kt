package com.aluobo.whatnet.net

sealed class RequestResult<T> {
    data class Success<T>(val data: T) : RequestResult<T>()

    /**
     * 这个 nothing ???
     */
    data class Error(
        val errorCode: Int,
        val errorMsg: String? = ""
    ) : RequestResult<Nothing>()
}