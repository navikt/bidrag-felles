package no.nav.bidrag.transport.behandling.felles.grunnlag

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.annotations.media.Schema
import no.nav.bidrag.domene.enums.grunnlag.GrunnlagDatakilde
import no.nav.bidrag.domene.enums.person.Bostatuskode
import no.nav.bidrag.domene.enums.person.Familierelasjon
import no.nav.bidrag.domene.enums.person.Unntakskode
import no.nav.bidrag.domene.tid.Datoperiode
import no.nav.bidrag.domene.tid.ÅrMånedsperiode
import java.time.LocalDate
import java.time.LocalDateTime

data class InnhentetHusstandsmedlem(
    override val datakilde: GrunnlagDatakilde = GrunnlagDatakilde.PDL,
    override val grunnlag: HusstandsmedlemPDL,
    override val hentetTidspunkt: LocalDateTime,
) : InnhentetGrunnlagInnhold<InnhentetHusstandsmedlem.HusstandsmedlemPDL> {
    data class HusstandsmedlemPDL(
        @Schema(description = "Referanse til person som er husstandsmedlem")
        @Deprecated("Erstattet av gjelderPerson", ReplaceWith("gjelderPerson"))
        @JsonProperty(access = JsonProperty.Access.READ_ONLY)
        val relatertPerson: Grunnlagsreferanse? = null,
        @Schema(description = "Referanse til person som er husstandsmedlem")
        val gjelderPerson: Grunnlagsreferanse? = relatertPerson,
        @Deprecated("Erstattet av relasjon == BARN", ReplaceWith("relasjon"))
        @JsonProperty(access = JsonProperty.Access.READ_ONLY)
        val erBarnAvBmBp: Boolean? = null,
        @Schema(description = "Angir gjelderPersons relasjon til BM/BP")
        val relasjon: Familierelasjon = if (erBarnAvBmBp == true) Familierelasjon.BARN else Familierelasjon.INGEN,
        @Schema(description = "Navn på den relaterte personen, format <Fornavn, mellomnavn, Etternavn")
        val navn: String? = null,
        @Schema(description = "Den relaterte personens fødselsdato")
        val fødselsdato: LocalDate? = null,
        val perioder: List<Datoperiode>,
    )
}

@Schema(description = "Bostatus for person")
data class BostatusPeriode(
    override val periode: ÅrMånedsperiode,
    val bostatus: Bostatuskode,
    @Schema(description = "Referanse til BM eller BP som bostatus for personen gjelder for")
    val relatertTilPart: Grunnlagsreferanse,
    override val manueltRegistrert: Boolean,
) : GrunnlagPeriodeInnhold

@Schema(description = "Unntak som ikke påvirker beregningsresultatet for person")
data class Unntak(
    val unntak: Unntakskode,
    @Schema(description = "Referanse til BM eller BP som unntak for personen gjelder for")
    val relatertTilPart: Grunnlagsreferanse,
) : GrunnlagInnhold
