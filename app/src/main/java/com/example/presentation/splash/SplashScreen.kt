package com.example.presentation.splash

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.LocalNumiTheme
import com.example.ui.typography.NumiFonts
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    onSplashComplete: () -> Unit,
    modifier: Modifier = Modifier
) {
    val theme = LocalNumiTheme.current
    val progress = remember { Animatable(0f) }
    val iconScale = remember { Animatable(0.85f) }

    val infiniteTransition = rememberInfiniteTransition(label = "auraGlow")
    val auraAlpha by infiniteTransition.animateFloat(
        initialValue = 0.35f,
        targetValue = 0.70f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "auraAlpha"
    )

    LaunchedEffect(Unit) {
        iconScale.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 400, easing = FastOutSlowInEasing)
        )
    }

    LaunchedEffect(Unit) {
        progress.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 1300, easing = FastOutSlowInEasing)
        )
        delay(150)
        onSplashComplete()
    }

    val bgGradient = Brush.verticalGradient(
        colors = listOf(
            theme.backgroundTop,
            theme.backgroundBottom
        )
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(bgGradient),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(32.dp)
        ) {
            // App Icon with subtle glow and squircle clipping
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(140.dp)
                    .scale(iconScale.value)
            ) {
                Box(
                    modifier = Modifier
                        .size(130.dp)
                        .clip(RoundedCornerShape(32.dp))
                        .background(
                            Brush.radialGradient(
                                colors = listOf(
                                    Color(0xFF00E5FF).copy(alpha = auraAlpha),
                                    Color(0xFF0052D4).copy(alpha = auraAlpha * 0.3f),
                                    Color.Transparent
                                )
                            )
                        )
                )

                Image(
                    painter = painterResource(id = R.drawable.app_icon),
                    contentDescription = "App Icon",
                    modifier = Modifier
                        .size(110.dp)
                        .clip(RoundedCornerShape(26.dp))
                        .shadow(
                            elevation = 12.dp,
                            shape = RoundedCornerShape(26.dp)
                        )
                        .border(
                            width = 1.dp,
                            color = Color(0x40FFFFFF),
                            shape = RoundedCornerShape(26.dp)
                        )
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Brand Title
            Text(
                text = "NUMI",
                style = MaterialTheme.typography.displayMedium.copy(
                    fontFamily = NumiFonts.displayFamily(theme.fontStyle),
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 4.sp
                ),
                color = theme.primaryText
            )

            Spacer(modifier = Modifier.height(36.dp))

            // Progress Bar Section
            val currentPercent = (progress.value * 100).toInt().coerceIn(0, 100)

            Box(
                modifier = Modifier
                    .width(200.dp)
                    .height(6.dp)
                    .clip(CircleShape)
                    .background(Color(0x26FFFFFF))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(fraction = progress.value.coerceIn(0.01f, 1f))
                        .height(6.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.horizontalGradient(
                                colors = listOf(
                                    Color(0xFF00E5FF),
                                    theme.accentColor
                                )
                            )
                        )
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Progress percentage
            Text(
                text = if (currentPercent < 100) "$currentPercent%" else "Ready",
                style = MaterialTheme.typography.labelSmall.copy(
                    fontFamily = NumiFonts.PlusJakartaSansFamily,
                    fontWeight = FontWeight.Medium,
                    letterSpacing = 1.sp
                ),
                color = theme.secondaryText
            )
        }
    }
}
