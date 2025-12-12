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
import org.cescfe.book_publishing_app.R

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
        assertNull(state.errorResId)
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
        assertNull(state.errorResId)
        assertTrue(state.bookSummaries.isEmpty())
    }

    // ==================== ERROR CASES ====================

    @Test
    fun `loadBooks with network error should update error state`() = runTest {
        mockRepository.result = DomainResult.Error(DomainErrorType.NETWORK_ERROR)

        val viewModel = createViewModel()
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertFalse(state.sessionExpired)
        assertTrue(state.bookSummaries.isEmpty())
        assertEquals(R.string.error_network, state.errorResId)
    }

    @Test
    fun `loadBooks with server error should update error state`() = runTest {
        mockRepository.result = DomainResult.Error(DomainErrorType.SERVER_ERROR)

        val viewModel = createViewModel()
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertEquals(R.string.error_server, state.errorResId)
    }

    @Test
    fun `loadBooks with timeout should update error state`() = runTest {
        mockRepository.result = DomainResult.Error(DomainErrorType.TIMEOUT)

        val viewModel = createViewModel()
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertEquals(R.string.error_timeout, state.errorResId)
    }

    // ==================== SESSION EXPIRED ====================

    @Test
    fun `loadBooks with unauthorized should set sessionExpired true`() = runTest {
        mockRepository.result = DomainResult.Error(DomainErrorType.UNAUTHORIZED)

        val viewModel = createViewModel()
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertTrue(state.sessionExpired)
        assertNull(state.errorResId)
    }

    // ==================== RETRY ====================

    @Test
    fun `retry should reload books`() = runTest {
        // First try: error
        mockRepository.result = DomainResult.Error(DomainErrorType.NETWORK_ERROR)

        val viewModel = createViewModel()
        advanceUntilIdle()

        val errorState = viewModel.uiState.value
        assertEquals(R.string.error_network, errorState.errorResId)

        // Second try: success
        val books = listOf(createBook(id = "1", title = "Book One"))
        mockRepository.result = DomainResult.Success(books)

        viewModel.retry()
        advanceUntilIdle()

        val successState = viewModel.uiState.value
        assertFalse(successState.isLoading)
        assertNull(successState.errorResId)
        assertEquals(1, successState.bookSummaries.size)
    }

    @Test
    fun `retry should clear previous error`() = runTest {
        mockRepository.result = DomainResult.Error(DomainErrorType.NETWORK_ERROR)

        val viewModel = createViewModel()
        advanceUntilIdle()
        assertTrue(viewModel.uiState.value.errorResId != null)

        mockRepository.result = DomainResult.Success(emptyList())
        viewModel.retry()
        advanceUntilIdle()

        assertNull(viewModel.uiState.value.errorResId)
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
