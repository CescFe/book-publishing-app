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
class DeleteAuthorViewModelTest {

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

    private fun createViewModel(): AuthorViewModel = AuthorViewModel(mockRepository)

    // ==================== DELETE AUTHOR - SUCCESS ====================

    @Test
    fun `deleteAuthor with success should set deleteSuccess true`() = runTest {
        val author = TestAuthorFactory.createAuthor(
            id = "author-123",
            fullName = "Author One"
        )
        mockRepository.authorResult = DomainResult.Success(author)
        mockRepository.deleteResult = DomainResult.Success(Unit)

        val viewModel = createViewModel()
        viewModel.loadAuthor("author-123")
        advanceUntilIdle()

        viewModel.deleteAuthor()
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isDeleting)
        assertTrue(state.deleteSuccess)
        assertNull(state.errorResId)
    }

    // ==================== DELETE AUTHOR - ERROR CASES ====================

    @Test
    fun `deleteAuthor with network error should update error state`() = runTest {
        val author = TestAuthorFactory.createAuthor(
            id = "author-123",
            fullName = "Author One"
        )
        mockRepository.authorResult = DomainResult.Success(author)
        mockRepository.deleteResult = DomainResult.Error(DomainErrorType.NETWORK_ERROR)

        val viewModel = createViewModel()
        viewModel.loadAuthor("author-123")
        advanceUntilIdle()

        viewModel.deleteAuthor()
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isDeleting)
        assertFalse(state.deleteSuccess)
        assertEquals(R.string.error_network, state.errorResId)
    }

    @Test
    fun `deleteAuthor with server error should update error state`() = runTest {
        val author = TestAuthorFactory.createAuthor(
            id = "author-123",
            fullName = "Author One"
        )
        mockRepository.authorResult = DomainResult.Success(author)
        mockRepository.deleteResult = DomainResult.Error(DomainErrorType.SERVER_ERROR)

        val viewModel = createViewModel()
        viewModel.loadAuthor("author-123")
        advanceUntilIdle()

        viewModel.deleteAuthor()
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isDeleting)
        assertEquals(R.string.error_server, state.errorResId)
    }

    // ==================== DELETE AUTHOR - SESSION EXPIRED ====================

    @Test
    fun `deleteAuthor with unauthorized should set sessionExpired true`() = runTest {
        val author = TestAuthorFactory.createAuthor(
            id = "author-123",
            fullName = "Author One"
        )
        mockRepository.authorResult = DomainResult.Success(author)
        mockRepository.deleteResult = DomainResult.Error(DomainErrorType.UNAUTHORIZED)

        val viewModel = createViewModel()
        viewModel.loadAuthor("author-123")
        advanceUntilIdle()

        viewModel.deleteAuthor()
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isDeleting)
        assertTrue(state.sessionExpired)
        assertNull(state.errorResId)
    }

    // ==================== DELETE AUTHOR - NO AUTHOR LOADED ====================

    @Test
    fun `deleteAuthor without loaded author should not crash`() = runTest {
        val viewModel = createViewModel()

        viewModel.deleteAuthor()
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isDeleting)
        assertFalse(state.deleteSuccess)
    }
}
