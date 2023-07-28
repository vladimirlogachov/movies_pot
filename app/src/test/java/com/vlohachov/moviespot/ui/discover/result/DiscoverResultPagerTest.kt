package com.vlohachov.moviespot.ui.discover.result

import app.cash.turbine.test
import com.google.common.truth.Truth
import com.vlohachov.domain.usecase.DiscoverMoviesUseCase
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Test

class DiscoverResultPagerTest {

    private val useCase = mockk<DiscoverMoviesUseCase>()

    private val pager by lazy {
        every { useCase.resultFlow(param = any()) } returns flowOf()

        DiscoverResultPager(year = 2022, selectedGenres = intArrayOf(10), useCase = useCase)
    }

    @Test
    fun `pager data flow emits values`() = runTest {
        pager.pagingDataFlow.test {
            val actual = awaitItem()

            Truth.assertThat(actual).isNotNull()
        }
    }
}
