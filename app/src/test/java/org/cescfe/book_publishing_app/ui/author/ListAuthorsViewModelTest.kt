package org.cescfe.book_publishing_app.ui.author

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.cescfe.book_publishing_app.R
import org.cescfe.book_publishing_app.domain.shared.DomainErrorType
import org.cescfe.book_publishing_app.domain.shared.DomainResult
import org.cescfe.book_publishing_app.ui.author.helper.MockAuthorsRepository
import org.cescfe.book_publishing_app.ui.author.helper.TestAuthorFactory
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ListAuthorsViewModelTest {

    private lateinit var mockRepository: MockAuthorsRepository

    @Before
    fun setup() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        mockRepository = MockAuthorsRepository()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun createViewModel(): AuthorsViewModel = AuthorsViewModel(mockRepository)

    // ==================== SUCCESS CASES ====================

    @Test
    fun `loadAuthors with success should update authors list`() = runTest {
        val authors = listOf(
            TestAuthorFactory.createAuthorSummary(id = "1", fullName = "Author One"),
            TestAuthorFactory.createAuthorSummary(id = "2", fullName = "Author Two")
        )
        mockRepository.authorsResult = DomainResult.Success(authors)

        val viewModel = createViewModel()
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertNull(state.errorResId)
        assertFalse(state.sessionExpired)
        assertEquals(authors.size, state.authorSummaries.size)
        assertEquals(authors[0].fullName, state.authorSummaries[0].fullName)
        assertEquals(authors[1].fullName, state.authorSummaries[1].fullName)
    }

    @Test
    fun `loadAuthors with empty list should return empty authors`() = runTest {
        mockRepository.authorsResult = DomainResult.Success(emptyList())

        val viewModel = createViewModel()
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertNull(state.errorResId)
        assertTrue(state.authorSummaries.isEmpty())
    }

    // ==================== ERROR CASES ====================

    @Test
    fun `loadAuthors with network error should update error state`() = runTest {
        mockRepository.authorsResult = DomainResult.Error(DomainErrorType.NETWORK_ERROR)

        val viewModel = createViewModel()
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertFalse(state.sessionExpired)
        assertTrue(state.authorSummaries.isEmpty())
        assertEquals(R.string.error_network, state.errorResId)
    }

    @Test
    fun `loadAuthors with server error should update error state`() = runTest {
        mockRepository.authorsResult = DomainResult.Error(DomainErrorType.SERVER_ERROR)

        val viewModel = createViewModel()
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertEquals(R.string.error_server, state.errorResId)
    }

    @Test
    fun `loadAuthors with timeout should update error state`() = runTest {
        mockRepository.authorsResult = DomainResult.Error(DomainErrorType.TIMEOUT)

        val viewModel = createViewModel()
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertEquals(R.string.error_timeout, state.errorResId)
    }

    // ==================== SESSION EXPIRED ====================

    @Test
    fun `loadAuthors with unauthorized should set sessionExpired true`() = runTest {
        mockRepository.authorsResult = DomainResult.Error(DomainErrorType.UNAUTHORIZED)

        val viewModel = createViewModel()
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertTrue(state.sessionExpired)
        assertNull(state.errorResId)
    }

    // ==================== RETRY ====================

    @Test
    fun `retry should reload authors`() = runTest {
        // First try: error
        mockRepository.authorsResult = DomainResult.Error(DomainErrorType.NETWORK_ERROR)

        val viewModel = createViewModel()
        advanceUntilIdle()

        val errorState = viewModel.uiState.value
        assertEquals(R.string.error_network, errorState.errorResId)

        // Second try: success
        val authors = listOf(TestAuthorFactory.createAuthorSummary(id = "1", fullName = "Author One"))
        mockRepository.authorsResult = DomainResult.Success(authors)

        viewModel.retry()
        advanceUntilIdle()

        val successState = viewModel.uiState.value
        assertFalse(successState.isLoading)
        assertNull(successState.errorResId)
        assertEquals(1, successState.authorSummaries.size)
    }

    @Test
    fun `retry should clear previous error`() = runTest {
        mockRepository.authorsResult = DomainResult.Error(DomainErrorType.NETWORK_ERROR)

        val viewModel = createViewModel()
        advanceUntilIdle()
        assertTrue(viewModel.uiState.value.errorResId != null)

        mockRepository.authorsResult = DomainResult.Success(emptyList())
        viewModel.retry()
        advanceUntilIdle()

        assertNull(viewModel.uiState.value.errorResId)
    }
}
