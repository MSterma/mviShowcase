package com.example.mvishowcase.core.ui.navigator

import androidx.compose.runtime.snapshots.SnapshotStateList

interface Navigator {
    val backStack: SnapshotStateList<Any>
    fun navigateTo(route: Any)
    fun resetTo(route: Any)
    fun goBack()
}
