package com.jenzz.demo.di

import com.jenzz.demo.common.CoroutineDispatchers
import com.jenzz.demo.common.DefaultCoroutineDispatchers
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface CoroutineModule {

    @Binds
    fun DefaultCoroutineDispatchers.bind(): CoroutineDispatchers
}
