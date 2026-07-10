package com.example.mvishowcase.navigation

import androidx.compose.runtime.mutableStateListOf
import com.example.mvishowcase.core.ui.navigator.Navigator

class NavigatorImpl : Navigator {
    override val backStack = mutableStateListOf<Any>()
    override fun navigateTo(route: Any) { backStack.add(route) }
    override fun resetTo(route: Any) {
        backStack.clear()
        backStack.add(route)
    }
    override fun goBack() { backStack.removeLastOrNull() }
}
