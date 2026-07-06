package com.example.mvishowcase.navigation

import androidx.compose.foundation.layout.add
import androidx.compose.runtime.mutableStateListOf
import com.example.mvishowcase.core.ui.navigator.Navigator
import navigator.NavRoute

class NavigatorImpl : Navigator {
    override val backStack = mutableStateListOf<Any>(NavRoute.Home)
    override fun navigateTo(route: Any) { backStack.add(route) }
    override fun goBack() { backStack.removeLastOrNull() }
}
