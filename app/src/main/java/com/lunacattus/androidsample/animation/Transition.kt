package com.lunacattus.androidsample.animation

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.SeekableTransitionState
import androidx.compose.animation.core.animateIntOffset
import androidx.compose.animation.core.rememberTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Slider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

enum class Position {
    Left, Middle, Right
}

@Preview
@Composable
fun UpdateTransitionSample() {

    var boxState by remember { mutableStateOf(Position.Left) }
    val transition = updateTransition(
        targetState = boxState
    )
    val offsetAnimate by transition.animateIntOffset(
        transitionSpec = {
            when {
                Position.Left isTransitioningTo Position.Middle -> tween(500)
                Position.Middle isTransitioningTo Position.Right -> tween(2000)
                else -> spring(dampingRatio = 0.3f)
            }
        },
        targetValueByState = { position ->
            when (position) {
                Position.Left -> IntOffset(0, 0)
                Position.Middle -> IntOffset(175, 0)
                Position.Right -> IntOffset(350, 0)
            }
        }
    )

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .width(400.dp)
                .height(50.dp)
                .background(Color(0x66A2A2A2))
        ) {
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .offset(offsetAnimate.x.dp, offsetAnimate.y.dp)
                    .background(Color(0xFF4CAF50))
                    .pointerInput(Unit) {
                        detectTapGestures {
                            boxState = when (boxState) {
                                Position.Left -> Position.Middle
                                Position.Middle -> Position.Right
                                Position.Right -> Position.Left
                            }
                        }
                    }
            )
        }
    }
}

@Preview
@Composable
fun MutableTransitionStateSample() {
    val boxTransitionState = remember {
        MutableTransitionState(Position.Left).apply {
            targetState = Position.Middle
        }
    }
    val rememberTransition = rememberTransition(
        transitionState = boxTransitionState
    )
    val offsetAnimate by rememberTransition.animateIntOffset(
        transitionSpec = {
            when {
                Position.Left isTransitioningTo Position.Middle -> tween(500)
                Position.Middle isTransitioningTo Position.Right -> tween(2000)
                else -> spring(dampingRatio = 0.3f)
            }
        },
        targetValueByState = { position ->
            when (position) {
                Position.Left -> IntOffset(0, 0)
                Position.Middle -> IntOffset(175, 0)
                Position.Right -> IntOffset(350, 0)
            }
        }
    )

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .width(400.dp)
                .height(50.dp)
                .background(Color(0x66A2A2A2))
        ) {
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .offset(offsetAnimate.x.dp, offsetAnimate.y.dp)
                    .background(Color(0xFF4CAF50))
                    .pointerInput(Unit) {
                        detectTapGestures {
                            boxTransitionState.targetState =
                                when (boxTransitionState.currentState) {
                                    Position.Left -> Position.Middle
                                    Position.Middle -> Position.Right
                                    Position.Right -> Position.Left
                                }
                        }
                    }
            )
        }
    }
}

@Preview
@Composable
fun SeekableTransitionStateSample() {
    val boxTransitionState = remember {
        SeekableTransitionState(Position.Left)
    }
    val rememberTransition = rememberTransition(
        transitionState = boxTransitionState
    )
    val offsetAnimate by rememberTransition.animateIntOffset(
        transitionSpec = {
            tween(2000, easing = LinearEasing)
        },
        targetValueByState = { position ->
            when (position) {
                Position.Left -> IntOffset(0, 0)
                Position.Middle -> IntOffset(150, 0)
                Position.Right -> IntOffset(350, 0)
            }
        }
    )

    var sliderState by remember { mutableFloatStateOf(0f) }
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .width(400.dp)
                .height(50.dp)
                .background(Color(0x66A2A2A2))
        ) {
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .offset(offsetAnimate.x.dp, offsetAnimate.y.dp)
                    .background(Color(0xFF4CAF50))
                    .pointerInput(Unit) {
                        detectTapGestures {
                            val target = when (boxTransitionState.currentState) {
                                Position.Left -> Position.Right
                                Position.Right -> Position.Left
                                else -> Position.Right
                            }
                            scope.launch {
                                boxTransitionState.animateTo(
                                    target,
                                )
                            }
                        }
                    }
            )
        }
        Slider(
            value = sliderState,
            modifier = Modifier.width(400.dp),
            onValueChange = { value ->
                sliderState = value
                scope.launch {
                    val target = if (boxTransitionState.currentState == Position.Left) {
                        Position.Right
                    } else {
                        Position.Left
                    }
                    boxTransitionState.seekTo(sliderState, target)
                }
            })
    }
}