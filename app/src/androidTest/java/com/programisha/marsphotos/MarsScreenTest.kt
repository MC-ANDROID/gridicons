package com.programisha.marsphotos

import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.assertContentDescriptionEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onChild
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onLast
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performScrollToNode
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeUp
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.programisha.marsphotos.ui.theme.MarsPhotosTheme
import org.junit.Assert.assertNotEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MarsScreenTest {

    @get:Rule val composeTestRule = createComposeRule()

    @Before
    fun setup() {
        composeTestRule.setContent {
            MarsPhotosTheme {
                MarsPhotosScreen()
            }
        }
    }

    @Test
    fun testGridDisplayed(){
        composeTestRule
            .onNodeWithContentDescription(label = "grid of icons")
            .assertIsDisplayed()
    }

    @Test
    fun testGridAlwaysScrollsDown(){
        val lastNodeDescription = composeTestRule
            .onNodeWithContentDescription(label = "grid of icons")
            .performTouchInput { swipeUp(durationMillis = 500) }
            .performTouchInput { swipeUp(durationMillis = 500) }
            .performTouchInput { swipeUp(durationMillis = 500) }
            .onChildren()
            .onLast()
            .fetchSemanticsNode().config[SemanticsProperties.ContentDescription]
        val lastNode2Description = composeTestRule
            .onNodeWithContentDescription(label = "grid of icons")
            .performTouchInput { swipeUp(durationMillis = 500) }
            .performTouchInput { swipeUp(durationMillis = 500) }
            .performTouchInput { swipeUp(durationMillis = 500) }
            .onChildren()
            .onLast()
            .fetchSemanticsNode().config[SemanticsProperties.ContentDescription]

        println("$lastNodeDescription vs $lastNode2Description")
        assertNotEquals(lastNodeDescription.first(), lastNode2Description.first())

    }

}