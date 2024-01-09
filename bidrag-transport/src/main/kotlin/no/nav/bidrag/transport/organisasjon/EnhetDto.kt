package no.nav.bidrag.transport.organisasjon

import no.nav.bidrag.domene.enums.diverse.Enhetsstatus
import no.nav.bidrag.domene.organisasjon.Enhetsnummer

data class EnhetDto(
    val nummer: Enhetsnummer,
    val navn: String? = null,
    @Deprecated("bruk nummer", ReplaceWith("nummer"))
    val enhetIdent: Enhetsnummer = nummer,
    @Deprecated("bruk navn", ReplaceWith("navn"))
    val enhetNavn: String? = navn,
    val status: Enhetsstatus = Enhetsstatus.AKTIV,
)

data class EnhetKontaktinfoDto(
    val nummer: Enhetsnummer,
    val navn: String? = null,
    @Deprecated("bruk nummer", ReplaceWith("nummer"))
    val enhetIdent: Enhetsnummer = nummer,
    @Deprecated("bruk navn", ReplaceWith("navn"))
    val enhetNavn: String? = navn,
    val telefonnummer: String? = null,
    val postadresse: EnhetspostadresseDto? = null,

) {

    companion object {
        fun medStandardadresse(enhetsnummer: Enhetsnummer) = EnhetKontaktinfoDto(
            nummer = enhetsnummer,
            navn = "NAV Familie- og pensjonsytelser",
            telefonnummer = "55553333",
            postadresse = EnhetspostadresseDto(
                postnummer = "0603",
                adresselinje1 = "Postboks 6215 Etterstad",
                poststed = "Oslo",
                land = "Norway",
                kommunenr = "0301",
            ),
        )
    }
}

data class EnhetspostadresseDto(
    val postnummer: String? = null,
    val adresselinje1: String? = null,
    val adresselinje2: String? = null,
    val poststed: String? = null,
    val land: String? = null,
    val kommunenr: String? = null,
)
