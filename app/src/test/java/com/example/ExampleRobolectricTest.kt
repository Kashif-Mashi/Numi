package com.example

import android.content.Context
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.doubleClick
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToNode
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeDown
import androidx.compose.ui.test.swipeRight
import androidx.test.core.app.ApplicationProvider
import com.example.data.preferences.NumiPreferencesRepository
import com.example.data.preferences.numiDataStore
import com.example.domain.models.AnimationIntensity
import com.example.domain.models.TextDepthStyle
import com.example.domain.models.TextFontOverride
import com.example.domain.models.TextShadowStyle
import com.example.domain.models.ThemeId
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun `app name matches NUMI`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        assertEquals("NUMI", context.getString(R.string.app_name))
    }

    @Test
    fun `theme animation and text customization preferences persist via DataStore`() = runTest {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val repo = NumiPreferencesRepository(context.numiDataStore)

        repo.setTheme(ThemeId.CYBER_NEON)
        repo.setAnimationIntensity(AnimationIntensity.ENERGETIC)
        repo.setTextCustomization(
            fontOverride = TextFontOverride.FREDOKA,
            depthStyle = TextDepthStyle.DEEP,
            shadowStyle = TextShadowStyle.GLOW,
            numberScaleFactor = 1.12f
        )
        val updated = repo.preferencesFlow.first()
        assertEquals(ThemeId.CYBER_NEON, updated.themeId)
        assertEquals(AnimationIntensity.ENERGETIC, updated.animationIntensity)
        assertEquals(TextFontOverride.FREDOKA, updated.textFontOverride)
        assertEquals(TextDepthStyle.DEEP, updated.textDepthStyle)
        assertEquals(TextShadowStyle.GLOW, updated.textShadowStyle)
        assertEquals(1.12f, updated.numberScaleFactor, 0.001f)

        repo.resetPreferences()
        val reset = repo.preferencesFlow.first()
        assertEquals(ThemeId.CREAM_CLASSIC, reset.themeId)
        assertEquals(TextFontOverride.THEME_DEFAULT, reset.textFontOverride)
        assertEquals(TextDepthStyle.MEDIUM, reset.textDepthStyle)
    }

    @Test
    fun `calculator arithmetic 3D drag double tap spin and text studio customization work`() {
        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithTag("key_7").performClick()
        composeTestRule.onNodeWithTag("key_add").performClick()
        composeTestRule.onNodeWithTag("key_3").performClick()
        composeTestRule.onNodeWithTag("key_equals").performClick()
        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithTag("display_result").assertTextContains("10")

        // Repeated equals (+ 3)
        composeTestRule.onNodeWithTag("key_equals").performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithTag("display_result").assertTextContains("13")

        // Drag and double-tap the 3D display and verify calculator remains interactive
        composeTestRule.onNodeWithTag("calculator_display_3d").performTouchInput {
            swipeRight()
            swipeDown()
            doubleClick()
        }
        composeTestRule.waitForIdle()

        // Toggle scientific mode
        composeTestRule.onNodeWithTag("top_bar_scientific").performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithTag("key_sin").assertExists()

        // Navigate to Theme Studio, switch to Midnight, then customize Text & 3D Style
        composeTestRule.onNodeWithTag("top_bar_themes").performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithTag("themes_list")
            .performScrollToNode(hasTestTag("theme_card_midnight"))
        composeTestRule.onNodeWithTag("theme_card_midnight").performClick()
        composeTestRule.onNodeWithTag("themes_apply_button").performClick()

        composeTestRule.onNodeWithTag("studio_tab_text_style").performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithTag("font_option_fredoka").performClick()
        composeTestRule.onNodeWithTag("text_customization_panel")
            .performScrollToNode(hasTestTag("depth_option_deep"))
        composeTestRule.onNodeWithTag("depth_option_deep").performClick()
        composeTestRule.onNodeWithTag("text_customization_panel")
            .performScrollToNode(hasTestTag("shadow_option_glow"))
        composeTestRule.onNodeWithTag("shadow_option_glow").performClick()
        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithTag("themes_back_button").performClick()
        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithTag("display_result").assertTextContains("13")
    }
}
