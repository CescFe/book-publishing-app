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
class UpdateAuthorViewModelTest {

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

    private fun createViewModel(): UpdateAuthorViewModel = UpdateAuthorViewModel(mockRepository)

    // ==================== LOAD AUTHOR CASES ====================

    @Test
    fun `loadAuthor with valid authorId should prefilled form fields`() = runTest {
        val author = TestAuthorFactory.createAuthor(
            id = "author-123",
            fullName = "J.R.R. Tolkien",
            pseudonym = "Tolkien",
            biography = "English writer",
            email = "tolkien@example.com",
            website = "https://tolkien.com"
        )
        mockRepository.authorResult = DomainResult.Success(author)

        val viewModel = createViewModel()
        viewModel.loadAuthor("author-123")
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertEquals("J.R.R. Tolkien", state.fullName)
        assertEquals("Tolkien", state.pseudonym)
        assertEquals("English writer", state.biography)
        assertEquals("tolkien@example.com", state.email)
        assertEquals("https://tolkien.com", state.website)
    }

    @Test
    fun `loadAuthor with author having null fields should use empty strings`() = runTest {
        val author = TestAuthorFactory.createAuthor(
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
        assertEquals("George Orwell", state.fullName)
        assertEquals("", state.pseudonym)
        assertEquals("", state.biography)
        assertEquals("", state.email)
        assertEquals("", state.website)
    }

    @Test
    fun `loadAuthor with error should set error state`() = runTest {
        mockRepository.authorResult = DomainResult.Error(DomainErrorType.NETWORK_ERROR)

        val viewModel = createViewModel()
        viewModel.loadAuthor("author-123")
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertTrue(state.errorResId != null)
    }

    // ==================== UPDATE AUTHOR SUCCESS CASES ====================

    @Test
    fun `updateAuthor with all fields should return updatedAuthorId`() = runTest {
        val author = TestAuthorFactory.createAuthor(id = "author-123")
        mockRepository.authorResult = DomainResult.Success(author)
        mockRepository.updateAuthorResult = DomainResult.Success(
            TestAuthorFactory.createAuthor(id = "author-123")
        )

        val viewModel = createViewModel()
        viewModel.loadAuthor("author-123")
        advanceUntilIdle()

        viewModel.onFullNameChange("J.R.R. Tolkien")
        viewModel.onPseudonymChange("Tolkien")
        viewModel.onBiographyChange("English writer")
        viewModel.onEmailChange("tolkien@example.com")
        viewModel.onWebsiteChange("https://tolkien.com")
        viewModel.updateAuthor()
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertEquals("author-123", state.updatedAuthorId)
        assertEquals("J.R.R. Tolkien", state.fullName)
        assertEquals("Tolkien", state.pseudonym)
        assertEquals("English writer", state.biography)
        assertEquals("tolkien@example.com", state.email)
        assertEquals("https://tolkien.com", state.website)
    }

    @Test
    fun `updateAuthor converts blank strings to null for optional fields`() = runTest {
        val author = TestAuthorFactory.createAuthor(id = "author-123")
        mockRepository.authorResult = DomainResult.Success(author)
        mockRepository.updateAuthorResult = DomainResult.Success(
            TestAuthorFactory.createAuthor(id = "author-123")
        )

        val viewModel = createViewModel()
        viewModel.loadAuthor("author-123")
        advanceUntilIdle()

        viewModel.onFullNameChange("J.R.R. Tolkien")
        viewModel.onPseudonymChange("   ")
        viewModel.onBiographyChange("")
        viewModel.onEmailChange("  ")
        viewModel.onWebsiteChange("")
        viewModel.updateAuthor()
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals("author-123", state.updatedAuthorId)
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
    fun `updateAuthor does nothing when form is invalid`() = runTest {
        val author = TestAuthorFactory.createAuthor(id = "author-123")
        mockRepository.authorResult = DomainResult.Success(author)

        val viewModel = createViewModel()
        viewModel.loadAuthor("author-123")
        advanceUntilIdle()

        viewModel.updateAuthor()
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertNull(state.updatedAuthorId)
        assertFalse(state.isLoading)
    }

    @Test
    fun `updateAuthor does nothing when authorId is not set`() = runTest {
        val viewModel = createViewModel()
        viewModel.onFullNameChange("J.R.R. Tolkien")
        viewModel.updateAuthor()
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertNull(state.updatedAuthorId)
        assertFalse(state.isLoading)
    }

    // ==================== SESSION EXPIRED ====================

    @Test
    fun `updateAuthor with UNAUTHORIZED error should set sessionExpired`() = runTest {
        val author = TestAuthorFactory.createAuthor(id = "author-123")
        mockRepository.authorResult = DomainResult.Success(author)
        mockRepository.updateAuthorResult = DomainResult.Error(DomainErrorType.UNAUTHORIZED)

        val viewModel = createViewModel()
        viewModel.loadAuthor("author-123")
        advanceUntilIdle()

        viewModel.onFullNameChange("Test Author")
        viewModel.updateAuthor()
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue(state.sessionExpired)
        assertNull(state.errorResId)
    }

    @Test
    fun `loadAuthor with UNAUTHORIZED error should set sessionExpired`() = runTest {
        mockRepository.authorResult = DomainResult.Error(DomainErrorType.UNAUTHORIZED)

        val viewModel = createViewModel()
        viewModel.loadAuthor("author-123")
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue(state.sessionExpired)
        assertNull(state.errorResId)
    }

    // ==================== CONFIRMATION DIALOG ====================

    @Test
    fun `onSaveClicked with valid form should show confirmation dialog`() = runTest {
        val viewModel = createViewModel()
        viewModel.onFullNameChange("J.R.R. Tolkien")
        viewModel.onSaveClicked()

        val state = viewModel.uiState.value
        assertTrue(state.showConfirmDialog)
    }

    @Test
    fun `onSaveClicked with invalid form should not show confirmation dialog`() = runTest {
        val viewModel = createViewModel()
        viewModel.onSaveClicked()

        val state = viewModel.uiState.value
        assertFalse(state.showConfirmDialog)
    }

    @Test
    fun `dismissConfirmDialog should hide confirmation dialog`() = runTest {
        val viewModel = createViewModel()
        viewModel.onFullNameChange("J.R.R. Tolkien")
        viewModel.onSaveClicked()

        var state = viewModel.uiState.value
        assertTrue(state.showConfirmDialog)

        viewModel.dismissConfirmDialog()

        state = viewModel.uiState.value
        assertFalse(state.showConfirmDialog)
    }

    @Test
    fun `updateAuthor dismisses confirmation dialog before updating`() = runTest {
        val author = TestAuthorFactory.createAuthor(id = "author-123")
        mockRepository.authorResult = DomainResult.Success(author)
        mockRepository.updateAuthorResult = DomainResult.Success(
            TestAuthorFactory.createAuthor(id = "author-123")
        )

        val viewModel = createViewModel()
        viewModel.loadAuthor("author-123")
        advanceUntilIdle()

        viewModel.onFullNameChange("J.R.R. Tolkien")
        viewModel.onSaveClicked()

        var state = viewModel.uiState.value
        assertTrue(state.showConfirmDialog)

        viewModel.updateAuthor()
        advanceUntilIdle()

        state = viewModel.uiState.value
        assertFalse(state.showConfirmDialog)
    }

    // ==================== FIELD VALIDATION ====================

    @Test
    fun `onFullNameChange validates fullName field`() = runTest {
        val viewModel = createViewModel()
        viewModel.onFullNameChange("   ")

        val state = viewModel.uiState.value
        assertEquals(R.string.error_full_name_required, state.fullNameError)
    }

    @Test
    fun `onEmailChange validates email field`() = runTest {
        val viewModel = createViewModel()
        viewModel.onEmailChange("invalid-email")

        val state = viewModel.uiState.value
        assertEquals(R.string.error_email_invalid, state.emailError)
    }

    @Test
    fun `onWebsiteChange validates website field`() = runTest {
        val viewModel = createViewModel()
        viewModel.onWebsiteChange("invalid-url")

        val state = viewModel.uiState.value
        assertEquals(R.string.error_website_invalid_protocol, state.websiteError)
    }

    // ==================== ERROR HANDLING ====================

    @Test
    fun `updateAuthor with network error should set errorResId`() = runTest {
        val author = TestAuthorFactory.createAuthor(id = "author-123")
        mockRepository.authorResult = DomainResult.Success(author)
        mockRepository.updateAuthorResult = DomainResult.Error(DomainErrorType.NETWORK_ERROR)

        val viewModel = createViewModel()
        viewModel.loadAuthor("author-123")
        advanceUntilIdle()

        viewModel.onFullNameChange("Test Author")
        viewModel.updateAuthor()
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertTrue(state.errorResId != null)
        assertFalse(state.sessionExpired)
    }

    @Test
    fun `onSessionExpiredHandled should clear sessionExpired flag`() = runTest {
        mockRepository.authorResult = DomainResult.Error(DomainErrorType.UNAUTHORIZED)

        val viewModel = createViewModel()
        viewModel.loadAuthor("author-123")
        advanceUntilIdle()

        var state = viewModel.uiState.value
        assertTrue(state.sessionExpired)

        viewModel.onSessionExpiredHandled()

        state = viewModel.uiState.value
        assertFalse(state.sessionExpired)
    }
}
