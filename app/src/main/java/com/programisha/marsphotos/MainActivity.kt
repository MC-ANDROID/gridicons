package com.programisha.marsphotos

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.programisha.marsphotos.ui.theme.MarsPhotosTheme
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map


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

private const val MIN_ITEMS_BELOW_BOTTOM_TO_LOAD_MORE = 100

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MarsPhotosScreen(viewModel: MarsViewModel = viewModel(), modifier: Modifier = Modifier) {
    val marsState by viewModel.marsListState.collectAsStateWithLifecycle()
    var itemsNumber by remember { mutableIntStateOf(0) }
    LaunchedEffect(true) {
        viewModel.generateMore()
    }
    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text("items #${itemsNumber}")
                }
            )
        },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            val listState = rememberLazyGridState()
            LaunchedEffect(listState) {
                snapshotFlow { listState.firstVisibleItemIndex + listState.layoutInfo.visibleItemsInfo.size}
                    .distinctUntilChanged()
                    .collect {
                        itemsNumber = it
                    }

            }
            LaunchedEffect(listState) {
                snapshotFlow { listState.firstVisibleItemIndex }
                    .map { index ->
                        val loadMoreLimit = (listState.layoutInfo.totalItemsCount - listState.layoutInfo.visibleItemsInfo.size) -
                                             MIN_ITEMS_BELOW_BOTTOM_TO_LOAD_MORE
                        index >= loadMoreLimit
                    }.filter { it }
                    .collect {
                        viewModel.generateMore()
                    }
            }
            LazyVerticalGrid(
                state = listState,
                columns = GridCells.Fixed(4),
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.semantics{ contentDescription = "grid of icons" }
            ) {
                itemsIndexed(marsState.items, key = {i, e -> "$i${e.iconId}" }) { i,item ->
                    Card (
                        colors = CardDefaults.cardColors(
                            contentColor = MaterialTheme.colorScheme.surfaceVariant
                        ),
                        modifier = Modifier
                            .size(50.dp)
                            .clickable { viewModel.changeIconId(i) }
                            .semantics {
                                contentDescription = "grid item $i"
                            }
                    ) {
                        GridItemPlaceholder(item)
                    }
                }
            }
        }
    }
}

@Composable
fun GridItemPlaceholder(marsItem: MarsItem, modifier: Modifier = Modifier) {
    Box (modifier = Modifier.background(color = Color(marsItem.bgColor))) {
        ImagePlaceholder(
            iconId = marsItem.iconId,
            animationDuration = marsItem.animationDuration,
            tint = Color(marsItem.fgColor),
            modifier = Modifier
                .fillMaxSize()
                .wrapContentSize()
        )
    }
}

@Composable
fun ImagePlaceholder(iconId:Int,animationDuration: Int, tint: Color, modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition()
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(animationDuration, easing = LinearEasing),
        )
    )
    Icon(
        tint = tint,
        modifier = modifier
            .graphicsLayer {
                rotationZ = rotation
            }
            .scale(1.61f),
        imageVector = when(iconId){
            1 -> Icons.Default.Settings
            2 -> Icons.Default.Build
            3 -> Icons.Default.AccountCircle
            4 -> Icons.Default.FavoriteBorder
            5 -> Icons.Default.Star
            6 -> Icons.Default.AddCircle
            7 -> Icons.Default.Share
            8 -> Icons.Default.Warning
            9 -> Icons.Default.Refresh
            0 -> Icons.Default.Clear
            else -> Icons.Default.ThumbUp
        },
        contentDescription = "loading"
    )
}


@Composable
fun ImagePlaceholderNoAnimation(iconId:Int,animationDuration: Int, tint: Color, modifier: Modifier = Modifier) {
    Icon(
        tint = tint,
        modifier = modifier
            .scale(1.61f),
        imageVector = when(iconId){
            1 -> Icons.Default.Settings
            2 -> Icons.Default.Build
            3 -> Icons.Default.AccountCircle
            4 -> Icons.Default.FavoriteBorder
            5 -> Icons.Default.Star
            6 -> Icons.Default.AddCircle
            7 -> Icons.Default.Share
            8 -> Icons.Default.Warning
            9 -> Icons.Default.Refresh
            0 -> Icons.Default.Clear
            else -> Icons.Default.ThumbUp
        },
        contentDescription = "loading"
    )
}

@Preview(showBackground = true)
@Composable
private fun AppPreview() {
    MarsPhotosScreen()
}