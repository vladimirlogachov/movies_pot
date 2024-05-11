package com.vlohachov.shared.ui.screen.search

import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.SoftwareKeyboardController
import com.vlohachov.shared.domain.model.movie.Movie

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal actual fun MoviesSearch(
    onBack: () -> Unit,
    onMovieDetails: (Movie) -> Unit,
    gridState: LazyGridState,
    snackbarHostState: SnackbarHostState,
    scrollBehavior: TopAppBarScrollBehavior,
    keyboardController: SoftwareKeyboardController?,
) {
}
