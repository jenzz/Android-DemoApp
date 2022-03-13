package com.jenzz.demo.posts

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.jenzz.demo.R
import com.jenzz.demo.common.ToastMessageId
import com.jenzz.demo.common.rememberFlowWithLifecycle
import com.jenzz.demo.common.ui.widgets.LoadingView
import com.jenzz.demo.common.ui.widgets.Toast
import com.jenzz.demo.ui.theme.elevation
import com.jenzz.demo.ui.theme.spacing
import com.jenzz.demo.users.User
import com.jenzz.demo.users.UserId
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

data class PostsScreenNavArgs(
    val userId: UserId,
)

@Destination(navArgsDelegate = PostsScreenNavArgs::class)
@Composable
fun PostsScreen(
    viewModel: PostsViewModel = hiltViewModel(),
    destinationsNavigator: DestinationsNavigator,
) {
    val state by rememberFlowWithLifecycle(viewModel.state)
        .collectAsState(initial = viewModel.initialState)
    PostsContent(
        state = state,
        onNavigateUp = { destinationsNavigator.navigateUp() },
        onToastShown = viewModel::onToastShown,
    )
}

@Composable
private fun PostsContent(
    state: PostsViewState,
    onNavigateUp: () -> Unit,
    onToastShown: (ToastMessageId) -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = onNavigateUp) {
                        Icon(
                            imageVector = Icons.Rounded.ArrowBack,
                            contentDescription = stringResource(id = R.string.back)
                        )
                    }
                },
                title = { Text(text = stringResource(R.string.app_name)) },
            )
        }
    ) {
        PostsLoaded(state.posts)
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
private fun PostsLoaded(
    posts: Posts,
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(space = MaterialTheme.spacing.small),
    ) {
        if (posts.user != null) {
            item {
                UserHeader(
                    modifier = Modifier.padding(all = MaterialTheme.spacing.large),
                    user = posts.user,
                )
            }
            if (posts.entries.isNotEmpty()) {
                stickyHeader {
                    PostsHeader(posts.user)
                }
            }
        }
        items(posts.entries) { post ->
            PostRow(
                modifier = Modifier
                    .padding(all = MaterialTheme.spacing.medium)
                    .fillMaxSize(),
                post = post,
            )
        }
    }
}

@Composable
private fun UserHeader(
    modifier: Modifier = Modifier,
    user: User,
) {
    Column(
        modifier = modifier
    ) {
        Text(text = user.name.value, style = MaterialTheme.typography.h4)
        Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))
        Text(text = user.nickname.value, style = MaterialTheme.typography.h6)
        Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))
        Text(text = user.email.value)
        Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))
        Text(
            text = user.website.toString(),
            color = MaterialTheme.colors.secondary,
        )
        Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))
        Text(text = user.phone.value)
    }
}

@Composable
private fun PostsHeader(
    user: User,
    backgroundColor: Color = MaterialTheme.colors.primarySurface,
    contentColor: Color = contentColorFor(backgroundColor),
) {
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = backgroundColor)
            .padding(
                vertical = MaterialTheme.spacing.medium,
                horizontal = MaterialTheme.spacing.large,
            ),
        text = stringResource(id = R.string.posts_by, user.name.value),
        color = contentColor,
    )
}

@Composable
private fun PostRow(
    modifier: Modifier = Modifier,
    post: Post,
) {
    Card(
        modifier = modifier,
        elevation = MaterialTheme.elevation.small,
        shape = RoundedCornerShape(corner = CornerSize(8.dp)),
        content = {
            Column(
                modifier = Modifier.padding(all = MaterialTheme.spacing.medium),
            ) {
                Text(text = post.title.value, style = MaterialTheme.typography.h6)
                Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))
                Text(
                    text = post.body.value,
                    style = MaterialTheme.typography.body2,
                    fontWeight = FontWeight.Light,
                )
            }
        }
    )
}
