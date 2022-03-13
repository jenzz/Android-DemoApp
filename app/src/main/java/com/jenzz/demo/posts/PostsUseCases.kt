package com.jenzz.demo.posts

import com.jenzz.demo.users.UserId
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

data class PostsUseCases @Inject constructor(
    val observePosts: ObservePostsUseCase,
    val fetchPosts: FetchPostsUseCase,
)

class ObservePostsUseCase @Inject constructor(
    private val postsRepository: PostsRepository,
) {

    operator fun invoke(userId: UserId): Flow<Posts> =
        postsRepository.observePosts(userId)
//            .onEach { delay(1000) } // Deliberate delay to see loading spinner.
}


class FetchPostsUseCase @Inject constructor(
    private val postsRepository: PostsRepository,
) {

    suspend operator fun invoke(userId: UserId): Result<Unit> {
        delay(1000) // Deliberate delay to see loading spinner.
        return postsRepository.fetchPosts(userId)
    }
}
