package no.nav.bidrag.transport.behandling.felles.grunnlag

import io.swagger.v3.oas.annotations.media.Schema
import no.nav.bidrag.domene.enums.beregning.Samværsklasse
import no.nav.bidrag.domene.enums.privatavtale.PrivatAvtaleType
import no.nav.bidrag.domene.enums.sak.Sakskategori
import no.nav.bidrag.domene.enums.samhandler.Valutakode
import no.nav.bidrag.domene.enums.vedtak.Stønadstype
import no.nav.bidrag.domene.tid.ÅrMånedsperiode
import java.math.BigDecimal
import java.time.LocalDate

@Schema(description = "Privat avtale i bidragssaken")
data class PrivatAvtaleGrunnlag(
    val avtaleInngåttDato: LocalDate,
    val avtaleType: PrivatAvtaleType = PrivatAvtaleType.PRIVAT_AVTALE,
    val skalIndeksreguleres: Boolean,
    val utlandsbidrag: Boolean = false,
) : GrunnlagInnhold

data class PrivatAvtalePeriodeGrunnlag(
    override val periode: ÅrMånedsperiode,
    val beløp: BigDecimal,
    val samværsklasse: Samværsklasse? = null,
    val valutakode: Valutakode? = null,
    override val manueltRegistrert: Boolean = true,
) : GrunnlagPeriodeInnhold

@Deprecated(
    "Skal erstattes av DelberegningPrivatAvtaleV2",
    ReplaceWith("DelberegningPrivatAvtaleV2"),
)
data class DelberegningPrivatAvtale(
    val nesteIndeksreguleringsår: BigDecimal? = null,
    val perioder: List<DelberegningPrivatAvtalePeriode>,
) : DelberegningUtenPeriode

@Deprecated(
    "Skal erstattes av DelberegningPrivatAvtalePeriodeV2",
    ReplaceWith("DelberegningPrivatAvtalePeriodeV2"),
)
data class DelberegningPrivatAvtalePeriode(
    override val periode: ÅrMånedsperiode,
    val indeksreguleringFaktor: BigDecimal? = null,
    val beløp: BigDecimal,
) : Delberegning

data class DelberegningPrivatAvtaleV2(
    val nesteIndeksreguleringsår: BigDecimal? = null,
    val valutakode: Valutakode = Valutakode.NOK,
    val stønadstype: Stønadstype = Stønadstype.BIDRAG,
    val sakskategori: Sakskategori = Sakskategori.N,
) : DelberegningUtenPeriode

data class DelberegningPrivatAvtalePeriodeV2(
    override val periode: ÅrMånedsperiode,
    val indeksreguleringFaktor: BigDecimal? = null,
    val indeksregulertBeløp: BigDecimal,
    val samværsklasse: Samværsklasse? = null,
    val samværsfradrag: BigDecimal,
    val bidragTilFordeling: BigDecimal,
) : Delberegning
