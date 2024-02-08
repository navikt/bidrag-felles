package no.nav.bidrag.transport.behandling.felles.grunnlag

import com.fasterxml.jackson.databind.JsonNode
import io.swagger.v3.oas.annotations.media.Schema
import no.nav.bidrag.domene.enums.grunnlag.Grunnlagstype
import no.nav.bidrag.domene.ident.Personident
import no.nav.bidrag.domene.tid.Datoperiode
import no.nav.bidrag.domene.tid.ÅrMånedsperiode
import no.nav.bidrag.transport.felles.commonObjectmapper
import java.time.LocalDate
import java.time.LocalDateTime

val NULL_PERIODE_FRA = LocalDate.of(0, 1, 1)

@Schema(description = "Grunnlag")
data class GrunnlagDto(
    override val referanse: String,
    override val type: Grunnlagstype,
    override val innhold: JsonNode,
    override val grunnlagsreferanseListe: List<String> = emptyList(),
) : BaseGrunnlag {
    override fun toString(): String {
        return super.asString()
    }
}

@Schema(description = "BaseGrunnlag")
interface BaseGrunnlag {
    @get:Schema(description = "Referanse (unikt navn på grunnlaget)")
    val referanse: String

    @get:Schema(description = "Grunnlagstype")
    val type: Grunnlagstype

    @get:Schema(description = "Grunnlagsinnhold (generisk)", type = "GrunnlagInnhold")
    val innhold: JsonNode

    @get:Schema(description = "Liste over grunnlagsreferanser")
    val grunnlagsreferanseListe: List<String>

    fun asString(): String {
        return "$type - ${::referanse.name}=$referanse, " +
            "${::grunnlagsreferanseListe.name}=${grunnlagsreferanseListe.ifEmpty { listOf("<tomt>") }.joinToString(",")}, " +
            "${::innhold.name}=${commonObjectmapper.writeValueAsString(innhold)}"
    }

    fun valider() {
        require(innhold.asText() != "null" && !innhold.isNull) { "innhold kan ikke være null" }
        require(referanse.isNotEmpty()) { "referanse kan ikke være en tom streng" }
    }
}

interface GrunnlagInnhold

interface GrunnlagBeregningPeriode : GrunnlagInnhold {
    val periode: ÅrMånedsperiode
}

interface Delberegning : GrunnlagBeregningPeriode

interface GrunnlagPeriodeInnhold : GrunnlagInnhold {
    val periode: ÅrMånedsperiode

    @get:Schema(
        description =
            "Om grunnlaget er manuelt registrert av saksbehandler " +
                "eller om det er innhentet fra ekstern kilde (skatt/folkregisteret...)",
    )
    val manueltRegistrert: Boolean
}

interface InnhentetGrunnlagPeriodeInnhold : GrunnlagInnhold {
    val periode: Datoperiode
}

interface InnhentetGrunnlagInnhold<out T> : InnhentetGrunnlagPeriodeInnhold {
    @get:Schema(description = "Tidspunkt data hentet fra kilden")
    val hentetTidspunkt: LocalDateTime
    val grunnlag: T
}

@Schema(description = "Informasjon om en person som er inkludert i vedtaket")
data class Person(
    val ident: Personident? = null,
    val navn: String? = null,
    val fødselsdato: LocalDate = LocalDate.parse("2000-01-01"),
) : GrunnlagInnhold
