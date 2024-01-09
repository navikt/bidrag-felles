package no.nav.bidrag.domene.util

fun String?.trimToNull(): String? {
    return this?.trim()?.ifBlank { null }
}
