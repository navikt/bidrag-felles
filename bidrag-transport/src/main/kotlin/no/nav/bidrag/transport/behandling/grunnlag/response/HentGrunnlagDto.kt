package no.nav.bidrag.transport.behandling.grunnlag.response

import io.swagger.v3.oas.annotations.media.Schema
import no.nav.bidrag.transport.person.SivilstandPdlHistorikkDto
import java.time.LocalDateTime

data class HentGrunnlagDto(

    @Schema(description = "Periodisert liste over innhentede inntekter fra a-inntekt og underliggende poster")
    val ainntektListe: List<AinntektGrunnlagDto>,

    @Schema(description = "Periodisert liste over innhentede fra skatt og underliggende poster")
    val skattegrunnlagListe: List<SkattegrunnlagGrunnlagDto>,

    @Schema(description = "Periodisert liste over innhentet utvidet barnetrygd og småbarnstillegg")
    val ubstListe: List<UtvidetBarnetrygdOgSmaabarnstilleggGrunnlagDto>,

    @Schema(description = "Periodisert liste over innhentet barnetillegg")
    val barnetilleggListe: List<BarnetilleggGrunnlagDto>,

    @Schema(description = "Periodisert liste over innhentet kontantstøtte")
    val kontantstøtteListe: List<KontantstøtteGrunnlagDto>,

    @Schema(
        description = "Liste over alle personer som har bodd sammen med BM/BP i perioden fra virkningstidspunkt og fremover med en liste " +
            "over hvilke perioder de har delt bolig. Listen inkluderer i tillegg personens egne barn, selv om de ikke har delt bolig med BM/BP",
    )
    val husstandsmedlemmerOgEgneBarnListe: List<RelatertPersonGrunnlagDto>,

    @Schema(description = "Periodisert liste over en persons sivilstand")
    val sivilstandListe: List<SivilstandPdlHistorikkDto>,

    @Schema(description = "Periodisert liste over innhentet barnetilsyn")
    val barnetilsynListe: List<BarnetilsynGrunnlagDto>,

    @Schema(description = "Periodisert liste over arbeidsforhold")
    val arbeidsforholdListe: List<ArbeidsforholdGrunnlagDto>,

    @Schema(description = "Hentet tidspunkt")
    val hentetTidspunkt: LocalDateTime,
)
