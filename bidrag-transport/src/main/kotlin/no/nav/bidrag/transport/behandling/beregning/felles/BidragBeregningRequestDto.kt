package no.nav.bidrag.transport.behandling.beregning.felles

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import no.nav.bidrag.domene.enums.vedtak.Stønadstype
import no.nav.bidrag.domene.ident.Personident
import java.time.LocalDate

data class BidragBeregningRequestDto(
    val hentBeregningerFor: List<HentBidragBeregning>,
) {
    data class HentBidragBeregning(
        @field:NotBlank(message = "Saksnummer kan ikke være blank")
        @field:Size(max = 7, min = 7, message = "Saksnummer skal ha sju tegn")
        val saksnummer: String,
        @field:NotBlank(message = "personidentBarn kan ikke være blank")
        @field:Size(max = 11, message = "Personident kan ha maks 11 tegn")
        val personidentBarn: Personident,
        val datoSøknad: LocalDate,
        val stønadstype: Stønadstype,
    )
}
