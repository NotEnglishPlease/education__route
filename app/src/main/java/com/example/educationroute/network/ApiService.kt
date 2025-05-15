package com.example.educationroute.network

import com.example.educationroute.model.EmployeeDTO
import com.example.educationroute.model.LoginResponse
import com.example.educationroute.model.LessonDTO
import com.example.educationroute.model.ClientDTO
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import retrofit2.http.Path
import retrofit2.http.PUT
import retrofit2.http.DELETE

interface ApiService {
    @GET("login")
    suspend fun login(
        @Query("email") email: String,
        @Query("password") password: String
    ): Response<LoginResponse>

    @FormUrlEncoded
    @POST("register")
    suspend fun register(
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("name") name: String,
        @Field("phone") phone: String,
        @Field("child_name") childName: String,
        @Field("child_birthday") childBirthday: String
    ): Response<Unit>

    @GET("lessons")
    suspend fun getLessons(): List<LessonDTO>

    @GET("lessons/{id}")
    suspend fun getLessonById(@Path("id") id: Int): LessonDTO

    @POST("lessons")
    suspend fun createLesson(@Body lesson: LessonDTO): Response<Map<String, Int>>

    @PUT("lessons/{id}")
    suspend fun updateLesson(@Path("id") id: Int, @Body lesson: LessonDTO): Response<Unit>

    @DELETE("lessons/{id}")
    suspend fun deleteLesson(@Path("id") id: Int): Response<Unit>

    @GET("employee/by_name")
    suspend fun getEmployeeIdByName(@Query("name") name: String): Map<String, Int>

    @GET("employees")
    suspend fun getEmployees(): List<EmployeeDTO>

    @GET("clients")
    suspend fun getClients(): List<ClientDTO>

    @FormUrlEncoded
    @PUT("clients/{id}/paid_lessons")
    suspend fun updatePaidLessons(
        @Path("id") id: Int,
        @Field("paid_lessons") paidLessons: Int
    ): Response<Unit>

    @GET("lessons/available/{clientId}")
    suspend fun getAvailableLessons(@Path("clientId") clientId: Int): Response<List<LessonDTO>>
} 