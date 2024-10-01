package no.nav.bidrag.transport.notat

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import io.swagger.v3.oas.annotations.media.Schema
import no.nav.bidrag.domene.enums.beregning.Resultatkode
import no.nav.bidrag.domene.enums.diverse.Kilde
import no.nav.bidrag.domene.enums.inntekt.Inntektsrapportering
import no.nav.bidrag.domene.enums.inntekt.Inntektstype
import no.nav.bidrag.domene.enums.person.Bostatuskode
import no.nav.bidrag.domene.enums.person.Sivilstandskode
import no.nav.bidrag.domene.enums.person.SivilstandskodePDL
import no.nav.bidrag.domene.enums.rolle.Rolletype
import no.nav.bidrag.domene.enums.rolle.SøktAvType
import no.nav.bidrag.domene.enums.særbidrag.Særbidragskategori
import no.nav.bidrag.domene.enums.særbidrag.Utgiftstype
import no.nav.bidrag.domene.enums.vedtak.Vedtakstype
import no.nav.bidrag.domene.enums.vedtak.VirkningstidspunktÅrsakstype
import no.nav.bidrag.domene.ident.Personident
import no.nav.bidrag.domene.tid.ÅrMånedsperiode
import no.nav.bidrag.domene.util.visningsnavn
import no.nav.bidrag.domene.util.visningsnavnIntern
import no.nav.bidrag.domene.util.visningsnavnMedÅrstall
import no.nav.bidrag.transport.behandling.felles.grunnlag.DelberegningBidragspliktigesAndel
import no.nav.bidrag.transport.behandling.felles.grunnlag.DelberegningSumInntekt
import no.nav.bidrag.transport.behandling.felles.grunnlag.DelberegningUtgift
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.YearMonth
import java.util.*

data class VedtakNotatDto(
    val type: NotatMalType = NotatMalType.FORSKUDD,
    val saksnummer: String,
    val behandling: NotatBehandlingDetaljerDto,
    val saksbehandlerNavn: String?,
    val virkningstidspunkt: NotatVirkningstidspunktDto,
    val utgift: NotatSærbidragUtgifterDto?,
    val boforhold: NotatBoforholdDto,
    val roller: List<NotatRolleDto>,
    val inntekter: NotatInntekterDto,
    val vedtak: NotatVedtakDetaljerDto,
)

@Schema(enumAsRef = true)
enum class NotatMalType {
    FORSKUDD,
    SÆRBIDRAG,
    BIDRAG,
}

data class NotatBehandlingDetaljerDto(
    val søknadstype: String?,
    val vedtakstype: Vedtakstype?,
    val opprinneligVedtakstype: Vedtakstype? = null,
    val kategori: NotatSærbidragKategoriDto? = null,
    val søktAv: SøktAvType?,
    val mottattDato: LocalDate?,
    val søktFraDato: YearMonth?,
    val virkningstidspunkt: LocalDate?,
    val avslag: Resultatkode?,
    val klageMottattDato: LocalDate? = null,
) {
    @get:Schema(name = "avslagVisningsnavn")
    val avslagVisningsnavn
        get() = vedtakstype?.let { avslag?.visningsnavnIntern(vedtakstype) } ?: avslag?.visningsnavn?.intern

    @get:Schema(name = "avslagVisningsnavnUtenPrefiks")
    val avslagVisningsnavnUtenPrefiks
        get() = avslag?.visningsnavn?.intern

    @get:Schema(name = "kategoriVisningsnavn")
    val kategoriVisningsnavn
        get() =
            if (kategori?.kategori == Særbidragskategori.ANNET) {
                kategori.kategori.visningsnavn.intern + ": " + kategori.beskrivelse
            } else {
                kategori?.kategori?.visningsnavn?.intern
            }

    @get:Schema(name = "vedtakstypeVisningsnavn")
    val vedtakstypeVisningsnavn get() = vedtakstype?.visningsnavnIntern(opprinneligVedtakstype)
}

data class NotatSærbidragUtgifterDto(
    val beregning: NotatUtgiftBeregningDto? = null,
    val maksGodkjentBeløp: NotatMaksGodkjentBeløpDto? = null,
    val begrunnelse: NotatBegrunnelseDto,
    @Deprecated("Bruk begrunnelse", replaceWith = ReplaceWith("begrunnelse"))
    val notat: NotatBegrunnelseDto = begrunnelse,
    val utgifter: List<NotatUtgiftspostDto> = emptyList(),
    val totalBeregning: List<NotatTotalBeregningUtgifterDto> = emptyList(),
)

data class NotatTotalBeregningUtgifterDto(
    val betaltAvBp: Boolean,
    val utgiftstype: String,
    val totalKravbeløp: BigDecimal,
    val totalGodkjentBeløp: BigDecimal,
) {
    @get:Schema(name = "utgiftstypeVisningsnavn")
    val utgiftstypeVisningsnavn
        get() =
            try {
                Utgiftstype.valueOf(utgiftstype).visningsnavn.intern
            } catch (e: IllegalArgumentException) {
                utgiftstype
            }
}

data class NotatMaksGodkjentBeløpDto(
    val taMed: Boolean = true,
    val beløp: BigDecimal? = null,
    val begrunnelse: String? = null,
)

data class NotatUtgiftspostDto(
    @Schema(description = "Når utgifter gjelder. Kan være feks dato på kvittering")
    val dato: LocalDate,
    @Schema(
        description = "Type utgift. Kan feks være hva som ble kjøpt for kravbeløp (bugnad, klær, sko, etc)",
        oneOf = [Utgiftstype::class, String::class],
    )
    val type: String,
    @Schema(description = "Beløp som er betalt for utgiften det gjelder")
    val kravbeløp: BigDecimal,
    @Schema(description = "Beløp som er godkjent for beregningen")
    val godkjentBeløp: BigDecimal = kravbeløp,
    @Schema(description = "Begrunnelse for hvorfor godkjent beløp avviker fra kravbeløp. Må settes hvis godkjent beløp er ulik kravbeløp")
    val begrunnelse: String? = null,
    @Schema(description = "Om utgiften er betalt av BP")
    val betaltAvBp: Boolean = false,
) {
    @get:Schema(name = "utgiftstypeVisningsnavn")
    val utgiftstypeVisningsnavn
        get() =
            try {
                Utgiftstype.valueOf(type).visningsnavn.intern
            } catch (e: IllegalArgumentException) {
                type
            }
}

data class NotatUtgiftBeregningDto(
    @Schema(description = "Beløp som er direkte betalt av BP")
    val beløpDirekteBetaltAvBp: BigDecimal = BigDecimal.ZERO,
    @Schema(description = "Summen av godkjente beløp som brukes for beregningen")
    val totalGodkjentBeløp: BigDecimal = BigDecimal.ZERO,
    @Schema(description = "Summen av kravbeløp")
    val totalKravbeløp: BigDecimal = BigDecimal.ZERO,
    @Schema(description = "Summen av godkjente beløp som brukes for beregningen")
    val totalGodkjentBeløpBp: BigDecimal? = null,
    @Schema(description = "Summen av godkjent beløp for utgifter BP har betalt plus beløp som er direkte betalt av BP")
    val totalBeløpBetaltAvBp: BigDecimal = (totalGodkjentBeløpBp ?: BigDecimal.ZERO) + beløpDirekteBetaltAvBp,
)

data class NotatSærbidragKategoriDto(
    val kategori: Særbidragskategori,
    val beskrivelse: String? = null,
)

data class NotatVirkningstidspunktDto(
    val søknadstype: String?,
    val vedtakstype: Vedtakstype?,
    val søktAv: SøktAvType?,
    @Schema(type = "string", format = "date", example = "01.12.2025")
    @JsonFormat(pattern = "yyyy-MM-dd")
    val mottattDato: LocalDate?,
    @Schema(type = "string", format = "date", example = "01.12.2025")
    @JsonFormat(pattern = "yyyy-MM")
    val søktFraDato: YearMonth?,
    @Schema(type = "string", format = "date", example = "01.12.2025")
    @JsonFormat(pattern = "yyyy-MM-dd")
    val virkningstidspunkt: LocalDate?,
    val avslag: Resultatkode?,
    @Schema(name = "årsak", enumAsRef = true)
    val årsak: VirkningstidspunktÅrsakstype?,
    val begrunnelse: NotatBegrunnelseDto,
    @Deprecated("Bruk begrunnelse", replaceWith = ReplaceWith("begrunnelse"))
    val notat: NotatBegrunnelseDto = begrunnelse,
) {
    @get:Schema(name = "årsakVisningsnavn")
    val årsakVisningsnavn get() = årsak?.visningsnavn?.intern

    @get:Schema(name = "avslagVisningsnavn")
    val avslagVisningsnavn
        get() = vedtakstype?.let { avslag?.visningsnavnIntern(vedtakstype) } ?: avslag?.visningsnavn?.intern
}

@Schema(description = "Notat begrunnelse skrevet av saksbehandler")
data class NotatBegrunnelseDto(
    val innhold: String?,
    @Schema(name = "intern", deprecated = true)
    val intern: String? = innhold,
    val gjelder: NotatRolleDto? = null,
)

data class NotatBoforholdDto(
    val barn: List<BoforholdBarn> = emptyList(),
    val andreVoksneIHusstanden: NotatAndreVoksneIHusstanden? = null,
    val sivilstand: NotatSivilstand,
    val begrunnelse: NotatBegrunnelseDto,
    @Deprecated("Bruk begrunnelse", replaceWith = ReplaceWith("begrunnelse"))
    val notat: NotatBegrunnelseDto = begrunnelse,
)

data class NotatSivilstand(
    val opplysningerFraFolkeregisteret: List<OpplysningerFraFolkeregisteret<SivilstandskodePDL>> =
        emptyList(),
    val opplysningerBruktTilBeregning: List<OpplysningerBruktTilBeregning<Sivilstandskode>> =
        emptyList(),
)

data class NotatAndreVoksneIHusstanden(
    val opplysningerFraFolkeregisteret: List<OpplysningerFraFolkeregisteretMedDetaljer<Bostatuskode, AndreVoksneIHusstandenDetaljerDto>> =
        emptyList(),
    val opplysningerBruktTilBeregning: List<OpplysningerBruktTilBeregning<Bostatuskode>> =
        emptyList(),
)

data class AndreVoksneIHusstandenDetaljerDto(
    val totalAntallHusstandsmedlemmer: Int,
    val husstandsmedlemmer: List<VoksenIHusstandenDetaljerDto>,
)

data class VoksenIHusstandenDetaljerDto(
    val navn: String,
    val fødselsdato: LocalDate?,
    val harRelasjonTilBp: Boolean,
)

data class BoforholdBarn(
    val gjelder: NotatRolleDto,
    val medIBehandling: Boolean,
    val kilde: Kilde,
    val opplysningerFraFolkeregisteret: List<OpplysningerFraFolkeregisteret<Bostatuskode>> =
        emptyList(),
    val opplysningerBruktTilBeregning: List<OpplysningerBruktTilBeregning<Bostatuskode>> =
        emptyList(),
)
typealias OpplysningerFraFolkeregisteret<T> = OpplysningerFraFolkeregisteretMedDetaljer<T, Unit>

data class OpplysningerFraFolkeregisteretMedDetaljer<T, K : Any>(
    val periode: ÅrMånedsperiode,
    val status: T?,
    val detaljer: K? = null,
) {
    val statusVisningsnavn get() = toVisningsnavn(status)
}

data class OpplysningerBruktTilBeregning<T>(
    val periode: ÅrMånedsperiode,
    val status: T,
    val kilde: Kilde,
) {
    val statusVisningsnavn get() = toVisningsnavn(status)
}

private fun <T> toVisningsnavn(value: T): String? =
    when (val enum = value) {
        is Særbidragskategori -> enum.visningsnavn.intern
        is Utgiftstype -> enum.visningsnavn.intern
        is Bostatuskode -> enum.visningsnavn.intern
        is Inntektsrapportering -> enum.visningsnavn.intern
        is Resultatkode -> enum.visningsnavn.intern
        is Sivilstandskode -> enum.visningsnavn.intern
        is SivilstandskodePDL ->
            enum.name.lowercase().replaceFirstChar {
                if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
            }

        is Kilde -> enum.name.lowercase().replaceFirstChar { it.uppercase() }
        is VirkningstidspunktÅrsakstype -> enum.visningsnavn.intern
        is SøktAvType ->
            enum.name.lowercase().replaceFirstChar {
                if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
            }

        else -> null
    }

data class NotatRolleDto(
    val rolle: Rolletype?,
    val navn: String?,
    val fødselsdato: LocalDate?,
    val ident: Personident?,
)

data class NotatInntekterDto(
    val inntekterPerRolle: List<InntekterPerRolle>,
    val offentligeInntekterPerRolle: List<InntekterPerRolle> = emptyList(),
    val notat: NotatBegrunnelseDto,
    val notatPerRolle: Set<NotatBegrunnelseDto> = emptySet(),
    val begrunnelsePerRolle: Set<NotatBegrunnelseDto> = notatPerRolle,
)

data class InntekterPerRolle(
    val gjelder: NotatRolleDto,
    val arbeidsforhold: List<Arbeidsforhold> = emptyList(),
    @Schema(name = "årsinntekter")
    val årsinntekter: List<NotatInntektDto> = emptyList(),
    val barnetillegg: List<NotatInntektDto> = emptyList(),
    val utvidetBarnetrygd: List<NotatInntektDto> = emptyList(),
    val småbarnstillegg: List<NotatInntektDto> = emptyList(),
    val kontantstøtte: List<NotatInntektDto> = emptyList(),
    val beregnetInntekter: List<NotatBeregnetInntektDto> = emptyList(),
) {
    val harInntekter get() =
        årsinntekter.isNotEmpty() ||
            barnetillegg.isNotEmpty() ||
            utvidetBarnetrygd.isNotEmpty() ||
            småbarnstillegg.isNotEmpty() ||
            kontantstøtte.isNotEmpty()
}

data class NotatBeregnetInntektDto(
    val gjelderBarn: NotatRolleDto,
    val summertInntektListe: List<DelberegningSumInntekt>,
)

data class Arbeidsforhold(
    val periode: ÅrMånedsperiode,
    val arbeidsgiver: String,
    val stillingProsent: String?,
    val lønnsendringDato: LocalDate?,
)

data class NotatInntektDto(
    val periode: ÅrMånedsperiode?,
    val opprinneligPeriode: ÅrMånedsperiode?,
    val beløp: BigDecimal,
    val kilde: Kilde = Kilde.OFFENTLIG,
    val type: Inntektsrapportering,
    val medIBeregning: Boolean = false,
    val gjelderBarn: NotatRolleDto?,
    val inntektsposter: List<NotatInntektspostDto> = emptyList(),
) {
    val visningsnavn
        get() =
            type.visningsnavnMedÅrstall(
                opprinneligPeriode?.fom?.year ?: periode?.fom?.year,
                opprinneligPeriode,
            )
}

data class NotatInntektspostDto(
    val kode: String?,
    val inntektstype: Inntektstype?,
    val beløp: BigDecimal,
    val visningsnavn: String? = inntektstype?.visningsnavn?.intern,
)

data class NotatVedtakDetaljerDto(
    val erFattet: Boolean,
    val fattetAvSaksbehandler: String?,
    val fattetTidspunkt: LocalDateTime?,
    val resultat: List<VedtakResultatInnhold>,
)

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "type",
)
@JsonSubTypes(
    JsonSubTypes.Type(value = NotatResultatSærbidragsberegningDto::class, name = "SÆRBIDRAG"),
    JsonSubTypes.Type(value = NotatResultatForskuddBeregningBarnDto::class, name = "FORSKUDD"),
)
abstract class VedtakResultatInnhold(
    val type: NotatMalType,
)

data class NotatResultatSærbidragsberegningDto(
    val periode: ÅrMånedsperiode,
    val bpsAndel: DelberegningBidragspliktigesAndel? = null,
    val beregning: UtgiftBeregningDto? = null,
    val inntekter: ResultatSærbidragsberegningInntekterDto? = null,
    val delberegningUtgift: DelberegningUtgift? = null,
    val resultat: BigDecimal,
    val resultatKode: Resultatkode,
    val antallBarnIHusstanden: Double? = null,
    val voksenIHusstanden: Boolean? = null,
    val enesteVoksenIHusstandenErEgetBarn: Boolean? = null,
    val erDirekteAvslag: Boolean = false,
    val bpHarEvne: Boolean = resultatKode != Resultatkode.SÆRBIDRAG_IKKE_FULL_BIDRAGSEVNE,
) : VedtakResultatInnhold(NotatMalType.SÆRBIDRAG) {
    val resultatVisningsnavn get() = resultatKode.visningsnavn.intern
    val beløpSomInnkreves: BigDecimal
        get() =
            maxOf(
                resultat - (beregning?.totalBeløpBetaltAvBp ?: BigDecimal.ZERO),
                BigDecimal.ZERO,
            )

    data class ResultatSærbidragsberegningInntekterDto(
        val inntektBM: BigDecimal? = null,
        val inntektBP: BigDecimal? = null,
        val inntektBarn: BigDecimal? = null,
    )

    data class UtgiftBeregningDto(
        @Schema(description = "Beløp som er direkte betalt av BP")
        val beløpDirekteBetaltAvBp: BigDecimal = BigDecimal.ZERO,
        @Schema(description = "Summen av godkjente beløp som brukes for beregningen")
        val totalGodkjentBeløp: BigDecimal = BigDecimal.ZERO,
        @Schema(description = "Summen av kravbeløp")
        val totalKravbeløp: BigDecimal = BigDecimal.ZERO,
        @Schema(description = "Summen av godkjente beløp som brukes for beregningen")
        val totalGodkjentBeløpBp: BigDecimal? = null,
        @Schema(description = "Summen av godkjent beløp for utgifter BP har betalt plus beløp som er direkte betalt av BP")
        val totalBeløpBetaltAvBp: BigDecimal = (totalGodkjentBeløpBp ?: BigDecimal.ZERO) + beløpDirekteBetaltAvBp,
    )
}

data class NotatResultatForskuddBeregningBarnDto(
    val barn: NotatRolleDto,
    val perioder: List<NotatResultatPeriodeDto>,
) : VedtakResultatInnhold(NotatMalType.FORSKUDD) {
    data class NotatResultatPeriodeDto(
        val periode: ÅrMånedsperiode,
        val beløp: BigDecimal,
        val resultatKode: Resultatkode,
        val regel: String,
        val sivilstand: Sivilstandskode?,
        val inntekt: BigDecimal,
        val vedtakstype: Vedtakstype?,
        val antallBarnIHusstanden: Int,
    ) {
        val resultatKodeVisningsnavn
            get() = vedtakstype?.let { resultatKode.visningsnavnIntern(it) } ?: resultatKode.visningsnavn.intern
        val sivilstandVisningsnavn get() = sivilstand?.visningsnavn?.intern
    }
}
typealias NotatResultatForskuddBeregningBarnListe = List<NotatResultatForskuddBeregningBarnDto>
