package no.nav.bidrag.transport.behandling.felles.grunnlag

import io.swagger.v3.oas.annotations.media.Schema
import no.nav.bidrag.domene.enums.person.Bostatuskode
import no.nav.bidrag.domene.enums.person.Sivilstandskode
import no.nav.bidrag.domene.ident.Personident
import no.nav.bidrag.domene.tid.ÅrMånedsperiode
import java.time.LocalDate

@Schema(description = "Informasjon om en person som er inkludert i vedtaket")
data class Person(
    @Schema(description = "Ident") val ident: Personident = Personident(""),
    @Schema(description = "Navn") val navn: String = "",
    @Schema(description = "Fødselsdato") val fødselsdato: LocalDate = LocalDate.parse("2000-01-01"),
)

@Schema(description = "Bostatus for person")
data class BostatusPeriode(
    @Schema(description = "Periode") val periode: ÅrMånedsperiode,
    @Schema(description = "Bostatus") val bostatus: Bostatuskode,
    @Schema(description = "Er registrert manuelt?") val manueltRegistrert: Boolean,
)

@Schema(description = "Sivilstand for person")
data class SivilstandPeriode(
    @Schema(description = "Periode") val periode: ÅrMånedsperiode,
    @Schema(description = "Sivilstand") val sivilstand: Sivilstandskode,
)
