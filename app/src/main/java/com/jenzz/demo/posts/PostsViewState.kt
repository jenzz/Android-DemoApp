package com.jenzz.demo.posts

import com.jenzz.demo.common.ToastMessage
import com.jenzz.demo.users.User
import com.jenzz.demo.users.UserId

data class PostsViewState(
    val isLoading: Boolean,
    val posts: Posts,
    val toastMessage: ToastMessage? = null,
)

data class Post(
    val id: PostId,
    val title: PostTitle,
    val body: PostBody,
    val userId: UserId,
)

data class Posts(
    val entries: List<Post>,
    val user: User? = null,
)

@JvmInline
value class PostId(val value: Int)

@JvmInline
value class PostTitle(val value: String)

@JvmInline
value class PostBody(val value: String)
