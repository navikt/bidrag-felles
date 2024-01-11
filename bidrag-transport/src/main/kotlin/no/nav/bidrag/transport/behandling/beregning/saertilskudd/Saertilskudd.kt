package no.nav.bidrag.transport.behandling.beregning.saertilskudd

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer
import io.swagger.v3.oas.annotations.media.Schema
import no.nav.bidrag.domene.enums.beregning.ResultatkodeSærtilskudd
import no.nav.bidrag.domene.enums.rolle.Rolle
import no.nav.bidrag.domene.tid.ÅrMånedsperiode
import no.nav.bidrag.transport.behandling.beregning.felles.Grunnlag
import java.math.BigDecimal
import java.time.LocalDate

// Grunnlag
open class BasePeriode {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @JsonDeserialize(using = LocalDateDeserializer::class)
    @JsonSerialize(using = LocalDateSerializer::class)
    val datoFom: LocalDate?

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @JsonDeserialize(using = LocalDateDeserializer::class)
    @JsonSerialize(using = LocalDateSerializer::class)
    val datoTil: LocalDate?

    constructor(datoFom: LocalDate, datoTil: LocalDate) {
        this.datoFom = datoFom
        this.datoTil = datoTil
    }
}

open class InntektBase(
    datoFom: LocalDate,
    datoTil: LocalDate,
    val rolle: Rolle,
    val inntektType: String?,
    val belop: BigDecimal?,
) : BasePeriode(datoFom, datoTil)

class BPInntekt(
    datoFom: LocalDate,
    datoTil: LocalDate,
    rolle: Rolle,
    inntektType: String?,
    belop: BigDecimal?,
) : InntektBase(datoFom, datoTil, rolle, inntektType, belop)

class BMInntekt(
    datoFom: LocalDate,
    datoTil: LocalDate,
    inntektType: String?,
    belop: BigDecimal?,
    rolle: Rolle,
    val deltFordel: Boolean?,
    val skatteklasse2: Boolean?,
) : InntektBase(datoFom, datoTil, rolle, inntektType, belop)

class SBInntekt(
    datoFom: LocalDate,
    datoTil: LocalDate,
    rolle: Rolle,
    inntektType: String?,
    belop: BigDecimal?,
    val soknadsbarnId: Int?,
) : InntektBase(datoFom, datoTil, rolle, inntektType, belop)

class BarnIHusstand(
    datoFom: LocalDate,
    datoTil: LocalDate,
    val antall: Double?,
) : BasePeriode(datoFom, datoTil)

class Bostatus(
    datoFom: LocalDate,
    datoTil: LocalDate,
    val bostatusKode: String?,
) : BasePeriode(datoFom, datoTil)

class Saerfradrag(
    datoFom: LocalDate,
    datoTil: LocalDate,
    val saerfradragKode: String?,
) : BasePeriode(datoFom, datoTil)

class Skatteklasse(
    datoFom: LocalDate,
    datoTil: LocalDate,
    val skatteklasseId: Int?,
) : BasePeriode(datoFom, datoTil)

class NettoSaertilskudd(
    datoFom: LocalDate,
    datoTil: LocalDate,
    val nettoSaertilskuddBelop: BigDecimal?,
) : BasePeriode(datoFom, datoTil)

class Samvaersklasse(
    datoFom: LocalDate,
    datoTil: LocalDate,
    val soknadsbarnId: Int?,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @JsonDeserialize(using = LocalDateDeserializer::class)
    @JsonSerialize(using = LocalDateSerializer::class)
    val soknadsbarnFodselsdato: LocalDate?,
    val samvaersklasseId: String?,
) : BasePeriode(datoFom, datoTil)

class LopendeBidrag(
    datoFom: LocalDate,
    datoTil: LocalDate,
    val soknadsbarnId: Int?,
    val belop: BigDecimal?,
    val opprinneligBPAndelUnderholdskostnadBelop: BigDecimal?,
    val opprinneligBidragBelop: BigDecimal?,
    val opprinneligSamvaersfradragBelop: BigDecimal?,
) : BasePeriode(datoFom, datoTil)

@JsonIgnoreProperties(ignoreUnknown = true)
data class InntektRolle(
    val rolle: Rolle,
)

data class SoknadsBarnInfo(
    val id: Int,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @JsonDeserialize(using = LocalDateDeserializer::class)
    @JsonSerialize(using = LocalDateSerializer::class)
    val fodselsdato: LocalDate,
)

// Resultat
data class BeregnetTotalSaertilskuddResultat(
    @Schema(description = "Periodisert liste over resultat av særtilskuddsberegning") var beregnetSaertilskuddPeriodeListe: List<ResultatPeriode> = emptyList(),
    @Schema(description = "Liste over grunnlag brukt i beregning") var grunnlagListe: List<Grunnlag> = emptyList(),
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
    @Schema(description = "Resultat kode") var kode: ResultatkodeSærtilskudd,
)

// Resultat
class BidragsevneResultatPeriode(
    datoFom: LocalDate,
    datoTil: LocalDate,
    val belop: BigDecimal,
    val grunnlagsreferanseListe: List<String>,
) :
    BasePeriode(datoFom, datoTil)

class BPsAndelSaertilskuddResultatPeriode(
    datoFom: LocalDate,
    datoTil: LocalDate,
    val belop: BigDecimal,
    val prosent: BigDecimal,
    val selvforsorget: Boolean,
    val grunnlagsreferanseListe: List<String>,
) : BasePeriode(datoFom, datoTil)

class SamvaersfradragResultatPeriode(
    datoFom: LocalDate,
    datoTil: LocalDate,
    val belop: BigDecimal,
    val barn: Int,
    val grunnlagsreferanseListe: List<String>,
) : BasePeriode(datoFom, datoTil)

class SjablonResultatPeriode(
    datoFom: LocalDate,
    datoTil: LocalDate,
    val sjablonNavn: String,
    val sjablonVerdi: Int,
) : BasePeriode(datoFom, datoTil)
