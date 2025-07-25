package com.lunacattus.androidsample.animation

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.DecayAnimationSpec
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.exponentialDecay
import androidx.compose.animation.core.spring
import androidx.compose.animation.splineBasedDecay
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.util.VelocityTracker
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

@Preview
@Composable
fun AnimatableExample() {

    val scope = rememberCoroutineScope()

    val loveOffsetAnimate = remember {
        Animatable(initialValue = Offset(0f, 0f), Offset.VectorConverter)
    }
    val loveOffsetState by loveOffsetAnimate.asState()

    val boxOffsetAnimate = remember {
        Animatable(initialValue = Offset(300f, 300f), Offset.VectorConverter)
    }
    val boxOffsetState by boxOffsetAnimate.asState()

    val decaySpec: DecayAnimationSpec<Offset> = remember {
        exponentialDecay(frictionMultiplier = 2.1f, absVelocityThreshold = 0.1f)
    }

    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        Text(
            text = "方块红色表示动画正在运行，绿色表示停止状态。",
            fontSize = 22.sp,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(100.dp)
        )
        Box(
            modifier = Modifier
                .size(50.dp)
                .offset(boxOffsetState.x.dp, boxOffsetState.y.dp)
                .background(
                    if (boxOffsetAnimate.isRunning) {
                        Color(0xFFE91E63)
                    } else {
                        Color(0xFF4CAF50)
                    }
                )
                .pointerInput(Unit) {
                    detectTapGestures {
                        scope.launch {
                            if (boxOffsetState.x == 300f) {
                                boxOffsetAnimate.animateTo(
                                    targetValue = Offset(350f, 500f),
                                    animationSpec = spring(
                                        stiffness = 10f,
                                        dampingRatio = 1f,
                                        visibilityThreshold = Offset(1f, 1f)
                                    ),
                                    initialVelocity = Offset(5000f, 0f)
                                )
                            } else {
                                boxOffsetAnimate.animateTo(
                                    targetValue = Offset(300f, 300f),
                                    animationSpec = spring(
                                        stiffness = 10f,
                                        dampingRatio = 1f,
                                        visibilityThreshold = Offset(1f, 1f)
                                    ),
                                    initialVelocity = Offset(-1000f, 0f)
                                )
                            }
                        }
                    }
                }
        )
        Image(
            imageVector = Icons.Filled.Favorite,
            colorFilter = ColorFilter.tint(Color(0x99E91E63)),
            contentDescription = "",
            modifier = Modifier
                .size(150.dp)
                .offset(loveOffsetState.x.dp, loveOffsetState.y.dp)
                .pointerInput(Unit) {
                    val velocityTracker = VelocityTracker()
                    detectDragGestures(
                        onDragStart = {
                            velocityTracker.resetTracking()
                            scope.launch {
                                loveOffsetAnimate.stop()
                            }
                        },
                        onDrag = { change, dragAmount ->
                            scope.launch {
                                loveOffsetAnimate.snapTo(loveOffsetAnimate.value + (dragAmount / density))
                            }
                            velocityTracker.addPosition(change.uptimeMillis, change.position)
                        },
                        onDragEnd = {
                            val velocity = velocityTracker.calculateVelocity().let {
                                Offset(-it.x, -it.y) / density
                            }
                            scope.launch {
                                loveOffsetAnimate.animateDecay(
                                    velocity,
                                    splineBasedDecay(this@pointerInput)
                                )
                            }
                        }
                    )
                }
        )
    }
}
