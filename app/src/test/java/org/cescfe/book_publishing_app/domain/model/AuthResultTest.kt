package org.cescfe.book_publishing_app.domain.model

import org.cescfe.book_publishing_app.domain.auth.model.AuthResult
import org.cescfe.book_publishing_app.domain.auth.model.AuthToken
import org.cescfe.book_publishing_app.domain.auth.model.ErrorType
import org.junit.Assert.assertEquals
import org.junit.Test

class AuthResultTest {

    @Test
    fun `Success should contain AuthToken`() {
        val authToken = AuthToken(
            accessToken = "test_token",
            expiresIn = 86400,
            scope = "read write delete",
            userId = "user123"
        )
        val result = AuthResult.Success(authToken)

        assertEquals(authToken, result.token)
    }

    @Test
    fun `Error with type should contain error type and message`() {
        val errorType = ErrorType.INVALID_CREDENTIALS
        val message = "Invalid credentials"
        val result = AuthResult.Error(errorType, message)

        assertEquals(errorType, result.type)
        assertEquals(message, result.message)
    }

    @Test
    fun `Error convenience constructor should use UNKNOWN type`() {
        val message = "Something went wrong"
        val result = AuthResult.Error(message)

        assertEquals(ErrorType.UNKNOWN, result.type)
        assertEquals(message, result.message)
    }
}
