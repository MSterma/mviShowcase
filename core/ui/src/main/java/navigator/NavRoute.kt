package navigator

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

sealed interface NavRoute {
    @Serializable
    @SerialName("Login")
    data object Login : NavRoute

    @Serializable
    @SerialName("Register")
    data object Register : NavRoute

    @Serializable
    @SerialName("Home")
    data object Home : NavRoute

    @Serializable
    @SerialName("Details")
    data class Details(
        @SerialName("countryId")
        val countryId: String
    ) : NavRoute
}
