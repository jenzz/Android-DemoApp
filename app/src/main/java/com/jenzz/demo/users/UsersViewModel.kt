package com.jenzz.demo.users

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
class UsersViewModel @Inject constructor(
    useCases: UsersUseCases,
) : ViewModel() {

    val initialState = UsersViewState(
        isLoading = true,
        users = emptyList(),
    )
    private val isLoading = MutableStateFlow(initialState.isLoading)
    private val toastMessageManager = ToastMessageManager()

    val state = combine(
        isLoading,
        useCases.observeUsers(),
        toastMessageManager.message,
        ::UsersViewState,
    ).catch {
        toastMessageManager.emitMessage(
            ToastMessage(text = R.string.loading_users_failed)
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = initialState,
    )

    init {
        viewModelScope.launch {
            val users = useCases.fetchUsers()
            if (users.isFailure) {
                toastMessageManager.emitMessage(
                    ToastMessage(text = R.string.fetching_users_failed)
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
