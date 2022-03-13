package com.jenzz.demo.posts.di

import androidx.lifecycle.SavedStateHandle
import com.jenzz.demo.destinations.PostsScreenDestination
import com.jenzz.demo.posts.PostsScreenNavArgs
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object PostsScreenModule {

    @Provides
    fun provideNavArgs(savedStateHandle: SavedStateHandle): PostsScreenNavArgs =
        PostsScreenDestination.argsFrom(savedStateHandle)
}
