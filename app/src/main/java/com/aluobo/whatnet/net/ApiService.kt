package com.aluobo.whatnet.net

import retrofit2.http.GET

interface ApiService {
    @GET("/banner/json")
    suspend fun getBanners():Response<List<Banner>>
}

