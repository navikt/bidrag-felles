package no.nav.bidrag.transport.behandling.beregning.barnebidrag

import com.fasterxml.jackson.annotation.JsonAlias
import io.swagger.v3.oas.annotations.media.Schema
import no.nav.bidrag.domene.enums.beregning.Beregningstype
import no.nav.bidrag.domene.enums.vedtak.BeregnTil
import no.nav.bidrag.domene.enums.vedtak.Vedtakstype
import no.nav.bidrag.domene.sak.Stønadsid
import no.nav.bidrag.domene.tid.ÅrMånedsperiode
import no.nav.bidrag.transport.behandling.beregning.felles.BeregnGrunnlag
import no.nav.bidrag.transport.behandling.felles.grunnlag.GrunnlagDto
import java.math.BigDecimal
import java.time.LocalDate
import java.util.Collections.emptyList

@Schema(description = "Resultatet av en barnebidragsberegning")
data class BeregnetBarnebidragResultat(
    @Schema(description = "Periodisert liste over resultat av barnebidragsberegning")
    var beregnetBarnebidragPeriodeListe: List<ResultatPeriode> = emptyList(),
    @Schema(description = "Liste over grunnlag brukt i beregning")
    var grunnlagListe: List<GrunnlagDto> = emptyList(),
)

@Schema(description = "Resultatet av en beregning for en gitt periode - barnebidrag")
data class ResultatPeriode(
    @Schema(description = "Beregnet resultat periode") var periode: ÅrMånedsperiode,
    @Schema(description = "Beregnet resultat") var resultat: ResultatBeregning,
    @Schema(description = "Beregnet grunnlag innhold") var grunnlagsreferanseListe: List<String>,
)

@Schema(description = "Resultatet av en beregning")
data class ResultatBeregning(
    @Schema(description = "Resultat beløp") var beløp: BigDecimal?,
)

@Schema(description = "Request til BidragsberegningOrkestrator")
data class BidragsberegningOrkestratorRequest(
    @Schema(description = "Grunnlag for beregning av barnebidrag")
    val beregnGrunnlag: BeregnGrunnlag,
    @Schema(description = "Grunnlag for orkestrering av klage")
    @JsonAlias("klageOrkestratorGrunnlag")
    val omgjøringOrkestratorGrunnlag: OmgjøringOrkestratorGrunnlag? = null,
    @Schema(description = "Type beregning")
    val beregningstype: Beregningstype = Beregningstype.BIDRAG,
)

@Schema(description = "Grunnlag for orkestrering av aldersjustering")
data class OmgjøringOrkestratorGrunnlag(
    @Schema(description = "Id til stønad")
    val stønad: Stønadsid,
    @Schema(description = "Vedtaksid til påklaget vedtak")
    @JsonAlias("påklagetVedtakId")
    val omgjørVedtakId: Int,
    val manuellAldersjustering: List<OmgjøringorkestratorManuellAldersjustering> = emptyList(),
    @Schema(description = "Om behandlingen gjelder paragraf35c")
    val gjelderParagraf35c: Boolean = false,
    val gjelderKlage: Boolean = false,
    val skalInnkreves: Boolean,
    val erBeregningsperiodeLøpende: Boolean,
)

data class OmgjøringorkestratorManuellAldersjustering(
    val aldersjusteringForÅr: Int,
    val grunnlagFraVedtak: Int?,
    val grunnlagFraOmgjøringsvedtak: Boolean = false,
)

@Schema(description = "Response fra BidragsberegningOrkestrator")
data class BidragsberegningOrkestratorResponse(
    @Schema(description = "Liste over resultat av bidragsberegning")
    val resultatVedtakListe: List<ResultatVedtak> = emptyList(),
)

@Schema(description = "Resultat av bidragsberegning")
data class ResultatVedtak(
    @Schema(description = "Resultat av beregning")
    val resultat: BeregnetBarnebidragResultat,
    @Schema(description = "Er delvedtak?")
    val delvedtak: Boolean = false,
    @Schema(description = "Er klagevedtak eller omgjøringsvedtak")
    @JsonAlias("klagevedtak")
    val omgjøringsvedtak: Boolean = false,
    val beregnet: Boolean = false,
    val vedtakstype: Vedtakstype,
    val beregnetFraDato: LocalDate = resultat.beregnetBarnebidragPeriodeListe.minOf { it.periode.fom }.atDay(1),
) {
    val endeligVedtak get() = !delvedtak && !omgjøringsvedtak
}
