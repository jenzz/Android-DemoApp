package com.jenzz.demo.users

import com.google.gson.annotations.SerializedName
import retrofit2.http.GET

interface UsersService {

    @GET("/users")
    suspend fun fetchUsers(): List<UserDto>
}

data class UserDto(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("username")
    val nickname: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("website")
    val website: String,
    @SerializedName("phone")
    val phone: String,
)
