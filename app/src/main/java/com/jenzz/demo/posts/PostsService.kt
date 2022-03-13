package com.jenzz.demo.posts

import com.google.gson.annotations.SerializedName
import retrofit2.http.GET
import retrofit2.http.Query

interface PostsService {

    @GET("/posts")
    suspend fun fetchPosts(@Query("userId") userId: Int): List<PostDto>
}

data class PostDto(
    @SerializedName("id")
    val id: Int,
    @SerializedName("userId")
    val userId: Int,
    @SerializedName("title")
    val title: String,
    @SerializedName("body")
    val body: String,
)
