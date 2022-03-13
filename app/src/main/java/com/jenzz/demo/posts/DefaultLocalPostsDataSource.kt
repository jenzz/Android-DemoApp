package com.jenzz.demo.posts

import com.jenzz.demo.common.CoroutineDispatchers
import com.jenzz.demo.users.UserId
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

val toPost =
    { id: Int, userId: Int, title: String, body: String ->
        Post(
            id = PostId(id),
            title = PostTitle(title),
            body = PostBody(body),
            userId = UserId(userId),
        )
    }

interface LocalPostsDataSource {

    fun observePosts(userId: UserId): Flow<List<Post>>

    suspend fun add(posts: List<Post>): Result<List<Post>>
}

class DefaultLocalPostsDataSource @Inject constructor(
    private val postsQueries: PostsQueries,
    private val dispatchers: CoroutineDispatchers,
) : LocalPostsDataSource {

    override fun observePosts(userId: UserId): Flow<List<Post>> =
        postsQueries
            .selectAll(userId.value, toPost)
            .asFlow()
            .mapToList(dispatchers.Default)

    override suspend fun add(posts: List<Post>): Result<List<Post>> =
        runCatching {
            withContext(dispatchers.Default) {
                postsQueries.transaction {
                    posts.forEach { post ->
                        postsQueries.upsert(
                            id = post.id.value,
                            userId = post.userId.value,
                            title = post.title.value,
                            body = post.body.value,
                        )
                    }
                }
                posts
            }
        }
}
