package org.cescfe.book_publishing_app.domain.author.validation

import org.cescfe.book_publishing_app.R
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class AuthorValidationTest {

    @Test
    fun `validateFullName returns valid for correct name`() {
        val result = AuthorValidation.validateFullName("John Doe")
        assertTrue(result is ValidationResult.Valid)
    }

    @Test
    fun `validateFullName returns error for blank name`() {
        val result = AuthorValidation.validateFullName("  ")
        assertTrue(result is ValidationResult.Error)
        assertEquals(R.string.error_full_name_required, (result as ValidationResult.Error).messageResId)
    }

    @Test
    fun `validateFullName returns error for too long name`() {
        val result = AuthorValidation.validateFullName("a".repeat(256))
        assertTrue(result is ValidationResult.Error)
        assertEquals(R.string.error_full_name_too_long, (result as ValidationResult.Error).messageResId)
    }

    @Test
    fun `validatePseudonym returns valid for empty`() {
        assertTrue(AuthorValidation.validatePseudonym("") is ValidationResult.Valid)
        assertTrue(AuthorValidation.validatePseudonym("  ") is ValidationResult.Valid)
    }

    @Test
    fun `validatePseudonym returns error for too long`() {
        val result = AuthorValidation.validatePseudonym("a".repeat(256))
        assertTrue(result is ValidationResult.Error)
        assertEquals(R.string.error_pseudonym_too_long, (result as ValidationResult.Error).messageResId)
    }

    @Test
    fun `validateBiography returns valid for empty`() {
        assertTrue(AuthorValidation.validateBiography("") is ValidationResult.Valid)
    }

    @Test
    fun `validateBiography returns error for too long`() {
        val result = AuthorValidation.validateBiography("a".repeat(2001))
        assertTrue(result is ValidationResult.Error)
        assertEquals(R.string.error_biography_too_long, (result as ValidationResult.Error).messageResId)
    }

    @Test
    fun `validateEmail returns valid for correct email`() {
        assertTrue(AuthorValidation.validateEmail("test@example.com") is ValidationResult.Valid)
    }

    @Test
    fun `validateEmail returns error for invalid format`() {
        val result = AuthorValidation.validateEmail("invalid-email")
        assertTrue(result is ValidationResult.Error)
        assertEquals(R.string.error_email_invalid, (result as ValidationResult.Error).messageResId)
    }

    @Test
    fun `validateWebsite returns valid for correct URL`() {
        assertTrue(AuthorValidation.validateWebsite("http://example.com") is ValidationResult.Valid)
        assertTrue(AuthorValidation.validateWebsite("https://example.com") is ValidationResult.Valid)
    }

    @Test
    fun `validateWebsite returns error for missing protocol`() {
        val result = AuthorValidation.validateWebsite("example.com")
        assertTrue(result is ValidationResult.Error)
        assertEquals(R.string.error_website_invalid_protocol, (result as ValidationResult.Error).messageResId)
    }
}
