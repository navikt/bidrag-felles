package no.nav.bidrag.transport.behandling.beregning.felles

import no.nav.bidrag.domene.enums.beregning.Samværsklasse
import no.nav.bidrag.domene.ident.Personident
import no.nav.bidrag.domene.tid.Datoperiode
import java.math.BigDecimal

data class BidragBeregningResponsDto(
    val beregningListe: List<BidragBeregning>,
) {
    data class BidragBeregning(
        val saksnummer: String,
        val personidentBarn: Personident,
        val periode: Datoperiode,
        val beregnetBeløp: BigDecimal,
        val faktiskBeløp: BigDecimal,
        val beløpSamvær: BigDecimal,
        val samværsklasse: Samværsklasse,
    )
}
