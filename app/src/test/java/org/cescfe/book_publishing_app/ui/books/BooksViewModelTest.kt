package org.cescfe.book_publishing_app.ui.books

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.cescfe.book_publishing_app.domain.book.model.BookSummary
import org.cescfe.book_publishing_app.domain.book.repository.BooksRepository
import org.cescfe.book_publishing_app.domain.shared.DomainErrorType
import org.cescfe.book_publishing_app.domain.shared.DomainResult
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class BooksViewModelTest {

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

    private fun createViewModel(): BooksViewModel = BooksViewModel(mockRepository)

    // ==================== SUCCESS CASES ====================

    @Test
    fun `loadBooks with success should update books list`() = runTest {
        val books = listOf(
            createBook(id = "1", title = "Book One"),
            createBook(id = "2", title = "Book Two")
        )
        mockRepository.result = DomainResult.Success(books)

        val viewModel = createViewModel()
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertNull(state.error)
        assertFalse(state.sessionExpired)
        assertEquals(2, state.bookSummaries.size)
        assertEquals("Book One", state.bookSummaries[0].title)
        assertEquals("Book Two", state.bookSummaries[1].title)
    }

    @Test
    fun `loadBooks with empty list should return empty books`() = runTest {
        mockRepository.result = DomainResult.Success(emptyList())

        val viewModel = createViewModel()
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertNull(state.error)
        assertTrue(state.bookSummaries.isEmpty())
    }

    // ==================== ERROR CASES ====================

    @Test
    fun `loadBooks with network error should update error state`() = runTest {
        mockRepository.result = DomainResult.Error(
            DomainErrorType.NETWORK_ERROR,
            "Network error. Please check your connection."
        )

        val viewModel = createViewModel()
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertFalse(state.sessionExpired)
        assertTrue(state.bookSummaries.isEmpty())
        assertEquals("Network error. Please check your connection.", state.error)
    }

    @Test
    fun `loadBooks with server error should update error state`() = runTest {
        mockRepository.result = DomainResult.Error(
            DomainErrorType.SERVER_ERROR,
            "Server error. Please try again later."
        )

        val viewModel = createViewModel()
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertEquals("Server error. Please try again later.", state.error)
    }

    @Test
    fun `loadBooks with timeout should update error state`() = runTest {
        mockRepository.result = DomainResult.Error(
            DomainErrorType.TIMEOUT,
            "Request timeout. Please try again."
        )

        val viewModel = createViewModel()
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertTrue(state.error?.contains("timeout", ignoreCase = true) == true)
    }

    // ==================== SESSION EXPIRED ====================

    @Test
    fun `loadBooks with unauthorized should set sessionExpired true`() = runTest {
        mockRepository.result = DomainResult.Error(
            DomainErrorType.UNAUTHORIZED,
            "Session expired. Please login again."
        )

        val viewModel = createViewModel()
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertTrue(state.sessionExpired)
        assertNull(state.error)
    }

    // ==================== RETRY ====================

    @Test
    fun `retry should reload books`() = runTest {
        // First try: error
        mockRepository.result = DomainResult.Error(
            DomainErrorType.NETWORK_ERROR,
            "Network error"
        )

        val viewModel = createViewModel()
        advanceUntilIdle()

        val errorState = viewModel.uiState.value
        assertEquals("Network error", errorState.error)

        // Second try: success
        val books = listOf(createBook(id = "1", title = "Book One"))
        mockRepository.result = DomainResult.Success(books)

        viewModel.retry()
        advanceUntilIdle()

        val successState = viewModel.uiState.value
        assertFalse(successState.isLoading)
        assertNull(successState.error)
        assertEquals(1, successState.bookSummaries.size)
    }

    @Test
    fun `retry should clear previous error`() = runTest {
        mockRepository.result = DomainResult.Error(
            DomainErrorType.NETWORK_ERROR,
            "Network error"
        )

        val viewModel = createViewModel()
        advanceUntilIdle()
        assertTrue(viewModel.uiState.value.error != null)

        mockRepository.result = DomainResult.Success(emptyList())
        viewModel.retry()
        advanceUntilIdle()

        assertNull(viewModel.uiState.value.error)
    }

    // ==================== HELPERS ====================

    private fun createBook(
        id: String = "default-id",
        title: String = "Default Title",
        author: String = "Default Author",
        collection: String = "Default Collection",
        finalPrice: Double = 10.0,
        isbn: String = "978-0-000000-00-0"
    ) = BookSummary(
        id = id,
        title = title,
        author = author,
        collection = collection,
        finalPrice = finalPrice,
        isbn = isbn
    )
}

// ==================== MOCK ====================

class MockBooksRepository : BooksRepository {
    var result: DomainResult<List<BookSummary>> = DomainResult.Success(emptyList())

    override suspend fun getBooks(): DomainResult<List<BookSummary>> = result
}
