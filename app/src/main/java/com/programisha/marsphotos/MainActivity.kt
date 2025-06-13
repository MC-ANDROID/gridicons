package com.programisha.marsphotos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.programisha.marsphotos.ui.theme.MarsPhotosTheme
import kotlin.random.Random


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MarsPhotosTheme {
                MarsPhotosScreen()
            }
        }
    }
}

@Composable
fun MarsPhotosScreen(modifier: Modifier = Modifier) {
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column {
            Text(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxWidth(),
                textAlign = TextAlign.Center,
                text = stringResource(R.string.main_screen_title),
                style = MaterialTheme.typography.titleMedium
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                items(32) { i ->
                    Card (
                        colors = CardDefaults.cardColors(
                            contentColor = MaterialTheme.colorScheme.surfaceVariant
                        ),
                        modifier = Modifier.size(100.dp)
                    ) {
                        GridItemPlaceholder()
                    }
                }
            }
        }
    }
}

@Composable
fun GridItemPlaceholder(modifier: Modifier = Modifier) {
    val randomColor = remember { 0xff000000 + Random.nextInt(0xffffff) }
    Box (modifier = Modifier.background(color = Color(randomColor))) {
        ImagePlaceholder(
            tint = Color((0xffffffff - randomColor) + 0xff000000),
            modifier = Modifier
                .fillMaxSize()
                .wrapContentSize()
        )
    }
}

@Composable
fun ImagePlaceholder(tint: Color, modifier: Modifier = Modifier) {
    val randomSpeed = remember { 333 + Random.nextInt(1333) }
    val infiniteTransition = rememberInfiniteTransition()
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(randomSpeed, easing = LinearEasing),
        )
    )
    Icon(
        tint = tint,
        modifier = modifier
            .rotate(rotation)
            .scale(1.61f),
        imageVector = Icons.Default.ThumbUp,
        contentDescription = "loading"
    )
}

@Preview(showBackground = true)
@Composable
private fun AppPreview() {
    MarsPhotosScreen()
}