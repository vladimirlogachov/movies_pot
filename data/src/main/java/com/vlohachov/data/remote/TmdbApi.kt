package com.vlohachov.data.remote

import com.vlohachov.data.remote.schema.genre.GenresSchema
import com.vlohachov.data.remote.schema.movie.MovieDetailsSchema
import com.vlohachov.data.remote.schema.movie.MoviesPaginatedSchema
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TmdbApi {

    @GET("/3/genre/movie/list")
    suspend fun getGenres(
        @Query("language") language: String?,
        @Query("api_key") apiKey: String = TmdbConfig.API_KEY,
    ): GenresSchema

    @GET("/3/movie/upcoming")
    suspend fun getUpcomingMovies(
        @Query("page") page: Int,
        @Query("language") language: String?,
        @Query("region") region: String?,
        @Query("api_key") apiKey: String = TmdbConfig.API_KEY,
    ): MoviesPaginatedSchema

    @GET("/3/movie/now_playing")
    suspend fun getNowPlayingMovies(
        @Query("page") page: Int,
        @Query("language") language: String?,
        @Query("region") region: String?,
        @Query("api_key") apiKey: String = TmdbConfig.API_KEY,
    ): MoviesPaginatedSchema

    @GET("/3/movie/popular")
    suspend fun getPopularMovies(
        @Query("page") page: Int,
        @Query("language") language: String?,
        @Query("region") region: String?,
        @Query("api_key") apiKey: String = TmdbConfig.API_KEY,
    ): MoviesPaginatedSchema

    @GET("/3/movie/top_rated")
    suspend fun getTopRatedMovies(
        @Query("page") page: Int,
        @Query("language") language: String?,
        @Query("region") region: String?,
        @Query("api_key") apiKey: String = TmdbConfig.API_KEY,
    ): MoviesPaginatedSchema

    @GET("/3/movie/{id}")
    suspend fun getMovieDetails(
        @Path("id") id: Long,
        @Query("language") language: String?,
        @Query("api_key") apiKey: String = TmdbConfig.API_KEY,
    ): MovieDetailsSchema
}