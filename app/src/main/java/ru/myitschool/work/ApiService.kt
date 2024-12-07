package ru.myitschool.work

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("{login}/auth")
    suspend fun authenticate(@Path("login") login: String): Response<Void>
}