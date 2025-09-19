package no.nav.bidrag.transport.notat

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import io.swagger.v3.oas.annotations.media.Schema
import no.nav.bidrag.domene.enums.barnetilsyn.Skolealder
import no.nav.bidrag.domene.enums.barnetilsyn.Tilsynstype
import no.nav.bidrag.domene.enums.beregning.Resultatkode
import no.nav.bidrag.domene.enums.beregning.Resultatkode.Companion.erDirekteAvslag
import no.nav.bidrag.domene.enums.beregning.Samværsklasse
import no.nav.bidrag.domene.enums.diverse.Kilde
import no.nav.bidrag.domene.enums.inntekt.Inntektsrapportering
import no.nav.bidrag.domene.enums.inntekt.Inntektstype
import no.nav.bidrag.domene.enums.person.Bostatuskode
import no.nav.bidrag.domene.enums.person.Sivilstandskode
import no.nav.bidrag.domene.enums.person.SivilstandskodePDL
import no.nav.bidrag.domene.enums.privatavtale.PrivatAvtaleType
import no.nav.bidrag.domene.enums.rolle.Rolletype
import no.nav.bidrag.domene.enums.rolle.SøktAvType
import no.nav.bidrag.domene.enums.samværskalkulator.SamværskalkulatorFerietype
import no.nav.bidrag.domene.enums.samværskalkulator.SamværskalkulatorNetterFrekvens
import no.nav.bidrag.domene.enums.særbidrag.Særbidragskategori
import no.nav.bidrag.domene.enums.særbidrag.Utgiftstype
import no.nav.bidrag.domene.enums.vedtak.BeregnTil
import no.nav.bidrag.domene.enums.vedtak.Stønadstype
import no.nav.bidrag.domene.enums.vedtak.Vedtakstype
import no.nav.bidrag.domene.enums.vedtak.VirkningstidspunktÅrsakstype
import no.nav.bidrag.domene.ident.Personident
import no.nav.bidrag.domene.tid.DatoperiodeDto
import no.nav.bidrag.domene.tid.ÅrMånedsperiode
import no.nav.bidrag.domene.util.lastVisningsnavnFraFil
import no.nav.bidrag.domene.util.visningsnavn
import no.nav.bidrag.domene.util.visningsnavnIntern
import no.nav.bidrag.domene.util.visningsnavnMedÅrstall
import no.nav.bidrag.domene.util.årsbeløpTilMåndesbeløp
import no.nav.bidrag.transport.behandling.beregning.samvær.SamværskalkulatorDetaljer
import no.nav.bidrag.transport.behandling.felles.grunnlag.BeregnetBidragPerBarn
import no.nav.bidrag.transport.behandling.felles.grunnlag.DelberegningBarnetilleggSkattesats
import no.nav.bidrag.transport.behandling.felles.grunnlag.DelberegningBidragspliktigesAndel
import no.nav.bidrag.transport.behandling.felles.grunnlag.DelberegningBoforhold
import no.nav.bidrag.transport.behandling.felles.grunnlag.DelberegningEndringSjekkGrensePeriode
import no.nav.bidrag.transport.behandling.felles.grunnlag.DelberegningSumInntekt
import no.nav.bidrag.transport.behandling.felles.grunnlag.DelberegningUnderholdskostnad
import no.nav.bidrag.transport.behandling.felles.grunnlag.DelberegningUtgift
import no.nav.bidrag.transport.behandling.felles.grunnlag.ResultatFraVedtakGrunnlag
import no.nav.bidrag.transport.behandling.felles.grunnlag.SluttberegningBarnebidrag
import no.nav.bidrag.transport.behandling.felles.grunnlag.SluttberegningBarnebidragAldersjustering
import no.nav.bidrag.transport.behandling.felles.grunnlag.SluttberegningIndeksregulering
import no.nav.bidrag.transport.behandling.vedtak.response.erIndeksEllerAldersjustering
import no.nav.bidrag.transport.felles.tilVisningsnavn
import no.nav.bidrag.transport.notat.NotatResultatBidragsberegningBarnDto.ResultatBarnebidragsberegningPeriodeDto
import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.YearMonth
import java.util.Locale

data class VedtakNotatDto(
    val type: NotatMalType = NotatMalType.FORSKUDD,
    val erOrkestrertVedtak: Boolean,
    val stønadstype: Stønadstype? = null,
    val medInnkreving: Boolean = true,
    val saksnummer: String,
    val behandling: NotatBehandlingDetaljerDto,
    val saksbehandlerNavn: String?,
    val virkningstidspunkt: NotatVirkningstidspunktDto,
    val utgift: NotatSærbidragUtgifterDto?,
    val boforhold: NotatBoforholdDto,
    val samvær: List<NotatSamværDto> = emptyList(),
    val gebyr: List<NotatGebyrRolleDto>? = null,
    var underholdskostnader: NotatUnderholdDto? = null,
    val personer: List<NotatPersonDto>,
    val privatavtale: List<NotatPrivatAvtaleDto>,
    val roller: List<NotatPersonDto> = personer,
    val inntekter: NotatInntekterDto,
    val vedtak: NotatVedtakDetaljerDto,
)

@Schema(enumAsRef = true)
enum class ResultatInnholdType {
    FORSKUDD,
    SÆRBIDRAG,
    BIDRAG,
    GEBYR,
}

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

data class NotatUnderholdDto(
    val underholdskostnaderBarn: List<NotatUnderholdBarnDto> = emptyList(),
    val offentligeOpplysninger: List<NotatOffentligeOpplysningerUnderholdBarn> = emptyList(),
    val offentligeOpplysningerV2: NotatOffentligeOpplysningerUnderhold =
        NotatOffentligeOpplysningerUnderhold(
            offentligeOpplysninger,
            emptyList(),
            offentligeOpplysninger.any { it.harTilleggsstønad },
        ),
)

data class NotatOffentligeOpplysningerUnderhold(
    val offentligeOpplysningerBarn: List<NotatOffentligeOpplysningerUnderholdBarn> = emptyList(),
    val andreBarnTilBidragsmottaker: List<NotatPersonDto> = emptyList(),
    val bidragsmottakerHarInnvilgetTilleggsstønad: Boolean,
)

data class NotatOffentligeOpplysningerUnderholdBarn(
    val gjelder: NotatPersonDto,
    val gjelderBarn: NotatPersonDto? = null,
    val barnetilsyn: List<NotatBarnetilsynOffentligeOpplysninger> = emptyList(),
    val harTilleggsstønad: Boolean,
) {
    data class NotatBarnetilsynOffentligeOpplysninger(
        val periode: ÅrMånedsperiode,
        val tilsynstype: Tilsynstype? = null,
        val skolealder: Skolealder? = null,
    )
}

data class NotatUnderholdBarnDto(
    val gjelderBarn: NotatPersonDto,
    val harTilsynsordning: Boolean? = null,
    val stønadTilBarnetilsyn: List<NotatStønadTilBarnetilsynDto> = emptyList(),
    val faktiskTilsynsutgift: List<NotatFaktiskTilsynsutgiftDto>,
    val tilleggsstønad: List<NotatTilleggsstønadDto> = emptyList(),
    val underholdskostnad: List<NotatUnderholdskostnadBeregningDto>,
    val begrunnelse: NotatBegrunnelseDto? = null,
) {
    data class NotatUnderholdskostnadPeriodeBeregningsdetaljer(
        val tilsynsutgifterBarn: List<NotatTilsynsutgiftBarn> = emptyList(),
        val sjablonMaksTilsynsutgift: BigDecimal,
        val sjablonMaksFradrag: BigDecimal,
        val antallBarnBMUnderTolvÅr: Int,
        val antallBarnBMBeregnet: Int = antallBarnBMUnderTolvÅr,
        val antallBarnMedTilsynsutgifter: Int = antallBarnBMBeregnet,
        val skattesatsFaktor: BigDecimal,
        val totalTilsynsutgift: BigDecimal,
        val sumTilsynsutgifter: BigDecimal,
        val bruttoTilsynsutgift: BigDecimal,
        val justertBruttoTilsynsutgift: BigDecimal,
        val nettoTilsynsutgift: BigDecimal,
        val erBegrensetAvMaksTilsyn: Boolean,
        val fordelingFaktor: BigDecimal,
        val skattefradragPerBarn: BigDecimal,
        val maksfradragAndel: BigDecimal,
        val skattefradrag: BigDecimal,
        val skattefradragMaksFradrag: BigDecimal,
        val skattefradragTotalTilsynsutgift: BigDecimal,
    )

    data class NotatTilsynsutgiftBarn(
        val gjelderBarn: NotatPersonDto,
        val totalTilsynsutgift: BigDecimal,
        val beløp: BigDecimal,
        val kostpenger: BigDecimal? = null,
        val tilleggsstønad: BigDecimal? = null,
    )

    data class NotatFaktiskTilsynsutgiftDto(
        val periode: DatoperiodeDto,
        val utgift: BigDecimal,
        val kostpenger: BigDecimal? = null,
        val kommentar: String? = null,
        val total: BigDecimal,
    )

    data class NotatStønadTilBarnetilsynDto(
        val periode: DatoperiodeDto,
        val skolealder: Skolealder,
        val tilsynstype: Tilsynstype,
        val kilde: Kilde = Kilde.MANUELL,
    ) {
        val skoleaderVisningsnavn = skolealder.visningsnavn.intern
        val tilsynstypeVisningsnavn = tilsynstype.visningsnavn.intern
    }

    data class NotatTilleggsstønadDto(
        val periode: DatoperiodeDto,
        val dagsats: BigDecimal,
        val total: BigDecimal,
    )

    data class NotatUnderholdskostnadBeregningDto(
        val periode: DatoperiodeDto,
        val forbruk: BigDecimal = BigDecimal.ZERO,
        val boutgifter: BigDecimal = BigDecimal.ZERO,
        val stønadTilBarnetilsyn: BigDecimal = BigDecimal.ZERO,
        val tilsynsutgifter: BigDecimal = BigDecimal.ZERO,
        val barnetrygd: BigDecimal = BigDecimal.ZERO,
        val total: BigDecimal,
        val beregningsdetaljer: NotatUnderholdskostnadPeriodeBeregningsdetaljer? = null,
    )
}

data class NotatSamværDto(
    val gjelderBarn: NotatPersonDto,
    val begrunnelse: NotatBegrunnelseDto?,
    val perioder: List<NotatSamværsperiodeDto> = emptyList(),
) {
    data class NotatSamværsperiodeDto(
        val periode: DatoperiodeDto,
        val samværsklasse: Samværsklasse,
        val gjennomsnittligSamværPerMåned: BigDecimal,
        val beregning: SamværskalkulatorDetaljer? = null,
    ) {
        val samværsklasseVisningsnavn: String = samværsklasse.visningsnavn.intern

        val ferieVisningsnavnMap = SamværskalkulatorFerietype.entries.associateWith { it.visningsnavn.intern }
        val frekvensVisningsnavnMap = SamværskalkulatorNetterFrekvens.entries.associateWith { it.visningsnavn.intern }
    }
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
    val beregnTilDato: YearMonth?,
    val beregnTil: BeregnTil?,
    val etterfølgendeVedtakVirkningstidspunkt: YearMonth?,
    @Schema(type = "string", format = "date", example = "01.12.2025")
    @JsonFormat(pattern = "yyyy-MM-dd")
    val virkningstidspunkt: LocalDate?,
    val avslag: Resultatkode?,
    @Schema(name = "årsak", enumAsRef = true)
    val årsak: VirkningstidspunktÅrsakstype?,
    val begrunnelse: NotatBegrunnelseDto,
    val begrunnelseVurderingAvSkolegang: NotatBegrunnelseDto? = null,
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
    val innholdFraOmgjortVedtak: String?,
    @Schema(name = "intern", deprecated = true)
    val intern: String? = innhold,
    val gjelder: NotatPersonDto? = null,
)

data class NotatBoforholdDto(
    val barn: List<BoforholdBarn> = emptyList(),
    val andreVoksneIHusstanden: NotatAndreVoksneIHusstanden? = null,
    val boforholdBMSøknadsbarn: List<NotatBoforholdTilBMMedSøknadsbarn> = emptyList(),
    val sivilstand: NotatSivilstand,
    val begrunnelse: NotatBegrunnelseDto,
    @Deprecated("Bruk begrunnelse", replaceWith = ReplaceWith("begrunnelse"))
    val notat: NotatBegrunnelseDto = begrunnelse,
    val beregnetBoforhold: List<DelberegningBoforhold> = emptyList(),
)

data class NotatBoforholdTilBMMedSøknadsbarn(
    val gjelderBarn: NotatPersonDto,
    val perioder: List<OpplysningerFraFolkeregisteret<Bostatuskode>> = emptyList(),
)

data class NotatGebyrRolleDto(
    val inntekt: NotatGebyrInntektDto,
    val manueltOverstyrtGebyr: NotatManueltOverstyrGebyrDto? = null,
    val beregnetIlagtGebyr: Boolean,
    val endeligIlagtGebyr: Boolean,
    val begrunnelse: String? = null,
    val beløpGebyrsats: BigDecimal,
    val rolle: NotatPersonDto,
) {
    val erManueltOverstyrt get() = beregnetIlagtGebyr != endeligIlagtGebyr
    val gebyrResultatVisningsnavn get() =
        when (endeligIlagtGebyr) {
            true -> "Ilagt"
            false -> "Fritatt"
            else -> "Ikke valgt"
        }

    data class NotatGebyrInntektDto(
        val skattepliktigInntekt: BigDecimal,
        val maksBarnetillegg: BigDecimal? = null,
    ) {
        val totalInntekt get() = skattepliktigInntekt + (maksBarnetillegg ?: BigDecimal.ZERO)
    }

    data class NotatManueltOverstyrGebyrDto(
        val begrunnelse: String? = null,
        @Schema(description = "Skal bare settes hvis det er avslag")
        val ilagtGebyr: Boolean? = null,
    )
}

data class NotatSivilstand(
    val opplysningerFraFolkeregisteret: List<OpplysningerFraFolkeregisteret<SivilstandskodePDL>> =
        emptyList(),
    val opplysningerBruktTilBeregning: List<OpplysningerBruktTilBeregning<Sivilstandskode>> =
        emptyList(),
)

data class NotatAndreVoksneIHusstanden(
    val opplysningerFraFolkeregisteret:
        List<OpplysningerFraFolkeregisteretMedDetaljer<Bostatuskode, NotatAndreVoksneIHusstandenDetaljerDto>> =
        emptyList(),
    val opplysningerBruktTilBeregning: List<OpplysningerBruktTilBeregning<Bostatuskode>> =
        emptyList(),
)

data class NotatAndreVoksneIHusstandenDetaljerDto(
    val totalAntallHusstandsmedlemmer: Int,
    val husstandsmedlemmer: List<NotatVoksenIHusstandenDetaljerDto>,
)

data class NotatVoksenIHusstandenDetaljerDto(
    val navn: String,
    val fødselsdato: LocalDate?,
    val erBeskyttet: Boolean = false,
    val harRelasjonTilBp: Boolean,
)

data class BoforholdBarn(
    val gjelder: NotatPersonDto,
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

data class NotatPersonDto(
    val rolle: Rolletype? = null,
    val navn: String?,
    val fødselsdato: LocalDate?,
    val ident: Personident?,
    val erBeskyttet: Boolean = false,
    val innbetaltBeløp: BigDecimal? = null,
    val opphørsdato: LocalDate? = null,
    val virkningstidspunkt: LocalDate? = null,
)

data class NotatInntekterDto(
    val inntekterPerRolle: List<InntekterPerRolle>,
    val offentligeInntekterPerRolle: List<InntekterPerRolle> = emptyList(),
    val notat: NotatBegrunnelseDto,
    val notatPerRolle: Set<NotatBegrunnelseDto> = emptySet(),
    val begrunnelsePerRolle: Set<NotatBegrunnelseDto> = notatPerRolle,
)

data class InntekterPerRolle(
    val gjelder: NotatPersonDto,
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
    val gjelderBarn: NotatPersonDto,
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
    val gjelderBarn: NotatPersonDto?,
    val historisk: Boolean = false,
    val inntektsposter: List<NotatInntektspostDto> = emptyList(),
) {
    @get:Schema(description = "Avrundet månedsbeløp for barnetillegg")
    val månedsbeløp: BigDecimal?
        get() =
            if (Inntektsrapportering.BARNETILLEGG == type) {
                beløp.divide(BigDecimal(12), 0, RoundingMode.HALF_UP)
            } else {
                null
            }

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
    JsonSubTypes.Type(value = NotatResultatBidragsberegningBarnDto::class, name = "BIDRAG"),
)
abstract class VedtakResultatInnhold(
    val type: NotatMalType,
)

data class NotatResultatSærbidragsberegningDto(
    val periode: ÅrMånedsperiode,
    val bpsAndel: DelberegningBidragspliktigesAndel? = null,
    val beregning: UtgiftBeregningDto? = null,
    val forskuddssats: BigDecimal? = null,
    val maksGodkjentBeløp: BigDecimal? = null,
    val inntekter: NotatResultatBeregningInntekterDto? = null,
    val delberegningBidragspliktigesBeregnedeTotalbidrag: NotatDelberegningBidragspliktigesBeregnedeTotalbidragDto? = null,
    val delberegningBidragsevne: NotatDelberegningBidragsevneDto? = null,
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

data class NotatDelberegningBidragspliktigesBeregnedeTotalbidragDto(
    val beregnetBidragPerBarnListe: List<NotatBeregnetBidragPerBarnDto>,
    val bidragspliktigesBeregnedeTotalbidrag: BigDecimal,
    val periode: ÅrMånedsperiode,
) {
    data class NotatBeregnetBidragPerBarnDto(
        val beregnetBidragPerBarn: BeregnetBidragPerBarn,
        val personidentBarn: String,
    )
}

data class NotatDelberegningBidragsevneDto(
    val sumInntekt25Prosent: BigDecimal,
    val bidragsevne: BigDecimal,
    val skatt: NotatSkattBeregning,
    val underholdEgneBarnIHusstand: NotatUnderholdEgneBarnIHusstand,
    val utgifter: NotatBidragsevneUtgifterBolig,
) {
    data class NotatUnderholdEgneBarnIHusstand(
        val årsbeløp: BigDecimal,
        val sjablon: BigDecimal,
        val antallBarnIHusstanden: Double,
    ) {
        val måndesbeløp get() = årsbeløp.årsbeløpTilMåndesbeløp()
    }

    data class NotatSkattBeregning(
        val sumSkatt: BigDecimal,
        val skattAlminneligInntekt: BigDecimal,
        val trinnskatt: BigDecimal,
        val trygdeavgift: BigDecimal,
    ) {
        val skattMånedsbeløp get() = sumSkatt.årsbeløpTilMåndesbeløp()
        val trinnskattMånedsbeløp get() = trinnskatt.årsbeløpTilMåndesbeløp()
        val skattAlminneligInntektMånedsbeløp get() = skattAlminneligInntekt.årsbeløpTilMåndesbeløp()
        val trygdeavgiftMånedsbeløp get() = trygdeavgift.årsbeløpTilMåndesbeløp()
    }

    data class NotatBidragsevneUtgifterBolig(
        val borMedAndreVoksne: Boolean,
        val boutgiftBeløp: BigDecimal,
        val underholdBeløp: BigDecimal,
    )
}

data class NotatResultatForskuddBeregningBarnDto(
    val barn: NotatPersonDto,
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

data class EndeligOrkestrertVedtak(
    val type: Vedtakstype?,
    val perioder: List<ResultatBarnebidragsberegningPeriodeDto>,
)

data class NotatResultatBidragsberegningBarnDto(
    val barn: NotatPersonDto,
    val indeksår: Int? = null,
    val innkrevesFraDato: YearMonth? = null,
    val orkestrertVedtak: EndeligOrkestrertVedtak? = null,
    val perioder: List<ResultatBarnebidragsberegningPeriodeDto>,
) : VedtakResultatInnhold(NotatMalType.BIDRAG) {
    data class ResultatBarnebidragsberegningPeriodeDto(
        val periode: ÅrMånedsperiode,
        val underholdskostnad: BigDecimal = BigDecimal.ZERO,
        val bpsAndelU: BigDecimal = BigDecimal.ZERO,
        val bpsAndelBeløp: BigDecimal = BigDecimal.ZERO,
        val samværsfradrag: BigDecimal = BigDecimal.ZERO,
        val beregnetBidrag: BigDecimal = BigDecimal.ZERO,
        val faktiskBidrag: BigDecimal = BigDecimal.ZERO,
        val resultatKode: Resultatkode? = null,
        val erOpphør: Boolean = false,
        val erDirekteAvslag: Boolean = false,
        val vedtakstype: Vedtakstype,
        val beregningsdetaljer: BidragPeriodeBeregningsdetaljer? = null,
        val klageOmgjøringDetaljer: KlageOmgjøringDetaljer? = null,
        var delvedtakstypeVisningsnavn: String = "",
        var resultatkodeVisningsnavn: String = "",
        val resultatFraVedtak: ResultatFraVedtakGrunnlag? = null,
    ) {
        init {
            if (delvedtakstypeVisningsnavn.isEmpty()) {
                delvedtakstypeVisningsnavn = tilDelvedtakstypeVisningsnavn()
            }
            if (resultatkodeVisningsnavn.isEmpty()) {
                resultatkodeVisningsnavn = tilResultatkodeVisningsnavn()
            }
        }

        fun tilDelvedtakstypeVisningsnavn(): String {
            if (resultatFraVedtak == null) return ""
            return when {
                resultatFraVedtak.omgjøringsvedtak && vedtakstype == Vedtakstype.KLAGE -> "Klagevedtak"
                resultatFraVedtak.omgjøringsvedtak && !vedtakstype.erIndeksEllerAldersjustering -> "Omgjøringsvedtak"
                resultatFraVedtak.beregnet && vedtakstype == Vedtakstype.ALDERSJUSTERING -> "Aldersjustering"
                resultatFraVedtak.beregnet && vedtakstype == Vedtakstype.INDEKSREGULERING -> "Indeksregulering"
                klageOmgjøringDetaljer != null &&
                    klageOmgjøringDetaljer.beregnTilDato != null && periode.fom >= klageOmgjøringDetaljer.beregnTilDato
                -> {
                    val prefiks =
                        if (vedtakstype == Vedtakstype.ALDERSJUSTERING) {
                            "Aldersjustering"
                        } else if (vedtakstype == Vedtakstype.INDEKSREGULERING) {
                            "Indeksregulering"
                        } else {
                            "Vedtak"
                        }
                    "$prefiks (${resultatFraVedtak.vedtakstidspunkt?.toLocalDate().tilVisningsnavn()})"
                }
                resultatFraVedtak.vedtaksid == null && resultatKode == Resultatkode.OPPHØR -> "Opphør"
                resultatFraVedtak.vedtakstidspunkt != null
                -> "Vedtak (${resultatFraVedtak.vedtakstidspunkt.toLocalDate().tilVisningsnavn()})"
                else -> "Vedtak"
            }
        }

        @Suppress("unused")
        fun tilResultatkodeVisningsnavn(): String =
            when {
                erOpphør ->
                    if (beregningsdetaljer?.sluttberegning?.ikkeOmsorgForBarnet == true ||
                        beregningsdetaljer?.sluttberegning?.barnetErSelvforsørget == true
                    ) {
                        beregningsdetaljer.sluttberegning.resultatVisningsnavn!!.intern
                    } else if (resultatFraVedtak?.omgjøringsvedtak == true && resultatKode != null) {
                        resultatKode.visningsnavn.intern
                    } else {
                        "Opphør"
                    }
                vedtakstype == Vedtakstype.INNKREVING -> "Innkreving"

                vedtakstype == Vedtakstype.ALDERSJUSTERING -> "Aldersjustering"

                vedtakstype == Vedtakstype.INDEKSREGULERING -> "Indeksregulering"

                resultatKode?.erDirekteAvslag() == true ||
                    resultatKode == Resultatkode.INGEN_ENDRING_UNDER_GRENSE ||
                    resultatKode == Resultatkode.INNVILGET_VEDTAK -> resultatKode.visningsnavnIntern(vedtakstype)

                else -> beregningsdetaljer?.sluttberegning?.resultatVisningsnavn?.intern ?: ""
            }

        data class KlageOmgjøringDetaljer(
            val resultatFraVedtakVedtakstidspunkt: LocalDateTime? = null,
            val beregnTilDato: YearMonth? = null,
            val manuellAldersjustering: Boolean = false,
            val delAvVedtaket: Boolean = true,
            val kanOpprette35c: Boolean = false,
            val skalOpprette35c: Boolean = false,
        )

        data class BidragPeriodeBeregningsdetaljer(
            val bpHarEvne: Boolean,
            val antallBarnIHusstanden: Double? = null,
            val forskuddssats: BigDecimal = BigDecimal.ZERO,
            val barnetilleggBM: NotatDelberegningBarnetilleggDto? = null,
            val barnetilleggBP: NotatDelberegningBarnetilleggDto? = null,
            val voksenIHusstanden: Boolean? = null,
            val enesteVoksenIHusstandenErEgetBarn: Boolean? = null,
            val bpsAndel: DelberegningBidragspliktigesAndel? = null,
            val inntekter: NotatResultatBeregningInntekterDto? = null,
            val delberegningBidragsevne: NotatDelberegningBidragsevneDto? = null,
            val samværsfradrag: NotatBeregningsdetaljerSamværsfradrag? = null,
            val endringUnderGrense: DelberegningEndringSjekkGrensePeriode? = null,
            val sluttberegning: SluttberegningBarnebidrag? = null,
            val delberegningUnderholdskostnad: DelberegningUnderholdskostnad? = null,
            val indeksreguleringDetaljer: IndeksreguleringDetaljer? = null,
            val sluttberegningAldersjustering: SluttberegningBarnebidragAldersjustering? = null,
            val delberegningBidragspliktigesBeregnedeTotalBidrag: NotatDelberegningBidragspliktigesBeregnedeTotalbidragDto? = null,
        ) {
            data class IndeksreguleringDetaljer(
                val sluttberegning: SluttberegningIndeksregulering?,
                val faktor: BigDecimal,
            )

            data class NotatBeregningsdetaljerSamværsfradrag(
                val samværsfradrag: BigDecimal,
                val samværsklasse: Samværsklasse,
                val gjennomsnittligSamværPerMåned: BigDecimal,
            ) {
                val samværsklasseVisningsnavn: String = samværsklasse.visningsnavn.intern
            }

            val deltBosted get() =
                sluttberegning?.bidragJustertForDeltBosted == true ||
                    sluttberegning?.resultat == SluttberegningBarnebidrag::bidragJustertForDeltBosted.name
        }
    }
}

data class NotatDelberegningBarnetilleggDto(
    val barnetillegg: List<NotatBarnetilleggDetaljerDto> = emptyList(),
    val skattFaktor: BigDecimal = BigDecimal.ZERO,
    val delberegningSkattesats: DelberegningBarnetilleggSkattesats? = null,
    val sumBruttoBeløp: BigDecimal = BigDecimal.ZERO,
    val sumNettoBeløp: BigDecimal = BigDecimal.ZERO,
) {
    data class NotatBarnetilleggDetaljerDto(
        val bruttoBeløp: BigDecimal,
        val nettoBeløp: BigDecimal,
        val visningsnavn: String,
    )
}

data class NotatResultatBeregningInntekterDto(
    val inntektBM: BigDecimal? = null,
    val inntektBP: BigDecimal? = null,
    val inntektBarn: BigDecimal? = null,
    val barnEndeligInntekt: BigDecimal? = null,
) {
    val totalEndeligInntekt get() =
        (inntektBM ?: BigDecimal.ZERO) +
            (inntektBP ?: BigDecimal.ZERO) +
            (barnEndeligInntekt ?: BigDecimal.ZERO)
    val inntektBPMånedlig get() = inntektBP?.divide(BigDecimal(12), MathContext(10, RoundingMode.HALF_UP))
    val inntektBMMånedlig get() = inntektBM?.divide(BigDecimal(12), MathContext(10, RoundingMode.HALF_UP))
    val inntektBarnMånedlig get() = inntektBarn?.divide(BigDecimal(12), MathContext(10, RoundingMode.HALF_UP))
}

data class NotatPrivatAvtaleDto(
    val gjelderBarn: NotatPersonDto,
    val avtaleDato: LocalDate?,
    val avtaleType: PrivatAvtaleType?,
    val skalIndeksreguleres: Boolean,
    val begrunnelse: NotatBegrunnelseDto? = null,
    val perioder: List<NotatPrivatAvtalePeriodeDto> = emptyList(),
    val beregnetPrivatAvtalePerioder: List<NotatBeregnetPrivatAvtalePeriodeDto> = emptyList(),
) {
    val avtaleTypeVisningsnavn get() = avtaleType?.visningsnavn?.intern
}

data class NotatPrivatAvtalePeriodeDto(
    val periode: DatoperiodeDto,
    val beløp: BigDecimal,
)

data class NotatBeregnetPrivatAvtalePeriodeDto(
    val periode: DatoperiodeDto,
    val indeksfaktor: BigDecimal,
    val beløp: BigDecimal,
)

typealias NotatResultatForskuddBeregningBarnListe = List<NotatResultatForskuddBeregningBarnDto>
