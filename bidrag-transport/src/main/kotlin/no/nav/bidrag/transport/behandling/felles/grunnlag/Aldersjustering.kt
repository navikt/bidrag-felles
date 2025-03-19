package no.nav.bidrag.transport.behandling.felles.grunnlag

import no.nav.bidrag.domene.enums.barnetilsyn.Skolealder
import no.nav.bidrag.domene.enums.barnetilsyn.Tilsynstype
import no.nav.bidrag.domene.enums.beregning.Samværsklasse
import no.nav.bidrag.domene.tid.ÅrMånedsperiode
import java.math.BigDecimal

data class KopiDelberegningUnderholdskostnad(
    override val periode: ÅrMånedsperiode,
    override val fraVedtakId: Long,
    val nettoTilsynsutgift: BigDecimal?,
) : GrunnlagPeriodeInnholdKopi

data class KopiDelberegningBidragspliktigesAndel(
    override val periode: ÅrMånedsperiode,
    override val fraVedtakId: Long,
    val endeligAndelFaktor: BigDecimal,
) : GrunnlagPeriodeInnholdKopi

data class KopiBarnetilsynMedStønadPeriode(
    override val periode: ÅrMånedsperiode,
    override val fraVedtakId: Long,
    val tilsynstype: Tilsynstype,
    val skolealder: Skolealder,
) : GrunnlagPeriodeInnholdKopi

data class KopiSamværsperiodeGrunnlag(
    override val periode: ÅrMånedsperiode,
    override val fraVedtakId: Long,
    val samværsklasse: Samværsklasse,
) : GrunnlagPeriodeInnholdKopi
