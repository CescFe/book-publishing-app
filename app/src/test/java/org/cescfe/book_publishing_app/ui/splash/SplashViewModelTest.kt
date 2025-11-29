package org.cescfe.book_publishing_app.ui.splash

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SplashViewModelTest {

    private lateinit var viewModel: SplashViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        viewModel = SplashViewModel()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state should be loading`() {
        val initialState = viewModel.uiState.value
        assertTrue(initialState.isLoading)
        assertFalse(initialState.isReady)
    }

    @Test
    fun `after delay should be ready`() = runTest {
        advanceTimeBy(2000L)
        advanceUntilIdle()

        val finalState = viewModel.uiState.value
        assertFalse(finalState.isLoading)
        assertTrue(finalState.isReady)
    }

    @Test
    fun `before delay should still be loading`() = runTest {
        advanceTimeBy(1000L)
        runCurrent()

        val state = viewModel.uiState.value
        assertTrue(state.isLoading)
        assertFalse(state.isReady)
    }
}