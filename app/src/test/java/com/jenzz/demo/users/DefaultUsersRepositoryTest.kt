package com.jenzz.demo.users

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.jenzz.demo.common.ApiResponse
import kotlinx.coroutines.test.runTest
import org.junit.Test

class DefaultUsersRepositoryTest {

    @Test
    fun `observes users via local data source`() = runTest {
        val localDataSource = FakeLocalUsersDataSource()
        val sut = createRepository(localDataSource = localDataSource)

        sut.observeUsers().test {
            localDataSource.emit(testUsers)

            val users = awaitItem()
            assertThat(users).isEqualTo(testUsers)

            assertThat(cancelAndConsumeRemainingEvents()).isEmpty()
        }
    }

    @Test
    fun `observes single user via local data source`() = runTest {
        val user = testUsers.random()
        val localDataSource = FakeLocalUsersDataSource()
        val sut = createRepository(localDataSource = localDataSource)

        sut.observeUser(user.id).test {
            localDataSource.emit(user)

            val users = awaitItem()
            assertThat(users).isEqualTo(user)

            assertThat(cancelAndConsumeRemainingEvents()).isEmpty()
        }
    }

    @Test
    fun `adds user to local data source when fetching users succeeds`() = runTest {
        val user = testUsers.random()
        val localDataSource = FakeLocalUsersDataSource()
        val remoteDataSource = FakeRemoteUsersDataSource(
            apiResponse = ApiResponse.Success(listOf(user))
        )
        val sut = createRepository(
            localDataSource = localDataSource,
            remoteDataSource = remoteDataSource,
        )

        sut.observeUsers().test {
            val result = sut.fetchUsers()
            assertThat(result).isEqualTo(Result.success(Unit))

            val users = awaitItem()
            assertThat(users).contains(user)

            assertThat(cancelAndConsumeRemainingEvents()).isEmpty()
        }
    }

    @Test
    fun `does not add user to local data source when fetching users fails`() = runTest {
        val error = Exception("Error fetching users.")
        val remoteDataSource = FakeRemoteUsersDataSource(
            apiResponse = ApiResponse.Error(error)
        )
        val sut = createRepository(
            remoteDataSource = remoteDataSource,
        )

        sut.observeUsers().test {
            val result = sut.fetchUsers()
            assertThat(result).isEqualTo(Result.failure<Unit>(error))

            expectNoEvents()
        }
    }
}

private fun createRepository(
    localDataSource: LocalUsersDataSource = FakeLocalUsersDataSource(),
    remoteDataSource: RemoteUsersDataSource = FakeRemoteUsersDataSource(
        ApiResponse.Success(emptyList())
    ),
): UsersRepository =
    DefaultUsersRepository(
        localDataSource = localDataSource,
        remoteDataSource = remoteDataSource,
    )
