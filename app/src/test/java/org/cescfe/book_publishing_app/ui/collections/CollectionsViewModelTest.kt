package org.cescfe.book_publishing_app.ui.collections

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.cescfe.book_publishing_app.R
import org.cescfe.book_publishing_app.domain.collection.model.CollectionSummary
import org.cescfe.book_publishing_app.domain.collection.repository.CollectionsRepository
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
class CollectionsViewModelTest {

    private lateinit var mockRepository: MockCollectionsRepository

    @Before
    fun setup() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        mockRepository = MockCollectionsRepository()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun createViewModel(): CollectionsViewModel = CollectionsViewModel(mockRepository)

    // ==================== SUCCESS CASES ====================

    @Test
    fun `loadCollections with success should update collections list`() = runTest {
        val collections = listOf(
            createCollection(id = "1", name = "Collection One"),
            createCollection(id = "2", name = "Collection Two")
        )
        mockRepository.result = DomainResult.Success(collections)

        val viewModel = createViewModel()
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertNull(state.errorResId)
        assertFalse(state.sessionExpired)
        assertEquals(2, state.collectionSummaries.size)
        assertEquals("Collection One", state.collectionSummaries[0].name)
        assertEquals("Collection Two", state.collectionSummaries[1].name)
    }

    @Test
    fun `loadCollections with empty list should return empty collections`() = runTest {
        mockRepository.result = DomainResult.Success(emptyList())

        val viewModel = createViewModel()
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertNull(state.errorResId)
        assertTrue(state.collectionSummaries.isEmpty())
    }

    // ==================== ERROR CASES ====================

    @Test
    fun `loadCollections with network error should update error state`() = runTest {
        mockRepository.result = DomainResult.Error(DomainErrorType.NETWORK_ERROR)

        val viewModel = createViewModel()
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertFalse(state.sessionExpired)
        assertTrue(state.collectionSummaries.isEmpty())
        assertEquals(R.string.error_network, state.errorResId)
    }

    @Test
    fun `loadCollections with server error should update error state`() = runTest {
        mockRepository.result = DomainResult.Error(DomainErrorType.SERVER_ERROR)

        val viewModel = createViewModel()
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertEquals(R.string.error_server, state.errorResId)
    }

    @Test
    fun `loadCollections with timeout should update error state`() = runTest {
        mockRepository.result = DomainResult.Error(DomainErrorType.TIMEOUT)

        val viewModel = createViewModel()
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertEquals(R.string.error_timeout, state.errorResId)
    }

    // ==================== SESSION EXPIRED ====================

    @Test
    fun `loadCollections with unauthorized should set sessionExpired true`() = runTest {
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
    fun `retry should reload collections`() = runTest {
        // First try: error
        mockRepository.result = DomainResult.Error(DomainErrorType.NETWORK_ERROR)

        val viewModel = createViewModel()
        advanceUntilIdle()

        val errorState = viewModel.uiState.value
        assertEquals(R.string.error_network, errorState.errorResId)

        // Second try: success
        val collections = listOf(createCollection(id = "1", name = "Collection One"))
        mockRepository.result = DomainResult.Success(collections)

        viewModel.retry()
        advanceUntilIdle()

        val successState = viewModel.uiState.value
        assertFalse(successState.isLoading)
        assertNull(successState.errorResId)
        assertEquals(1, successState.collectionSummaries.size)
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

    private fun createCollection(
        id: String = "default-id",
        name: String = "Default Name",
        readingLevel: String? = null,
        primaryLanguage: String? = null,
        primaryGenre: String? = null
    ) = CollectionSummary(
        id = id,
        name = name,
        readingLevel = readingLevel,
        primaryLanguage = primaryLanguage,
        primaryGenre = primaryGenre
    )
}

// ==================== MOCK ====================

class MockCollectionsRepository : CollectionsRepository {
    var result: DomainResult<List<CollectionSummary>> = DomainResult.Success(emptyList())

    override suspend fun getCollections(): DomainResult<List<CollectionSummary>> = result
}
