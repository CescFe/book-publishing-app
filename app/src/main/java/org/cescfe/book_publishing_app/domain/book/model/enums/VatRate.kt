package org.cescfe.book_publishing_app.domain.book.model.enums

enum class VatRate(val percentage: Int, val decimalValue: Double) {
    FOUR_PERCENT(4, 0.04),
    TEN_PERCENT(10, 0.10),
    TWENTY_ONE_PERCENT(21, 0.21);

    fun toDisplayString(): String = "$percentage%"
}
