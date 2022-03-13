package com.jenzz.demo.users

import com.jenzz.demo.common.CoroutineDispatchers
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import com.squareup.sqldelight.runtime.coroutines.mapToOne
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import okhttp3.HttpUrl
import javax.inject.Inject

val toUser =
    {
            id: Int, name: String, nickname: String, email: String, website: String, phone: String,
        ->
        User(
            id = UserId(id),
            name = UserName(name),
            nickname = UserNickname(nickname),
            email = UserEmail(email),
            website = HttpUrl.get(website),
            phone = PhoneNumber(phone),
        )
    }

interface LocalUsersDataSource {

    fun observeUsers(): Flow<List<User>>

    fun observeUser(userId: UserId): Flow<User>

    suspend fun add(users: List<User>): Result<List<User>>
}

class DefaultLocalUsersDataSource @Inject constructor(
    private val usersQueries: UsersQueries,
    private val dispatchers: CoroutineDispatchers,
) : LocalUsersDataSource {

    override fun observeUsers(): Flow<List<User>> =
        usersQueries
            .selectAll(toUser)
            .asFlow()
            .mapToList(dispatchers.Default)

    override fun observeUser(userId: UserId): Flow<User> =
        usersQueries
            .selectById(userId.value, toUser)
            .asFlow()
            .mapToOne(dispatchers.Default)

    override suspend fun add(users: List<User>): Result<List<User>> =
        runCatching {
            withContext(dispatchers.Default) {
                usersQueries.transaction {
                    users.forEach { user ->
                        usersQueries.upsert(
                            id = user.id.value,
                            name = user.name.value,
                            nickname = user.nickname.value,
                            email = user.email.value,
                            website = user.website.toString(),
                            phone = user.phone.value,
                        )
                    }
                }
                users
            }
        }
}
