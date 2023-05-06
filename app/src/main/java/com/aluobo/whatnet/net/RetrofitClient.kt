package com.aluobo.whatnet.net

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.aluobo.whatnet.App
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/***
 * 使用拦截器处理 cookie
 * 但是没有完全实现
 */
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
            .addInterceptor(
                HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                }
            )
            .addInterceptor(ReceiverCookiesInterceptor)
            .addInterceptor(AutoAddCookiesInterceptor)
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

object ReceiverCookiesInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originResponse = chain.proceed(chain.request())
        Log.d("okhttp", "intercept: ${originResponse.headers("Set-Cookie")}")
        if (originResponse.headers("Set-Cookie").isNotEmpty()) {
            val cookies = mutableSetOf<String>()
            // 获取所有的 cookie
            originResponse.headers("Set-Cookie").forEach {
                cookies.add(it)
            }
            // 保存 cookie 到 dataStore
            MainScope().launch(Dispatchers.IO) {
                App.appContext.dataStore.edit {
                    it[cookiesKey] = cookies
                }
            }
        }

        return originResponse
    }

}


object AutoAddCookiesInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var builder :Request.Builder
        // 从本地存储中获取到保存的 cookies
        runBlocking(Dispatchers.IO){
            builder = chain.request().newBuilder()
            val cookies = App.appContext.dataStore.data.map { it[cookiesKey] }.first()
            cookies?.let {
                it.forEach { cookie ->
                    builder.addHeader("Cookie", cookie)
                }
            }
        }

        return chain.proceed(builder.build())
    }

}


val cookiesKey = stringSetPreferencesKey("cookie")

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "cookies")

