package com.jenzz.demo.users

import com.jenzz.demo.posts.Post
import com.jenzz.demo.posts.PostBody
import com.jenzz.demo.posts.PostId
import com.jenzz.demo.posts.PostTitle
import okhttp3.HttpUrl

val testUsers = listOf(
    User(
        id = UserId(1),
        name = UserName("local name 1"),
        nickname = UserNickname("local nickname 1"),
        email = UserEmail("local email 1"),
        website = HttpUrl.get("http://www.local.website1.com"),
        phone = PhoneNumber("1111"),
    ),
    User(
        id = UserId(2),
        name = UserName("local name 2"),
        nickname = UserNickname("local nickname 2"),
        email = UserEmail("local email 2"),
        website = HttpUrl.get("http://www.local.website2.com"),
        phone = PhoneNumber("2222"),
    ),
    User(
        id = UserId(3),
        name = UserName("local name 3"),
        nickname = UserNickname("local nickname 3"),
        email = UserEmail("local email 3"),
        website = HttpUrl.get("http://www.local.website3.com"),
        phone = PhoneNumber("3333"),
    ),
)

val testPosts = listOf(
    Post(
        id = PostId(value = 1),
        title = PostTitle("title 1"),
        body = PostBody("body 1"),
        userId = UserId(1),
    ),
    Post(
        id = PostId(value = 2),
        title = PostTitle("title 2"),
        body = PostBody("body 2"),
        userId = UserId(2),
    ),
    Post(
        id = PostId(value = 3),
        title = PostTitle("title 3"),
        body = PostBody("body 3"),
        userId = UserId(3),
    ),
)
