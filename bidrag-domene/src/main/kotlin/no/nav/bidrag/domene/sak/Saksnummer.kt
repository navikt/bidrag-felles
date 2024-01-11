@file:Suppress("unused")

package no.nav.bidrag.domene.sak

import jakarta.persistence.AttributeConverter
import no.nav.bidrag.domene.felles.Verdiobjekt
import no.nav.bidrag.domene.util.trimToNull
import org.springframework.core.convert.converter.Converter

class Saksnummer(override val verdi: String) : Verdiobjekt<String>() {
    override fun gyldig() = verdi.matches(SEVEN_DIGITS_REGEX)

    companion object {
        private val SEVEN_DIGITS_REGEX = Regex("^\\d{7}$")
    }
}

class SaksnummerReadingConverter : Converter<String, Saksnummer> {
    override fun convert(source: String) = source.trimToNull()?.let { Saksnummer(source) }
}

class SaksnummerWritingConverter : Converter<Saksnummer, String> {
    override fun convert(source: Saksnummer) = source.verdi.trimToNull()
}

class SaksnummerConverter : AttributeConverter<Saksnummer, String> {
    override fun convertToEntityAttribute(source: String?) = source?.trimToNull()?.let { Saksnummer(source) }
    override fun convertToDatabaseColumn(source: Saksnummer?) = source?.verdi.trimToNull()
}
