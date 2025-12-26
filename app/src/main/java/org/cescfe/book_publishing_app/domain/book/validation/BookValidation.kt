package org.cescfe.book_publishing_app.domain.book.validation

import androidx.annotation.StringRes
import java.text.SimpleDateFormat
import java.util.Locale
import org.cescfe.book_publishing_app.R
import org.cescfe.book_publishing_app.domain.shared.enums.Genre
import org.cescfe.book_publishing_app.domain.shared.enums.Language

object BookValidation {
    private val ISBN_REGEX = Regex("^(978|979)\\d{10}$")
    private val DATE_REGEX = Regex("^\\d{4}-\\d{2}-\\d{2}$")
    private val DATE_FORMAT = SimpleDateFormat("yyyy-MM-dd", Locale.US).apply {
        isLenient = false
    }

    fun validateTitle(value: String): ValidationResult = when {
        value.isBlank() -> ValidationResult.Error(R.string.error_title_required)
        value.length > 200 -> ValidationResult.Error(R.string.error_title_too_long)
        else -> ValidationResult.Valid
    }

    fun validateBasePrice(value: String): ValidationResult = when {
        value.isBlank() -> ValidationResult.Error(R.string.error_base_price_required)
        else -> {
            try {
                val price = value.toDouble()
                when {
                    price < 0 -> ValidationResult.Error(R.string.error_base_price_negative)
                    else -> {
                        val rounded = (price * 100).toInt() / 100.0
                        val difference = kotlin.math.abs(price - rounded)
                        if (difference >= 0.001) {
                            ValidationResult.Error(R.string.error_base_price_invalid_precision)
                        } else {
                            ValidationResult.Valid
                        }
                    }
                }
            } catch (_: NumberFormatException) {
                ValidationResult.Error(R.string.error_base_price_invalid)
            }
        }
    }

    fun validateIsbn(value: String): ValidationResult {
        val trimmed = value.trim()
        if (trimmed.isEmpty()) return ValidationResult.Valid
        return when {
            !trimmed.matches(ISBN_REGEX) -> ValidationResult.Error(R.string.error_isbn_invalid)
            else -> ValidationResult.Valid
        }
    }

    fun validatePublicationDate(value: String): ValidationResult {
        val trimmed = value.trim()
        if (trimmed.isEmpty()) return ValidationResult.Valid
        return when {
            !trimmed.matches(DATE_REGEX) -> ValidationResult.Error(R.string.error_publication_date_invalid_format)
            else -> {
                try {
                    DATE_FORMAT.parse(trimmed)
                    ValidationResult.Valid
                } catch (_: Exception) {
                    ValidationResult.Error(R.string.error_publication_date_invalid_format)
                }
            }
        }
    }

    fun validatePageCount(value: String): ValidationResult {
        val trimmed = value.trim()
        if (trimmed.isEmpty()) return ValidationResult.Valid
        return try {
            val count = trimmed.toInt()
            when {
                count < 1 -> ValidationResult.Error(R.string.error_page_count_invalid)
                else -> ValidationResult.Valid
            }
        } catch (_: NumberFormatException) {
            ValidationResult.Error(R.string.error_page_count_invalid)
        }
    }

    fun validateDescription(value: String): ValidationResult {
        val trimmed = value.trim()
        if (trimmed.isEmpty()) return ValidationResult.Valid
        return when {
            value.length > 2000 -> ValidationResult.Error(R.string.error_description_too_long)
            else -> ValidationResult.Valid
        }
    }

    fun validateSecondaryLanguages(secondaryLanguages: List<Language>, primaryLanguage: Language?): ValidationResult? {
        if (secondaryLanguages.isEmpty()) return null

        return when {
            secondaryLanguages.size > 3 -> ValidationResult.Error(R.string.error_secondary_languages_too_many)
            secondaryLanguages.size != secondaryLanguages.distinct().size ->
                ValidationResult.Error(R.string.error_secondary_languages_duplicated)
            primaryLanguage != null && secondaryLanguages.contains(primaryLanguage) ->
                ValidationResult.Error(R.string.error_secondary_language_same_as_primary)
            else -> null
        }
    }

    fun validateSecondaryGenres(secondaryGenres: List<Genre>, primaryGenre: Genre?): ValidationResult? {
        if (secondaryGenres.isEmpty()) return null

        return when {
            secondaryGenres.size > 3 -> ValidationResult.Error(R.string.error_secondary_genres_too_many)
            secondaryGenres.size != secondaryGenres.distinct().size ->
                ValidationResult.Error(R.string.error_secondary_genres_duplicated)
            primaryGenre != null && secondaryGenres.contains(primaryGenre) ->
                ValidationResult.Error(R.string.error_secondary_genre_same_as_primary)
            else -> null
        }
    }
}

sealed class ValidationResult {
    object Valid : ValidationResult()
    data class Error(@param:StringRes val messageResId: Int) : ValidationResult()
}
