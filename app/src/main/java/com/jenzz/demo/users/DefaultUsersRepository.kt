package com.jenzz.demo.users

import com.jenzz.demo.common.ApiResponse
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface UsersRepository {

    fun observeUsers(): Flow<List<User>>

    fun observeUser(userId: UserId): Flow<User>

    suspend fun fetchUsers(): Result<Unit>
}

class DefaultUsersRepository @Inject constructor(
    private val localDataSource: LocalUsersDataSource,
    private val remoteDataSource: RemoteUsersDataSource,
) : UsersRepository {

    override fun observeUsers(): Flow<List<User>> =
        localDataSource.observeUsers()

    override fun observeUser(userId: UserId): Flow<User> =
        localDataSource.observeUser(userId)

    override suspend fun fetchUsers(): Result<Unit> =
        when (val users = remoteDataSource.fetchUsers()) {
            is ApiResponse.Success -> {
                localDataSource.add(users.data)
                Result.success(Unit)
            }
            is ApiResponse.Error ->
                Result.failure(users.error)
        }
}
