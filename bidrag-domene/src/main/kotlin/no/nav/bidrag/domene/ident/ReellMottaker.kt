@file:Suppress("unused")

package no.nav.bidrag.domene.ident

import jakarta.persistence.AttributeConverter
import no.nav.bidrag.domene.felles.Verdiobjekt
import no.nav.bidrag.domene.util.trimToNull
import org.springframework.core.convert.converter.Converter

class ReellMottaker(
    override val verdi: String,
) : Verdiobjekt<String>() {
    override fun gyldig(): Boolean = Personident(verdi).gyldig() || SamhandlerId(verdi).gyldig()

    fun erPersonIdent() = Personident(verdi).gyldig()

    fun erSamhandlerId() = SamhandlerId(verdi).gyldig()

    fun personIdent() = if (erPersonIdent()) Personident(verdi) else null

    fun samhandlerId() = if (erSamhandlerId()) SamhandlerId(verdi) else null
}

class ReellMottagerReadingConverter : Converter<String, ReellMottaker> {
    override fun convert(source: String) = source.trimToNull()?.let { ReellMottaker(source) }
}

class ReellMottagerWritingConverter : Converter<ReellMottaker, String> {
    override fun convert(source: ReellMottaker) = source.verdi.trimToNull()
}

class ReellMottagerConverter : AttributeConverter<ReellMottaker, String> {
    override fun convertToEntityAttribute(source: String?) = source?.trimToNull()?.let { ReellMottaker(source) }

    override fun convertToDatabaseColumn(source: ReellMottaker?) = source?.verdi.trimToNull()
}
