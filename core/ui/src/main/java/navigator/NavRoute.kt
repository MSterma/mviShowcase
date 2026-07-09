package navigator

import kotlinx.serialization.Serializable

sealed interface NavRoute {
    @Serializable
    data object Login : NavRoute

    @Serializable
    data object Register : NavRoute

    @Serializable
    data object Home : NavRoute

    @Serializable
    data class Details(val countryId: String) : NavRoute
}
