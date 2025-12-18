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
class CreateAuthorViewModelTest {

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

    private fun createViewModel(): CreateAuthorViewModel = CreateAuthorViewModel(mockRepository)

    // ==================== SUCCESS CASES ====================

    @Test
    fun `createAuthor with valid fields should return createdAuthorId`() = runTest {
        val createdAuthor = Author(
            id = "author-123",
            fullName = "J.R.R. Tolkien",
            pseudonym = "Tolkien",
            biography = "English writer",
            email = "tolkien@example.com",
            website = "https://www.tolkienestate.com"
        )
        mockRepository.createAuthorResult = DomainResult.Success(createdAuthor)

        val viewModel = createViewModel()
        viewModel.onFullNameChange("J.R.R. Tolkien")
        viewModel.createAuthor()
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertEquals(createdAuthor.id, state.createdAuthorId)
        assertEquals(createdAuthor.fullName, state.fullName)
        assertEquals(createdAuthor.pseudonym, state.pseudonym)
        assertEquals(createdAuthor.biography, state.biography)
        assertEquals(createdAuthor.email, state.email)
        assertEquals(createdAuthor.website, state.website)
    }

    // ==================== VALIDATION CASES ====================

    @Test
    fun `createAuthor with blank fullName should set validation error`() = runTest {
        val viewModel = createViewModel()
        viewModel.onFullNameChange("   ")
        viewModel.createAuthor()

        val state = viewModel.uiState.value
        assertEquals(R.string.error_full_name_required, state.errorResId)
        assertNull(state.createdAuthorId)
    }

    @Test
    fun `createAuthor with empty fullName should set validation error`() = runTest {
        val viewModel = createViewModel()
        viewModel.createAuthor()

        val state = viewModel.uiState.value
        assertEquals(R.string.error_full_name_required, state.errorResId)
        assertNull(state.createdAuthorId)
    }

    // ==================== SESSION EXPIRED ====================

    @Test
    fun `createAuthor with UNAUTHORIZED error should set sessionExpired`() = runTest {
        mockRepository.createAuthorResult = DomainResult.Error(DomainErrorType.UNAUTHORIZED)

        val viewModel = createViewModel()
        viewModel.onFullNameChange("Test Author")
        viewModel.createAuthor()
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue(state.sessionExpired)
        assertNull(state.errorResId)
    }
}
