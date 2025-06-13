package com.programisha.marsphotos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.yield
import kotlin.coroutines.CoroutineContext
import kotlin.random.Random

private const val GENERATE_MORE_AMOUNT = 10

class MarsViewModel(
    private val defaultDispatcher: CoroutineContext = Dispatchers.Default
): ViewModel(){

    private val _marsList = mutableListOf<MarsItem>()
    private val _uiState = MutableStateFlow<MarsUiState>(MarsUiState(_marsList.toList()))
    val marsListState: StateFlow<MarsUiState> = _uiState.asStateFlow()

    fun generateMore(){
        viewModelScope.launch(defaultDispatcher) {
            repeat(GENERATE_MORE_AMOUNT) {
                val randomColor = 0xff000000 + Random.nextInt(0xffffff)
                val item = MarsItem(
                    bgColor = randomColor,
                    fgColor = (0xffffffff - randomColor) + 0xff000000,
                    animationDuration = 1667
                )
                _marsList+=item
                yield()
            }
            _uiState.update { state ->
                state.copy(items = _marsList)
            }
        }
    }

}

data class MarsUiState(val items:List<MarsItem>)