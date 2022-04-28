package com.example.todojeanlorisgankpe_v2.network

import com.example.todojeanlorisgankpe_v2.tasklist.Task
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.SerialName
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import kotlinx.serialization.Serializable
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Response
import retrofit2.http.*

@Serializable
data class UserInfo(
    @SerialName("email")
    val email: String,
    @SerialName("firstname")
    val firstName: String,
    @SerialName("lastname")
    val lastName: String
)

interface UserWebService {
    @GET("users/info")
    suspend fun getInfo(): Response<UserInfo>
}

interface TasksWebService {
    @GET("tasks")
    suspend fun getTasks(): Response<List<Task>>

    @POST("tasks")
    suspend fun create(@Body task: Task): Response<Task>

    @PATCH("tasks/{id}")
    suspend fun update(@Body task: Task, @Path("id") id: String = task.id): Response<Task>

    @DELETE("tasks/{id}")
    suspend fun delete(@Path("id") id: String): Response<Unit>
}

object Api {

    val userWebService : UserWebService by lazy {
        retrofit.create(UserWebService::class.java)
    }

    val tasksWebService : TasksWebService by lazy {
        retrofit.create(TasksWebService::class.java)
    }

    private const val TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VyX2lkIjo3MjAsImV4cCI6MTY4MjY4Nzk5M30.MavgrxhFzRbiFYdiamkYgXY1Cv1SoUZRLqYPcH3b0is"

    private val retrofit by lazy {
        // client HTTP
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .addInterceptor { chain ->
                // intercepteur qui ajoute le `header` d'authentification avec votre token:
                val newRequest = chain.request().newBuilder()
                    .addHeader("Authorization", "Bearer $TOKEN")
                    .build()
                chain.proceed(newRequest)
            }
            .build()

        // transforme le JSON en objets kotlin et inversement
        val jsonSerializer = Json {
            ignoreUnknownKeys = true
            coerceInputValues = true
        }

        // instance retrofit pour implémenter les webServices:
        Retrofit.Builder()
            .baseUrl("https://android-tasks-api.herokuapp.com/api/")
            .client(okHttpClient)
            .addConverterFactory(jsonSerializer.asConverterFactory("application/json".toMediaType()))
            .build()
    }
}

