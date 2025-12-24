package org.cescfe.book_publishing_app.ui.book

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
import org.cescfe.book_publishing_app.ui.book.helper.MockBooksRepository
import org.cescfe.book_publishing_app.ui.book.helper.TestBookFactory
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GetBookViewModelTest {

    private lateinit var mockRepository: MockBooksRepository

    @Before
    fun setup() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        mockRepository = MockBooksRepository()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun createViewModel(): BookViewModel = BookViewModel(mockRepository)

    // ==================== SUCCESS CASES ====================

    @Test
    fun `loadBook with success should update book`() = runTest {
        val book = TestBookFactory.createBook(
            id = "book-123",
            title = "Test Book"
        )
        mockRepository.bookResult = DomainResult.Success(book)

        val viewModel = createViewModel()
        viewModel.loadBook("book-123")
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertNull(state.errorResId)
        assertFalse(state.sessionExpired)
        assertEquals(book.id, state.book!!.id)
        assertEquals(book.title, state.book.title)
    }

    // ==================== ERROR CASES ====================

    @Test
    fun `loadBook with network error should update error state`() = runTest {
        mockRepository.bookResult = DomainResult.Error(DomainErrorType.NETWORK_ERROR)

        val viewModel = createViewModel()
        viewModel.loadBook("book-123")
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertFalse(state.sessionExpired)
        assertNull(state.book)
        assertEquals(R.string.error_network, state.errorResId)
    }

    @Test
    fun `loadBook with server error should update error state`() = runTest {
        mockRepository.bookResult = DomainResult.Error(DomainErrorType.SERVER_ERROR)

        val viewModel = createViewModel()
        viewModel.loadBook("book-123")
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertEquals(R.string.error_server, state.errorResId)
    }

    // ==================== SESSION EXPIRED ====================

    @Test
    fun `loadBook with unauthorized should set sessionExpired true`() = runTest {
        mockRepository.bookResult = DomainResult.Error(DomainErrorType.UNAUTHORIZED)

        val viewModel = createViewModel()
        viewModel.loadBook("book-123")
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertTrue(state.sessionExpired)
        assertNull(state.errorResId)
    }

    // ==================== RETRY ====================

    @Test
    fun `retry should reload book`() = runTest {
        // First try: error
        mockRepository.bookResult = DomainResult.Error(DomainErrorType.NETWORK_ERROR)

        val viewModel = createViewModel()
        viewModel.loadBook("book-123")
        advanceUntilIdle()

        val errorState = viewModel.uiState.value
        assertEquals(R.string.error_network, errorState.errorResId)

        // Second try: success
        val book = TestBookFactory.createBook(
            id = "book-123",
            title = "Test Book"
        )
        mockRepository.bookResult = DomainResult.Success(book)

        viewModel.retry()
        advanceUntilIdle()

        val successState = viewModel.uiState.value
        assertFalse(successState.isLoading)
        assertNull(successState.errorResId)
        assertEquals(book.id, successState.book?.id)
    }

    @Test
    fun `retry should clear previous error`() = runTest {
        mockRepository.bookResult = DomainResult.Error(DomainErrorType.NETWORK_ERROR)

        val viewModel = createViewModel()
        viewModel.loadBook("book-123")
        advanceUntilIdle()
        assertTrue(viewModel.uiState.value.errorResId != null)

        val book = TestBookFactory.createBook(id = "book-123", title = "Test Book")
        mockRepository.bookResult = DomainResult.Success(book)
        viewModel.retry()
        advanceUntilIdle()

        assertNull(viewModel.uiState.value.errorResId)
    }

    @Test
    fun `retry without loaded book should not crash`() = runTest {
        val viewModel = createViewModel()
        viewModel.retry()
        advanceUntilIdle()

        assertNull(viewModel.uiState.value.book)
    }
}
