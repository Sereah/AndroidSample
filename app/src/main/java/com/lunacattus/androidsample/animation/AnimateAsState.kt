package com.lunacattus.androidsample.animation

import androidx.compose.animation.core.AnimationVector2D
import androidx.compose.animation.core.TwoWayConverter
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateValueAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Preview
@Composable
fun AnimateAsStateSample() {

    var enable by remember { mutableStateOf(false) }
    val alphaAnimate by animateFloatAsState(
        targetValue = if (enable) 1.0f else 0.1f,
        animationSpec = tween(2000)
    )
    val sizeAnimate by animateValueAsState(
        targetValue = if (enable) {
            CustomSize(240f, 240f)
        } else {
            CustomSize(120f, 120f)
        },
        typeConverter = TwoWayConverter(
            convertToVector = { AnimationVector2D(it.width, it.height) },
            convertFromVector = { CustomSize(it.v1, it.v2) }
        ),
        animationSpec = tween(2000)
    )

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            imageVector = Icons.Filled.Favorite,
            contentDescription = null,
            colorFilter = ColorFilter.tint(Color(0xFFE91E63)),
            modifier = Modifier
                .size(sizeAnimate.width.dp, sizeAnimate.height.dp)
                .align(Alignment.Center)
                .alpha(alphaAnimate)
                .pointerInput(Unit) {
                    detectTapGestures {
                        enable = !enable
                    }
                }
        )
    }
}

data class CustomSize(
    val width: Float,
    val height: Float
)