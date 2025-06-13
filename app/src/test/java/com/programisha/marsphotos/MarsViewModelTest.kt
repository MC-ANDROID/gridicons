package com.programisha.marsphotos

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

@ExperimentalCoroutinesApi
class MarsViewModelTest {

    private val unconfinedTestDispatcher = UnconfinedTestDispatcher()

    private val viewModel = MarsViewModel(defaultDispatcher = unconfinedTestDispatcher)

    @Test
    fun testInitialState(){
        val state = viewModel.marsListState.value
        assertTrue(state.items.isEmpty())
    }

    @Test
    fun testGenerateItems() = runTest(unconfinedTestDispatcher) {
        viewModel.generateMore()
        val state = viewModel.marsListState.value

        assertFalse(state.items.isEmpty())
    }

    @Test
    fun testGeneratedItemsSizeIncreasing() = runTest(unconfinedTestDispatcher) {

        viewModel.generateMore()
        val sizeBefore = viewModel.marsListState.value.items.size
        viewModel.generateMore()
        val sizeAfter = viewModel.marsListState.value.items.size

        assertTrue(sizeAfter > sizeBefore)
    }
}