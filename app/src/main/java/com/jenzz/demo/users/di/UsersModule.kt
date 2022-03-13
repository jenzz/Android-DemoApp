package com.jenzz.demo.users.di

import com.jenzz.demo.users.*
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
interface UsersRepositoryModule {

    @Binds
    fun DefaultUsersRepository.bind(): UsersRepository

}

@InstallIn(SingletonComponent::class)
@Module
interface UsersDataSourceModule {

    @Binds
    fun DefaultLocalUsersDataSource.bindLocal(): LocalUsersDataSource

    @Binds
    fun DefaultRemoteUsersDataSource.bindRemote(): RemoteUsersDataSource
}
