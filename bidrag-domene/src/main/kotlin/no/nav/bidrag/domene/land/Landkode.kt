@file:Suppress("unused")

package no.nav.bidrag.domene.land

import jakarta.persistence.AttributeConverter
import no.nav.bidrag.domene.felles.Verdiobjekt
import no.nav.bidrag.domene.util.trimToNull
import org.springframework.core.convert.converter.Converter

class Landkode(override val verdi: String) : Verdiobjekt<String>() {

    fun alfa2() = verdi.length == 2

    fun alfa3() = verdi.length == 3

    override fun gyldig() = verdi.length in setOf(2, 3)
}

class LandkodeReadingConverter : Converter<String, Landkode> {
    override fun convert(source: String) = source.trimToNull()?.let { Landkode(source) }
}

class LandkodeWritingConverter : Converter<Landkode, String> {
    override fun convert(source: Landkode) = source.verdi.trimToNull()
}

class LandkodeConverter : AttributeConverter<Landkode, String> {
    override fun convertToEntityAttribute(source: String?) = source?.trimToNull()?.let { Landkode(source) }
    override fun convertToDatabaseColumn(source: Landkode?) = source?.verdi.trimToNull()
}
