package com.jenzz.demo.users

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.consumeAsFlow

class FakeLocalUsersDataSource() : LocalUsersDataSource {

    private val usersEmitter = Channel<List<User>>()
    private val userEmitter = Channel<User>()

    override fun observeUsers(): Flow<List<User>> =
        usersEmitter.consumeAsFlow()

    override fun observeUser(userId: UserId): Flow<User> =
        userEmitter.consumeAsFlow()

    override suspend fun add(users: List<User>): Result<List<User>> {
        emit(users)
        return Result.success(users)
    }

    fun emit(users: List<User>) {
        usersEmitter.trySend(users)
    }

    fun emit(users: User) {
        userEmitter.trySend(users)
    }
}
