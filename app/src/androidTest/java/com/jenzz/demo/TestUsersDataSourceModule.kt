package com.jenzz.demo

import com.jenzz.demo.users.DefaultRemoteUsersDataSource
import com.jenzz.demo.users.LocalUsersDataSource
import com.jenzz.demo.users.RemoteUsersDataSource
import com.jenzz.demo.users.di.UsersDataSourceModule
import dagger.Binds
import dagger.Module
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn

@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [UsersDataSourceModule::class],
)
@Module
interface TestUsersDataSourceModule {

    @Binds
    fun FakeLocalUsersDataSource.bindLocal(): LocalUsersDataSource

    @Binds
    fun DefaultRemoteUsersDataSource.bindRemote(): RemoteUsersDataSource
}
