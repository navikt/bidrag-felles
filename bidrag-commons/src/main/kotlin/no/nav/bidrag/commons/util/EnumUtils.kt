package no.nav.bidrag.commons.util

object EnumUtils {
    inline fun <reified T : Enum<T>> erAvEnumType(value: String): Boolean {
        return try {
            enumValueOf<T>(value)
            true
        } catch (e: IllegalArgumentException) {
            false
        }
    }
}
