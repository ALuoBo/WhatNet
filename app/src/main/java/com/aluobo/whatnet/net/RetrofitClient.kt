package com.aluobo.whatnet.net

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {
    private const val BASE_URL = "https://www.wanandroid.com"

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private val okHttpClient by lazy {
        OkHttpClient.Builder()
            .connectTimeout(5, TimeUnit.SECONDS)
            .readTimeout(5, TimeUnit.SECONDS)
            .writeTimeout(5, TimeUnit.SECONDS)
            .build()
    }

    fun <T> createService(serviceClazz: Class<T>): T = retrofit.create(serviceClazz)

    /**
     * reified 关键字是用于 Kotlin 内联函数的,修饰内联函数的泛型
     * 泛型被修饰后,在方法体里,能从泛型拿到泛型的 Class对象
     * 这与 java是不同的,java 需要泛型且需要泛型的 Class类型时,是要把 Class传过来的
     * 但是 kotlin不用了
     */
    inline fun <reified T> createService(): T = createService(T::class.java)

}