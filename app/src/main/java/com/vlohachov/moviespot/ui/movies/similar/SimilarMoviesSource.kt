package com.vlohachov.moviespot.ui.movies.similar

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.vlohachov.domain.Result
import com.vlohachov.domain.model.PaginatedData
import com.vlohachov.domain.model.movie.Movie
import com.vlohachov.domain.usecase.movie.MovieRecommendationsUseCase
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

class SimilarMoviesSource(
    private val movieId: Long,
    private val useCase: MovieRecommendationsUseCase,
) : PagingSource<Int, Movie>() {

    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
        return state.anchorPosition?.let { position ->
            state.closestPageToPosition(anchorPosition = position)?.run {
                prevKey?.plus(1) ?: nextKey?.minus(1)
            }
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> =
        runCatching { loadPage(page = params.key ?: 1) }
            .fold(
                onSuccess = { result ->
                    LoadResult.Page(
                        data = result.data,
                        prevKey = result.prevKey(),
                        nextKey = result.nextKey(),
                    )
                },
                onFailure = { e -> LoadResult.Error(e) }
            )

    private fun PaginatedData<Movie>.prevKey(): Int? =
        if (page == 1) null else page.minus(1)

    private fun PaginatedData<Movie>.nextKey(): Int? =
        if (page >= totalPages) null else page.plus(1)

    private suspend fun loadPage(page: Int): PaginatedData<Movie> =
        useCase.resultFlow(param = MovieRecommendationsUseCase.Param(id = movieId, page = page))
            .filter { result -> result !is Result.Loading }
            .onEach { result -> if (result is Result.Error) throw result.exception }
            .map { result -> (result as Result.Success).value }
            .first()
}
