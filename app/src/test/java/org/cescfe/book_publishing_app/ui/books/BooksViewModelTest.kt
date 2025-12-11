package org.cescfe.book_publishing_app.ui.books

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.cescfe.book_publishing_app.domain.book.model.Book
import org.cescfe.book_publishing_app.domain.book.model.BooksErrorType
import org.cescfe.book_publishing_app.domain.book.model.BooksResult
import org.cescfe.book_publishing_app.domain.book.repository.BooksRepository
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
        mockRepository.result = BooksResult.Success(books)

        val viewModel = createViewModel()
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertNull(state.error)
        assertFalse(state.sessionExpired)
        assertEquals(2, state.books.size)
        assertEquals("Book One", state.books[0].title)
        assertEquals("Book Two", state.books[1].title)
    }

    @Test
    fun `loadBooks with empty list should return empty books`() = runTest {
        mockRepository.result = BooksResult.Success(emptyList())

        val viewModel = createViewModel()
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertNull(state.error)
        assertTrue(state.books.isEmpty())
    }

    // ==================== ERROR CASES ====================

    @Test
    fun `loadBooks with network error should update error state`() = runTest {
        mockRepository.result = BooksResult.Error(
            BooksErrorType.NETWORK_ERROR,
            "Network error. Please check your connection."
        )

        val viewModel = createViewModel()
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertFalse(state.sessionExpired)
        assertTrue(state.books.isEmpty())
        assertEquals("Network error. Please check your connection.", state.error)
    }

    @Test
    fun `loadBooks with server error should update error state`() = runTest {
        mockRepository.result = BooksResult.Error(
            BooksErrorType.SERVER_ERROR,
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
        mockRepository.result = BooksResult.Error(
            BooksErrorType.TIMEOUT,
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
        mockRepository.result = BooksResult.Error(
            BooksErrorType.UNAUTHORIZED,
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
        mockRepository.result = BooksResult.Error(
            BooksErrorType.NETWORK_ERROR,
            "Network error"
        )

        val viewModel = createViewModel()
        advanceUntilIdle()

        val errorState = viewModel.uiState.value
        assertEquals("Network error", errorState.error)

        // Second try: success
        val books = listOf(createBook(id = "1", title = "Book One"))
        mockRepository.result = BooksResult.Success(books)

        viewModel.retry()
        advanceUntilIdle()

        val successState = viewModel.uiState.value
        assertFalse(successState.isLoading)
        assertNull(successState.error)
        assertEquals(1, successState.books.size)
    }

    @Test
    fun `retry should clear previous error`() = runTest {
        mockRepository.result = BooksResult.Error(
            BooksErrorType.NETWORK_ERROR,
            "Network error"
        )

        val viewModel = createViewModel()
        advanceUntilIdle()
        assertTrue(viewModel.uiState.value.error != null)

        mockRepository.result = BooksResult.Success(emptyList())
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
    ) = Book(
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
    var result: BooksResult<List<Book>> = BooksResult.Success(emptyList())

    override suspend fun getBooks(): BooksResult<List<Book>> = result
}
