package com.aluobo.whatnet.net

data class Response<T>(
    val errorCode:Int = 0,
    val errorMsg:String = "",
    val data:T? =null

){
    fun isSuccess() = errorCode == 0
    fun isFailure() = !isSuccess()
}
