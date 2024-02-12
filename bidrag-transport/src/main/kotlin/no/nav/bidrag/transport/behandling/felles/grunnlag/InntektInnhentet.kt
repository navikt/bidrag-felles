package no.nav.bidrag.transport.behandling.felles.grunnlag

import io.swagger.v3.oas.annotations.media.Schema
import no.nav.bidrag.domene.enums.barnetilsyn.Skolealder
import no.nav.bidrag.domene.enums.barnetilsyn.Tilsynstype
import no.nav.bidrag.domene.tid.Datoperiode
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.YearMonth

data class InnhentetArbeidsforhold(
    override val periode: Datoperiode,
    override val grunnlag: Arbeidsforhold,
    override val hentetTidspunkt: LocalDateTime,
) : InnhentetGrunnlagInnhold<InnhentetArbeidsforhold.Arbeidsforhold> {
    data class Arbeidsforhold(
        @Schema(description = "Startdato for arbeidsforholdet")
        val startdato: LocalDate?,
        @Schema(description = "Eventuell sluttdato for arbeidsforholdet")
        val sluttdato: LocalDate?,
        @Schema(description = "Navn på arbeidsgiver")
        val arbeidsgiverNavn: String?,
        @Schema(description = "Arbeidsgivers organisasjonsnummer")
        val arbeidsgiverOrgnummer: String?,
        @Schema(description = "Liste av ansettelsesdetaljer, med eventuell historikk")
        val ansettelsesdetaljerListe: List<Ansettelsesdetaljer> = emptyList(),
        @Schema(description = "Liste over registrerte permisjoner")
        val permisjonListe: List<Permisjon> = emptyList(),
        @Schema(description = "Liste over registrerte permitteringer")
        val permitteringListe: List<Permittering> = emptyList(),
    ) {
        data class Permisjon(
            val startdato: LocalDate?,
            val sluttdato: LocalDate?,
            val beskrivelse: String?,
            val prosent: Double?,
        )

        data class Permittering(
            val startdato: LocalDate?,
            val sluttdato: LocalDate?,
            val beskrivelse: String?,
            val prosent: Double?,
        )
    }

    data class Ansettelsesdetaljer(
        @Schema(description = "Fradato for ansettelsesdetalj. År + måned")
        val periodeFra: YearMonth?,
        @Schema(description = "Eventuell sluttdato for ansettelsesdetalj. År + måned")
        val periodeTil: YearMonth?,
        @Schema(description = "Type arbeidsforhold, Ordinaer, Maritim, Forenklet, Frilanser'")
        val arbeidsforholdType: String?,
        @Schema(description = "Beskrivelse av arbeidstidsordning. Eks: 'Ikke skift'")
        val arbeidstidsordningBeskrivelse: String?,
        @Schema(description = "Beskrivelse av ansettelsesform. Eks: 'Fast ansettelse'")
        val ansettelsesformBeskrivelse: String?,
        @Schema(description = "Beskrivelse av yrke. Eks: 'KONTORLEDER'")
        val yrkeBeskrivelse: String?,
        @Schema(description = "Avtalt antall timer i uken")
        val antallTimerPrUke: Double?,
        @Schema(description = "Avtalt stillingsprosent")
        val avtaltStillingsprosent: Double?,
        @Schema(description = "Dato for forrige endring i stillingsprosent")
        val sisteStillingsprosentendringDato: LocalDate?,
        @Schema(description = "Dato for forrige lønnsendring")
        val sisteLønnsendringDato: LocalDate?,
    )
}

data class InnhentetSkattegrunnlag(
    override val periode: Datoperiode,
    val år: Int = periode.fom.year,
    override val hentetTidspunkt: LocalDateTime,
    override val grunnlag: Skattegrunnlag,
) : InnhentetGrunnlagInnhold<InnhentetSkattegrunnlag.Skattegrunnlag> {
    data class Skattegrunnlag(
        val skattegrunnlagListe: List<Skattegrunnlagspost>,
    )

    data class Skattegrunnlagspost(
        @Schema(description = "Type skattegrunnlag: Ordinær eller Svalbard")
        val skattegrunnlagType: String,
        @Schema(
            description =
                "Tekniske navnet på inntektsposten. Er samme verdi som \"Summert skattegrunnlag\"" +
                    " fra NAV kodeverk ( https://kodeverk-web.dev.adeo.no/kodeverksoversikt/kodeverk/Summert%20skattegrunnlag )",
        )
        val kode: String,
        @Schema(description = "Beløp")
        val beløp: BigDecimal,
    )
}

data class InnhentetBarnetillegg(
    override val periode: Datoperiode,
    override val hentetTidspunkt: LocalDateTime,
    override val grunnlag: Barnetillegg,
) : InnhentetGrunnlagInnhold<InnhentetBarnetillegg.Barnetillegg> {
    data class Barnetillegg(
        @Schema(description = "Referansen barnet barnetillegget er rapportert for")
        val gjelderBarn: Grunnlagsreferanse,
        @Schema(description = "Type barnetillegg.")
        val barnetilleggType: String,
        val beløpBrutto: BigDecimal,
        @Schema(description = "Angir om barnet er felles- eller særkullsbarn")
        val barnType: String,
    )
}

data class InnhentetAinntekt(
    override val periode: Datoperiode,
    override val hentetTidspunkt: LocalDateTime,
    override val grunnlag: AinntektInnhentet,
) : InnhentetGrunnlagInnhold<InnhentetAinntekt.AinntektInnhentet> {
    data class AinntektInnhentet(
        val ainntektspostListe: List<Ainntektspost>,
    )

    data class Ainntektspost(
        val utbetalingsperiode: String?,
        val opptjeningsperiodeFra: LocalDate?,
        val opptjeningsperiodeTil: LocalDate?,
        val kategori: String,
        val fordelType: String?,
        val beløp: BigDecimal,
        val etterbetalingsperiodeFra: LocalDate?,
        val etterbetalingsperiodeTil: LocalDate?,
    )
}

// Innhentet grunnlag (rådata)

data class InnhentetUtvidetBarnetrygd(
    override val periode: Datoperiode,
    override val hentetTidspunkt: LocalDateTime,
    override val grunnlag: UtvidetBarnetrygd,
) : InnhentetGrunnlagInnhold<InnhentetUtvidetBarnetrygd.UtvidetBarnetrygd> {
    data class UtvidetBarnetrygd(
        @Schema(description = "Beløp")
        val beløp: BigDecimal,
        @Schema(description = "Angir om stønaden er manuelt beregnet")
        val manueltBeregnet: Boolean,
    )
}

data class InnhentetSmåbarnstillegg(
    override val periode: Datoperiode,
    override val hentetTidspunkt: LocalDateTime,
    override val grunnlag: Småbarnstillegg,
) : InnhentetGrunnlagInnhold<InnhentetSmåbarnstillegg.Småbarnstillegg> {
    data class Småbarnstillegg(
        @Schema(description = "Beløp")
        val beløp: BigDecimal,
        @Schema(description = "Angir om stønaden er manuelt beregnet")
        val manueltBeregnet: Boolean,
    )
}

data class InnhentetBarnetilsyn(
    override val periode: Datoperiode,
    override val hentetTidspunkt: LocalDateTime,
    override val grunnlag: Barnetilsyn,
) : InnhentetGrunnlagInnhold<InnhentetBarnetilsyn.Barnetilsyn> {
    data class Barnetilsyn(
        val gjelderBarn: Grunnlagsreferanse,
        val beløp: Int?,
        val tilsynstype: Tilsynstype?,
        val skolealder: Skolealder?,
    )
}

data class InnhentetKontantstøtte(
    override val periode: Datoperiode,
    override val hentetTidspunkt: LocalDateTime,
    override val grunnlag: Kontantstøtte,
) : InnhentetGrunnlagInnhold<InnhentetKontantstøtte.Kontantstøtte> {
    data class Kontantstøtte(
        val gjelderBarn: Grunnlagsreferanse,
        val beløp: Int,
    )
}
