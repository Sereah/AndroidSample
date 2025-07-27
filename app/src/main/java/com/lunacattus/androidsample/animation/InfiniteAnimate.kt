package com.lunacattus.androidsample.animation

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Preview
@Composable
fun InfiniteAnimate() {

    val infiniteTransition = rememberInfiniteTransition()
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.3f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 300,
                easing = FastOutSlowInEasing
            ),
            repeatMode = RepeatMode.Reverse,
        )
    )

    val scaleReal = remember { Animatable(1f) }
    LaunchedEffect(Unit) {
        while (true) {
            scaleReal.animateTo(1.3f, tween(150, easing = FastOutLinearInEasing))
            scaleReal.animateTo(1f, tween(150, easing = LinearOutSlowInEasing))
            delay(100)
            scaleReal.animateTo(1.3f, tween(150, easing = FastOutLinearInEasing))
            scaleReal.animateTo(1f, tween(150, easing = LinearOutSlowInEasing))
            delay(600)
        }
    }


    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            imageVector = Icons.Filled.Favorite,
            contentDescription = null,
            colorFilter = ColorFilter.tint(Color(0xFFDF6F94)),
            modifier = Modifier
                .size(200.dp)
                .align(Alignment.Center)
                .scale(scaleReal.value)
        )
    }

}