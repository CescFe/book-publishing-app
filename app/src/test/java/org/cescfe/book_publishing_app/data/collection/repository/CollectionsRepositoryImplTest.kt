package org.cescfe.book_publishing_app.data.collection.repository

import java.io.IOException
import java.net.SocketTimeoutException
import kotlinx.coroutines.test.runTest
import org.cescfe.book_publishing_app.data.collection.remote.api.CollectionsApi
import org.cescfe.book_publishing_app.data.collection.remote.dto.CollectionSummaryDTO
import org.cescfe.book_publishing_app.data.collection.remote.dto.CollectionsResponse
import org.cescfe.book_publishing_app.data.shared.remote.dto.PaginationMeta
import org.cescfe.book_publishing_app.data.shared.repository.helper.TestHttpExceptionFactory
import org.cescfe.book_publishing_app.domain.shared.DomainErrorType
import org.cescfe.book_publishing_app.domain.shared.DomainResult
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.HttpException

class CollectionsRepositoryImplTest {

    private lateinit var mockCollectionsApi: MockCollectionsApi
    private lateinit var repository: CollectionsRepositoryImpl

    @Before
    fun setup() {
        mockCollectionsApi = MockCollectionsApi()
        repository = CollectionsRepositoryImpl(mockCollectionsApi)
    }

    // ==================== SUCCESS CASES ====================

    @Test
    fun `getCollections with valid response should return Success with collections list`() = runTest {
        val collectionDto = createCollectionDTO(
            id = "collection-123",
            name = "Fantasy Classics",
            readingLevel = "Adult",
            primaryLanguage = "English",
            primaryGenre = "Fantasy"
        )
        mockCollectionsApi.successResponse = createCollectionsResponse(listOf(collectionDto))

        val result = repository.getCollections()

        assertTrue(result is DomainResult.Success)
        val success = result as DomainResult.Success
        assertEquals(1, success.data.size)
        assertEquals("collection-123", success.data[0].id)
        assertEquals("Fantasy Classics", success.data[0].name)
    }

    @Test
    fun `getCollections with empty list should return Success with empty list`() = runTest {
        mockCollectionsApi.successResponse = createCollectionsResponse(emptyList())

        val result = repository.getCollections()

        assertTrue(result is DomainResult.Success)
        val success = result as DomainResult.Success
        assertEquals(0, success.data.size)
    }

    @Test
    fun `getCollections with multiple collections should return all collections`() = runTest {
        val collections = listOf(
            createCollectionDTO(id = "collection-1", name = "Collection One"),
            createCollectionDTO(id = "collection-2", name = "Collection Two"),
            createCollectionDTO(id = "collection-3", name = "Collection Three")
        )
        mockCollectionsApi.successResponse = createCollectionsResponse(collections)

        val result = repository.getCollections()

        assertTrue(result is DomainResult.Success)
        val success = result as DomainResult.Success
        assertEquals(3, success.data.size)
    }

    // ==================== DTO TRANSFORMATION ====================

    @Test
    fun `getCollections should transform DTO to domain model correctly`() = runTest {
        val collectionDto = createCollectionDTO(
            id = "8f5ef275-4987-47bc-8643-ff4e5efd6523",
            name = "Science Fiction",
            readingLevel = null,
            primaryLanguage = null,
            primaryGenre = null
        )
        mockCollectionsApi.successResponse = createCollectionsResponse(listOf(collectionDto))

        val result = repository.getCollections()

        assertTrue(result is DomainResult.Success)
        val collection = (result as DomainResult.Success).data[0]
        assertEquals("8f5ef275-4987-47bc-8643-ff4e5efd6523", collection.id)
        assertEquals("Science Fiction", collection.name)
        assertEquals("", collection.readingLevel)
        assertEquals("", collection.primaryLanguage)
        assertEquals("", collection.primaryGenre)
    }

    // ==================== ERROR HANDLING ====================

    @Test
    fun `getCollections with SocketTimeoutException should return Timeout error`() = runTest {
        mockCollectionsApi.exception = SocketTimeoutException("Connection timed out")

        val result = repository.getCollections()

        assertTrue(result is DomainResult.Error)
        val error = result as DomainResult.Error
        assertEquals(DomainErrorType.TIMEOUT, error.type)
    }

    @Test
    fun `getCollections with IOException should return NetworkError`() = runTest {
        mockCollectionsApi.exception = IOException("Network unavailable")

        val result = repository.getCollections()

        assertTrue(result is DomainResult.Error)
        val error = result as DomainResult.Error
        assertEquals(DomainErrorType.NETWORK_ERROR, error.type)
    }

    @Test
    fun `getCollections with 401 HttpException should return Unauthorized error`() = runTest {
        mockCollectionsApi.httpException = TestHttpExceptionFactory.create(401)

        val result = repository.getCollections()

        assertTrue(result is DomainResult.Error)
        val error = result as DomainResult.Error
        assertEquals(DomainErrorType.UNAUTHORIZED, error.type)
    }

    @Test
    fun `getCollections with 500 HttpException should return ServerError`() = runTest {
        mockCollectionsApi.httpException = TestHttpExceptionFactory.create(500)

        val result = repository.getCollections()

        assertTrue(result is DomainResult.Error)
        val error = result as DomainResult.Error
        assertEquals(DomainErrorType.SERVER_ERROR, error.type)
    }

    @Test
    fun `getCollections with 503 HttpException should return ServerError`() = runTest {
        mockCollectionsApi.httpException = TestHttpExceptionFactory.create(503)

        val result = repository.getCollections()

        assertTrue(result is DomainResult.Error)
        val error = result as DomainResult.Error
        assertEquals(DomainErrorType.SERVER_ERROR, error.type)
    }

    @Test
    fun `getCollections with unknown exception should return Unknown error`() = runTest {
        mockCollectionsApi.exception = RuntimeException("Something unexpected")

        val result = repository.getCollections()

        assertTrue(result is DomainResult.Error)
        val error = result as DomainResult.Error
        assertEquals(DomainErrorType.UNKNOWN, error.type)
    }

    @Test
    fun `getCollections with 404 HttpException should return Unknown error`() = runTest {
        mockCollectionsApi.httpException = TestHttpExceptionFactory.create(404)

        val result = repository.getCollections()

        assertTrue(result is DomainResult.Error)
        val error = result as DomainResult.Error
        assertEquals(DomainErrorType.UNKNOWN, error.type)
    }

    // ==================== HELPERS ====================

    private fun createCollectionDTO(
        id: String = "default-id",
        name: String = "Default Collection",
        readingLevel: String? = null,
        primaryLanguage: String? = null,
        primaryGenre: String? = null
    ) = CollectionSummaryDTO(
        id = id,
        name = name,
        readingLevel = readingLevel,
        primaryLanguage = primaryLanguage,
        primaryGenre = primaryGenre
    )

    private fun createCollectionsResponse(collections: List<CollectionSummaryDTO>) = CollectionsResponse(
        data = collections,
        meta = PaginationMeta(
            page = 1,
            limit = 20,
            total = collections.size,
            totalPages = 1
        )
    )
}

// ==================== MOCK ====================

class MockCollectionsApi : CollectionsApi {
    var successResponse: CollectionsResponse? = null
    var httpException: HttpException? = null
    var exception: Throwable? = null

    override suspend fun getCollections(): CollectionsResponse = when {
        httpException != null -> throw httpException!!
        exception != null -> throw exception!!
        successResponse != null -> successResponse!!
        else -> throw RuntimeException("Mock not configured")
    }
}
