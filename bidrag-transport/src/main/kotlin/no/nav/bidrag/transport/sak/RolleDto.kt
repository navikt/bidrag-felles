package no.nav.bidrag.transport.sak

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.annotations.media.Schema
import no.nav.bidrag.domene.enums.rolle.Rolletype
import no.nav.bidrag.domene.ident.Personident
import no.nav.bidrag.domene.ident.ReellMottager
import no.nav.bidrag.domene.ident.SamhandlerId

data class RolleDto(
    @JsonProperty("fodselsnummer")
    val fødselsnummer: Personident? = null,
    @Schema(description = "Kode for rolletype tilsvarende kodene i T_KODE_ROLLETYPE.")
    val type: Rolletype,
    @Deprecated("Internlogisk felt, burde ikke brukes utenfor back end.")
    val objektnummer: String? = null,
    val reellMottager: ReellMottager? = null,
    val mottagerErVerge: Boolean = false,
    val samhandlerIdent: SamhandlerId? = null,
    @Deprecated("Bruk fødselsnummer", ReplaceWith("fødselsnummer"))
    val foedselsnummer: Personident? = fødselsnummer,
    @Deprecated("Bruk rolletype", ReplaceWith("type"))
    val rolleType: Rolletype = type,
) {
    fun valider() {
        require(reellMottager == null || type == Rolletype.BARN) { "Reell mottager kan kun opprettes for barn." }
    }

    fun rmErSamhandlerId() = reellMottager?.erSamhandlerId() ?: false

    fun rmSamhandlerId() = reellMottager?.samhandlerId()
    fun rmFødselsnummer() = reellMottager?.personIdent()
}
