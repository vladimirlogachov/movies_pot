package com.vlohachov.moviespot.ui.details

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.ArrowForward
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.vlohachov.domain.model.movie.Movie
import com.vlohachov.domain.model.movie.MovieDetails
import com.vlohachov.moviespot.R
import com.vlohachov.moviespot.core.LoremIpsum
import com.vlohachov.moviespot.core.ViewState
import com.vlohachov.moviespot.core.utils.DateUtils
import com.vlohachov.moviespot.core.utils.DecimalUtils.format
import com.vlohachov.moviespot.core.utils.TimeUtils
import com.vlohachov.moviespot.ui.components.movie.MoviesSection
import com.vlohachov.moviespot.ui.components.movie.Poster
import com.vlohachov.moviespot.ui.components.section.Section
import com.vlohachov.moviespot.ui.components.section.SectionDefaults
import com.vlohachov.moviespot.ui.components.section.SectionTitle
import com.vlohachov.moviespot.ui.destinations.*
import com.vlohachov.moviespot.ui.theme.MoviesPotTheme
import com.vlohachov.moviespot.ui.theme.Typography
import org.koin.androidx.compose.getViewModel
import org.koin.core.parameter.parametersOf
import kotlin.text.Typography as Chars

@OptIn(ExperimentalMaterial3Api::class)
@Destination
@Composable
fun MovieDetails(
    navigator: DestinationsNavigator,
    movieId: Long,
    movieTitle: String,
    viewModel: MovieDetailsViewModel = getViewModel { parametersOf(movieId) },
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val unknownErrorText = stringResource(id = R.string.uknown_error)
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(connection = scrollBehavior.nestedScrollConnection),
        topBar = {
            CenterAlignedTopAppBar(
                modifier = Modifier.fillMaxWidth(),
                title = { },
                navigationIcon = {
                    IconButton(onClick = { navigator.navigateUp() }) {
                        Icon(
                            imageVector = Icons.Rounded.ArrowBack,
                            contentDescription = null,
                        )
                    }
                },
                scrollBehavior = scrollBehavior,
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues = paddingValues),
        ) {
            Content(
                modifier = Modifier.fillMaxSize(),
                detailsViewState = uiState.detailsViewState,
                recommendationsViewState = uiState.recommendationsViewState,
                onPoster = { path -> navigator.navigate(FullscreenImageDestination(path = path)) },
                onCast = { navigator.navigate(CastDestination(movieId = movieId)) },
                onCrew = { navigator.navigate(CrewDestination(movieId = movieId)) },
                onMoreAbout = {},
                onMoreRecommendations = {
                    navigator.navigate(
                        SimilarMoviesDestination(
                            movieId = movieId,
                            movieTitle = movieTitle,
                        )
                    )
                },
                onMovieClick = { movie ->
                    navigator.navigate(
                        MovieDetailsDestination(
                            movieId = movie.id,
                            movieTitle = movie.title,
                        )
                    )
                },
                onError = viewModel::onError,
            )

            uiState.error?.run {
                LaunchedEffect(snackbarHostState) {
                    snackbarHostState.showSnackbar(message = localizedMessage ?: unknownErrorText)
                    viewModel.onErrorConsumed()
                }
            }
        }
    }
}

@Composable
private fun Content(
    modifier: Modifier,
    detailsViewState: ViewState<MovieDetails>,
    recommendationsViewState: ViewState<List<Movie>>,
    onPoster: (path: String) -> Unit,
    onCast: () -> Unit,
    onCrew: () -> Unit,
    onMoreAbout: () -> Unit,
    onMoreRecommendations: () -> Unit,
    onMovieClick: (movie: Movie) -> Unit,
    onError: (error: Throwable) -> Unit,
) {
    LazyColumn(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        item {
            Details(
                modifier = Modifier.fillMaxWidth(),
                viewState = detailsViewState,
                onPoster = onPoster,
                onCast = onCast,
                onCrew = onCrew,
                onMore = onMoreAbout,
                onError = onError,
            )
        }
        item {
            MoviesSection(
                modifier = Modifier.fillMaxWidth(),
                title = stringResource(id = R.string.recommendations),
                viewState = recommendationsViewState,
                onMore = onMoreRecommendations,
                onMovieClick = onMovieClick,
                textStyles = SectionDefaults.smallTextStyles(),
            )
        }
    }
}

@Composable
private fun Details(
    modifier: Modifier,
    viewState: ViewState<MovieDetails>,
    onPoster: (path: String) -> Unit,
    onCast: () -> Unit,
    onCrew: () -> Unit,
    onMore: () -> Unit,
    onError: (error: Throwable) -> Unit,
) {
    when (viewState) {
        ViewState.Loading -> CircularProgressIndicator(modifier = Modifier.padding(all = 16.dp))
        is ViewState.Error -> viewState.error?.run(onError)
        is ViewState.Success -> with(viewState.data) {
            Column(modifier = modifier) {
                Headline(
                    modifier = Modifier
                        .padding(all = 16.dp)
                        .fillMaxWidth(),
                    poster = { modifier ->
                        Poster(
                            modifier = modifier,
                            painter = rememberAsyncImagePainter(model = posterPath),
                            onClick = { onPoster(posterPath) },
                        )
                    },
                    title = { Text(text = title) },
                    info = {
                        Text(
                            text = buildString {
                                append(DateUtils.format(date = releaseDate))
                                append(" ${Chars.bullet} ")
                                append(status)
                            }
                        )
                    },
                )
                BriefInfo(
                    modifier = Modifier
                        .padding(vertical = 8.dp, horizontal = 16.dp)
                        .fillMaxWidth(),
                    voteAverage = voteAverage,
                    voteCount = voteCount,
                    isAdult = isAdult,
                    runtime = runtime,
                )
                Row(
                    modifier = Modifier
                        .align(alignment = Alignment.End)
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(space = 16.dp)
                ) {
                    OutlinedButton(onClick = onCast) {
                        Text(text = stringResource(id = R.string.cast))
                    }
                    OutlinedButton(onClick = onCrew) {
                        Text(text = stringResource(id = R.string.crew))
                    }
                }
                Divider(modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp))
                About(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp, horizontal = 16.dp),
                    text = overview,
                    onMore = onMore,
                )
            }
        }
    }
}

@Composable
private fun About(
    modifier: Modifier,
    text: String,
    onMore: () -> Unit,
) {
    Section(
        modifier = Modifier
            .clickable(onClick = onMore)
            .then(other = modifier),
        title = {
            SectionTitle(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(id = R.string.about_this_film),
                trailing = {
                    Icon(
                        imageVector = Icons.Rounded.ArrowForward,
                        contentDescription = stringResource(id = R.string.more),
                        tint = MaterialTheme.colorScheme.primary,
                    )
                },
                horizontalArrangement = Arrangement.SpaceBetween,
            )
        },
        verticalArrangement = Arrangement.spacedBy(space = 16.dp),
        textStyles = SectionDefaults.smallTextStyles(
            contentTextStyle = MaterialTheme.typography.bodyMedium,
        ),
    ) {
        Text(
            text = text,
            maxLines = 4,
            overflow = TextOverflow.Ellipsis,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
private fun BriefInfo(
    modifier: Modifier,
    voteAverage: Float,
    voteCount: Int,
    isAdult: Boolean,
    runtime: Int,
    tint: Color = MaterialTheme.colorScheme.onSurfaceVariant,
) {
    CompositionLocalProvider(LocalContentColor provides tint) {
        Row(
            modifier = modifier
                .then(other = Modifier.height(intrinsicSize = IntrinsicSize.Min)),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Section(
                modifier = Modifier.weight(weight = 1f),
                title = {
                    SectionTitle(
                        text = voteAverage.format(),
                        trailing = {
                            Icon(
                                imageVector = Icons.Rounded.Star,
                                contentDescription = null,
                            )
                        }
                    )
                },
                horizontalAlignment = Alignment.CenterHorizontally,
                textStyles = SectionDefaults.smallTextStyles(),
            ) {
                Text(text = stringResource(id = R.string.reviews, voteCount))
            }
            Divider(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(width = 1.dp)
                    .padding(vertical = 8.dp)
            )
            Section(
                modifier = Modifier.weight(weight = 1f),
                title = {
                    Text(
                        modifier = Modifier
                            .height(height = 32.dp)
                            .padding(vertical = 6.dp)
                            .border(
                                width = 1.dp,
                                color = LocalContentColor.current,
                                shape = ShapeDefaults.ExtraSmall,
                            )
                            .padding(horizontal = 4.dp),
                        text = if (isAdult) "R" else "G"
                    )
                },
                horizontalAlignment = Alignment.CenterHorizontally,
                textStyles = SectionDefaults.smallTextStyles(
                    titleTextStyle = Typography.titleSmall,
                ),
            ) {
                Text(text = stringResource(id = R.string.audience))
            }
            Divider(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(width = 1.dp)
                    .padding(vertical = 8.dp)
            )
            Section(
                modifier = Modifier.weight(weight = 1f),
                title = {
                    SectionTitle(
                        text = stringResource(
                            id = R.string.format_duration,
                            TimeUtils.hours(runtime),
                            TimeUtils.minutes(runtime),
                        ),
                    )
                },
                horizontalAlignment = Alignment.CenterHorizontally,
                textStyles = SectionDefaults.smallTextStyles(),
            ) {
                Text(text = stringResource(id = R.string.duration))
            }
        }
    }
}

@Composable
private fun Headline(
    modifier: Modifier,
    poster: @Composable RowScope.(modifier: Modifier) -> Unit,
    title: @Composable ColumnScope.() -> Unit,
    info: @Composable ColumnScope.() -> Unit,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(space = 16.dp),
    ) {
        poster(
            modifier = Modifier
                .weight(weight = 1f)
                .aspectRatio(ratio = .75f),
        )
        Column(
            modifier = Modifier.weight(weight = 2f),
        ) {
            ProvideTextStyle(value = Typography.headlineSmall) {
                title()
            }
            ProvideTextStyle(value = Typography.bodyMedium) {
                CompositionLocalProvider(
                    LocalContentColor provides MaterialTheme.colorScheme.onSurfaceVariant
                ) {
                    info()
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HeadlinePreview() {
    MoviesPotTheme {
        Headline(
            modifier = Modifier.padding(all = 16.dp),
            poster = { modifier ->
                Surface(modifier = modifier, color = Color.Red) {}
            },
            title = {
                Text(text = "Title (2022)")
            },
            info = {
                Text(text = "12 Mar, 2022 ${Chars.bullet} Released")
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun BriefInfoPreview() {
    MoviesPotTheme {
        BriefInfo(
            modifier = Modifier.padding(all = 16.dp),
            voteAverage = 7.25f,
            voteCount = 567,
            isAdult = true,
            runtime = 127,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun AboutPreview() {
    MoviesPotTheme {
        About(
            modifier = Modifier,
            text = LoremIpsum,
            onMore = {},
        )
    }
}