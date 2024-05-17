# Movies Pot [![Validation](https://github.com/vladimirlogachov/MoviesPot/actions/workflows/validation.yml/badge.svg?branch=master)](https://github.com/vladimirlogachov/MoviesPot/actions/workflows/validation.yml)

This Kotlin and Compose multiplatform project allows users to explore the world of movies with
ease. It utilizes the TMDB API to fetch movie data and presents it in a visually appealing manner.
Jetpack Navigation ensures smooth navigation between screens, while the Paging library enables
efficient data loading for a seamless user experience. The user interface adheres to Material3
design
guidelines, providing a modern and intuitive way to discover and engage with movie content.

## Target audience

- Developers interested in learning more about Kotlin and Compose multiplatform.
- Designers and UI enthusiasts seeking inspiration for Material3-based interfaces.

## Demo

![App Journey](https://github.com/vladimirlogachov/MoviesPot/blob/master/media/app_journey.gif?raw=true)

## Screenshots

#### App icon

| Default                                                                                                            | Themed (light)                                                                                                                  | Themed (dark)                                                                                                                 |
|:-------------------------------------------------------------------------------------------------------------------|:--------------------------------------------------------------------------------------------------------------------------------|:------------------------------------------------------------------------------------------------------------------------------|
| ![Default App Icon](https://github.com/vladimirlogachov/MoviesPot/blob/master/media/default_app_icon.jpg?raw=true) | ![Themed App Icon (light)](https://github.com/vladimirlogachov/MoviesPot/blob/master/media/dynamic_app_icon_light.jpg?raw=true) | ![Themed App Icon (dark)](https://github.com/vladimirlogachov/MoviesPot/blob/master/media/dynamic_app_icon_dark.jpg?raw=true) |

#### App Theme

| Default (light)                                                                                                                  | Default (dark)                                                                                                                 |
|:---------------------------------------------------------------------------------------------------------------------------------|:-------------------------------------------------------------------------------------------------------------------------------|
| ![Themed App Icon (light)](https://github.com/vladimirlogachov/MoviesPot/blob/master/media/default_app_theme_light.jpg?raw=true) | ![Themed App Icon (dark)](https://github.com/vladimirlogachov/MoviesPot/blob/master/media/default_app_theme_dark.jpg?raw=true) |

| Dynamic (light)                                                                                                                  | Dynamic (dark)                                                                                                                 |
|:---------------------------------------------------------------------------------------------------------------------------------|:-------------------------------------------------------------------------------------------------------------------------------|
| ![Themed App Icon (light)](https://github.com/vladimirlogachov/MoviesPot/blob/master/media/dynamic_app_theme_light.jpg?raw=true) | ![Themed App Icon (dark)](https://github.com/vladimirlogachov/MoviesPot/blob/master/media/dynamic_app_theme_dark.jpg?raw=true) |

## Features

- UI implemented using [Jetpack Compose](https://developer.android.com/jetpack/compose)
  and [Material 3](https://m3.material.io/) design guidelines
- Supports [Dynamic Colors](https://m3.material.io/styles/color/dynamic-color/overview) schema
- Adaptive
  launcher [Themed Icon](https://developer.android.com/develop/ui/views/launch/icon_design_adaptive) (
  for Android 13+)
- Navigation based on [Compose Destinations](https://composedestinations.rafaelcosta.xyz/) library,
  thanks to its author [Rafael Costa](https://github.com/raamcosta)
- Uses [TMDB](https://www.themoviedb.org) open API
- Infinite lists (pagination),
  using [Jetpack Paging](https://developer.android.com/topic/libraries/architecture/paging/v3-overview)
- Data flow implemented
  using [Kotlin Coroutines Flow](https://kotlinlang.org/api/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines.flow/-flow/)

## Tools

- [Kotlin for Android](https://kotlinlang.org/docs/android-overview.html)
- [Kotlin Coroutines](https://github.com/Kotlin/kotlinx.coroutines)
- [Jetpack Compose](https://developer.android.com/jetpack/compose)
- [Jetpack Paging](https://developer.android.com/topic/libraries/architecture/paging/v3-overview)
  for Compose
- [Accompaninst](https://google.github.io/accompanist/)
- [Compose Destinations](https://composedestinations.rafaelcosta.xyz/)
- [Retrofit](https://square.github.io/retrofit/)
- [OkHttp](https://square.github.io/okhttp/)
- [Coil](https://coil-kt.github.io/coil/)
- [Koin](https://insert-koin.io/)
- [JUnit](https://junit.org/junit4/)
- [Mockk](https://mockk.io/)
- [Truth](https://truth.dev/)
- [Turbine](https://github.com/google/turbine)
- [Detekt](https://detekt.dev/)
