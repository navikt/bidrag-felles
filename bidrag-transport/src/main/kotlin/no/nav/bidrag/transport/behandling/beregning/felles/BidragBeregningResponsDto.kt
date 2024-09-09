package no.nav.bidrag.transport.behandling.beregning.felles

import no.nav.bidrag.domene.enums.beregning.Samværsklasse
import no.nav.bidrag.domene.enums.vedtak.Stønadstype
import no.nav.bidrag.domene.ident.Personident
import java.math.BigDecimal
import java.time.LocalDate

data class BidragBeregningResponsDto(
    val beregningListe: List<BidragBeregning>,
) {
    data class BidragBeregning(
        val saksnummer: String,
        val personidentBarn: Personident,
        val gjelderFom: LocalDate,
        val datoSøknad: LocalDate,
        val beregnetBeløp: BigDecimal,
        val faktiskBeløp: BigDecimal,
        val beløpSamvær: BigDecimal,
        val stønadstype: Stønadstype,
        val samværsklasse: Samværsklasse? = null,
    )
}
