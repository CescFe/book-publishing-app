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
import org.cescfe.book_publishing_app.ui.shared.toStringResId
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class DeleteBookViewModelTest {

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

    // ==================== DELETE BOOK - SUCCESS ====================

    @Test
    fun `deleteBook with success should set deleteSuccess true`() = runTest {
        val book = TestBookFactory.createBook(
            id = "book-123",
            title = "Book One"
        )
        mockRepository.bookResult = DomainResult.Success(book)
        mockRepository.deleteResult = DomainResult.Success(Unit)

        val viewModel = createViewModel()
        viewModel.loadBook("book-123")
        advanceUntilIdle()

        viewModel.deleteBook()
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isDeleting)
        assertTrue(state.deleteSuccess)
        assertNull(state.errorResId)
    }

    // ==================== DELETE BOOK - ERROR CASES ====================

    @Test
    fun `deleteBook with network error should update error state`() = runTest {
        val book = TestBookFactory.createBook(
            id = "book-123",
            title = "Book One"
        )
        mockRepository.bookResult = DomainResult.Success(book)
        mockRepository.deleteResult = DomainResult.Error(DomainErrorType.NETWORK_ERROR)

        val viewModel = createViewModel()
        viewModel.loadBook("book-123")
        advanceUntilIdle()

        viewModel.deleteBook()
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isDeleting)
        assertFalse(state.deleteSuccess)
        assertEquals(R.string.error_network, state.errorResId)
    }

    @Test
    fun `deleteBook with server error should update error state`() = runTest {
        val book = TestBookFactory.createBook(
            id = "book-123",
            title = "Book One"
        )
        mockRepository.bookResult = DomainResult.Success(book)
        mockRepository.deleteResult = DomainResult.Error(DomainErrorType.SERVER_ERROR)

        val viewModel = createViewModel()
        viewModel.loadBook("book-123")
        advanceUntilIdle()

        viewModel.deleteBook()
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isDeleting)
        assertEquals(R.string.error_server, state.errorResId)
    }

    @Test
    fun `deleteBook should handle 403 FORBIDDEN error and set errorResId`() = runTest {
        val book = TestBookFactory.createBook(
            id = "book-123",
            title = "Book One"
        )
        mockRepository.bookResult = DomainResult.Success(book)
        mockRepository.deleteResult = DomainResult.Error(DomainErrorType.FORBIDDEN)

        val viewModel = createViewModel()
        viewModel.loadBook("book-123")

        viewModel.deleteBook()

        val uiState = viewModel.uiState.value
        assertEquals(DomainErrorType.FORBIDDEN.toStringResId(), uiState.errorResId)
    }

    // ==================== DELETE BOOK - SESSION EXPIRED ====================

    @Test
    fun `deleteBook with unauthorized should set sessionExpired true`() = runTest {
        val book = TestBookFactory.createBook(
            id = "book-123",
            title = "Book One"
        )
        mockRepository.bookResult = DomainResult.Success(book)
        mockRepository.deleteResult = DomainResult.Error(DomainErrorType.UNAUTHORIZED)

        val viewModel = createViewModel()
        viewModel.loadBook("book-123")
        advanceUntilIdle()

        viewModel.deleteBook()
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isDeleting)
        assertTrue(state.sessionExpired)
        assertNull(state.errorResId)
    }

    // ==================== DELETE BOOK - NO BOOK LOADED ====================

    @Test
    fun `deleteBook without loaded book should not crash`() = runTest {
        val viewModel = createViewModel()

        viewModel.deleteBook()
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isDeleting)
        assertFalse(state.deleteSuccess)
    }

    // ==================== DELETE BOOK - DIALOG MANAGEMENT ====================

    @Test
    fun `onDeleteClicked should show delete dialog`() = runTest {
        val viewModel = createViewModel()

        viewModel.onDeleteClicked()

        val state = viewModel.uiState.value
        assertTrue(state.showDeleteDialog)
    }

    @Test
    fun `onDeleteDialogDismissed should hide delete dialog`() = runTest {
        val viewModel = createViewModel()
        viewModel.onDeleteClicked()

        viewModel.onDeleteDialogDismissed()

        val state = viewModel.uiState.value
        assertFalse(state.showDeleteDialog)
    }

    @Test
    fun `onDeleteConfirmed should hide dialog and start deletion`() = runTest {
        val book = TestBookFactory.createBook(
            id = "book-123",
            title = "Book One"
        )
        mockRepository.bookResult = DomainResult.Success(book)
        mockRepository.deleteResult = DomainResult.Success(Unit)

        val viewModel = createViewModel()
        viewModel.loadBook("book-123")
        advanceUntilIdle()
        viewModel.onDeleteClicked()

        viewModel.onDeleteConfirmed()
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.showDeleteDialog)
        assertTrue(state.deleteSuccess)
    }
}
