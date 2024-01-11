@file:Suppress("unused")

package no.nav.bidrag.domene.ident

import jakarta.persistence.AttributeConverter
import no.nav.bidrag.domene.felles.Verdiobjekt
import no.nav.bidrag.domene.util.trimToNull
import org.springframework.core.convert.converter.Converter

class Organisasjonsnummer(override val verdi: String) : Verdiobjekt<String>() {

    override fun gyldig(): Boolean {
        if (verdi.length != 9 || verdi.toLongOrNull() == null) {
            return false
        }

        val siffer = verdi.chunked(1).map { it.toInt() }
        val vekting = intArrayOf(3, 2, 7, 6, 5, 4, 3, 2)

        val kontrollMod = 11 - (0..7).sumOf { vekting[it] * siffer[it] } % 11
        val kontrollsiffer = siffer[8]

        return gyldigKontrollSiffer(kontrollMod, kontrollsiffer)
    }

    private fun gyldigKontrollSiffer(kontrollMod: Int, kontrollsiffer: Int): Boolean {
        if (kontrollMod == kontrollsiffer) {
            return true
        }
        if (kontrollMod == 11 && kontrollsiffer == 0) {
            return true
        }
        return false
    }
}

class OrganisasjonsnummerReadingConverter : Converter<String, Organisasjonsnummer> {
    override fun convert(source: String) = source.trimToNull()?.let { Organisasjonsnummer(source) }
}

class OrganisasjonsnummerWritingConverter : Converter<Organisasjonsnummer, String> {

    override fun convert(source: Organisasjonsnummer) = source.verdi.trimToNull()
}

class OrganisasjonsnummerConverter : AttributeConverter<Organisasjonsnummer, String> {
    override fun convertToEntityAttribute(source: String?) = source?.trimToNull()?.let { Organisasjonsnummer(source) }
    override fun convertToDatabaseColumn(source: Organisasjonsnummer?) = source?.verdi.trimToNull()
}
