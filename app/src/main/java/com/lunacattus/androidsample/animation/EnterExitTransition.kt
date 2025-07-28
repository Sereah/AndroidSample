package com.lunacattus.androidsample.animation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandIn
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.lunacattus.androidsample.R

@Composable
@Preview
fun EnterExitTransition() {

    var visible by remember { mutableStateOf(true) }
    val listState = rememberLazyListState()

    LaunchedEffect(listState) {
        snapshotFlow { listState.firstVisibleItemIndex }
            .collect {
                visible = it >= 1
            }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(20.dp)
    ) {
        LazyColumn(
            state = listState,
            modifier = Modifier
                .width(400.dp)
                .align(Alignment.Center),
            contentPadding = PaddingValues(vertical = 10.dp)
        ) {
            items(8) {
                AsyncImage(
                    model = "https://bing.biturl.top/?resolution=1920&format=image&index=$it",
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    alpha = 0.6f,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .padding(10.dp)
                        .clip(RoundedCornerShape(10.dp))
                )
            }
        }
        AnimatedVisibility(
            visible = visible,
            enter = fadeIn(tween(1000)),
            exit = fadeOut(tween(1000)),
            modifier = Modifier
                .size(100.dp)
                .align(Alignment.BottomEnd)
        ) {
            Image(
                imageVector = Icons.Filled.KeyboardArrowUp,
                contentDescription = null,
                colorFilter = ColorFilter.tint(Color.White),
            )
        }
    }
}

@Composable
@Preview
fun ExpandSample() {

    var visible by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Gray),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Image(
            imageVector = Icons.Filled.PlayArrow,
            contentDescription = null,
            modifier = Modifier
                .size(50.dp)
                .pointerInput(Unit) {
                    detectTapGestures {
                        visible = !visible
                    }
                }
        )
        Spacer(modifier = Modifier.height(20.dp))
        AnimatedVisibility(
            visible = visible,
            enter = expandIn(
                animationSpec = spring(dampingRatio = 1f),
                expandFrom = Alignment.Center,
                clip = false
            ),
            exit = shrinkOut(
                animationSpec = spring(dampingRatio = 1f),
                shrinkTowards = Alignment.Center,
                clip = true
            )
        ) {
            Image(
                painter = painterResource(R.drawable.android),
                contentDescription = null,
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(20.dp))
            )
        }
    }
}