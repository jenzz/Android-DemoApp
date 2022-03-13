package com.jenzz.demo.posts

import com.jenzz.demo.common.ApiResponse
import com.jenzz.demo.users.UserId

class FakeRemotePostsDataSource(
    private val apiResponse: ApiResponse<List<Post>>,
) : RemotePostsDataSource {

    override suspend fun fetchPosts(userId: UserId): ApiResponse<List<Post>> =
        apiResponse
}
