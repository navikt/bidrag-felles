package no.nav.bidrag.transport.behandling.beregning.felles

import no.nav.bidrag.domene.enums.vedtak.Stønadstype
import no.nav.bidrag.domene.ident.Personident
import java.time.LocalDate

data class BidragBeregningRequestDto(
    val hentBeregningerFor: List<HentBidragBeregning>,
) {
    data class HentBidragBeregning(
        val saksnummer: String,
        val personidentBarn: Personident,
        val datoSøknad: LocalDate,
        val stønadstype: Stønadstype,
    )
}
