@file:Suppress("unused")

package no.nav.bidrag.domene.ident

import jakarta.persistence.AttributeConverter
import no.nav.bidrag.domene.felles.Verdiobjekt
import no.nav.bidrag.domene.util.trimToNull
import org.springframework.core.convert.converter.Converter

class Ident(override val verdi: String) : Verdiobjekt<String>() {

    override fun gyldig(): Boolean {
        return Organisasjonsnummer(verdi).gyldig() ||
            Personident(verdi).gyldig() ||
            SamhandlerId(verdi).gyldig()
    }

    fun erOrganisasjonsnummer() = Organisasjonsnummer(verdi).gyldig()

    fun erPersonIdent() = Personident(verdi).gyldig()

    fun erSamhandlerId() = SamhandlerId(verdi).gyldig()

    fun type() =
        when {
            Organisasjonsnummer(verdi).gyldig() -> Identtype.Organisasjonsnummer
            Personident(verdi).gyldig() -> Identtype.PersonIdent
            SamhandlerId(verdi).gyldig() -> Identtype.SamhandlerId
            else -> Identtype.Ukjent
        }

    override fun toString(): String {
        return if (erPersonIdent()) {
            verdi.mapIndexed { index, c -> if (index % 2 == 0) c else '*' }.joinToString("")
        } else {
            super.toString()
        }
    }
}

enum class Identtype {
    PersonIdent, // Fødselsnummer, D-nummer, Nav-syntetisk, Skatt-syntetisk
    Organisasjonsnummer,
    SamhandlerId,
    Ukjent,
}

class IdentReadingConverter : Converter<String, Ident> {
    override fun convert(source: String) = source.trimToNull()?.let { Ident(source) }
}

class IdentWritingConverter : Converter<Ident, String> {

    override fun convert(source: Ident) = source.verdi.trimToNull()
}

class IdentConverter : AttributeConverter<Ident, String> {
    override fun convertToEntityAttribute(source: String?) = source?.trimToNull()?.let { Ident(source) }
    override fun convertToDatabaseColumn(source: Ident?) = source?.verdi.trimToNull()
}
