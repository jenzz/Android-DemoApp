package com.jenzz.demo.users

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.map

class FakeUsersRepository : UsersRepository {

    private val usersEmitter = Channel<List<User>>()

    override fun observeUsers(): Flow<List<User>> =
        usersEmitter.consumeAsFlow()

    override fun observeUser(userId: UserId): Flow<User> =
        usersEmitter.consumeAsFlow().map(List<User>::first)

    override suspend fun fetchUsers(): Result<Unit> =
        Result.success(Unit)

    fun emit(users: List<User>) {
        usersEmitter.trySend(users)
    }

    fun emit(user: User) {
        emit(listOf(user))
    }
}
