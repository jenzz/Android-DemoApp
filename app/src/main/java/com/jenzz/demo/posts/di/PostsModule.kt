package com.jenzz.demo.posts.di

import com.jenzz.demo.posts.*
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
interface PostsRepositoryModule {

    @Binds
    fun DefaultPostsRepository.bind(): PostsRepository
}

@InstallIn(SingletonComponent::class)
@Module
interface PostsDataSourcesModule {

    @Binds
    fun DefaultLocalPostsDataSource.bindLocal(): LocalPostsDataSource

    @Binds
    fun DefaultRemotePostsDataSource.bindRemote(): RemotePostsDataSource
}
