package com.example.presentation.about

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.outlined.Calculate
import androidx.compose.material.icons.outlined.Palette
import androidx.compose.material.icons.outlined.TouchApp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.LocalNumiTheme
import com.example.ui.typography.NumiFonts

@Composable
fun AboutScreen(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val theme = LocalNumiTheme.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(listOf(theme.backgroundTop, theme.backgroundBottom))
            )
            .windowInsetsPadding(WindowInsets.safeDrawing)
    ) {
        // Top Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .defaultMinSize(minWidth = 44.dp, minHeight = 44.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(theme.displaySurface)
                    .border(1.dp, theme.displayBorder, RoundedCornerShape(14.dp))
                    .clickable(role = Role.Button, onClick = onNavigateBack),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                    contentDescription = "Back",
                    tint = theme.primaryText
                )
            }
            Spacer(modifier = Modifier.width(14.dp))
            Text(
                text = "About NUMI",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = theme.primaryText
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // App Icon
            Image(
                painter = painterResource(id = R.drawable.app_icon),
                contentDescription = "NUMI App Icon",
                modifier = Modifier
                    .size(96.dp)
                    .clip(RoundedCornerShape(22.dp))
                    .shadow(12.dp, RoundedCornerShape(22.dp))
                    .border(1.dp, Color(0x33FFFFFF), RoundedCornerShape(22.dp))
            )

            Spacer(modifier = Modifier.height(16.dp))

            // App Name
            Text(
                text = "NUMI",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontFamily = NumiFonts.displayFamily(theme.fontStyle),
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 2.sp
                ),
                color = theme.primaryText
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Version 1.0",
                style = MaterialTheme.typography.bodyMedium,
                color = theme.secondaryText
            )

            Spacer(modifier = Modifier.height(28.dp))

            // Description card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp))
                    .background(theme.displaySurface)
                    .border(1.dp, theme.displayBorder, RoundedCornerShape(18.dp))
                    .padding(18.dp)
            ) {
                Text(
                    text = "NUMI is a clean and simple scientific calculator designed for clarity, precision, and focus. Switch seamlessly between standard and scientific calculation modes, and personalize your experience with refined color themes.",
                    style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 22.sp),
                    color = theme.primaryText
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Feature Highlights
            AboutFeatureItem(
                icon = Icons.Outlined.Calculate,
                title = "Scientific Calculator",
                description = "Trigonometry (sin, cos, tan), inverse functions, logarithms, powers, square root, parentheses, and radian/degree modes.",
                theme = theme
            )

            Spacer(modifier = Modifier.height(10.dp))

            AboutFeatureItem(
                icon = Icons.Outlined.Palette,
                title = "Clean Color Themes",
                description = "Dark, light, charcoal, ocean, and emerald palettes designed for optimal contrast and eye comfort.",
                theme = theme
            )

            Spacer(modifier = Modifier.height(10.dp))

            AboutFeatureItem(
                icon = Icons.Outlined.TouchApp,
                title = "Tactile Response",
                description = "Precise haptic feedback and gentle audio cues crafted for responsive, accurate input.",
                theme = theme
            )

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun AboutFeatureItem(
    icon: ImageVector,
    title: String,
    description: String,
    theme: com.example.domain.models.NumiThemeSpec
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(theme.displaySurface)
            .border(1.dp, theme.displayBorder, RoundedCornerShape(16.dp))
            .padding(14.dp)
    ) {
        Row(verticalAlignment = Alignment.Top) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(theme.accentColor.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = theme.accentColor,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
                    color = theme.primaryText
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall.copy(lineHeight = 18.sp),
                    color = theme.secondaryText
                )
            }
        }
    }
}
