package com.jenzz.demo.posts

import com.jenzz.demo.common.ApiResponse
import com.jenzz.demo.users.UserId
import kotlinx.coroutines.CancellationException
import javax.inject.Inject

fun PostDto.toPost(): Post =
    Post(
        id = PostId(id),
        title = PostTitle(title),
        body = PostBody(body),
        userId = UserId(userId),
    )

interface RemotePostsDataSource {

    suspend fun fetchPosts(userId: UserId): ApiResponse<List<Post>>
}

class DefaultRemotePostsDataSource @Inject constructor(
    private val postsService: PostsService,
) : RemotePostsDataSource {

    override suspend fun fetchPosts(userId: UserId): ApiResponse<List<Post>> =
        try {
            val posts = postsService.fetchPosts(userId.value)
            ApiResponse.Success(posts.map(PostDto::toPost))
        } catch (e: Exception) {
            if (e is CancellationException) {
                /*
                 * Needs to be rethrown so parent scope is notified.
                 * TODO More fine-grained exception handling.
                 */
                throw e
            }
            ApiResponse.Error(e)
        }
}
