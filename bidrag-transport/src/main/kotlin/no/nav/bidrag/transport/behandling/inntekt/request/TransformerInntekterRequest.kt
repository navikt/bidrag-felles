package no.nav.bidrag.transport.behandling.inntekt.request

import io.swagger.v3.oas.annotations.media.Schema
import no.nav.bidrag.transport.behandling.grunnlag.response.SkattegrunnlagspostDto
import java.math.BigDecimal
import java.time.LocalDate

data class TransformerInntekterRequest(
    @Schema(description = "Dato ainntektene er hentet i bidrag-grunnlag, kommer fra hentetTidspunkt i responsen fra bidrag-grunnlag")
    val ainntektHentetDato: LocalDate,
    @Schema(description = "Periodisert liste over inntekter fra Ainntekt")
    val ainntektsposter: List<Ainntektspost> = emptyList(),
    @Schema(description = "Periodisert liste over inntekter fra Sigrun")
    val skattegrunnlagsliste: List<SkattegrunnlagForLigningsår> = emptyList(),
    @Schema(description = "Periodisert liste over kontantstøtte")
    val kontantstøtteliste: List<Kontantstøtte> = emptyList(),
    @Schema(description = "Periodisert liste over utvidet barnetrygd")
    val utvidetBarnetrygdliste: List<UtvidetBarnetrygd> = emptyList(),
    @Schema(description = "Periodisert liste over småbarnstillegg")
    val småbarnstilleggliste: List<Småbarnstillegg> = emptyList(),
    @Schema(description = "Periodisert liste over barnetillegg")
    val barnetilleggsliste: List<Barnetillegg> = emptyList(),
)

data class SkattegrunnlagForLigningsår(
    @Schema(description = "Årstall skattegrunnlaget gjelder for")
    val ligningsår: Int,
    @Schema(description = "Poster med skattegrunnlag")
    val skattegrunnlagsposter: List<SkattegrunnlagspostDto>,
)

data class Ainntektspost(
    @Schema(description = "Perioden innteksposten er utbetalt YYYYMM")
    val utbetalingsperiode: String?,
    @Schema(description = "Fra-dato for opptjening")
    val opptjeningsperiodeFra: LocalDate?,
    @Schema(description = "Til-dato for opptjening")
    val opptjeningsperiodeTil: LocalDate?,
    @Schema(description = "Beskrivelse av inntekt")
    val beskrivelse: String?,
    @Schema(description = "Beløp")
    val beløp: BigDecimal,
)

data class Kontantstøtte(
    @Schema(description = "Periode fra-dato")
    val periodeFra: LocalDate,
    @Schema(description = "Periode til-dato")
    val periodeTil: LocalDate?,
    @Schema(description = "Beløp kontantstøtte")
    val beløp: BigDecimal,
    @Schema(description = "Id til barnet kontantstøtten mottas for")
    val barnPersonId: String,
)

data class UtvidetBarnetrygd(
    @Schema(description = "Periode fra-dato")
    val periodeFra: LocalDate,
    @Schema(description = "Periode til-dato")
    val periodeTil: LocalDate?,
    @Schema(description = "Beløp utvidet barnetrygd")
    val beløp: BigDecimal,
)

data class Småbarnstillegg(
    @Schema(description = "Periode fra-dato")
    val periodeFra: LocalDate,
    @Schema(description = "Periode til-dato")
    val periodeTil: LocalDate?,
    @Schema(description = "Beløp småbarnstillegg")
    val beløp: BigDecimal,
)

data class Barnetillegg(
    @Schema(description = "Periode fra-dato")
    val periodeFra: LocalDate,
    @Schema(description = "Periode til-dato")
    val periodeTil: LocalDate?,
    @Schema(description = "Beløp barnetillegg")
    val beløp: BigDecimal,
    @Schema(description = "Id til barnet barnetillegget mottas for")
    val barnPersonId: String,
)
