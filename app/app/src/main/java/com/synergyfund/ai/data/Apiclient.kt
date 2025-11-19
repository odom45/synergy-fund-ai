package com.synergyfund.ai.data

import com.synergyfund.ai.data.models.ChatRequest
import com.synergyfund.ai.data.models.ChatResponse
import com.synergyfund.ai.data.models.LoginRequest
import com.synergyfund.ai.data.models.LoginResponse
import com.synergyfund.ai.data.models.UserSummary
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import java.util.concurrent.TimeUnit

private const val BASE_URL = "https://synergyfundai.com/" // adjust if needed

interface SynergyApi {
    @POST("api/mobile/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse

    @GET("api/mobile/summary")
    suspend fun getUserSummary(): UserSummary

    @POST("api/mobile/chat")
    suspend fun chat(@Body request: ChatRequest): ChatResponse
}

object ApiClient {
    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val authInterceptor = okhttp3.Interceptor { chain ->
        val original = chain.request()
        val builder = original.newBuilder()
        // Include Authorization header if we have a token
        SessionManager.getToken()?.let { token ->
            builder.header("Authorization", "Bearer $token")
        }
        chain.proceed(builder.build())
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(logging)
        .addInterceptor(authInterceptor)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    val api: SynergyApi = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(SynergyApi::class.java)
}
