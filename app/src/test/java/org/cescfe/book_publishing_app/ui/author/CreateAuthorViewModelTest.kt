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
import org.cescfe.book_publishing_app.ui.author.helper.TestAuthorFactory
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
    fun `createAuthor with all fields should return createdAuthorId`() = runTest {
        val createdAuthor = TestAuthorFactory.createAuthor(
            id = "author-456"
        )
        mockRepository.createAuthorResult = DomainResult.Success(createdAuthor)

        val viewModel = createViewModel()
        viewModel.onFullNameChange("J.R.R. Tolkien")
        viewModel.onPseudonymChange("Tolkien")
        viewModel.onBiographyChange("English writer")
        viewModel.onEmailChange("tolkien@example.com")
        viewModel.onWebsiteChange("https://tolkien.com")
        viewModel.createAuthor()
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertEquals("author-456", state.createdAuthorId)
        assertEquals("J.R.R. Tolkien", state.fullName)
        assertEquals("Tolkien", state.pseudonym)
        assertEquals("English writer", state.biography)
        assertEquals("tolkien@example.com", state.email)
        assertEquals("https://tolkien.com", state.website)
    }

    @Test
    fun `createAuthor with valid fullName should return createdAuthorId`() = runTest {
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
        assertEquals("author-123", state.createdAuthorId)
    }

    // ==================== VALIDATION CASES ====================

    @Test
    fun `form is invalid when fullName is blank`() = runTest {
        val viewModel = createViewModel()
        viewModel.onFullNameChange("   ")

        val state = viewModel.uiState.value
        assertFalse(state.isFormValid)
        assertEquals(R.string.error_full_name_required, state.fullNameError)
    }

    @Test
    fun `form is invalid when fullName is empty`() = runTest {
        val viewModel = createViewModel()

        val state = viewModel.uiState.value
        assertFalse(state.isFormValid)
    }

    @Test
    fun `createAuthor does nothing when form is invalid`() = runTest {
        val viewModel = createViewModel()
        viewModel.createAuthor()
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertNull(state.createdAuthorId)
        assertFalse(state.isLoading)
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
