package org.cescfe.book_publishing_app.ui.book

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.cescfe.book_publishing_app.R
import org.cescfe.book_publishing_app.data.author.repository.AuthorsRepositoryImpl
import org.cescfe.book_publishing_app.data.book.repository.BooksRepositoryImpl
import org.cescfe.book_publishing_app.data.collection.repository.CollectionsRepositoryImpl
import org.cescfe.book_publishing_app.data.shared.remote.RetrofitClient
import org.cescfe.book_publishing_app.domain.author.model.AuthorSummary
import org.cescfe.book_publishing_app.domain.author.repository.AuthorsRepository
import org.cescfe.book_publishing_app.domain.book.model.CreateBookRequest
import org.cescfe.book_publishing_app.domain.book.model.enums.Status
import org.cescfe.book_publishing_app.domain.book.repository.BooksRepository
import org.cescfe.book_publishing_app.domain.book.validation.BookValidation
import org.cescfe.book_publishing_app.domain.book.validation.ValidationResult
import org.cescfe.book_publishing_app.domain.collection.model.CollectionSummary
import org.cescfe.book_publishing_app.domain.collection.repository.CollectionsRepository
import org.cescfe.book_publishing_app.domain.shared.DomainErrorType
import org.cescfe.book_publishing_app.domain.shared.DomainResult
import org.cescfe.book_publishing_app.domain.shared.enums.Genre
import org.cescfe.book_publishing_app.domain.shared.enums.Language
import org.cescfe.book_publishing_app.domain.shared.enums.ReadingLevel
import org.cescfe.book_publishing_app.ui.shared.toStringResId

data class CreateBookUiState(
    val title: String = "",
    val authorName: String = "",
    val collectionName: String = "",
    val basePrice: String = "",
    val readingLevel: ReadingLevel? = null,
    val primaryLanguage: Language? = null,
    val secondaryLanguages: List<Language> = emptyList(),
    val primaryGenre: Genre? = null,
    val secondaryGenres: List<Genre> = emptyList(),
    val vatRate: String = "",
    val isbn: String = "",
    val publicationDate: String = "",
    val pageCount: String = "",
    val description: String = "",
    val status: Status? = null,
    val isLoading: Boolean = false,
    val isLoadingAuthors: Boolean = false,
    val isLoadingCollections: Boolean = false,
    val authors: List<AuthorSummary> = emptyList(),
    val collections: List<CollectionSummary> = emptyList(),
    @get:StringRes val titleError: Int? = null,
    @get:StringRes val authorNameError: Int? = null,
    @get:StringRes val collectionNameError: Int? = null,
    @get:StringRes val basePriceError: Int? = null,
    @get:StringRes val vatRateError: Int? = null,
    @get:StringRes val isbnError: Int? = null,
    @get:StringRes val publicationDateError: Int? = null,
    @get:StringRes val pageCountError: Int? = null,
    @get:StringRes val descriptionError: Int? = null,
    @get:StringRes val secondaryLanguagesError: Int? = null,
    @get:StringRes val secondaryGenresError: Int? = null,
    @get:StringRes val errorResId: Int? = null,
    val sessionExpired: Boolean = false,
    val createdBookId: String? = null,
    val showConfirmDialog: Boolean = false
) {
    val isFormValid: Boolean
        get() = title.isNotBlank() &&
                authorName.isNotBlank() &&
                collectionName.isNotBlank() &&
                basePrice.isNotBlank() &&
                titleError == null &&
                authorNameError == null &&
                collectionNameError == null &&
                basePriceError == null &&
                vatRateError == null &&
                isbnError == null &&
                publicationDateError == null &&
                pageCountError == null &&
                descriptionError == null &&
                secondaryLanguagesError == null &&
                secondaryGenresError == null &&
                findAuthorIdByName(authorName) != null &&
                findCollectionIdByName(collectionName) != null

    // Helper functions to find IDs by name
    fun findAuthorIdByName(name: String): String? {
        return authors.find { it.fullName.equals(name, ignoreCase = true) }?.id
    }

    fun findCollectionIdByName(name: String): String? {
        return collections.find { it.name.equals(name, ignoreCase = true) }?.id
    }
}

class CreateBookViewModel(
    private val booksRepository: BooksRepository = BooksRepositoryImpl(
        RetrofitClient.booksApi
    ),
    private val authorsRepository: AuthorsRepository = AuthorsRepositoryImpl(
        RetrofitClient.authorsApi
    ),
    private val collectionsRepository: CollectionsRepository = CollectionsRepositoryImpl(
        RetrofitClient.collectionsApi
    )
) : ViewModel() {

    private val _uiState = MutableStateFlow(CreateBookUiState())
    val uiState: StateFlow<CreateBookUiState> = _uiState.asStateFlow()

    init {
        loadAuthors()
        loadCollections()
    }

    fun onSessionExpiredHandled() {
        _uiState.value = _uiState.value.copy(sessionExpired = false)
    }

    private fun loadAuthors() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoadingAuthors = true)

            when (val result = authorsRepository.getAuthors()) {
                is DomainResult.Success -> {
                    _uiState.value = _uiState.value.copy(
                        isLoadingAuthors = false,
                        authors = result.data
                    )
                }
                is DomainResult.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isLoadingAuthors = false,
                        errorResId = result.type.toStringResId()
                    )
                }
            }
        }
    }

    private fun loadCollections() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoadingCollections = true)

            when (val result = collectionsRepository.getCollections()) {
                is DomainResult.Success -> {
                    _uiState.value = _uiState.value.copy(
                        isLoadingCollections = false,
                        collections = result.data
                    )
                }
                is DomainResult.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isLoadingCollections = false,
                        errorResId = result.type.toStringResId()
                    )
                }
            }
        }
    }

    fun onTitleChange(title: String) {
        updateField(
            value = title,
            validator = BookValidation::validateTitle
        ) { state, error ->
            state.copy(title = title, titleError = error)
        }
    }

    fun onAuthorNameChange(authorName: String) {  // Changed from onAuthorIdChange
        val currentState = _uiState.value
        val error = if (authorName.isBlank()) {
            R.string.error_author_id_required  // Keep same error string for now
        } else if (currentState.findAuthorIdByName(authorName) == null) {
            R.string.error_author_id_invalid_format  // Author not found
        } else {
            null
        }

        _uiState.value = currentState.copy(
            authorName = authorName,
            authorNameError = error,
            errorResId = null
        )
    }

    fun onCollectionNameChange(collectionName: String) {  // Changed from onCollectionIdChange
        val currentState = _uiState.value
        val error = if (collectionName.isBlank()) {
            R.string.error_collection_id_required  // Keep same error string for now
        } else if (currentState.findCollectionIdByName(collectionName) == null) {
            R.string.error_collection_id_invalid_format  // Collection not found
        } else {
            null
        }

        _uiState.value = currentState.copy(
            collectionName = collectionName,
            collectionNameError = error,
            errorResId = null
        )
    }

    fun onBasePriceChange(basePrice: String) {
        updateField(
            value = basePrice,
            validator = BookValidation::validateBasePrice
        ) { state, error ->
            state.copy(basePrice = basePrice, basePriceError = error)
        }
    }

    fun onReadingLevelChange(readingLevel: ReadingLevel?) {
        _uiState.value = _uiState.value.copy(readingLevel = readingLevel)
    }

    fun onPrimaryLanguageChange(primaryLanguage: Language?) {
        val currentState = _uiState.value
        val newState = currentState.copy(primaryLanguage = primaryLanguage)

        val secondaryLanguagesError = BookValidation.validateSecondaryLanguages(
            newState.secondaryLanguages,
            primaryLanguage
        )?.errorResIdOrNull()

        _uiState.value = newState.copy(secondaryLanguagesError = secondaryLanguagesError)
    }

    fun onSecondaryLanguagesChange(secondaryLanguages: List<Language>) {
        val currentState = _uiState.value
        val error = BookValidation.validateSecondaryLanguages(
            secondaryLanguages,
            currentState.primaryLanguage
        )?.errorResIdOrNull()

        _uiState.value = currentState.copy(
            secondaryLanguages = secondaryLanguages,
            secondaryLanguagesError = error,
            errorResId = null
        )
    }

    fun onPrimaryGenreChange(primaryGenre: Genre?) {
        val currentState = _uiState.value
        val newState = currentState.copy(primaryGenre = primaryGenre)

        val secondaryGenresError = BookValidation.validateSecondaryGenres(
            newState.secondaryGenres,
            primaryGenre
        )?.errorResIdOrNull()

        _uiState.value = newState.copy(secondaryGenresError = secondaryGenresError)
    }

    fun onSecondaryGenresChange(secondaryGenres: List<Genre>) {
        val currentState = _uiState.value
        val error = BookValidation.validateSecondaryGenres(
            secondaryGenres,
            currentState.primaryGenre
        )?.errorResIdOrNull()

        _uiState.value = currentState.copy(
            secondaryGenres = secondaryGenres,
            secondaryGenresError = error,
            errorResId = null
        )
    }

    fun onVatRateChange(vatRate: String) {
        updateField(
            value = vatRate,
            validator = BookValidation::validateVatRate
        ) { state, error ->
            state.copy(vatRate = vatRate, vatRateError = error)
        }
    }

    fun onIsbnChange(isbn: String) {
        updateField(
            value = isbn,
            validator = BookValidation::validateIsbn
        ) { state, error ->
            state.copy(isbn = isbn, isbnError = error)
        }
    }

    fun onPublicationDateChange(publicationDate: String) {
        updateField(
            value = publicationDate,
            validator = BookValidation::validatePublicationDate
        ) { state, error ->
            state.copy(publicationDate = publicationDate, publicationDateError = error)
        }
    }

    fun onPageCountChange(pageCount: String) {
        updateField(
            value = pageCount,
            validator = BookValidation::validatePageCount
        ) { state, error ->
            state.copy(pageCount = pageCount, pageCountError = error)
        }
    }

    fun onDescriptionChange(description: String) {
        updateField(
            value = description,
            validator = BookValidation::validateDescription
        ) { state, error ->
            state.copy(description = description, descriptionError = error)
        }
    }

    fun onStatusChange(status: Status?) {
        _uiState.value = _uiState.value.copy(status = status)
    }

    fun onSaveClicked() {
        validateAllFields()
        if (_uiState.value.isFormValid) {
            _uiState.value = _uiState.value.copy(showConfirmDialog = true)
        }
    }

    fun dismissConfirmDialog() {
        _uiState.value = _uiState.value.copy(showConfirmDialog = false)
    }

    fun createBook() {
        validateAllFields()
        if (!_uiState.value.isFormValid) return

        val currentState = _uiState.value

        // Find IDs from names
        val authorId = currentState.findAuthorIdByName(currentState.authorName)
        val collectionId = currentState.findCollectionIdByName(currentState.collectionName)

        if (authorId == null || collectionId == null) {
            // This shouldn't happen if validation is correct, but handle it anyway
            return
        }

        viewModelScope.launch {
            _uiState.value = currentState.copy(
                isLoading = true,
                errorResId = null,
                showConfirmDialog = false
            )

            val basePriceValue = currentState.basePrice.trim().toDouble()
            val roundedBasePrice = (basePriceValue * 100).toInt() / 100.0

            val vatRateValue = currentState.vatRate.trim().ifBlank { null }?.toDoubleOrNull()?.let { rate ->
                (rate * 100).toInt() / 100.0
            }

            val request = CreateBookRequest(
                title = currentState.title.trim(),
                authorId = authorId,  // Use found ID
                collectionId = collectionId,  // Use found ID
                basePrice = roundedBasePrice,
                readingLevel = currentState.readingLevel,
                primaryLanguage = currentState.primaryLanguage,
                secondaryLanguages = currentState.secondaryLanguages,
                primaryGenre = currentState.primaryGenre,
                secondaryGenres = currentState.secondaryGenres,
                vatRate = vatRateValue,
                isbn = currentState.isbn.trim().ifBlank { null },
                publicationDate = currentState.publicationDate.trim().ifBlank { null },
                pageCount = currentState.pageCount.trim().ifBlank { null }?.toIntOrNull(),
                description = currentState.description.trim().ifBlank { null },
                status = currentState.status
            )

            when (val result = booksRepository.createBook(request)) {
                is DomainResult.Success -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        createdBookId = result.data.id
                    )
                }
                is DomainResult.Error -> {
                    _uiState.value = _uiState.value.copy(isLoading = false)
                    handleError(result.type)
                }
            }
        }
    }

    private fun validateAllFields() {
        val s = _uiState.value
        _uiState.value = s.copy(
            titleError = BookValidation.validateTitle(s.title).errorResIdOrNull(),
            authorNameError = if (s.authorName.isBlank()) {
                R.string.error_author_id_required
            } else if (s.findAuthorIdByName(s.authorName) == null) {
                R.string.error_author_id_invalid_format
            } else {
                null
            },
            collectionNameError = if (s.collectionName.isBlank()) {
                R.string.error_collection_id_required
            } else if (s.findCollectionIdByName(s.collectionName) == null) {
                R.string.error_collection_id_invalid_format
            } else {
                null
            },
            basePriceError = BookValidation.validateBasePrice(s.basePrice).errorResIdOrNull(),
            vatRateError = BookValidation.validateVatRate(s.vatRate).errorResIdOrNull(),
            isbnError = BookValidation.validateIsbn(s.isbn).errorResIdOrNull(),
            publicationDateError = BookValidation.validatePublicationDate(s.publicationDate).errorResIdOrNull(),
            pageCountError = BookValidation.validatePageCount(s.pageCount).errorResIdOrNull(),
            descriptionError = BookValidation.validateDescription(s.description).errorResIdOrNull(),
            secondaryLanguagesError = BookValidation.validateSecondaryLanguages(
                s.secondaryLanguages,
                s.primaryLanguage
            )?.errorResIdOrNull(),
            secondaryGenresError = BookValidation.validateSecondaryGenres(
                s.secondaryGenres,
                s.primaryGenre
            )?.errorResIdOrNull()
        )
    }

    private fun handleError(errorType: DomainErrorType) {
        _uiState.value =
            if (errorType == DomainErrorType.UNAUTHORIZED) {
                _uiState.value.copy(sessionExpired = true)
            } else {
                _uiState.value.copy(errorResId = errorType.toStringResId())
            }
    }

    private fun ValidationResult.errorResIdOrNull(): Int? = when (this) {
        is ValidationResult.Valid -> null
        is ValidationResult.Error -> messageResId
    }

    private inline fun updateField(
        value: String,
        validator: (String) -> ValidationResult,
        crossinline reducer: (CreateBookUiState, Int?) -> CreateBookUiState
    ) {
        val error = validator(value).errorResIdOrNull()
        _uiState.value = reducer(_uiState.value, error).copy(errorResId = null)
    }
}
