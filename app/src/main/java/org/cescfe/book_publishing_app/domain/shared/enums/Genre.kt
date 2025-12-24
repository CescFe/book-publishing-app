package org.cescfe.book_publishing_app.domain.shared.enums

enum class Genre(val displayName: String) {
    ADVENTURE("Adventure"),
    ART_DESIGN("Art & Design"),
    AUTOBIOGRAPHY("Autobiography"),
    BIOGRAPHY("Biography"),
    BUSINESS("Business"),
    CHRISTIANITY("Christianity"),
    CHILDREN("Children"),
    COMICS("Comics"),
    COOKING("Cooking"),
    CULTURE("Culture"),
    DICTIONARIES("Dictionaries"),
    DIVERSE_TOPICS("Diverse Topics"),
    DRAMA("Drama"),
    EDUCATION("Education"),
    ESSAY("Essay"),
    ETHICS("Ethics"),
    FANTASY("Fantasy"),
    FICTION("Fiction"),
    GENERAL_INTEREST("General Interest"),
    GRAMMAR("Grammar"),
    GRAPHIC_NOVELS("Graphic Novels"),
    HEALTH_FITNESS("Health & Fitness"),
    HISTORICAL_FICTION("Historical Fiction"),
    HISTORY("History"),
    HORROR("Horror"),
    LEXICOGRAPHY("Lexicography"),
    MEMOIR("Memoir"),
    MYSTERY("Mystery"),
    NON_FICTION("Non-Fiction"),
    OTHERS("Others"),
    PHILOLOGY("Philology"),
    PHILOSOPHY("Philosophy"),
    POETRY("Poetry"),
    POLITICS_SOCIAL_SCIENCES("Politics & Social Sciences"),
    READING_PROMOTION("Reading Promotion"),
    RELIGION("Religion"),
    ROMANCE("Romance"),
    SCIENCE_FICTION("Science Fiction"),
    SCIENCE_NATURE("Science & Nature"),
    SELF_HELP("Self Help"),
    SPIRITUALITY("Spirituality"),
    SPORTS("Sports"),
    TEACHING("Teaching"),
    THEATER("Theater"),
    THRILLER("Thriller"),
    TOPONYMY("Toponymy"),
    TRADITIONS("Traditions"),
    TRAVEL("Travel"),
    TRUE_CRIME("True Crime"),
    YOUNG_ADULT("Young Adult");

    companion object {
        fun fromString(value: String): Genre? {
            return try {
                valueOf(value.uppercase().replace(" ", "_"))
            } catch (_: IllegalArgumentException) {
                null
            }
        }
    }
}
