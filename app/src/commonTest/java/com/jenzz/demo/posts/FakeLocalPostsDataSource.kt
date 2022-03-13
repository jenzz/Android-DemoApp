package com.jenzz.demo.posts

import com.jenzz.demo.users.UserId
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.consumeAsFlow

class FakeLocalPostsDataSource : LocalPostsDataSource {

    private val postsEmitter = Channel<List<Post>>()

    override fun observePosts(userId: UserId): Flow<List<Post>> =
        postsEmitter.consumeAsFlow()

    override suspend fun add(posts: List<Post>): Result<List<Post>> {
        emit(posts)
        return Result.success(posts)
    }

    fun emit(posts: List<Post>) {
        postsEmitter.trySend(posts)
    }
}
