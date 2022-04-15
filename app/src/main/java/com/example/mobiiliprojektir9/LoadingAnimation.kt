package com.example.mobiiliprojektir9

import androidx.compose.animation.Animatable
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.ui.layout.Row
import androidx.ui.temputils.delay

@Composable
fun LoadingAnimation(
    modifier: Modifier = Modifier,
    circleSize: Dp = 25.dp,
    circleColor: Color = MaterialTheme.colors.primary,
    spaceBetween: Dp = 10.dp,
    travelDistance: Dp = 20.dp,
) {
    val circles = listOf(
        remember { androidx.compose.animation.core.Animatable(initialValue = 0f) },
        remember { androidx.compose.animation.core.Animatable(initialValue = 0f) },
        remember { androidx.compose.animation.core.Animatable(initialValue = 0f) }
    )

    circles.forEachIndexed { index, animatable ->
        LaunchedEffect(key1 = animatable) {
            kotlinx.coroutines.delay(index * 100L)
            animatable.animateTo(
                targetValue = 1f,
                animationSpec = infiniteRepeatable(
                    animation = keyframes {
                        durationMillis = 1200
                        //keyframes
                        0.0f at 0 with LinearOutSlowInEasing     //start
                        12.0f at 300 with LinearOutSlowInEasing  //up
                        -5.0f at 600 with LinearOutSlowInEasing //down
                        3.0f at 700 with LinearOutSlowInEasing //bounce :)
                        0.0f at 1000 with LinearOutSlowInEasing //ease
                        0.0f at 1200 with LinearOutSlowInEasing //pause

                    },
                    repeatMode = RepeatMode.Restart
                )
            )
        }
    }

    val circleValues = circles.map { it.value }
    val distance = with(LocalDensity.current) { travelDistance.toPx() }
    val lastCircle = circleValues.size - 1

    androidx.compose.foundation.layout.Row(modifier = modifier) {
        circleValues.forEachIndexed { index, value ->

            Box(modifier = Modifier
                .size(circleSize)
                .graphicsLayer {
                    translationY = -value + distance
                }
                .background(
                    color = circleColor,
                    shape = CircleShape
                ))

            if (index != lastCircle) {
                Spacer(modifier = Modifier.width(spaceBetween))
            }
        }
    }

}