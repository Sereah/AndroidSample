package com.lunacattus.androidsample.animation

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.tween
import androidx.compose.animation.splineBasedDecay
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.util.VelocityTracker
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@Preview
@Composable
fun AnimatableExample() {

    val scope = rememberCoroutineScope()
    val offsetAnimate = remember {
        Animatable(initialValue = Offset(0f, 0f), Offset.VectorConverter)
    }
    val scaleAnimate = remember { Animatable(initialValue = 1.0f) }

    val offsetState by offsetAnimate.asState()

    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        Image(
            imageVector = Icons.Filled.Favorite,
            colorFilter = ColorFilter.tint(Color(0x99E91E63)),
            contentDescription = "",
            modifier = Modifier
                .size(100.dp * scaleAnimate.value)
                .offset(offsetState.x.dp, offsetState.y.dp)
                .pointerInput(Unit) {
                    val velocityTracker = VelocityTracker()
                    detectDragGestures(
                        onDragStart = {
                            velocityTracker.resetTracking()
                            scope.launch {
                                offsetAnimate.stop()
                                scaleAnimate.animateTo(2.0f, tween(1000))
                            }
                        },
                        onDrag = { change, dragAmount ->
                            scope.launch {
                                offsetAnimate.snapTo(offsetAnimate.value + (dragAmount / density))
                            }
                            velocityTracker.addPosition(change.uptimeMillis, change.position)
                        },
                        onDragEnd = {
                            val velocity = velocityTracker.calculateVelocity().let {
                                Offset(-it.x, -it.y) / density
                            }
                            scope.launch {
                                offsetAnimate.animateDecay(
                                    velocity,
                                    splineBasedDecay(this@pointerInput)
                                )
                                scaleAnimate.animateTo(1.0f, tween(1000))
                            }
                        }
                    )
                }
        )
    }
}
