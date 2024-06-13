package no.nav.bidrag.transport.behandling.beregning.særtilskudd

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer
import io.swagger.v3.oas.annotations.media.Schema
import no.nav.bidrag.domene.enums.beregning.Resultatkode
import no.nav.bidrag.domene.tid.ÅrMånedsperiode
import no.nav.bidrag.transport.behandling.felles.grunnlag.GrunnlagDto
import java.math.BigDecimal
import java.time.LocalDate

// Grunnlag
open class BasePeriode(
    datoFom: LocalDate,
    datoTil: LocalDate,
) {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @JsonDeserialize(using = LocalDateDeserializer::class)
    @JsonSerialize(using = LocalDateSerializer::class)
    val datoFom: LocalDate? = datoFom

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @JsonDeserialize(using = LocalDateDeserializer::class)
    @JsonSerialize(using = LocalDateSerializer::class)
    val datoTil: LocalDate? = datoTil
}

// Resultat
data class BeregnetSærtilskuddResultat(
    @Schema(description = "Periodisert liste over resultat av særtilskuddsberegning")
    var beregnetSærtilskuddPeriodeListe: List<ResultatPeriode> = emptyList(),
    @Schema(description = "Liste over grunnlag brukt i beregning") var grunnlagListe: List<GrunnlagDto> = emptyList(),
)

@Schema(description = "Resultatet av en beregning for en gitt periode")
data class ResultatPeriode(
    @Schema(description = "Søknadsbarn") var barn: Int = 0,
    @Schema(description = "Beregnet resultat periode") var periode: ÅrMånedsperiode,
    @Schema(description = "Beregnet resultat innhold") var resultat: ResultatBeregning,
    @Schema(description = "Beregnet grunnlag innhold") var grunnlagsreferanseListe: List<String>,
)

@Schema(description = "Resultatet av en beregning")
data class ResultatBeregning(
    @Schema(description = "Resultat beløp") var belop: BigDecimal,
    @Schema(description = "Resultat kode") var resultatkode: Resultatkode,
)

// Resultat
class BidragsevneResultatPeriode(
    datoFom: LocalDate,
    datoTil: LocalDate,
    val beløp: BigDecimal,
    val grunnlagsreferanseListe: List<String>,
) : BasePeriode(datoFom, datoTil)

class BPsAndelSærtilskuddResultatPeriode(
    datoFom: LocalDate,
    datoTil: LocalDate,
    val beløp: BigDecimal,
    val prosent: BigDecimal,
    val selvforsørget: Boolean,
    val grunnlagsreferanseListe: List<String>,
) : BasePeriode(datoFom, datoTil)

class SamværsfradragResultatPeriode(
    datoFom: LocalDate,
    datoTil: LocalDate,
    val beløp: BigDecimal,
    val barn: Int,
    val grunnlagsreferanseListe: List<String>,
) : BasePeriode(datoFom, datoTil)

class SjablonResultatPeriode(
    datoFom: LocalDate,
    datoTil: LocalDate,
    val sjablonNavn: String,
    val sjablonVerdi: Int,
) : BasePeriode(datoFom, datoTil)
