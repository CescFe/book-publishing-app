package org.cescfe.book_publishing_app.ui.author.helper

import org.cescfe.book_publishing_app.domain.author.model.Author
import org.cescfe.book_publishing_app.domain.author.model.AuthorSummary

object TestAuthorFactory {
    fun createAuthor(
        id: String = "default-id",
        fullName: String = "Default Name",
        pseudonym: String? = null,
        biography: String? = null,
        email: String? = null,
        website: String? = null
    ) = Author(
        id = id,
        fullName = fullName,
        pseudonym = pseudonym,
        biography = biography,
        email = email,
        website = website
    )

    fun createAuthorSummary(
        id: String = "default-id",
        fullName: String = "Default Name",
        pseudonym: String? = null,
        email: String? = null
    ) = AuthorSummary(
        id = id,
        fullName = fullName,
        pseudonym = pseudonym,
        email = email
    )
}
