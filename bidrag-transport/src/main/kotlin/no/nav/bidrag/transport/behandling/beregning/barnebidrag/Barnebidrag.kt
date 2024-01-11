package no.nav.bidrag.transport.behandling.beregning.barnebidrag

import io.swagger.v3.oas.annotations.media.Schema
import no.nav.bidrag.domene.enums.beregning.ResultatkodeBarnebidrag
import no.nav.bidrag.domene.tid.ÅrMånedsperiode
import no.nav.bidrag.transport.behandling.beregning.felles.Grunnlag
import java.math.BigDecimal

// Barnebidrag
@Schema(description = "Resultatet av en barnebidragsberegning")
data class BeregnetTotalBarnebidragResultat(
    @Schema(description = "Periodisert liste over resultat av barnebidragsberegning") var beregnetBarnebidragPeriodeListe: List<ResultatPeriode> = emptyList(),
    @Schema(description = "Liste over grunnlag brukt i beregning") var grunnlagListe: List<Grunnlag> = emptyList(),
)

@Schema(description = "Resultatet av en beregning for en gitt periode - barnebidrag")
data class ResultatPeriode(
    @Schema(description = "Søknadsbarn") var barn: Int = 0,
    @Schema(description = "Beregnet resultat periode") var periode: ÅrMånedsperiode,
    @Schema(description = "Beregnet resultat innhold") var resultat: ResultatBeregning,
    @Schema(description = "Beregnet grunnlag innhold") var grunnlagsreferanseListe: List<String>,
)

@Schema(description = "Resultatet av en beregning")
data class ResultatBeregning(
    @Schema(description = "Resultat beløp") var belop: BigDecimal,
    @Schema(description = "Resultat kode") var kode: ResultatkodeBarnebidrag,
)
