package navigator

import com.example.mvishowcase.core.model.Country
import kotlinx.serialization.Serializable

sealed interface NavRoute {
    @Serializable
    data object Home : NavRoute

    @Serializable
    data class Details(val country: Country) : NavRoute
}
