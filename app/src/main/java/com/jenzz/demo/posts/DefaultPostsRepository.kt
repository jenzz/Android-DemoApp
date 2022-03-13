package com.jenzz.demo.posts

import com.jenzz.demo.common.ApiResponse
import com.jenzz.demo.users.UserId
import com.jenzz.demo.users.UsersRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

interface PostsRepository {

    fun observePosts(userId: UserId): Flow<Posts>

    suspend fun fetchPosts(userId: UserId): Result<Unit>
}

class DefaultPostsRepository @Inject constructor(
    private val usersRepository: UsersRepository,
    private val localDataSource: LocalPostsDataSource,
    private val remoteDataSource: RemotePostsDataSource,
) : PostsRepository {

    override fun observePosts(userId: UserId): Flow<Posts> =
        combine(
            localDataSource.observePosts(userId),
            usersRepository.observeUser(userId),
            ::Posts
        )

    override suspend fun fetchPosts(userId: UserId): Result<Unit> {
        return when (val posts = remoteDataSource.fetchPosts(userId)) {
            is ApiResponse.Success -> {
                localDataSource.add(posts.data)
                return Result.success(Unit)
            }
            is ApiResponse.Error ->
                Result.failure(posts.error)
        }
    }
}
