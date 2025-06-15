package com.programisha.marsphotos

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import kotlin.random.Random

private const val ICON_ID_MAX = 10
private const val GENERATE_MORE_AMOUNT = 100
private const val RPS_MAX = 766
private const val RPS_MIN = 4444


class MarsViewModel(
    private val defaultDispatcher: CoroutineContext = Dispatchers.Default
): ViewModel(){

    private val _uiState = MutableStateFlow<MarsUiState>(MarsUiState(emptyList()))
    val marsListState: StateFlow<MarsUiState> = _uiState.asStateFlow()

    fun generateMore(){
        viewModelScope.launch(defaultDispatcher) {
            val newItems = mutableListOf<MarsItem>()
            repeat(GENERATE_MORE_AMOUNT) {
                val randomRGB = Random.nextInt(0xffffff)
                val item = MarsItem(
                    bgColor = 0xff000000 + randomRGB,
                    fgColor = 0xff000000 + invertColor(randomRGB),
                    animationDuration = RPS_MAX + Random.nextInt(RPS_MIN - RPS_MAX),
                    iconId = generateRandomIconId()
                )
                newItems+=item
            }
            _uiState.update { state ->
                state.copy(items = buildList {
                    addAll(state.items)
                    addAll(newItems)
                })
            }
        }
    }

    private fun invertColor(color: Int): Int {
        val r = ((color shr 16) and 0xFF)
        val g = ((color shr 8) and 0xFF)
        val b = (color and 0xFF)
        return ((r+0xff/2)%0xff shl 16) or ((g+0xff/2)%0xff shl 8) or (b+0xff/2)%0xff
    }

    private fun generateRandomIconId() = Random.nextInt(ICON_ID_MAX)
    private fun generateNewRandomIconId(prevId: Int) :Int {
        var newId = prevId
        while (prevId == newId){ newId = generateRandomIconId() }
        return newId
    }

    fun changeIconId(pos: Int){
        viewModelScope.launch(defaultDispatcher) {
            _uiState.update { state ->
                var prev = state.items[pos].iconId
                val newItem = state.items[pos].copy(iconId = generateNewRandomIconId(prev))
                val itemsArray = state.items.toTypedArray()
                itemsArray[pos] = newItem
                state.copy(
                    items = itemsArray.toList()
                )
            }
        }
    }
}

@Immutable
data class MarsUiState(val items:List<MarsItem>)