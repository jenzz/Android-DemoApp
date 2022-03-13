package com.jenzz.demo.users

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.jenzz.demo.R
import com.jenzz.demo.common.ToastMessageId
import com.jenzz.demo.common.rememberFlowWithLifecycle
import com.jenzz.demo.common.ui.widgets.LoadingView
import com.jenzz.demo.common.ui.widgets.Toast
import com.jenzz.demo.destinations.PostsScreenDestination
import com.jenzz.demo.ui.theme.elevation
import com.jenzz.demo.ui.theme.spacing
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination(start = true)
@Composable
fun UsersScreen(
    viewModel: UsersViewModel = hiltViewModel(),
    destinationsNavigator: DestinationsNavigator,
) {
    val state by rememberFlowWithLifecycle(viewModel.state)
        .collectAsState(initial = viewModel.initialState)
    UsersContent(
        state = state,
        onClick = { userId ->
            destinationsNavigator.navigate(PostsScreenDestination(userId))
        },
        onToastShown = viewModel::onToastShown,
    )
}

@Composable
private fun UsersContent(
    state: UsersViewState,
    onClick: (UserId) -> Unit,
    onToastShown: (ToastMessageId) -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(R.string.app_name)) },
            )
        }
    ) {
        UsersLoaded(
            users = state.users,
            onClick = onClick,
        )
        if (state.isLoading) {
            LoadingView()
        }
    }
    state.toastMessage?.let { message ->
        Toast(
            message = message,
            onToastShown = onToastShown,
        )
    }
}

@Composable
private fun UsersLoaded(
    users: List<User>,
    onClick: (UserId) -> Unit,
) {
    LazyColumn(
        contentPadding = PaddingValues(all = MaterialTheme.spacing.medium),
        verticalArrangement = Arrangement.spacedBy(space = MaterialTheme.spacing.medium),
    ) {
        items(users) { user ->
            UserRow(
                user = user,
                onClick = onClick,
            )
        }
    }
}

@Composable
private fun UserRow(
    user: User,
    onClick: (UserId) -> Unit,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = MaterialTheme.elevation.small,
        shape = RoundedCornerShape(corner = CornerSize(8.dp)),
        onClick = { onClick(user.id) },
        content = {
            Column(
                modifier = Modifier.padding(all = MaterialTheme.spacing.medium),
            ) {
                Text(text = user.name.value, style = MaterialTheme.typography.h6)
                Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))
                Text(text = user.email.value)
                Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))
                Text(
                    text = user.website.toString(),
                    color = MaterialTheme.colors.secondary,
                )
            }
        }
    )
}
