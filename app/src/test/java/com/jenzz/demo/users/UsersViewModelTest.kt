package com.jenzz.demo.users

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.jenzz.demo.rules.CoroutineTestRule
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

class UsersViewModelTest {

    @get:Rule
    val coroutinesTestRule = CoroutineTestRule()

    @Test
    fun `emits default state by default`() = runTest {
        val sut = createViewModel(usersRepository = FakeUsersRepository())

        sut.state.test {
            val state = awaitItem()
            assertThat(state).isEqualTo(
                UsersViewState(
                    isLoading = true,
                    users = emptyList(),
                )
            )
        }
    }

    @Test
    fun `emits cached users after default state`() = runTest {
        val usersRepository = FakeUsersRepository()
        val sut = createViewModel(usersRepository = usersRepository)

        sut.state.test {
            // default state
            assertThat(awaitItem().users).isEqualTo(emptyList<User>())

            // cached state
            val cachedUsers = testUsers
            usersRepository.emit(cachedUsers)
            assertThat(awaitItem().users).isEqualTo(cachedUsers)

            // fetched state
            val fetchedUsers = testUsers.mapIndexed { i, user ->
                user.copy(name = UserName("fetched name $i"))
            }
            usersRepository.emit(fetchedUsers)
            assertThat(awaitItem().users).isEqualTo(fetchedUsers)

            assertThat(cancelAndConsumeRemainingEvents()).isEmpty()
        }
    }
}

private fun createViewModel(
    usersRepository: UsersRepository,
): UsersViewModel =
    UsersViewModel(
        useCases = UsersUseCases(
            observeUsers = ObserveUsersUseCase(usersRepository),
            fetchUsers = FetchUsersUseCase(usersRepository),
        )
    )
