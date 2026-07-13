package com.example.mvishowcase.feature.home.ui

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.example.mvishowcase.feature.home.presentation.HomeState
import com.example.mvishowcase.feature.home.presentation.HomeUiState
import com.example.mvishowcase.core.model.Country
import org.junit.Rule
import org.junit.Test

class HomeScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun when_loading_state_then_circular_progress_indicator_is_displayed() {
        val state = HomeState(uiState = HomeUiState.Loading)

        composeTestRule.setContent {
            HomeContent(state = state, onIntent = {})
        }

        composeTestRule.onNode(hasScrollAction().not().and(hasNoClickAction())).assertExists()
        // Better: Find by type if possible, but hasProgressBarRangeInfo is for CircularProgressIndicator
        composeTestRule.onNode(hasProgressBarRangeInfo(ProgressBarRangeInfo.Indeterminate)).assertIsDisplayed()
    }

    @Test
    fun when_success_state_then_country_list_is_displayed() {
        val countries = listOf(
            Country("1", "Poland", "flag", "Warsaw", 38000000),
            Country("2", "Germany", "flag", "Berlin", 83000000)
        )
        val state = HomeState(uiState = HomeUiState.Success, countries = countries)

        composeTestRule.setContent {
            HomeContent(state = state, onIntent = {})
        }

        composeTestRule.onNodeWithText("Poland").assertIsDisplayed()
        composeTestRule.onNodeWithText("Germany").assertIsDisplayed()
    }

    @Test
    fun when_error_state_then_error_message_is_displayed() {
        val errorMessage = "Something went wrong"
        val state = HomeState(uiState = HomeUiState.Error(errorMessage))

        composeTestRule.setContent {
            HomeContent(state = state, onIntent = {})
        }

        composeTestRule.onNodeWithText(errorMessage).assertIsDisplayed()
    }

    @Test
    fun when_search_query_is_not_empty_then_clear_button_is_displayed() {
        val state = HomeState(uiState = HomeUiState.Success, searchQuery = "Pol")

        composeTestRule.setContent {
            HomeContent(state = state, onIntent = {})
        }

        // We can't easily use stringResource in tests without a context, 
        // but composeTestRule provides access to resources if needed.
        // For now let's use the text from strings.xml
        composeTestRule.onNodeWithContentDescription("Clear").assertIsDisplayed()
    }
}
