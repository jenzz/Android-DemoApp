package com.jenzz.demo.users

import com.jenzz.demo.common.ApiResponse
import kotlinx.coroutines.CancellationException
import okhttp3.HttpUrl
import javax.inject.Inject

fun UserDto.toUser(): User =
    User(
        id = UserId(id),
        name = UserName(name),
        nickname = UserNickname(nickname),
        email = UserEmail(email),
        // API returns malformed URL so need to manually prepend http to use type-safe HttpUrl.
        website = HttpUrl.Builder().scheme("http").host(website).build(),
        phone = PhoneNumber(phone),
    )

interface RemoteUsersDataSource {

    suspend fun fetchUsers(): ApiResponse<List<User>>
}

class DefaultRemoteUsersDataSource @Inject constructor(
    private val usersService: UsersService,
) : RemoteUsersDataSource {

    override suspend fun fetchUsers(): ApiResponse<List<User>> =
        try {
            val users = usersService.fetchUsers()
            ApiResponse.Success(users.map(UserDto::toUser))
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
