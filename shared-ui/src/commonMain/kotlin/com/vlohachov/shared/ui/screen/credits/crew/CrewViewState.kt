package com.vlohachov.shared.ui.screen.credits.crew

import com.vlohachov.shared.domain.model.movie.credit.CrewMember
import com.vlohachov.shared.ui.state.ViewState

internal data class CrewViewState(
    val viewState: ViewState<List<CrewMember>> = ViewState.Loading,
    val error: Throwable? = null,
)
