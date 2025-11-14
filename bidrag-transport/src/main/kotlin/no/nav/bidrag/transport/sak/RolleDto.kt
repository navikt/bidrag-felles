package no.nav.bidrag.transport.sak

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.annotations.media.Schema
import no.nav.bidrag.domene.enums.rolle.Rolletype
import no.nav.bidrag.domene.ident.Personident
import no.nav.bidrag.domene.ident.ReellMottaker

data class RolleDto(
    @field:Schema(
        description = "Personens fødselsnummer. Påkrevd for alle",
    )
    @field:JsonProperty("fodselsnummer")
    val fødselsnummer: Personident? = null,
    @field:Schema(
        description =
            "Kode for rolletype tilsvarende kodene i T_KODE_ROLLETYPE. " +
                "Gyldige verdier er f.eks. BM (bidragsmottaker), BP (bidragspliktig) og BA (barn).",
    )
    val type: Rolletype,
    @field:Schema(
        description =
            "Internt felt som identifiserer objektet i fagsystemet. " +
                "Brukes ikke eksternt og skal normalt ikke sendes inn.",
        deprecated = true,
    )
    @Deprecated("Internlogisk felt, burde ikke brukes utenfor back end.")
    val objektnummer: String? = null,
    @field:Schema(
        description =
            "Tidligere brukt felt for reell mottaker. " +
                "Erstattes av 'reellMottaker'.",
        deprecated = true,
    )
    @Deprecated("Bruk heller reellMottaker", replaceWith = ReplaceWith("reellMottaker"))
    val reellMottager: ReellMottaker? = null,
    @field:Schema(
        description =
            "Reell mottaker (RM) for barnet. " +
                "Kan kun registreres for barn (BA), og kan representere både person eller samhandler (organisasjon/verge). " +
                "Dette feltet brukes i stedet for samhandlerIdent når bidrag skal utbetales til annen part enn barnets BM.",
    )
    val reellMottaker: ReellMottakerDto? = null,
    @field:Schema(
        description =
            "Angir om mottaker samtidig er verge for barnet. " +
                "Settes til true dersom mottaker også er verge.",
    )
    val mottagerErVerge: Boolean = false,
    @field:Schema(
        description = "Tidligere brukt felt for fødselsnummer. Erstattes av 'fødselsnummer'.",
        deprecated = true,
    )
    @Deprecated("Bruk fødselsnummer", ReplaceWith("fødselsnummer"))
    val foedselsnummer: Personident? = fødselsnummer,
    @field:Schema(
        description = "Tidligere brukt felt for rolletype. Erstattes av 'type'.",
        deprecated = true,
    )
    @Deprecated("Bruk rolletype", ReplaceWith("type"))
    val rolleType: Rolletype = type,
) {
    fun valider() {
        fødselsnummer?.let {
            require(it.harVerdi()) { "Fødselsnummer kan ikke være tom streng." }
            require(it.gyldig()) { "Ugyldig fødselsnummer for rolle av type $type." }
        }

        require(type == Rolletype.BARN || !harRM()) {
            "Reell mottaker (RM) kan kun registreres på barn (BA)."
        }

        if (type == Rolletype.BARN) {
            val alder =
                fødselsnummer?.let { fnr ->
                    runCatching { fnr.beregnAlder() }
                        .onFailure { throw IllegalArgumentException("Kunne ikke beregne alder for barn med fødselsnummer ${fnr.verdi}.") }
                        .getOrNull()
                }

            if (alder != null && alder >= 18) {
                require(harRM()) {
                    "Hvis barnet er myndig, må reell mottaker (RM) være satt."
                }
            }
        }
    }

    fun erBMmedFnr() = type == Rolletype.BIDRAGSMOTTAKER && (fødselsnummer?.harVerdi() == true)

    fun rmErSamhandlerId() = reellMottaker?.ident?.erSamhandlerId() ?: reellMottager?.erSamhandlerId() ?: false

    fun rmErVerge() = reellMottaker?.verge ?: false

    fun rmSamhandlerId() = reellMottaker?.ident?.samhandlerId() ?: reellMottager?.samhandlerId()

    fun rmFødselsnummer() = reellMottaker?.ident?.personIdent() ?: reellMottager?.personIdent()

    fun harRM() = (rmFødselsnummer() != null || rmSamhandlerId() != null)
}

data class ReellMottakerDto(
    val ident: ReellMottaker,
    val verge: Boolean = false,
)
