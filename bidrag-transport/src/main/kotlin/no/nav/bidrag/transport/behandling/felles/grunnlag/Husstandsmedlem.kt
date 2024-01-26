package no.nav.bidrag.transport.behandling.felles.grunnlag

import io.swagger.v3.oas.annotations.media.Schema
import no.nav.bidrag.domene.enums.person.Bostatuskode
import no.nav.bidrag.domene.tid.ÅrMånedsperiode
import java.time.LocalDateTime

data class InnhentetHusstandsmedlemGrunnlag(
    override val periode: ÅrMånedsperiode,
    override val grunnlag: Husstandsmedlem,
    override val hentetTidspunkt: LocalDateTime,
) : InnhentetGrunnlagInnhold<InnhentetHusstandsmedlemGrunnlag.Husstandsmedlem> {
    data class Husstandsmedlem(
        val partPersonId: String?,
        val relatertPersonPersonId: String?,
        val erBarnAvBmBp: Boolean,
    )
}

@Schema(description = "Bostatus for person")
data class BostatusPeriode(
    override val periode: ÅrMånedsperiode,
    val bostatus: Bostatuskode,
    override val manueltRegistrert: Boolean,
) : GrunnlagPeriodeInnhold
