package com.aluobo.whatnet.net

import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    @GET("/banner/json")
    suspend fun getBanners(): Response<List<Banner>>

    @FormUrlEncoded
    @POST("/user/login")
    suspend fun login(
        @Field("username") username: String,
        @Field("password") password: String
    ): Response<LoginResult>

    @GET("/lg/collect/list/{page}/json")
    suspend fun getCollect(@Path ("page") page:Int):Response<Article>
}

