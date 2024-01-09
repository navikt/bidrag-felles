@file:Suppress("unused")

package no.nav.bidrag.domene.ident

import jakarta.persistence.AttributeConverter
import no.nav.bidrag.domene.felles.Verdiobjekt
import no.nav.bidrag.domene.util.trimToNull
import org.springframework.core.convert.converter.Converter

class SamhandlerId(override val verdi: String) : Verdiobjekt<String>() {

    override fun gyldig(): Boolean {
        return verdi.matches(SAMHANDLER_ID_REGEX)
    }

    companion object {
        private val SAMHANDLER_ID_REGEX = Regex("^[89]\\d{10}$")
    }
}

class SamhandlerIdReadingConverter : Converter<String, SamhandlerId> {
    override fun convert(source: String) = source.trimToNull()?.let { SamhandlerId(source) }
}

class SamhandlerIdWritingConverter : Converter<SamhandlerId, String> {

    override fun convert(source: SamhandlerId) = source.verdi.trimToNull()
}

class SamhandlerIdConverter : AttributeConverter<SamhandlerId, String> {
    override fun convertToEntityAttribute(source: String?) = source?.trimToNull()?.let { SamhandlerId(source) }
    override fun convertToDatabaseColumn(source: SamhandlerId?) = source?.verdi.trimToNull()
}
