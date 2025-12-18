package org.cescfe.book_publishing_app.ui.author

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.cescfe.book_publishing_app.R
import org.cescfe.book_publishing_app.domain.author.model.Author
import org.cescfe.book_publishing_app.domain.shared.DomainErrorType
import org.cescfe.book_publishing_app.domain.shared.DomainResult
import org.cescfe.book_publishing_app.ui.author.helper.MockAuthorsRepository
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AuthorViewModelTest {

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

    // ==================== SUCCESS CASES ====================

    @Test
    fun `loadAuthor with success should update author`() = runTest {
        val author = createAuthor(
            id = "author-123",
            fullName = "J.R.R. Tolkien",
            pseudonym = "Tolkien",
            biography = "English writer",
            email = "tolkien@example.com",
            website = "https://www.tolkienestate.com"
        )
        mockRepository.authorResult = DomainResult.Success(author)

        val viewModel = createViewModel()
        viewModel.loadAuthor("author-123")
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertNull(state.errorResId)
        assertFalse(state.sessionExpired)
        assertEquals(author.id, state.author!!.id)
        assertEquals(author.fullName, state.author.fullName)
        assertEquals(author.pseudonym, state.author.pseudonym)
        assertEquals(author.biography, state.author.biography)
        assertEquals(author.email, state.author.email)
        assertEquals(author.website, state.author.website)
    }

    @Test
    fun `loadAuthor with null optional fields should handle correctly`() = runTest {
        val author = createAuthor(
            id = "author-456",
            fullName = "George Orwell",
            pseudonym = null,
            biography = null,
            email = null,
            website = null
        )
        mockRepository.authorResult = DomainResult.Success(author)

        val viewModel = createViewModel()
        viewModel.loadAuthor("author-456")
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertEquals(author.id, state.author!!.id)
        assertEquals(author.fullName, state.author.fullName)
        assertNull(state.author.pseudonym)
        assertNull(state.author.biography)
        assertNull(state.author.email)
        assertNull(state.author.website)
    }

    // ==================== ERROR CASES ====================

    @Test
    fun `loadAuthor with network error should update error state`() = runTest {
        mockRepository.authorResult = DomainResult.Error(DomainErrorType.NETWORK_ERROR)

        val viewModel = createViewModel()
        viewModel.loadAuthor("author-123")
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertFalse(state.sessionExpired)
        assertNull(state.author)
        assertEquals(R.string.error_network, state.errorResId)
    }

    @Test
    fun `loadAuthor with server error should update error state`() = runTest {
        mockRepository.authorResult = DomainResult.Error(DomainErrorType.SERVER_ERROR)

        val viewModel = createViewModel()
        viewModel.loadAuthor("author-123")
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertEquals(R.string.error_server, state.errorResId)
    }

    @Test
    fun `loadAuthor with timeout should update error state`() = runTest {
        mockRepository.authorResult = DomainResult.Error(DomainErrorType.TIMEOUT)

        val viewModel = createViewModel()
        viewModel.loadAuthor("author-123")
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertEquals(R.string.error_timeout, state.errorResId)
    }

    // ==================== SESSION EXPIRED ====================

    @Test
    fun `loadAuthor with unauthorized should set sessionExpired true`() = runTest {
        mockRepository.authorResult = DomainResult.Error(DomainErrorType.UNAUTHORIZED)

        val viewModel = createViewModel()
        viewModel.loadAuthor("author-123")
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertTrue(state.sessionExpired)
        assertNull(state.errorResId)
    }

    // ==================== RETRY ====================

    @Test
    fun `retry should reload author`() = runTest {
        // First try: error
        mockRepository.authorResult = DomainResult.Error(DomainErrorType.NETWORK_ERROR)

        val viewModel = createViewModel()
        viewModel.loadAuthor("author-123")
        advanceUntilIdle()

        val errorState = viewModel.uiState.value
        assertEquals(R.string.error_network, errorState.errorResId)

        // Second try: success
        val author = createAuthor(id = "author-123", fullName = "Author One")
        mockRepository.authorResult = DomainResult.Success(author)

        viewModel.retry()
        advanceUntilIdle()

        val successState = viewModel.uiState.value
        assertFalse(successState.isLoading)
        assertNull(successState.errorResId)
        assertEquals(author.id, successState.author?.id)
    }

    @Test
    fun `retry should clear previous error`() = runTest {
        mockRepository.authorResult = DomainResult.Error(DomainErrorType.NETWORK_ERROR)

        val viewModel = createViewModel()
        viewModel.loadAuthor("author-123")
        advanceUntilIdle()
        assertTrue(viewModel.uiState.value.errorResId != null)

        val author = createAuthor(id = "author-123", fullName = "Author One")
        mockRepository.authorResult = DomainResult.Success(author)
        viewModel.retry()
        advanceUntilIdle()

        assertNull(viewModel.uiState.value.errorResId)
    }

    @Test
    fun `retry without loaded author should not crash`() = runTest {
        val viewModel = createViewModel()
        viewModel.retry()
        advanceUntilIdle()

        assertNull(viewModel.uiState.value.author)
    }

    // ==================== DELETE AUTHOR - SUCCESS ====================

    @Test
    fun `deleteAuthor with success should set deleteSuccess true`() = runTest {
        val author = createAuthor(id = "author-123", fullName = "Author One")
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
        val author = createAuthor(id = "author-123", fullName = "Author One")
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
        val author = createAuthor(id = "author-123", fullName = "Author One")
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
        val author = createAuthor(id = "author-123", fullName = "Author One")
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

    // ==================== HELPERS ====================

    private fun createAuthor(
        id: String = "default-id",
        fullName: String = "Default Name",
        pseudonym: String? = null,
        biography: String? = null,
        email: String? = null,
        website: String? = null
    ) = Author(
        id = id,
        fullName = fullName,
        pseudonym = pseudonym,
        biography = biography,
        email = email,
        website = website
    )
}
