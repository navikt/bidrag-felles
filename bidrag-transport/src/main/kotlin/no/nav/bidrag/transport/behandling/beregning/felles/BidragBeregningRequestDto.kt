package no.nav.bidrag.transport.behandling.beregning.felles

import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import no.nav.bidrag.domene.enums.vedtak.Stønadstype
import no.nav.bidrag.domene.ident.Personident
import java.time.LocalDate

data class BidragBeregningRequestDto(
    @field:Valid
    val hentBeregningerFor: List<HentBidragBeregning>,
) {
    data class HentBidragBeregning(
        @field:NotBlank(message = "Saksnummer kan ikke være blank")
        @field:Size(max = 7, min = 7, message = "Saksnummer må bestå av 7 tegn")
        val saksnummer: String,
        @field:GyldigPersonidentLengde
        val personidentBarn: Personident,
        val datoSøknad: LocalDate,
        val stønadstype: Stønadstype,
    )
}
