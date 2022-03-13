package com.jenzz.demo.posts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jenzz.demo.R
import com.jenzz.demo.common.ToastMessage
import com.jenzz.demo.common.ToastMessageId
import com.jenzz.demo.common.ToastMessageManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostsViewModel @Inject constructor(
    useCases: PostsUseCases,
    navArgs: PostsScreenNavArgs,
) : ViewModel() {

    val initialState = PostsViewState(
        isLoading = true,
        posts = Posts(
            entries = emptyList(),
            user = null
        ),
    )
    private val isLoading = MutableStateFlow(initialState.isLoading)
    private val toastMessageManager = ToastMessageManager()

    val state = combine(
        isLoading,
        useCases.observePosts(navArgs.userId),
        toastMessageManager.message,
        ::PostsViewState,
    ).catch {
        toastMessageManager.emitMessage(
            ToastMessage(text = R.string.loading_posts_failed)
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = initialState,
    )

    init {
        viewModelScope.launch {
            val posts = useCases.fetchPosts(navArgs.userId)
            if (posts.isFailure) {
                toastMessageManager.emitMessage(
                    ToastMessage(text = R.string.fetching_posts_failed)
                )
            }
            isLoading.value = false
        }
    }

    fun onToastShown(id: ToastMessageId) {
        viewModelScope.launch {
            toastMessageManager.clearMessage(id)
        }
    }
}
