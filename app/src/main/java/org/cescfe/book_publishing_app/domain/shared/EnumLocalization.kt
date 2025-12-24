package org.cescfe.book_publishing_app.domain.shared

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import org.cescfe.book_publishing_app.R
import org.cescfe.book_publishing_app.domain.book.model.enums.Status
import org.cescfe.book_publishing_app.domain.shared.enums.Genre
import org.cescfe.book_publishing_app.domain.shared.enums.Language
import org.cescfe.book_publishing_app.domain.shared.enums.ReadingLevel
import androidx.compose.ui.res.stringResource

@StringRes
fun Genre.toResId(): Int = when (this) {
    Genre.ADVENTURE -> R.string.enum_genre_adventure
    Genre.ART_DESIGN -> R.string.enum_genre_art_design
    Genre.AUTOBIOGRAPHY -> R.string.enum_genre_autobiography
    Genre.BIOGRAPHY -> R.string.enum_genre_biography
    Genre.BUSINESS -> R.string.enum_genre_business
    Genre.CHRISTIANITY -> R.string.enum_genre_christianity
    Genre.CHILDREN -> R.string.enum_genre_children
    Genre.COMICS -> R.string.enum_genre_comics
    Genre.COOKING -> R.string.enum_genre_cooking
    Genre.CULTURE -> R.string.enum_genre_culture
    Genre.DICTIONARIES -> R.string.enum_genre_dictionaries
    Genre.DIVERSE_TOPICS -> R.string.enum_genre_diverse_topics
    Genre.DRAMA -> R.string.enum_genre_drama
    Genre.EDUCATION -> R.string.enum_genre_education
    Genre.ESSAY -> R.string.enum_genre_essay
    Genre.ETHICS -> R.string.enum_genre_ethics
    Genre.FANTASY -> R.string.enum_genre_fantasy
    Genre.FICTION -> R.string.enum_genre_fiction
    Genre.GENERAL_INTEREST -> R.string.enum_genre_general_interest
    Genre.GRAMMAR -> R.string.enum_genre_grammar
    Genre.GRAPHIC_NOVELS -> R.string.enum_genre_graphic_novels
    Genre.HEALTH_FITNESS -> R.string.enum_genre_health_fitness
    Genre.HISTORICAL_FICTION -> R.string.enum_genre_historical_fiction
    Genre.HISTORY -> R.string.enum_genre_history
    Genre.HORROR -> R.string.enum_genre_horror
    Genre.LEXICOGRAPHY -> R.string.enum_genre_lexicography
    Genre.MEMOIR -> R.string.enum_genre_memoir
    Genre.MYSTERY -> R.string.enum_genre_mystery
    Genre.NON_FICTION -> R.string.enum_genre_non_fiction
    Genre.OTHERS -> R.string.enum_genre_others
    Genre.PHILOLOGY -> R.string.enum_genre_philology
    Genre.PHILOSOPHY -> R.string.enum_genre_philosophy
    Genre.POETRY -> R.string.enum_genre_poetry
    Genre.POLITICS_SOCIAL_SCIENCES -> R.string.enum_genre_politics_social_sciences
    Genre.READING_PROMOTION -> R.string.enum_genre_reading_promotion
    Genre.RELIGION -> R.string.enum_genre_religion
    Genre.ROMANCE -> R.string.enum_genre_romance
    Genre.SCIENCE_FICTION -> R.string.enum_genre_science_fiction
    Genre.SCIENCE_NATURE -> R.string.enum_genre_science_nature
    Genre.SELF_HELP -> R.string.enum_genre_self_help
    Genre.SPIRITUALITY -> R.string.enum_genre_spirituality
    Genre.SPORTS -> R.string.enum_genre_sports
    Genre.TEACHING -> R.string.enum_genre_teaching
    Genre.THEATER -> R.string.enum_genre_theater
    Genre.THRILLER -> R.string.enum_genre_thriller
    Genre.TOPONYMY -> R.string.enum_genre_toponymy
    Genre.TRADITIONS -> R.string.enum_genre_traditions
    Genre.TRAVEL -> R.string.enum_genre_travel
    Genre.TRUE_CRIME -> R.string.enum_genre_true_crime
    Genre.YOUNG_ADULT -> R.string.enum_genre_young_adult
}

@StringRes
fun ReadingLevel.toResId(): Int = when (this) {
    ReadingLevel.CHILDREN -> R.string.enum_reading_level_children
    ReadingLevel.YOUNG_ADULT -> R.string.enum_reading_level_young_adult
    ReadingLevel.ADULT -> R.string.enum_reading_level_adult
}

@StringRes
fun Language.toResId(): Int = when (this) {
    Language.CATALAN -> R.string.enum_language_catalan
    Language.VALENCIAN -> R.string.enum_language_valencian
    Language.SPANISH -> R.string.enum_language_spanish
    Language.ENGLISH -> R.string.enum_language_english
}

@StringRes
fun Status.toResId(): Int = when (this) {
    Status.DRAFT -> R.string.enum_status_draft
    Status.PUBLISHED -> R.string.enum_status_published
    Status.OUT_OF_PRINT -> R.string.enum_status_out_of_print
    Status.DISCONTINUED -> R.string.enum_status_discontinued
}

@Composable
fun Genre.toLocalizedString(): String {
    return stringResource(toResId())
}

@Composable
fun ReadingLevel.toLocalizedString(): String {
    return stringResource(toResId())
}

@Composable
fun Language.toLocalizedString(): String {
    return stringResource(toResId())
}

@Composable
fun Status.toLocalizedString(): String {
    return stringResource(toResId())
}
