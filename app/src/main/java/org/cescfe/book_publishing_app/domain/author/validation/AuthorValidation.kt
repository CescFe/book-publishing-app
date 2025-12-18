package org.cescfe.book_publishing_app.domain.author.validation

import androidx.annotation.StringRes
import org.cescfe.book_publishing_app.R

object AuthorValidation {
    private val EMAIL_REGEX = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")

    fun validateFullName(value: String): ValidationResult = when {
        value.isBlank() -> ValidationResult.Error(R.string.error_full_name_required)
        value.length > 255 -> ValidationResult.Error(R.string.error_full_name_too_long)
        else -> ValidationResult.Valid
    }

    fun validatePseudonym(value: String): ValidationResult {
        val trimmed = value.trim()
        if (trimmed.isEmpty()) return ValidationResult.Valid
        return when {
            value.length > 255 -> ValidationResult.Error(R.string.error_pseudonym_too_long)
            else -> ValidationResult.Valid
        }
    }

    fun validateBiography(value: String): ValidationResult {
        val trimmed = value.trim()
        if (trimmed.isEmpty()) return ValidationResult.Valid
        return when {
            value.length > 2000 -> ValidationResult.Error(R.string.error_biography_too_long)
            else -> ValidationResult.Valid
        }
    }

    fun validateEmail(value: String): ValidationResult {
        val trimmed = value.trim()
        if (trimmed.isEmpty()) return ValidationResult.Valid
        return when {
            !value.matches(EMAIL_REGEX) -> ValidationResult.Error(R.string.error_email_invalid)
            else -> ValidationResult.Valid
        }
    }

    fun validateWebsite(value: String): ValidationResult {
        val trimmed = value.trim()
        if (trimmed.isEmpty()) return ValidationResult.Valid
        return when {
            !value.startsWith("http://") && !value.startsWith("https://") ->
                ValidationResult.Error(R.string.error_website_invalid_protocol)
            else -> ValidationResult.Valid
        }
    }
}

sealed class ValidationResult {
    object Valid : ValidationResult()
    data class Error(@param:StringRes val messageResId: Int) : ValidationResult()
}
