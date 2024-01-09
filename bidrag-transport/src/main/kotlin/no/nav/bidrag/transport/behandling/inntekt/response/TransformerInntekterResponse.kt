package no.nav.bidrag.transport.behandling.inntekt.response

import io.swagger.v3.oas.annotations.media.Schema
import no.nav.bidrag.domene.enums.inntekt.Inntektsrapportering
import no.nav.bidrag.domene.tid.ÅrMånedsperiode
import java.math.BigDecimal
import java.time.YearMonth

data class TransformerInntekterResponse(
    @Schema(description = "Dato + commit hash", example = "20230705081501_68e71c7")
    val versjon: String = "",

    @Schema(description = "Liste over summerte månedsinntekter (Ainntekt ++))")
    val summertMånedsinntektListe: List<SummertMånedsinntekt> = emptyList(),

    @Schema(description = "Liste over summerte årsinntekter (Ainntekt + Sigrun ++)")
    val summertÅrsinntektListe: List<SummertÅrsinntekt> = emptyList(),
)

data class SummertMånedsinntekt(
    @Schema(description = "Perioden inntekten gjelder for (format YYYY-MM)", example = "2023-01", type = "String", pattern = "YYYY-MM")
    val gjelderÅrMåned: YearMonth,

    @Schema(description = "Summert inntekt for måneden", example = "50000")
    val sumInntekt: BigDecimal,

    @Schema(description = "Liste over inntektsposter som utgjør grunnlaget for summert inntekt")
    val inntektPostListe: List<InntektPost>,
)

data class SummertÅrsinntekt(
    @Schema(description = "Type inntektrapportering", example = "AINNTEKT")
    val inntektRapportering: Inntektsrapportering,

    @Schema(description = "Visningsnavn for inntekt", example = "Lønn og trekk 2022")
    val visningsnavn: String,

    @Schema(description = "Referanse", example = "Referanse")
    val referanse: String,

    @Schema(description = "Summert inntekt for perioden, omgjort til årsinntekt", example = "600000")
    val sumInntekt: BigDecimal,

    @Schema(description = "Perioden inntekten gjelder for (fom-til)")
    val periode: ÅrMånedsperiode,

    @Schema(description = "Id til barnet kontantstøtten mottas for, brukes kun for kontantstøtte", example = "12345678910")
    val gjelderBarnPersonId: String = "",

    @Schema(description = "Liste over inntektsposter (generisk, avhengig av type) som utgjør grunnlaget for summert inntekt")
    val inntektPostListe: List<InntektPost>,
)
data class InntektPost(
    @Schema(description = "Kode for inntektspost", example = "bonus")
    val kode: String,

    @Schema(description = "Visningsnavn for kode", example = "Bonus")
    val visningsnavn: String,

    @Schema(description = "Beløp som utgør inntektsposten", example = "60000")
    val beløp: BigDecimal,
)
