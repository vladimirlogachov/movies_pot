package com.vlohachov.shared.ui.screen.credits.cast

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import coil3.compose.rememberAsyncImagePainter
import com.vlohachov.shared.domain.model.movie.credit.CastMember
import com.vlohachov.shared.ui.component.Profile
import com.vlohachov.shared.ui.component.bar.AppBar
import com.vlohachov.shared.ui.component.bar.ErrorBar
import com.vlohachov.shared.ui.component.button.ScrollToTop
import com.vlohachov.shared.ui.screen.Screen
import com.vlohachov.shared.ui.state.ViewState
import moviespot.shared_ui.generated.resources.Res
import moviespot.shared_ui.generated.resources.cast
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import org.koin.core.parameter.parametersOf

internal data object CastScreen : Screen {

    private const val ArgMovieId = "movieId"

    private val arguments = listOf(
        navArgument(name = "movieId") { type = NavType.LongType }
    )

    override val path: String = "credits/cast?$ArgMovieId"

    fun NavGraphBuilder.cast(navController: NavController) {
        composable(route = "$path={$ArgMovieId}", arguments = arguments) { backStackEntry ->
            val movieId = requireNotNull(value = backStackEntry.arguments?.getLong(ArgMovieId)) {
                "Missing required argument $ArgMovieId"
            }

            Cast(movieId = movieId, onBack = navController::navigateUp)
        }
    }

}

private const val VISIBLE_ITEMS_THRESHOLD = 3

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun Cast(
    movieId: Long,
    onBack: () -> Unit,
    viewModel: CastViewModel = koinInject { parametersOf(movieId) },
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val gridState = rememberLazyGridState()
    val showScrollToTop by remember { derivedStateOf { gridState.firstVisibleItemIndex > VISIBLE_ITEMS_THRESHOLD } }
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(connection = scrollBehavior.nestedScrollConnection),
        topBar = {
            AppBar(
                modifier = Modifier.fillMaxWidth(),
                title = stringResource(resource = Res.string.cast),
                scrollBehavior = scrollBehavior,
                onBackClick = onBack,
            )
        },
        floatingActionButton = {
            ScrollToTop(visible = showScrollToTop, gridState = gridState)
        },
        snackbarHost = {
            SnackbarHost(
                modifier = Modifier
                    .testTag(tag = CastDefaults.ContentErrorTestTag)
                    .navigationBarsPadding(),
                hostState = snackbarHostState,
            )
        },
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues = paddingValues),
        ) {
            Content(
                modifier = Modifier
                    .testTag(tag = CastDefaults.ContentTestTag)
                    .fillMaxSize(),
                gridState = gridState,
                viewState = uiState.viewState,
                onCredit = { },
                onError = viewModel::onError,
            )

            uiState.error?.printStackTrace()

            ErrorBar(
                error = uiState.error,
                snackbarHostState = snackbarHostState,
                onDismissed = viewModel::onErrorConsumed,
            )
        }
    }
}

@Composable
private fun Content(
    modifier: Modifier,
    gridState: LazyGridState,
    viewState: ViewState<List<CastMember>>,
    onCredit: (creditId: Long) -> Unit,
    onError: (error: Throwable) -> Unit,
) = LazyVerticalGrid(
    modifier = modifier,
    state = gridState,
    columns = GridCells.Fixed(count = 2),
    contentPadding = PaddingValues(all = 16.dp),
    verticalArrangement = Arrangement.spacedBy(space = 16.dp),
    horizontalArrangement = Arrangement.spacedBy(space = 16.dp),
) {
    when (viewState) {
        ViewState.Loading -> item(span = { GridItemSpan(currentLineSpan = 2) }) {
            Box(
                modifier = Modifier.testTag(tag = CastDefaults.ContentLoadingTestTag),
                contentAlignment = Alignment.TopCenter,
            ) {
                CircularProgressIndicator()
            }
        }

        is ViewState.Error -> item {
            LaunchedEffect(key1 = viewState.error) {
                viewState.error?.run(onError)
            }
        }

        is ViewState.Success ->
            items(items = viewState.data, key = { item -> item.id }) { member ->
                var error by remember { mutableStateOf(false) }
                val painter = rememberAsyncImagePainter(
                    model = member.profilePath,
                    onError = { error = true },
                )
                Profile(
                    modifier = Modifier
                        .width(width = 164.dp)
                        .aspectRatio(ratio = 0.75f),
                    title = member.name,
                    body = member.character,
                    painter = painter,
                    onClick = { onCredit(member.id) },
                    error = error,
                )
            }
    }
}

internal object CastDefaults {

    const val ContentTestTag = "content"
    const val ContentLoadingTestTag = "content_loading"
    const val ContentErrorTestTag = "content_error"

}