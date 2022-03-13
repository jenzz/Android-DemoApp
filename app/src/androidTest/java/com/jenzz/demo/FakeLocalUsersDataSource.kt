package com.jenzz.demo

import com.jenzz.demo.users.LocalUsersDataSource
import com.jenzz.demo.users.User
import com.jenzz.demo.users.UserId
import com.jenzz.demo.users.testUsers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class FakeLocalUsersDataSource @Inject constructor() : LocalUsersDataSource {

    private val users = testUsers

    override fun observeUsers(): Flow<List<User>> =
        flowOf(users)

    override fun observeUser(userId: UserId): Flow<User> =
        flowOf(users.first())

    override suspend fun add(users: List<User>): Result<List<User>> =
        Result.success(users)
}
