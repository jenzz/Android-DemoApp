package com.jenzz.demo.users

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

data class UsersUseCases @Inject constructor(
    val observeUsers: ObserveUsersUseCase,
    val fetchUsers: FetchUsersUseCase,
)

class ObserveUsersUseCase @Inject constructor(
    private val usersRepository: UsersRepository,
) {

    operator fun invoke(): Flow<List<User>> =
        /*
         * Use cases like this would also perform any validation logic,
         * for example if there were any input fields on the screen.
         * For this simple app they just delegate to the repository.
         */
        usersRepository.observeUsers()
//            .onEach { delay(1000) } // Deliberate delay to see loading spinner.
}

class FetchUsersUseCase @Inject constructor(
    private val usersRepository: UsersRepository,
) {

    suspend operator fun invoke(): Result<Unit> {
        delay(1000) // Deliberate delay to see loading spinner.
        return usersRepository.fetchUsers()
    }
}
