@file:Suppress("unused")

package no.nav.bidrag.domene.land

import jakarta.persistence.AttributeConverter
import no.nav.bidrag.domene.felles.Verdiobjekt
import no.nav.bidrag.domene.util.trimToNull
import org.springframework.core.convert.converter.Converter

class Landkode2(override val verdi: String) : Verdiobjekt<String>() {
    override fun gyldig() = verdi.length == 2
}

class Landkode2ReadingConverter : Converter<String, Landkode2> {
    override fun convert(source: String) = source.trimToNull()?.let { Landkode2(source) }
}

class Landkode2WritingConverter : Converter<Landkode2, String> {
    override fun convert(source: Landkode2) = source.verdi
}

class Landkode2Converter : AttributeConverter<Landkode2, String> {
    override fun convertToEntityAttribute(source: String?) = source?.trimToNull()?.let { Landkode2(source) }
    override fun convertToDatabaseColumn(source: Landkode2?) = source?.verdi.trimToNull()
}
