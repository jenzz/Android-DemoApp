package com.jenzz.demo.posts

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.jenzz.demo.common.ApiResponse
import com.jenzz.demo.users.*
import kotlinx.coroutines.test.runTest
import org.junit.Test

class DefaultPostsRepositoryTest {

    private val user = testUsers.random()
    private val posts = testPosts.filter { post -> post.userId == user.id }

    @Test
    fun `emits posts when local data source emits post`() = runTest {
        val usersRepository = FakeUsersRepository()
        val localDataSource = FakeLocalPostsDataSource()
        val sut = createRepository(
            usersRepository = usersRepository,
            localDataSource = localDataSource,
        )

        sut.observePosts(user.id).test {
            usersRepository.emit(user)
            localDataSource.emit(posts)

            assertThat(awaitItem()).isEqualTo(Posts(entries = posts, user = user))

            val updatedPosts = posts.map { it.copy(title = PostTitle("updated title")) }
            localDataSource.emit(updatedPosts)
            assertThat(awaitItem()).isEqualTo(Posts(entries = updatedPosts, user = user))

            assertThat(cancelAndConsumeRemainingEvents()).isEmpty()
        }
    }

    @Test
    fun `emits posts when user repository emits updated user`() = runTest {
        val usersRepository = FakeUsersRepository()
        val localDataSource = FakeLocalPostsDataSource()
        val sut = createRepository(
            usersRepository = usersRepository,
            localDataSource = localDataSource,
        )

        sut.observePosts(user.id).test {
            usersRepository.emit(user)
            localDataSource.emit(posts)

            assertThat(awaitItem()).isEqualTo(Posts(entries = posts, user = user))

            val updatedUser = user.copy(name = UserName("updated name"))
            usersRepository.emit(updatedUser)
            assertThat(awaitItem()).isEqualTo(Posts(entries = posts, user = updatedUser))

            assertThat(cancelAndConsumeRemainingEvents()).isEmpty()
        }
    }

    @Test
    fun `adds posts to local data source when fetching posts succeeds`() = runTest {
        val usersRepository = FakeUsersRepository()
        val localDataSource = FakeLocalPostsDataSource()
        val remoteDataSource = FakeRemotePostsDataSource(
            apiResponse = ApiResponse.Success(testPosts)
        )
        val sut = createRepository(
            usersRepository = usersRepository,
            localDataSource = localDataSource,
            remoteDataSource = remoteDataSource,
        )

        sut.observePosts(user.id).test {
            usersRepository.emit(user)

            val result = sut.fetchPosts(user.id)
            assertThat(result).isEqualTo(Result.success(Unit))

            val posts = awaitItem()
            assertThat(posts.user).isEqualTo(user)
            assertThat(posts.entries).isEqualTo(testPosts)

            assertThat(cancelAndConsumeRemainingEvents()).isEmpty()
        }
    }

    @Test
    fun `does not add post to local data source when fetching posts fails`() = runTest {
        val error = Exception("Error fetching users.")
        val remoteDataSource = FakeRemotePostsDataSource(
            apiResponse = ApiResponse.Error(error)
        )
        val sut = createRepository(
            remoteDataSource = remoteDataSource,
        )

        sut.observePosts(user.id).test {
            val result = sut.fetchPosts(user.id)
            assertThat(result).isEqualTo(Result.failure<Unit>(error))

            expectNoEvents()
        }
    }
}

private fun createRepository(
    usersRepository: UsersRepository = FakeUsersRepository(),
    localDataSource: LocalPostsDataSource = FakeLocalPostsDataSource(),
    remoteDataSource: RemotePostsDataSource = FakeRemotePostsDataSource(
        ApiResponse.Success(emptyList())
    ),
): PostsRepository =
    DefaultPostsRepository(
        usersRepository = usersRepository,
        localDataSource = localDataSource,
        remoteDataSource = remoteDataSource,
    )
