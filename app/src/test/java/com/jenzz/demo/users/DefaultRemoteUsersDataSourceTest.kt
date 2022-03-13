package com.jenzz.demo.users

import com.google.common.truth.Truth.assertThat
import com.jenzz.demo.common.ApiResponse
import kotlinx.coroutines.test.runTest
import okhttp3.HttpUrl
import org.junit.Assert.fail
import org.junit.Test
import kotlin.coroutines.cancellation.CancellationException

class DefaultRemoteUsersDataSourceTest {

    @Test
    fun `returns response when users service succeeds`() = runTest {
        val userDto = UserDto(
            id = 1,
            name = "name 1",
            nickname = "nickname 1",
            email = "email 1",
            website = "website1.com",
            phone = "1111",
        )
        val usersService = FakeUsersService { listOf(userDto) }
        val sut = createDataSource(usersService)

        val response = sut.fetchUsers()

        val expectedUser = User(
            id = UserId(userDto.id),
            name = UserName(userDto.name),
            nickname = UserNickname(userDto.nickname),
            email = UserEmail(userDto.email),
            website = HttpUrl.get("http://website1.com"),
            phone = PhoneNumber(userDto.phone),
        )
        assertThat(response).isEqualTo(ApiResponse.Success(listOf(expectedUser)))
    }

    @Test
    fun `returns error when users service fails`() = runTest {
        val error = Exception("Error fetching users.")
        val usersService = FakeUsersService { throw error }
        val sut = createDataSource(usersService)

        val response = sut.fetchUsers()

        assertThat(response).isEqualTo(ApiResponse.Error(error))
    }

    @Test
    fun `rethrows cancellation exceptions`() = runTest {
        val error = CancellationException()
        val usersService = FakeUsersService { throw error }
        val sut = createDataSource(usersService)

        try {
            sut.fetchUsers()
            fail("Expected CancellationException to be thrown.")
        } catch (e: CancellationException) {
            // expected
        }
    }
}

private fun createDataSource(
    usersService: UsersService,
): RemoteUsersDataSource =
    DefaultRemoteUsersDataSource(
        usersService = usersService
    )
